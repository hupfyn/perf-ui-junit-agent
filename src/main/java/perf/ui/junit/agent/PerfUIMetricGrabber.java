package perf.ui.junit.agent;

import com.automation.remarks.video.RecorderFactory;
import com.automation.remarks.video.enums.RecorderType;
import com.automation.remarks.video.recorder.IVideoRecorder;
import org.aeonbits.owner.ConfigFactory;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;
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
            long mark1 = PerfUIHelper.getTime();
            System.out.println("start record: "+ (mark1 -startMark));
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
            long t1 = PerfUIHelper.getTime();
            this.auditResult = PerfUIHelper.getAuditResult(driver,this.startMark);
            long t2 = PerfUIHelper.getTime();
            System.out.println("get metric: "+(t2-t1));
        }
    }

    private void reportResult(Description description){
        long m1 = PerfUIHelper.getTime();
        String recodedVideoPath = VideoRecorderHelper.stopRecording(description, this.recorder);
        long m2 = PerfUIHelper.getTime();
        System.out.println("stop record result: "+ (m2-m1));
        m1 = PerfUIHelper.getTime();
        metricSender.sendMetric(auditResult,recodedVideoPath,description);
        m2 = PerfUIHelper.getTime();
        System.out.println("report result: "+ (m2-m1));
    }
}
