package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.ray3k.particleparkpro.undo.Undoable;
import lombok.Builder;

import static com.ray3k.particleparkpro.Core.emitterPropertiesPanel;

public class ScaledNumericValueUndoable implements Undoable {
    private ScaledNumericValueUndoableData data;

    public ScaledNumericValueUndoable(ScaledNumericValueUndoableData data) {
        this.data = data;
    }

    @Override
    public void undo() {
        data.value.set(data.oldValue);
        refreshDisplay();
    }

    @Override
    public void redo() {
        data.value.set(data.newValue);
        refreshDisplay();
    }

    @Override
    public void start() {
        data.value.set(data.newValue);
    }

    @Override
    public String getDescription() {
        return data.description;
    }

    private void refreshDisplay() {
        emitterPropertiesPanel.populateScrollTable(null);
    }

    @Builder(toBuilder = true)
    public static class ScaledNumericValueUndoableData {
        public final ScaledNumericValue newValue = new ScaledNumericValue();
        public final ScaledNumericValue oldValue = new ScaledNumericValue();
        private ScaledNumericValue value;
        private String description;
    }
}
