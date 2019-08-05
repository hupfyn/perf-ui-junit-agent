package perf.ui.junit.agent;

import com.automation.remarks.video.RecorderFactory;
import com.automation.remarks.video.RecordingUtils;
import com.automation.remarks.video.annotations.Video;
import com.automation.remarks.video.enums.RecorderType;
import com.automation.remarks.video.recorder.IVideoRecorder;
import com.automation.remarks.video.recorder.VideoRecorder;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import perf.ui.junit.agent.annotations.PerfUI;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Objects;

import static com.automation.remarks.video.RecordingUtils.doVideoProcessing;

public class PerfUIMetricGrabber extends TestWatcher {

    private JavascriptExecutor jsExecutor;

    private static final String PERF_ANNOTATION = "@perf.ui.junit.agent.annotations.PerfUI()";
    private static final String SCRIPT_TO_EXECUTE = "return " +
            "{'performance':performance.timing," +
            "'resource':performance.getEntriesByType('resource')," +
            "'paint':performance.getEntriesByType('paint')," +
            "'navigation':performance.getEntriesByType('navigation')}";


    private boolean isAnotation;
    private String testName;
    private IVideoRecorder recorder;

    private Object getPerformanceMetric() {
        return jsExecutor.executeScript(SCRIPT_TO_EXECUTE);
    }

    private void sendMetrick(String host, String port) {
        String hostAddress;
        if (Objects.nonNull(host)){
            hostAddress = host;
                    if(Objects.nonNull(port)){
                        hostAddress += ":"+port;
                    }
                    else {
                        hostAddress += "8585";
                    }
        }
        else {
            hostAddress = "http://localhost:8585";
        }
        hostAddress += "/metric";

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost metricSender = new HttpPost(hostAddress);
        metricSender.setHeader("Content-type", "application/json");
        try {
            metricSender.setEntity(new StringEntity(new Gson().toJson(getPerformanceMetric())));
            HttpResponse httpResponse = client.execute(metricSender);
            System.out.println(httpResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void starting(Description description) {
        super.starting(description);
        this.recorder = RecorderFactory.getRecorder(RecorderType.FFMPEG);
        recorder.start();
        this.isAnotation = checkForAnnotationIsPresent(description.getAnnotations());
        this.testName = description.getMethodName();
    }




    public void runAudit(WebDriver driver){
        if (this.isAnotation){
            this.jsExecutor = (JavascriptExecutor) driver;
            File file = recorder.stopAndSave(this.testName);
            doVideoProcessing(true,file);
            sendMetrick(null,null);
        }
    }



    private boolean checkForAnnotationIsPresent(Collection<Annotation> list) {
        boolean isPefUiAnnotation = false;
        for (Annotation annotation : list) {
            if (annotation.toString().contains(PERF_ANNOTATION)) {
                isPefUiAnnotation = true;
            }
        }
        return isPefUiAnnotation;
    }
}
