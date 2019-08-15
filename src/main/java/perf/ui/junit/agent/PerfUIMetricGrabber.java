package perf.ui.junit.agent;

import com.automation.remarks.video.RecorderFactory;
import com.automation.remarks.video.enums.RecorderType;
import com.automation.remarks.video.recorder.IVideoRecorder;
import org.aeonbits.owner.ConfigFactory;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import perf.ui.junit.agent.annotations.PerfUI;
import perf.ui.junit.agent.helper.PerfUIHelper;
import perf.ui.junit.agent.helper.VideoRecorderHelper;

import java.util.Date;
import java.util.Map;

public class PerfUIMetricGrabber extends TestWatcher {


    private boolean isAnnotation;
    private IVideoRecorder recorder;
    private PerfUIConfig perfUIConfig;
    private long startMark;
    private long endMark;
    private Map<String, Object> performanceMetric;
    private String auditResult;
    private String resourceTimingIE = "none";

    public PerfUIMetricGrabber() {
        perfUIConfig = ConfigFactory.create(PerfUIConfig.class);
    }

    @Override
    protected void starting(Description description) {
        this.isAnnotation = PerfUIHelper.checkForAnnotationIsPresent(description);
        if (this.isAnnotation) {
            this.recorder = RecorderFactory.getRecorder(RecorderType.FFMPEG);
            startMark = new Date().getTime();
            recorder.start();
        }
    }

    @Override
    protected void succeeded(Description description) {
        if (this.isAnnotation) {
            System.out.println("Test status is ok");
            this.endMark = PerfUIHelper.getTime();
            reportResult(description,"Passed");
        }
    }

    @Override
    protected void failed(Throwable e, Description description) {
        if (this.isAnnotation) {
            System.out.println("Test status is ko");
            this.endMark = PerfUIHelper.getTime();
            reportResult(description,"Failure");
        }
    }

    public void runAudit(WebDriver driver) {
        if (this.isAnnotation) {
            if(driver.getClass() == InternetExplorerDriver.class){
                this.resourceTimingIE = PerfUIHelper.getResourceTimingForIE11(driver);
            }
            this.performanceMetric = PerfUIHelper.getPerformanceMetric(driver);
            this.auditResult = PerfUIHelper.getAuditResult(driver);
        }
    }

    private void reportResult(Description description, String status){
        String recodedVideoPath = VideoRecorderHelper.stopRecording(description, this.recorder, true);
        String nameForReport = description.getAnnotation(PerfUI.class).name().length() != 0 ? description.getAnnotation(PerfUI.class).name() : description.getMethodName();
        String[] dataToSend = new String[3];
        dataToSend[0] = PerfUIHelper.prepareData(nameForReport,this.startMark, this.endMark, status, this.performanceMetric);
        dataToSend[1] = this.auditResult;
        dataToSend[2] = this.resourceTimingIE;
        PerfUIHelper.sendMetrick(perfUIConfig.protocol(), perfUIConfig.host(), perfUIConfig.port(), dataToSend, recodedVideoPath);
    }
}
