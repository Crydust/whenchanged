package whenchanged;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FolderListener {

    private static final Logger logger = Logger.getLogger(CommandRunnable.class.getName());
    private final Runnable callback;
    private final List<String> folders;
            
    public FolderListener(Runnable callback, final List<String> folders) {
        this.callback = callback;
        this.folders = folders;
    }

    public void run() {

        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            for (String folder : folders) {
                logger.fine(String.format("register watcher for folder: %s", folder));
                Path path = Paths.get(folder).toAbsolutePath();
                if (path.toFile().isDirectory()) {
                    path.register(watcher,
                            ENTRY_CREATE,
                            ENTRY_DELETE,
                            ENTRY_MODIFY);
                } else {
                    logger.warning(String.format("not a directory: %s", path.toString()));
                }
            }

            for (;;) {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind == OVERFLOW) {
                        continue;
                    }
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();
                    //Path child = dir.resolve(filename);
                    logger.fine(String.format("change detected: %s %s", filename.toString(), kind));
                    callback.run();
                }
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException | InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

    }
}
