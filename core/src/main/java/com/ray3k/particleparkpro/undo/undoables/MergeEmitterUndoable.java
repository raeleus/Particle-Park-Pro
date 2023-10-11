package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.ray3k.particleparkpro.undo.Undoable;
import lombok.Builder;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Core.emitterPropertiesPanel;
import static com.ray3k.particleparkpro.widgets.panels.EffectEmittersPanel.effectEmittersPanel;

@Builder
public class MergeEmitterUndoable implements Undoable {
    private Array<ParticleEmitter> oldEmitters;
    private ObjectMap<ParticleEmitter, Boolean> oldActiveEmitters;
    private ObjectMap<String, FileHandle> oldFileHandles;
    private ObjectMap<String, Sprite> oldSprites;
    private int oldSelectedIndex;
    private Array<ParticleEmitter> newEmitters;
    private ObjectMap<ParticleEmitter, Boolean> newActiveEmitters;
    private ObjectMap<String, FileHandle> newFileHandles;
    private ObjectMap<String, Sprite> newSprites;
    private int newSelectedIndex;
    private String description;

    @Override
    public void undo() {
        particleEffect.getEmitters().clear();
        particleEffect.getEmitters().addAll(oldEmitters);

        activeEmitters.clear();
        activeEmitters.putAll(oldActiveEmitters);

        fileHandles.clear();
        fileHandles.putAll(oldFileHandles);

        sprites.clear();
        sprites.putAll(oldSprites);

        selectedEmitter = particleEffect.getEmitters().get(oldSelectedIndex);

        refreshDisplay();
    }

    @Override
    public void redo() {
        particleEffect.getEmitters().clear();
        particleEffect.getEmitters().addAll(newEmitters);

        activeEmitters.clear();
        activeEmitters.putAll(newActiveEmitters);

        fileHandles.clear();
        fileHandles.putAll(newFileHandles);

        sprites.clear();
        sprites.putAll(newSprites);

        selectedEmitter = particleEffect.getEmitters().get(newSelectedIndex);

        refreshDisplay();
    }

    @Override
    public void start() {
        particleEffect.getEmitters().clear();
        particleEffect.getEmitters().addAll(newEmitters);

        activeEmitters.clear();
        activeEmitters.putAll(newActiveEmitters);

        fileHandles.clear();
        fileHandles.putAll(newFileHandles);

        sprites.clear();
        sprites.putAll(newSprites);

        selectedEmitter = particleEffect.getEmitters().get(newSelectedIndex);
    }

    @Override
    public String getDescription() {
        return description;
    }

    private void refreshDisplay() {
        effectEmittersPanel.populateEmitters();
        effectEmittersPanel.updateEmitterButtons();
        emitterPropertiesPanel.populateScrollTable(null);
    }
}
