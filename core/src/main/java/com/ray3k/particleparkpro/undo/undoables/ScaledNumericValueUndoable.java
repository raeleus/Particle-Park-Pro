package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.RangedNumericValue;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.math.MathUtils;
import com.ray3k.particleparkpro.undo.Undoable;
import com.ray3k.particleparkpro.widgets.LineGraph;
import com.ray3k.particleparkpro.widgets.ToggleWidget;
import com.ray3k.stripe.Spinner;
import lombok.Builder;

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
            data.spinnerHigh.setValue(data.value.getHighMin());
            data.spinnerHighMin.setValue(data.value.getHighMin());
            data.spinnerHighMax.setValue(data.value.getHighMax());
            if (!MathUtils.isEqual(data.value.getHighMin(), data.value.getHighMax())) data.toggleWidgetHigh.showTable2();

            data.spinnerLow.setValue(data.value.getLowMin());
            data.spinnerLowMin.setValue(data.value.getLowMin());
            data.spinnerLowMax.setValue(data.value.getLowMax());
            if (!MathUtils.isEqual(data.value.getLowMin(), data.value.getLowMax())) data.toggleWidgetLow.showTable2();

            data.graph.setNodes(data.value.getTimeline(), data.value.getScaling());
            data.graphExpanded.setNodes(data.value.getTimeline(), data.value.getScaling());
    }

    @Builder(toBuilder = true)
    public static class ScaledNumericValueUndoableData {
        public final ScaledNumericValue newValue = new ScaledNumericValue();
        public final ScaledNumericValue oldValue = new ScaledNumericValue();
        private ScaledNumericValue value;
        private Spinner spinnerLow;
        private Spinner spinnerLowMin;
        private Spinner spinnerLowMax;
        private Spinner spinnerHigh;
        private Spinner spinnerHighMin;
        private Spinner spinnerHighMax;
        private ToggleWidget toggleWidgetLow;
        private ToggleWidget toggleWidgetHigh;
        private String description;
        private LineGraph graph;
        private LineGraph graphExpanded;
    }
}
