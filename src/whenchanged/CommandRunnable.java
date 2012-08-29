package whenchanged;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandRunnable implements Runnable {

    private static final Logger logger = Logger.getLogger(CommandRunnable.class.getName());
    private final String dir;
    private final List<String> command;
    private final boolean redirectErrorStream;
    private Process process;

    public CommandRunnable(final String dir, final List<String> command, final boolean redirectErrorStream) {
        this.dir = dir;
        this.command = command;
        this.redirectErrorStream = redirectErrorStream;
    }

    @Override
    public void run() {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(redirectErrorStream);
        pb.directory(new File(dir));

        try {
            logger.fine("running");
            process = pb.start();

            try (Reader br = new BufferedReader(
                            new InputStreamReader(
                            process.getInputStream(),
                            "UTF-8"))) {
                
                Writer wr = new BufferedWriter(
                        new OutputStreamWriter(System.out, "UTF-8"));
                char[] buffer = new char[1024];
                int n;
                while ((n = br.read(buffer)) != -1) {
                    wr.write(buffer, 0, n);
                }
                wr.flush();
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
