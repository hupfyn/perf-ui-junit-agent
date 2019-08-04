package perf.ui.junit.agent;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class PerfUITestRunner extends BlockJUnit4ClassRunner {
    private PerfUIListener perfUIListener;

    public PerfUITestRunner(Class<?> klass) throws InitializationError {
        super(klass);
        perfUIListener = new PerfUIListener();
    }

    @Override
    public void run(RunNotifier notifier) {
        try {
            notifier.addListener(perfUIListener);
            super.run(notifier);
        } finally {
            notifier.removeListener(perfUIListener);
        }
    }
}
