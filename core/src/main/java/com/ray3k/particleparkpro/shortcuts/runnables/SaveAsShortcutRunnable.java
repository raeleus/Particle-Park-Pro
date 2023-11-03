package com.ray3k.particleparkpro.shortcuts.runnables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;
import com.ray3k.particleparkpro.Core;
import com.ray3k.particleparkpro.widgets.poptables.PopError;
import com.ray3k.particleparkpro.FileDialogs;
import com.ray3k.particleparkpro.Settings;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.*;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Settings.*;

public class SaveAsShortcutRunnable implements Runnable {

    private static boolean open;

    private SaveRunnable saveRunnable;
    private ExecutorService service;

    public SaveAsShortcutRunnable () {
        saveRunnable = new SaveRunnable();
        service = Executors.newSingleThreadExecutor();

        Gdx.app.addLifecycleListener(new LifecycleListener() {
            @Override
            public void pause () {

            }

            @Override
            public void resume () {

            }

            @Override
            public void dispose () {
                service.shutdownNow();
            }
        });
    }

    @Override
    public synchronized void run () {
        if (open) return;
        service.execute(saveRunnable);
    }

    private static class SaveRunnable implements Runnable {

        @Override
        public synchronized void run () {
            open = true;
            stage.unfocusAll();
            stage.getRoot().setTouchable(Touchable.disabled);

            var useFileExtension = preferences.getBoolean(NAME_PRESUME_FILE_EXTENSION, DEFAULT_PRESUME_FILE_EXTENSION);
            var filterPatterns = useFileExtension ? new String[] {"p"} : null;
            var saveHandle = FileDialogs.saveDialog("Save", getDefaultSavePath(), defaultFileName, filterPatterns, "Particle Files (*.p)");

            if (saveHandle != null) {
                saveFileHandle = saveHandle;
                Settings.setDefaultSavePath(saveHandle.parent());
                defaultFileName = saveHandle.name();

                Writer fileWriter = null;
                try {
                    fileWriter = new FileWriter(saveHandle.file());
                    particleEffect.save(fileWriter);
                } catch (IOException e) {
                    var error = "Error saving particle file.";
                    var pop = new PopError(error, e.getMessage());
                    pop.show(stage);

                    Gdx.app.error(Core.class.getName(), error, e);
                } finally {
                    StreamUtils.closeQuietly(fileWriter);
                }

                for (var fileHandle : fileHandles.values()) {
                    if (fileHandle.parent().equals(saveHandle.parent()))
                        break;
                    try {
                        fileHandle.copyTo(saveHandle.parent().child(fileHandle.name()));
                    } catch (GdxRuntimeException e) {
                        var error = "Error copying files to save location.";
                        var pop = new PopError(error, e.getMessage());
                        pop.show(stage);

                        Gdx.app.error(Core.class.getName(), error, e);
                    }
                }
            }

            stage.getRoot().setTouchable(Touchable.enabled);
            open = false;
        }
    }
}
