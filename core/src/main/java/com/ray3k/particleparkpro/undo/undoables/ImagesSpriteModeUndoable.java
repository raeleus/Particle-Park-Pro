package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpriteMode;
import com.ray3k.particleparkpro.undo.Undoable;
import lombok.AllArgsConstructor;

import static com.ray3k.particleparkpro.Core.emitterPropertiesPanel;
import static com.ray3k.particleparkpro.Core.selectedEmitter;

@AllArgsConstructor
public class ImagesSpriteModeUndoable implements Undoable {
    private SpriteMode spriteMode;
    private SpriteMode oldSpriteMode;
    private String description;

    @Override
    public void undo() {
        selectedEmitter.setSpriteMode(oldSpriteMode);
        refreshDisplay();
    }

    @Override
    public void redo() {
        selectedEmitter.setSpriteMode(spriteMode);
        refreshDisplay();
    }

    @Override
    public void start() {
        selectedEmitter.setSpriteMode(spriteMode);
    }

    @Override
    public String getDescription() {
        return description;
    }

    private void refreshDisplay() {
        emitterPropertiesPanel.populateScrollTable(null);
    }
}
