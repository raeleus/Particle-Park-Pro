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
        if (data.spinnerHigh != null) data.spinnerHigh.setValue(data.value.getHighMin());
        if (data.spinnerHighMin != null) data.spinnerHighMin.setValue(data.value.getHighMin());
        if (data.spinnerHighMax != null) data.spinnerHighMax.setValue(data.value.getHighMax());
        if (data.toggleWidgetHigh != null && !MathUtils.isEqual(data.value.getHighMin(), data.value.getHighMax())) data.toggleWidgetHigh.showTable2();

        if (data.spinnerLow != null) data.spinnerLow.setValue(data.value.getLowMin());
        if (data.spinnerLowMin != null) data.spinnerLowMin.setValue(data.value.getLowMin());
        if (data.spinnerLowMax != null) data.spinnerLowMax.setValue(data.value.getLowMax());
        if (data.toggleWidgetLow != null && !MathUtils.isEqual(data.value.getLowMin(), data.value.getLowMax())) data.toggleWidgetLow.showTable2();

        if (data.graph != null) data.graph.setNodes(data.value.getTimeline(), data.value.getScaling());
        if (data.graphExpanded != null) data.graphExpanded.setNodes(data.value.getTimeline(), data.value.getScaling());
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
