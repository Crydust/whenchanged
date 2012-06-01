package whenchanged;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Settings {

    private static final Logger logger = Logger.getLogger(Settings.class.getName());
    private static String DELAY_PROPERTYNAME = "delay";
    private static String COMMAND_DIR_PROPERTYNAME = "command.dir";
    private static String COMMAND_SIZE_PROPERTYNAME = "command.size";
    private static String COMMAND_ITEM_PROPERTYNAME = "command.%d";
    private static String FOLDER_SIZE_PROPERTYNAME = "folders.size";
    private static String FOLDER_ITEM_PROPERTYNAME = "folders.%d";
    private final String filename;
    private long delay;
    private String commandDir;
    private List<String> command;
    private List<String> folders;

    public Settings(String filename) {
        this.filename = filename;
        folders = new ArrayList<>();
    }

    private static List<String> getList(final String sizePropertyName, final String itemPropertyName, final Properties properties){
        int size = Integer.valueOf(properties.getProperty(sizePropertyName));
        List<String> items = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            items.add(properties.getProperty(String.format(itemPropertyName, i)));
        }
        return items;
    }
    
    private static void setList(final List<String> items, final String sizePropertyName, final String itemPropertyName, final Properties properties){
        properties.setProperty(sizePropertyName, Integer.toString(items.size()));
        for (int i = 0; i < items.size(); i++) {
            properties.setProperty(String.format(itemPropertyName, i), items.get(i));
        }
    }
    
    public Settings load() {
        Properties properties = new Properties();
        try {
            logger.config(String.format("loading properties filename = %s", filename));
            try (FileInputStream in = new FileInputStream(filename)) {
                properties.load(in);
            }
            delay = Long.parseLong(properties.getProperty(DELAY_PROPERTYNAME));
            logger.config(String.format("%s = %d", DELAY_PROPERTYNAME, delay));
            commandDir = properties.getProperty(COMMAND_DIR_PROPERTYNAME);
            logger.config(String.format("%s = %s", COMMAND_DIR_PROPERTYNAME, commandDir));
            command = getList(COMMAND_SIZE_PROPERTYNAME, COMMAND_ITEM_PROPERTYNAME, properties);
            logger.config(String.format("%s = %s", COMMAND_SIZE_PROPERTYNAME, properties.getProperty(COMMAND_SIZE_PROPERTYNAME)));
            for (String commandpart : command) {
                logger.config(String.format("%s = %s", COMMAND_ITEM_PROPERTYNAME, commandpart));
            }
            folders = getList(FOLDER_SIZE_PROPERTYNAME, FOLDER_ITEM_PROPERTYNAME, properties);
            logger.config(String.format("%s = %s", FOLDER_SIZE_PROPERTYNAME, properties.getProperty(FOLDER_SIZE_PROPERTYNAME)));
            for (String folder : folders) {
                logger.config(String.format("%s = %s", FOLDER_ITEM_PROPERTYNAME, folder));
            }
        } catch (IOException | NumberFormatException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return this;
    }

    public Settings store() {
        Properties properties = new Properties();
        properties.setProperty(DELAY_PROPERTYNAME, Long.toString(delay));
        properties.setProperty(COMMAND_DIR_PROPERTYNAME, commandDir);
        setList(command, COMMAND_SIZE_PROPERTYNAME, COMMAND_ITEM_PROPERTYNAME, properties);
        setList(folders, FOLDER_SIZE_PROPERTYNAME, FOLDER_ITEM_PROPERTYNAME, properties);
        try {
            try (FileOutputStream out = new FileOutputStream(filename)) {
                properties.store(out, null);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return this;
    }

    public long getDelay() {
        return delay;
    }

    public String getCommandDir() {
        return commandDir;
    }

    public List<String> getCommand() {
        return Collections.unmodifiableList(command);
    }
    
    public List<String> getFolders() {
        return Collections.unmodifiableList(folders);
    }
    
}
