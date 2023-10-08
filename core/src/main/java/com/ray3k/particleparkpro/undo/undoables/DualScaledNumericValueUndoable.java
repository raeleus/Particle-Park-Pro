package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.math.MathUtils;
import com.ray3k.particleparkpro.undo.Undoable;
import com.ray3k.particleparkpro.widgets.LineGraph;
import com.ray3k.particleparkpro.widgets.ToggleWidget;
import com.ray3k.stripe.Spinner;
import lombok.Builder;

import static com.ray3k.particleparkpro.Core.emitterPropertiesPanel;

public class DualScaledNumericValueUndoable implements Undoable {
    private DualScaledNumericValueUndoableData data;

    public DualScaledNumericValueUndoable(DualScaledNumericValueUndoableData data) {
        this.data = data;
    }

    @Override
    public void undo() {
        data.xValue.set(data.oldXvalue);
        data.yValue.set(data.oldYvalue);
        refreshDisplay();
    }

    @Override
    public void redo() {
        data.xValue.set(data.newXvalue);
        data.yValue.set(data.newYvalue);
        refreshDisplay();
    }

    @Override
    public void start() {
        data.xValue.set(data.newXvalue);
        data.yValue.set(data.newYvalue);
    }

    @Override
    public String getDescription() {
        return data.description;
    }

    private void refreshDisplay() {
        emitterPropertiesPanel.populateScrollTable(null);
    }

    @Builder(toBuilder = true)
    public static class DualScaledNumericValueUndoableData {
        public final ScaledNumericValue newXvalue = new ScaledNumericValue();
        public final ScaledNumericValue oldXvalue = new ScaledNumericValue();
        private ScaledNumericValue xValue;
        public final ScaledNumericValue newYvalue = new ScaledNumericValue();
        public final ScaledNumericValue oldYvalue = new ScaledNumericValue();
        private ScaledNumericValue yValue;
        private String description;
    }
}
