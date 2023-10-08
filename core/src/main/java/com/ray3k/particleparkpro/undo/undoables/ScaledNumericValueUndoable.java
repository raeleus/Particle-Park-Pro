package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.ray3k.particleparkpro.undo.Undoable;
import lombok.AllArgsConstructor;
import lombok.Builder;

import static com.ray3k.particleparkpro.Core.emitterPropertiesPanel;
@AllArgsConstructor
public class ScaledNumericValueUndoable implements Undoable {
    public final ScaledNumericValue newValue = new ScaledNumericValue();
    public final ScaledNumericValue oldValue = new ScaledNumericValue();
    private ScaledNumericValue value;
    private String description;

    @Override
    public void undo() {
        value.set(oldValue);
        refreshDisplay();
    }

    @Override
    public void redo() {
        value.set(newValue);
        refreshDisplay();
    }

    @Override
    public void start() {
        value.set(newValue);
    }

    @Override
    public String getDescription() {
        return description;
    }

    private void refreshDisplay() {
        emitterPropertiesPanel.populateScrollTable(null);
    }
}
