package perf.ui.junit.agent.helper;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.junit.runner.Description;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import perf.ui.junit.agent.annotations.PerfUI;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class PerfUIHelper {

    private static File auditScriptFile = new File("src/main/java/perf/ui/junit/agent/scripts/check_ui_performance.js");
    private static File polyFillFile = new File("src/main/java/perf/ui/junit/agent/scripts/polyfill_ie11.js");
    private static File isPageLoadScript = new File("src/main/java/perf/ui/junit/agent/scripts/page_really_loaded.js");

    public static boolean checkForAnnotationIsPresent(Description description) {
        return Objects.nonNull(description.getAnnotation(PerfUI.class));
    }

    public static String getAuditResult(WebDriver driver, long startTime, int loadTimeOut) {
        checkIsPageReallyLoaded(driver,loadTimeOut);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        if (driver instanceof InternetExplorerDriver) {
            jsExecutor.executeScript(getScriptCode(polyFillFile));
        }
        return (String) jsExecutor.executeScript(String.format("var testStartTimestamp=%d; return %s", startTime, getScriptCode(auditScriptFile)));
    }

    private static void checkIsPageReallyLoaded(WebDriver driver, int timeOut){
        Wait<WebDriver> wait = new WebDriverWait(driver, timeOut);
        wait.until(webDriver -> String
                .valueOf(((JavascriptExecutor) webDriver).executeScript(String.format("return %s", getScriptCode(isPageLoadScript))))
                .equals("true"));
    }

    private static String getScriptCode(File file) {
        String script = "";
        try {
            script = FileUtils.readFileToString(file, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return script;
    }

    public static long getTime() {
        return new Date().getTime();
    }

    private static String getReportName(Description description, String folder) {
        String fileName = description.getAnnotation(PerfUI.class).name().length() != 0 ? description.getAnnotation(PerfUI.class).name() : description.getMethodName();
        fileName = String.format("%s/%s_%d.html", folder, fileName, getTime());
        return fileName;
    }


    public static void writeHtmlToFile(HttpResponse response, Description description, String folder) {
        try {
            FileUtils.copyInputStreamToFile(response.getEntity().getContent(), new File(getReportName(description, folder)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
