package perf.ui.junit.agent.helper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.runner.Description;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import perf.ui.junit.agent.annotations.PerfUI;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PerfUIHelper {

    private static final String SCRIPT_TO_EXECUTE = "return JSON.stringify(" +
            "{'performance':performance.timing," +
            "'resource':performance.getEntriesByType('resource')," +
            "'paint':performance.getEntriesByType('paint')," +
            "'navigation':performance.getEntriesByType('navigation')," +
            "'testStatus':'testStatusPlaceholder'," +
            "'startMark':'startMarkPlaceholder'," +
            "'endMark':'endMarkPlaceholder'})";

    public static boolean checkForAnnotationIsPresent(Description description) {
        boolean isPefUiAnnotation = false;
        if (Objects.nonNull(description.getAnnotation(PerfUI.class))) {
            isPefUiAnnotation = true;
        }
        return isPefUiAnnotation;
    }

    public static String getPerformanceMetric(WebDriver driver) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return (String) jsExecutor.executeScript(SCRIPT_TO_EXECUTE);
    }

    public static String prepareData( long startMark, long endMark, boolean testStatus, String rawMetric) {
        rawMetric = rawMetric.replaceAll("testStatusPlaceholder", String.valueOf(testStatus));
        rawMetric = rawMetric.replaceAll("startMarkPlaceholder", String.valueOf(startMark));
        rawMetric = rawMetric.replaceAll("endMarkPlaceholder", String.valueOf(endMark));
        return rawMetric;
    }

    public static void sendMetrick(String protocol, String host, String port, String dataToSend, String videoPath) {
        File file = new File(videoPath);
        String hostAddress = protocol + "://" + host + ":" + port + "/metric";
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost metricSender = new HttpPost(hostAddress);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("video",file,ContentType.create("video/mp4"),"video.mp4");
        builder.addTextBody("data",dataToSend,ContentType.APPLICATION_JSON);
        HttpEntity entity = builder.build();
        metricSender.setEntity(entity);
        try {
            HttpResponse response = client.execute(metricSender);
            System.out.println(response.getStatusLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
