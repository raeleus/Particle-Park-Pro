package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.ray3k.particleparkpro.undo.Undoable;
import lombok.AllArgsConstructor;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.widgets.panels.EffectEmittersPanel.effectEmittersPanel;

@AllArgsConstructor
public class RenameEmitterUndoable implements Undoable {
    private ParticleEmitter emitter;
    private String oldName;
    private String newName;
    private String description;

    @Override
    public void undo() {
        emitter.setName(oldName);
        refreshDisplay();
    }

    @Override
    public void redo() {
        emitter.setName(newName);
        refreshDisplay();
    }

    @Override
    public void start() {
        emitter.setName(newName);
    }

    @Override
    public String getDescription() {
        return description;
    }

    private void refreshDisplay() {
        effectEmittersPanel.populateEmitters();
        effectEmittersPanel.updateDeleteButton();
        emitterPropertiesPanel.populateScrollTable(null);
    }
}