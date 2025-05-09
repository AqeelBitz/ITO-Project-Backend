package org.acme.respository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.*;

import java.io.ByteArrayOutputStream;
import java.util.logging.Logger;
import java.util.Map;

@ApplicationScoped
public class BirtService {
    private IReportEngine birtEngine;

    private static final Logger logger = Logger.getLogger(BirtService.class.getName());

    @PostConstruct
    public void init() {}

    public  byte[]  generateReport(String designFileName, String format, Map<String, Object> params) throws BirtException {
        // System.out.println("Inside BirtService: generating report with params:"+ params);
        EngineConfig engineConfig = new EngineConfig();
        engineConfig.setEngineHome("");
        Platform.startup(engineConfig);
        IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
        IReportEngine engine = factory.createReportEngine(engineConfig);
        IReportRunnable report = engine.openReportDesign("/home/bahl/Desktop/Training project/ITO/CTS/be/consignment-tracking/src/main/resources/report/cts_report.rptdesign");
        IRunAndRenderTask task = engine.createRunAndRenderTask(report);
        if (params != null) {
            task.setParameterValues(params);
        }
        PDFRenderOption options = new PDFRenderOption();
        options.setOutputFormat("pdf");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        options.setOutputStream(outputStream);

        task.setRenderOption(options);

        task.run();
        task.close();

       byte[] reportContent =  outputStream.toByteArray();
       return reportContent;
    }

    @PreDestroy
    public void shutdown() {
        if (birtEngine != null) {
            birtEngine.destroy();
            Platform.shutdown();
        }
    }
}