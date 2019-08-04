package perf.ui.junit.agent;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import org.openqa.selenium.WebDriver;

import java.lang.annotation.Annotation;
import java.util.Collection;

public class PerfUIListener extends RunListener {

    private WebDriver driver;
    private static final String PERF_ANNOTATION = "@perf.ui.junit.agent.annotations.PerfUI()";


    @Override
    public void testFinished(Description description) throws Exception {
        if (checkForAnnotationIsPresent(description.getAnnotations())) {
            System.out.println("My annotation is present");
            Object testClass = description.getTestClass();
            /// TODO: need to get WebDriver here
            driver = testClass.getWebDriver();
            String performanceMetric = new PerfUIMetricGrabber(driver).getPerformanceMetric();
            System.out.println(performanceMetric);
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
