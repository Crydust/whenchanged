package whenchanged;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Whenchanged {

    private static final Logger logger = Logger.getLogger(Whenchanged.class.getName());
    private static String PROPERTIES_FILENAME = "whenchanged.properties";

    public static void main(String[] args) {
        String propertiesFilename = PROPERTIES_FILENAME;
        if (args.length == 1) {
            propertiesFilename = args[0];
        }
        Settings settings = new Settings(propertiesFilename).load();
        Runnable callback = new CommandRunnable(
                settings.getCommandDir(),
                settings.getCommand(),
                settings.getRedirectErrorStream());
        Runnable debounced = new DebouncedRunnable(
                callback,
                settings.getDelay());
        debounced.run();
        try {
            new FolderListener(
                    debounced,
                    settings.getFolders()).run();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "something went wrong", ex);
            System.exit(1);
        }
    }
}
