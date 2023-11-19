package com.ray3k.particleparkpro.runnables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;
import com.ray3k.particleparkpro.Core;
import com.ray3k.particleparkpro.Settings;
import com.ray3k.particleparkpro.widgets.panels.EffectEmittersPanel;
import com.ray3k.particleparkpro.widgets.poptables.PopError;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Settings.*;
import static com.ray3k.particleparkpro.widgets.panels.EffectEmittersPanel.*;

public class SaveRunnable implements Runnable {

    private SaveAsRunnable saveAsRunnable;

    public void setSaveAsRunnable (SaveAsRunnable runnable) {
        saveAsRunnable = runnable;
    }

    @Override
    public void run () {
        if (openFileFileHandle != null) {
            if (preferences.getBoolean(NAME_PRESUME_FILE_EXTENSION, DEFAULT_PRESUME_FILE_EXTENSION) && !openFileFileHandle.extension().equals("p"))
                openFileFileHandle = openFileFileHandle.sibling(openFileFileHandle.name() + ".p");

            Settings.setDefaultSavePath(openFileFileHandle.parent());
            defaultFileName = openFileFileHandle.name();

            //enable all emitters for export
            particleEffect.getEmitters().clear();
            for (var entry : activeEmitters.entries()) {
                particleEffect.getEmitters().add(entry.key);
            }

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

            //reset enabled/disabled emitters
            particleEffect.getEmitters().clear();
            for (var entry : activeEmitters.entries()) {
                if (entry.value) particleEffect.getEmitters().add(entry.key);
            }

            effectEmittersPanel.hidePopEmitterControls();

            unsavedChangesMade = false;
            Core.updateWindowTitle();
        } else {
            saveAsRunnable.run();
        }
    }
}
