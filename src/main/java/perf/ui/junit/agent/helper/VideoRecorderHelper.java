package perf.ui.junit.agent.helper;

import com.automation.remarks.video.RecordingUtils;
import com.automation.remarks.video.recorder.IVideoRecorder;
import org.junit.runner.Description;


import static com.automation.remarks.video.RecordingUtils.doVideoProcessing;

import java.io.File;

public class VideoRecorderHelper {

    private static String getFileName(Description description) {
        String methodName = description.getMethodName();
        return RecordingUtils.getVideoFileName(null,methodName);
    }

    public static String stopRecording(Description description ,IVideoRecorder recorder,boolean testStatus) {
        String filename = getFileName(description);
        File file = null;
        if (recorder != null) {
             file = recorder.stopAndSave(filename);
        }
        return doVideoProcessing(testStatus,file);
    }
}
