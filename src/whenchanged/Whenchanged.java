package whenchanged;

import java.io.IOException;

public class Whenchanged {

    private static String PROPERTIES_FILENAME = "whenchanged.properties";
    
    public static void main(String[] args) {
        String propertiesFilename = PROPERTIES_FILENAME;
        if (args.length == 1) {
            propertiesFilename = args[0];
        }
        Settings settings = new Settings(propertiesFilename).load();
        Runnable callback = new CommandRunnable(settings.getCommandDir(), settings.getCommand());
        Runnable debounced = new DebouncedRunnable(callback, settings.getDelay());
        try {
            new FolderListener(debounced, settings.getFolders()).run();
        } catch (IOException ex) {
            System.err.println("something went wrong");
            System.exit(1);
        }
    }
}
