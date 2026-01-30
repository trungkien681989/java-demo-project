package core.util.reporting.listener;

import core.util.platform.host.os.OsHelper;
import core.util.platform.host.shell.CommandHelper;
import core.util.reporting.report.ExtentManager;
import core.util.reporting.report.HtmlReportGenerator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.testng.IAlterSuiteListener;
import org.testng.IExecutionListener;

public class ExecutionListener implements IExecutionListener, IAlterSuiteListener {
    private static final Logger LOGGER = LogManager.getLogger(ExecutionListener.class);

    @Override
    public void onExecutionStart() {
        LOGGER.info("Create report directory");
        HtmlReportGenerator.createTestOutputDirectory();

        LOGGER.info("Initialize ExtentReports");
        ExtentManager.getInstance();

        stopNodeJs();
    }

    @Override
    public void onExecutionFinish() {
        LOGGER.info("Flush ExtentReports");
        ExtentManager.flush();

        LOGGER.info("Copy report to Newest_Report folder");
        HtmlReportGenerator.copyNewestReport();
        stopNodeJs();
    }

    private void stopNodeJs() {
        String nodeJs;
        if (OsHelper.isUnix()) {
            nodeJs = "node";
        } else {
            nodeJs = "node.exe";
        }
        CommandHelper.killProcess(nodeJs);
    }

}
