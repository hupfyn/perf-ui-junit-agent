package perf.ui.junit.agent.helper;

import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
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
import java.util.*;

public class PerfUIHelper {

    private static final String SCRIPT_TO_EXECUTE = "return " +
            "{'performance':performance.timing," +
            "'resource':performance.getEntriesByType('resource')," +
            "'paint':performance.getEntriesByType('paint')," +
            "'pageName':'pageNamePlaceholder'," +
            "'status':'statusPlaceholder'," +
            "'startTime':'startTimePlaceholder'," +
            "'endTime':'endTimePlaceholder'," +
            "'metricExporter':'metricExporterPlaceholder'," +
            "'navigation':performance.getEntriesByType('navigation')}";


    public static boolean checkForAnnotationIsPresent(Description description) {
        boolean isPefUiAnnotation = false;
        if (Objects.nonNull(description.getAnnotation(PerfUI.class))) {
            isPefUiAnnotation = true;
        }
        return isPefUiAnnotation;
    }

    public static Map<String, Object> getPerformanceMetric(WebDriver driver) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return  (Map<String, Object>) jsExecutor.executeScript(SCRIPT_TO_EXECUTE);
    }

    public static String getAuditResult(WebDriver driver) {
        String auditScript = "";
        try {
            auditScript = FileUtils.readFileToString(new File("src/main/java/perf/ui/junit/agent/helper/source_audit_script.js"),"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return new GsonBuilder().create().toJson(jsExecutor.executeScript("return " + auditScript));
    }


    public static String prepareData(String testName, long startMark, long endMark, String testStatus, Map<String,Object> data) {
        String result = new GsonBuilder().create().toJson(data);
        result = result.replaceFirst("pageNamePlaceholder",String.valueOf(testName));
        result = result.replaceFirst("statusPlaceholder",testStatus);
        result = result.replaceFirst("startTimePlaceholder",String.valueOf(startMark));
        result = result.replaceFirst("endTimePlaceholder",String.valueOf(endMark));
        result = result.replaceFirst("metricExporterPlaceholder","junit4");
        return result;
    }

    public static void sendMetrick(String protocol, String host, String port, String[] dataToSend, String videoPath) {
        File file = new File(videoPath);
        String hostAddress = protocol + "://" + host + ":" + port + "/metric";
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost metricSender = new HttpPost(hostAddress);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("video",file,ContentType.create("video/mp4"),"video.mp4");
        builder.addTextBody("performanceData",dataToSend[0],ContentType.APPLICATION_JSON);
        builder.addTextBody("auditData",dataToSend[1],ContentType.APPLICATION_JSON);
        HttpEntity entity = builder.build();
        metricSender.setEntity(entity);
        try {
            HttpResponse response = client.execute(metricSender);
            System.out.println(response.getStatusLine());}

        catch(HttpHostConnectException e) {
            System.out.println("Can't send metric");
            System.out.println(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
