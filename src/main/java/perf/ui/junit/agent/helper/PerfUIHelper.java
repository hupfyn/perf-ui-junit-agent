package perf.ui.junit.agent.helper;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.runner.Description;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import perf.ui.junit.agent.annotations.PerfUI;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.*;

public class PerfUIHelper {

    private static final String SCRIPT_TO_EXECUTE = "return JSON.stringify(" +
            "{'performance':performance.timing," +
            "'resource':performance.getEntriesByType('resource')," +
            "'paint':performance.getEntriesByType('paint')," +
            "'navigation':performance.getEntriesByType('navigation')," +
            "'videoString':'decodedPlaceholder'," +
            "'testStatus':'testStatusPlaceholder'," +
            "'startMark':'startMarkPlaceholder'," +
            "'endMark':'endMarkPlaceholder'})";

    public static boolean checkForAnnotationIsPresent(Description description) {
        boolean isPefUiAnnotation = false;
        if(Objects.nonNull(description.getAnnotation(PerfUI.class))){
            isPefUiAnnotation = true;
        }
        return isPefUiAnnotation;
    }

    public static String getPerformanceMetric(WebDriver driver) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return (String) jsExecutor.executeScript(SCRIPT_TO_EXECUTE);
    }

    public static String prepareData(String encodedVideo,long startMark, long endMark, boolean testStatus, String rawMetric){
        rawMetric = rawMetric.replaceAll("testStatusPlaceholder", String.valueOf(testStatus));
        rawMetric = rawMetric.replaceAll("startMarkPlaceholder", String.valueOf(startMark));
        rawMetric = rawMetric.replaceAll("endMarkPlaceholder", String.valueOf(endMark));
        rawMetric = rawMetric.replaceAll("decodedPlaceholder", encodedVideo);
        return rawMetric;
    }

    public static void sendMetrick(String protocol, String host, String port, String dataToSend) {
        String hostAddress = protocol + "://" + host + ":" + port + "/metric";
        System.out.println(hostAddress);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost metricSender = new HttpPost(hostAddress);
        metricSender.setHeader("Content-type", "application/json");
        try {
            metricSender.setEntity(new StringEntity(dataToSend));
          HttpResponse httpResponse = client.execute(metricSender);
          System.out.println(httpResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String encodeVideoToBase64(String fileName) {
        File file = new File(fileName);
        String encoded = null;
        try {
            //Todo: fix encoding video;
            encoded = java.util.Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encoded;
    }

}
