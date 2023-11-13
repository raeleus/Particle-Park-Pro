package com.ray3k.particleparkpro.runnables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;
import com.ray3k.particleparkpro.Core;
import com.ray3k.particleparkpro.FileDialogs;
import com.ray3k.particleparkpro.Settings;
import com.ray3k.particleparkpro.widgets.poptables.PopError;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Settings.*;

public class SaveAsRunnable implements Runnable {

    private static boolean open;
    private SaveRunnable saveRunnable;

    public void setSaveRunnable (SaveRunnable runnable) {
        saveRunnable = runnable;
    }

    @Override
    public void run () {
        if (open) return;

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            open = true;
            stage.getRoot().setTouchable(Touchable.disabled);

            var useFileExtension = preferences.getBoolean(NAME_PRESUME_FILE_EXTENSION, DEFAULT_PRESUME_FILE_EXTENSION);
            var filterPatterns = useFileExtension ? new String[] {"p"} : null;
            var saveHandle = FileDialogs.saveDialog("Save", getDefaultSavePath(), defaultFileName, filterPatterns, "Particle Files (*.p)");

            if (saveHandle != null) {
                openFileFileHandle = saveHandle;
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
        });
        service.shutdown();
    }
}
