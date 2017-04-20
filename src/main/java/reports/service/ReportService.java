package reports.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import reports.commons.Parameter;
import reports.commons.ParameterType;
import reports.commons.ReportFront;

@Service
public class ReportService {

	private static final Logger log = LoggerFactory.getLogger(ReportService.class);

	private final ClassLoader loader;

	public ReportService() {
		this.loader = Thread.currentThread().getContextClassLoader();
	}

	public ReportFront getReport(String reportName) {
		InputStream inputStream = getReportInputStream(reportName);
		ReportFront reportFront = new ReportFront(reportName);
		try {
			JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			Stream.of(jasperDesign.getParameters())
					.filter(jrParameter -> !jrParameter.isSystemDefined())
					.filter(jrParameter -> !jrParameter.getName().contains("image_"))
					.filter(jrParameter -> !jrParameter.getName().contains("sub_"))
					.forEach(jrParameter -> {
						Parameter parameter = new Parameter();
						parameter.setName(jrParameter.getName());
						parameter.setType(ParameterType.toType(jrParameter.getValueClass()));
						reportFront.addParameter(parameter);
					});
		} catch (JRException e) {
			log.error("Problems to make JasperDesing object.");
			throw new RuntimeException(e);
		}
		return reportFront;
	}

	public byte[] getPDF(ReportFront reportFront) {
		File pdf = null;
		try {
			try {
				pdf = File.createTempFile(UUID.randomUUID().toString(), ".pdf");
			} catch (IOException e) {
				log.error("Problems to make the temporary report file.");
				throw new RuntimeException(e);
			}

			InputStream reportInputStream = this.getReportInputStream(reportFront.getReportName());

			JasperDesign jasperDesign;
			try {
				jasperDesign = JRXmlLoader.load(reportInputStream);
			} catch (JRException e) {
				log.error("Problems to make the design file from report.");
				throw new RuntimeException(e);
			}

			Map<String, JRParameter> parametersMap = jasperDesign.getParametersMap();

			HashMap<String, Object> parameters = new HashMap<>();

			reportFront.getParameters().forEach(parameter -> {
				parameters.put(parameter.getName(), parameter.getValue());
			});

			parametersMap.entrySet().stream()
					.filter(p -> !parameters.containsKey(p.getKey()))
					.forEach(parameter -> {
						String key = parameter.getKey();
						JRParameter value = parameter.getValue();
						if (value == null)
							parameters.put(key, null);
						else {
							JRExpression valueExpression = value.getDefaultValueExpression();
							if (valueExpression != null)
								parameters.put(key, valueExpression.getText());
						}
					});

			parameters.entrySet().stream()
					.filter(parameter -> parameter.getKey().contains("image_"))
					.forEach(parameter -> {
						JRParameter jrParameter = parametersMap.get(parameter.getKey());
						JRExpression defaultValueExpression = jrParameter.getDefaultValueExpression();
						if (defaultValueExpression != null) {
							URL resource = loader.getResource(defaultValueExpression.getText().replaceAll("\"", ""));
							if (resource != null)
								parameters.put(parameter.getKey(), resource.getPath());
						}
					});

			parameters.entrySet().stream()
					.filter(parameter -> parameter.getKey().contains("sub_"))
					.forEach(parameter -> {
						JRParameter jrParameter = parametersMap.get(parameter.getKey());
						JRExpression defaultValueExpression = jrParameter.getDefaultValueExpression();
						if (defaultValueExpression != null) {
							URL resource = loader.getResource(defaultValueExpression.getText().replaceAll("\"", "").replaceAll(".jrxml", ".jasper"));
							if (resource != null)
								parameters.put(parameter.getKey(), resource.getPath());
						}
					});

			JasperPrint jasperPrint;
			try (Connection connection = this.getConnection(jasperDesign)) {
				JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
				jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
			} catch (JRException | SQLException e) {
				log.error("Problems during the compile.");
				throw new RuntimeException(e);
			}

			try {
				JasperExportManager.exportReportToPdfFile(jasperPrint, pdf.getAbsolutePath());
			} catch (JRException e) {
				log.error("Problems to export report to PDF file.");
				throw new RuntimeException(e);
			}

			java.nio.file.Path path = Paths.get(pdf.getAbsolutePath());
			try {
				return Files.readAllBytes(path);
			} catch (IOException e) {
				log.error("Problems to export report file to byte array.");
				throw new RuntimeException(e);
			}
		} finally {
			if (pdf != null && pdf.exists())
				pdf.delete();
		}
	}

	private InputStream getReportInputStream(String reportName) {
		String report = "reports/".concat(reportName).concat(".jrxml");
		InputStream inputStream = loader.getResourceAsStream(report);
		if (inputStream == null)
			throw new RuntimeException("File [" + reportName + "].jrxml not found.");
		return inputStream;
	}

	private Connection getConnection(JasperDesign jasperDesign) {
		String datasource = jasperDesign.getProperty("DATASOURCE");
		if (datasource != null && !datasource.isEmpty() && !"null".equals(datasource)) {
			javax.naming.Context context = null;
			DataSource dataSource = null;
			try {
				context = (javax.naming.Context) new InitialContext().lookup("java:/comp/env");
				dataSource = (DataSource) context.lookup(datasource);
			} catch (NamingException e) {
				try {
					if (context != null)
						dataSource = (DataSource) context.lookup(datasource.toLowerCase());
				} catch (NamingException e1) {
					throw new RuntimeException(new Exception("Connection context not found.\nError: " + e.getMessage()));
				}
			}
			try {
				if (dataSource != null)
					return dataSource.getConnection();
			} catch (SQLException e) {
				throw new RuntimeException(new Exception("Trouble getting a connection from the context.\nError: " + e.getMessage()));
			}
		}
		return null;
	}

}
