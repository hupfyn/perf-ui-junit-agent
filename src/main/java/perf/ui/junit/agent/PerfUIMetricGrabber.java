package perf.ui.junit.agent;

import com.automation.remarks.video.RecorderFactory;
import com.automation.remarks.video.enums.RecorderType;
import com.automation.remarks.video.recorder.IVideoRecorder;
import org.aeonbits.owner.ConfigFactory;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;
import perf.ui.junit.agent.config.PerfUIConfig;
import perf.ui.junit.agent.helper.PerfUIHelper;
import perf.ui.junit.agent.http.PerfUIMetricSender;
import perf.ui.junit.agent.helper.VideoRecorderHelper;

public class PerfUIMetricGrabber extends TestWatcher {

    private boolean isAnnotation;
    private IVideoRecorder recorder;
    private PerfUIMetricSender metricSender;
    private long startMark;
    private String auditResult;

    public PerfUIMetricGrabber() {
        metricSender = new PerfUIMetricSender(ConfigFactory.create(PerfUIConfig.class));
        this.recorder = RecorderFactory.getRecorder(RecorderType.FFMPEG);
    }

    @Override
    protected void starting(Description description) {
        this.isAnnotation = PerfUIHelper.checkForAnnotationIsPresent(description);
        if (this.isAnnotation) {
            startMark = PerfUIHelper.getTime();
            recorder.start();
        }
    }

    @Override
    protected void succeeded(Description description) {
        if (this.isAnnotation) {
            reportResult(description);
        }
    }

    @Override
    protected void failed(Throwable e, Description description) {
        if (this.isAnnotation) {
            reportResult(description);
        }
    }

    public void runAudit(WebDriver driver) {
        if (this.isAnnotation) {
            this.auditResult = PerfUIHelper.getAuditResult(driver,this.startMark);
        }
    }

    private void reportResult(Description description){
        String recodedVideoPath = VideoRecorderHelper.stopRecording(description, this.recorder);
        metricSender.sendMetric(auditResult,recodedVideoPath,description);
    }
}
