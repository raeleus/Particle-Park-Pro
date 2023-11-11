package com.ray3k.particleparkpro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;
import com.ray3k.particleparkpro.widgets.poptables.PopError;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static com.ray3k.particleparkpro.Core.*;

public class SaveRunnable implements Runnable {

    private SaveAsRunnable saveAsRunnable;

    public void setSaveAsRunnable (SaveAsRunnable runnable) {
        saveAsRunnable = runnable;
    }

    @Override
    public void run () {
        if (openFileFileHandle != null) {
            Settings.setDefaultSavePath(openFileFileHandle.parent());
            defaultFileName = openFileFileHandle.name();

            Writer fileWriter = null;
            try {
                fileWriter = new FileWriter(openFileFileHandle.file());
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
                if (fileHandle.parent().equals(openFileFileHandle.parent()))
                    break;
                try {
                    fileHandle.copyTo(openFileFileHandle.parent().child(fileHandle.name()));
                } catch (GdxRuntimeException e) {
                    var error = "Error copying files to save location.";
                    var pop = new PopError(error, e.getMessage());
                    pop.show(stage);

                    Gdx.app.error(Core.class.getName(), error, e);
                }
            }
        } else {
            saveAsRunnable.run();
        }
    }
}
