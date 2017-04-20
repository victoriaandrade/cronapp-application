package reports.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReportFront implements Serializable {

    private static final long serialVersionUID = 1L;

    private String reportName;

    private List<Parameter> parameters;

    public ReportFront() {
        this.reportName = "";
        this.parameters = new ArrayList<>(1024);
    }

    public ReportFront(String reportName) {
        this.reportName = reportName;
        this.parameters = new ArrayList<>(1024);
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public ReportFront addParameter(Parameter parameter) {
        getParameters().add(parameter);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        ReportFront that = (ReportFront)o;
        return reportName != null ? reportName.equals(that.reportName)
            : that.reportName == null && parameters.equals(that.parameters);
    }

    @Override
    public int hashCode() {
        int result = reportName != null ? reportName.hashCode() : 0;
        result = 31 * result + parameters.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ReportFront{" + "reportName='" + reportName + '\'' + ", parameters=" + parameters + '}';
    }

}
