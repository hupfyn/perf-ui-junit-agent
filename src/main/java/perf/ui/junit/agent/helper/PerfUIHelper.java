package perf.ui.junit.agent.helper;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.junit.runner.Description;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import perf.ui.junit.agent.annotations.PerfUI;
import perf.ui.junit.agent.config.PerfUIConfig;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class PerfUIHelper {

    public static boolean checkForAnnotationIsPresent(Description description) {
        return Objects.nonNull(description.getAnnotation(PerfUI.class));
    }

    public static String getAuditResult(WebDriver driver, long startTime) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        if (driver instanceof InternetExplorerDriver) {
            jsExecutor.executeScript(getPolyfill());
        }
        return (String) jsExecutor.executeScript(String.format("var testStartTimestamp=%d; return %s", startTime, getScript()));
    }

    private static String getScript() {
        String script = "";
        try {
            script = FileUtils.readFileToString(new File("src/main/java/perf/ui/junit/agent/scripts/check_ui_performance.js"), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return script;
    }

    private static String getPolyfill() {
        String polyfill = "";
        try {
            polyfill = FileUtils.readFileToString(new File("src/main/java/perf/ui/junit/agent/scripts/polyfill_ie11.js"), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return polyfill;
    }

    public static long getTime() {
        return new Date().getTime();
    }

    private static String getReportName(Description description, String folder) {
        String fileName = description.getAnnotation(PerfUI.class).name().length() != 0 ? description.getAnnotation(PerfUI.class).name() : description.getMethodName();
        fileName = folder + "/" + fileName + "_" + getTime() + ".html";
        return fileName;
    }

    public static void writeHtmlToFile(HttpResponse response, Description description, String folder) {
        try {
            FileUtils.copyInputStreamToFile(response.getEntity().getContent(), new File(getReportName(description, folder)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setConfigValueForRecorder(PerfUIConfig config) {
        System.setProperty("video.save.mode", "ALL");
        System.setProperty("video.frame.rate", config.frameRate());
        if (Objects.nonNull(config.videoDisplay())) {
            System.setProperty("ffmpeg.display", config.videoDisplay());
        }
    }
}
