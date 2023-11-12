package com.ray3k.particleparkpro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.ray3k.particleparkpro.undo.UndoManager;
import com.ray3k.particleparkpro.widgets.panels.EffectEmittersPanel;
import com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Settings.*;

public class OpenRunnable implements Runnable {

    public static boolean open;

    @Override
    public void run () {
        if (open)
            return;

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            open = true;
            stage.getRoot().setTouchable(Touchable.disabled);

            EffectEmittersPanel effectEmittersPanel = EffectEmittersPanel.effectEmittersPanel;
            EmitterPropertiesPanel emitterPropertiesPanel = EmitterPropertiesPanel.emitterPropertiesPanel;

            if (effectEmittersPanel == null || emitterPropertiesPanel == null)
                return;

            var useFileExtension = preferences.getBoolean(NAME_PRESUME_FILE_EXTENSION, DEFAULT_PRESUME_FILE_EXTENSION);
            var filterPatterns = useFileExtension ? new String[] {"p"} : null;
            var fileHandle = FileDialogs.openDialog("Open", getDefaultSavePath(), filterPatterns, "Particle files (*.p)");

            if (fileHandle != null) {
                defaultFileName = fileHandle.name();
                Settings.setDefaultSavePath(fileHandle.parent());
                Gdx.app.postRunnable(() -> {
                    loadOnMainThread(fileHandle);
                });

                openFileFileHandle = fileHandle;
            }

            stage.getRoot().setTouchable(Touchable.enabled);
            open = false;
        });
        service.shutdown();
    }

    private void loadOnMainThread (FileHandle fileHandle) {
        EffectEmittersPanel effectEmittersPanel = EffectEmittersPanel.effectEmittersPanel;
        EmitterPropertiesPanel emitterPropertiesPanel = EmitterPropertiesPanel.emitterPropertiesPanel;
        Utils.loadParticle(fileHandle);
        selectedEmitter = particleEffect.getEmitters().first();

        effectEmittersPanel.populateEmitters();
        effectEmittersPanel.updateDisableableWidgets();
        emitterPropertiesPanel.populateScrollTable(null);

        UndoManager.clear();
    }
}
