package whenchanged;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandRunnable implements Runnable {

    private static final Logger logger = Logger.getLogger(CommandRunnable.class.getName());
    private final String dir;
    private final List<String> command;
    private Process process;

    public CommandRunnable(final String dir, final List<String> command) {
        this.dir = dir;
        this.command = command;
    }

    @Override
    public void run() {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(dir));
        try {
            logger.fine("running");
            process = pb.start();

            try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(
                            process.getInputStream(),
                            "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
            int result = process.waitFor();
            if (result != 0) {
                logger.severe(String.format("The process ended with error code %s", result));
            }
            logger.fine("ran");
        } catch (InterruptedException | IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
}
