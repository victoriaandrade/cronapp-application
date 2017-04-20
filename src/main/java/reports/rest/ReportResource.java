package reports.rest;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reports.commons.ReportFront;
import reports.service.ReportService;

@RestController
@RequestMapping("/api/rest")
public class ReportResource {

    private static final Logger log = LoggerFactory.getLogger(ReportResource.class);

    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "/report/{reportName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportFront> getReport(@PathVariable String reportName) {
        log.debug("Find report [" + reportName + "].");
        return ResponseEntity.ok().body(reportService.getReport(reportName));
    }

    @RequestMapping(value = "/report/pdf", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<byte[]> getPDF(@RequestBody ReportFront reportFront, HttpServletResponse response) {
        log.debug("Print report [" + reportFront.getReportName() + "]");
        response.setHeader("Content-Disposition", "inline; filename=" + reportFront.getReportName() + ".pdf");
        response.setContentType("application/pdf");
        byte[] reportResult = this.reportService.getPDF(reportFront);
        return new ResponseEntity<>(reportResult, HttpStatus.OK);
    }

}
