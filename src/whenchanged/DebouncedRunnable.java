package whenchanged;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

class DebouncedRunnable implements Runnable {

    private static final Logger logger = Logger.getLogger(DebouncedRunnable.class.getName());
    private Runnable callback;
    private long delay;
    private Timer task;
    private boolean isRunning;
    private boolean isRequestedAfterRun;

    /**
     *
     * @param callback to run after there haven't been any runs for a while
     * @param delay how long in milliseconds to wait before running callback
     */
    public DebouncedRunnable(Runnable callback, long delay) {
        this.callback = callback;
        this.delay = delay;
        this.isRunning = false;
        this.isRequestedAfterRun = false;
    }

    @Override
    public void run() {
        if (task != null) {
            task.cancel();
        }
        if (isRunning) {
            isRequestedAfterRun = true;
        } else {
            isRequestedAfterRun = false;
            task = new Timer();
            task.schedule(new TimerTask() {
                @Override
                public void run() {
                    isRunning = true;
                    callback.run();
                    isRunning = false;
                    if (isRequestedAfterRun) {
                        isRequestedAfterRun = false;
                        logger.info("debouncer.call again");
                        DebouncedRunnable.this.run();
                    }
                }
            }, this.delay);
        }
    }
}