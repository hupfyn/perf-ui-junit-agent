package perf.ui.junit.agent.helper;

import com.automation.remarks.video.RecordingUtils;
import com.automation.remarks.video.recorder.IVideoRecorder;
import org.junit.runner.Description;
import perf.ui.junit.agent.config.PerfUIConfig;


import static com.automation.remarks.video.RecordingUtils.doVideoProcessing;

import java.io.File;
import java.util.Objects;

public class VideoRecorderHelper {

    private static String getFileName(Description description) {
        String methodName = description.getMethodName();
        return RecordingUtils.getVideoFileName(null,methodName);
    }

    public static String stopRecording(Description description, IVideoRecorder recorder) {
        String filename = getFileName(description);
        PerfUIHelper.syncTimeout(100);
        File file = Objects.nonNull(recorder)?recorder.stopAndSave(filename):null;
        return doVideoProcessing(true,file);
    }

    public static void setConfigValueForRecorder(PerfUIConfig config) {
        System.setProperty("video.folder","PerfUiVideo");
        System.setProperty("video.save.mode", "ALL");
        System.setProperty("video.frame.rate", config.frameRate());
        if (Objects.nonNull(config.videoDisplay())) {
            System.setProperty("ffmpeg.display", config.videoDisplay());
        }
    }
}
