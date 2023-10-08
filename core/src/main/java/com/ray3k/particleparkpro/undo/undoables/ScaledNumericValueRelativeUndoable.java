package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.ray3k.particleparkpro.undo.Undoable;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.ray3k.particleparkpro.Core.emitterPropertiesPanel;

@Data
@AllArgsConstructor
public class ScaledNumericValueRelativeUndoable implements Undoable {
    private ScaledNumericValue value;
    private boolean active;
    private String description;

    @Override
    public void undo() {
        value.setRelative(!active);
        refreshDisplay();
    }

    @Override
    public void redo() {
        value.setRelative(active);
        refreshDisplay();
    }

    @Override
    public void start() {
        value.setRelative(active);
    }

    @Override
    public String getDescription() {
        return description;
    }

    private void refreshDisplay() {
        emitterPropertiesPanel.populateScrollTable(null);
    }
}