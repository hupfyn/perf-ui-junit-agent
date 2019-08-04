package perf.ui.junit.agent;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class PerfUIMetricGrabber {

    private JavascriptExecutor jsExecutor;
    private final static String SCRIPT_TO_EXECUTE = "return JSON.stringify(" +
            "{'performance':performance.timing," +
            "'resource':performance.getEntriesByType('resources')," +
            "'paint':performance.getEntriesByType('paint')," +
            "'navigation':performance.getEntriesByType('navigation')})";

    public PerfUIMetricGrabber(WebDriver driver) throws IOException {
        this.jsExecutor = (JavascriptExecutor) driver;
    }

    public String getPerformanceMetric() {
        return (String) jsExecutor.executeScript(SCRIPT_TO_EXECUTE);
    }

}
