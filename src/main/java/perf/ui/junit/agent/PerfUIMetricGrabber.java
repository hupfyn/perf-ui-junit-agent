package perf.ui.junit.agent;

import com.automation.remarks.video.RecorderFactory;
import com.automation.remarks.video.enums.RecorderType;
import com.automation.remarks.video.recorder.IVideoRecorder;
import org.aeonbits.owner.ConfigFactory;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;
import perf.ui.junit.agent.annotations.PerfUI;
import perf.ui.junit.agent.helper.PerfUIHelper;
import perf.ui.junit.agent.helper.VideoRecorderHelper;

import java.util.Date;
import java.util.Map;

public class PerfUIMetricGrabber extends TestWatcher {


    private boolean isAnotation;
    private IVideoRecorder recorder;
    private PerfUIConfig perfUIConfig;
    private long startMark;
    private long endMark;
    private Map<String, Object> performanceMetric;
    private String auditResult;

    public PerfUIMetricGrabber() {
        perfUIConfig = ConfigFactory.create(PerfUIConfig.class);
    }

    @Override
    protected void starting(Description description) {
        this.isAnotation = PerfUIHelper.checkForAnnotationIsPresent(description);
        if (this.isAnotation) {
            this.recorder = RecorderFactory.getRecorder(RecorderType.FFMPEG);
            startMark = new Date().getTime();
            recorder.start();
        }
    }

    @Override
    protected void succeeded(Description description) {
        if (this.isAnotation) {
            System.out.println("Test status is ok");
            this.endMark = new Date().getTime();
            String recodedVideoPath = VideoRecorderHelper.stopRecording(description, this.recorder, true);
            String nameForReport = description.getAnnotation(PerfUI.class).name().length() != 0 ? description.getAnnotation(PerfUI.class).name() : description.getMethodName();
            String[] dataToSend = new String[2];
            dataToSend[0] = PerfUIHelper.prepareData(nameForReport,startMark, endMark, "Passed", this.performanceMetric);
            dataToSend[1] = this.auditResult;
            PerfUIHelper.sendMetrick(perfUIConfig.protocol(), perfUIConfig.host(), perfUIConfig.port(), dataToSend, recodedVideoPath);
        }
    }

    @Override
    protected void failed(Throwable e, Description description) {
        if (this.isAnotation) {
            System.out.println("Test status is ko");
            this.endMark = new Date().getTime();
            String recordedVideoPath = VideoRecorderHelper.stopRecording(description, this.recorder, false);
            String nameForReport = description.getAnnotation(PerfUI.class).name().length() != 0 ? description.getAnnotation(PerfUI.class).name() : description.getMethodName();
            String[] dataToSend = new String[2];
            dataToSend[0] = PerfUIHelper.prepareData(nameForReport,startMark, endMark, "Failure", this.performanceMetric);
            dataToSend[1] = this.auditResult;
            PerfUIHelper.sendMetrick(perfUIConfig.protocol(), perfUIConfig.host(), perfUIConfig.port(), dataToSend, recordedVideoPath);
        }
    }

    public void runAudit(WebDriver driver) {
        if (this.isAnotation) {
            this.performanceMetric = PerfUIHelper.getPerformanceMetric(driver);
            this.auditResult = PerfUIHelper.getAuditResult(driver);
        }
    }
}
