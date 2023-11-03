package com.ray3k.particleparkpro.shortcuts.runnables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;
import com.ray3k.particleparkpro.Core;
import com.ray3k.particleparkpro.Settings;
import com.ray3k.particleparkpro.widgets.poptables.PopError;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static com.ray3k.particleparkpro.Core.*;

public class SaveShortcutRunnable implements Runnable {

    SaveAsShortcutRunnable saveAsShortcutRunnable;

    public SaveShortcutRunnable(SaveAsShortcutRunnable saveAsShortcutRunnable) {
       this.saveAsShortcutRunnable = saveAsShortcutRunnable;
    }

    @Override
    public void run () {
       if (saveFileHandle != null) {
           FileHandle saveHandle = saveFileHandle;
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
       } else {
           saveAsShortcutRunnable.run();
       }
    }
}
