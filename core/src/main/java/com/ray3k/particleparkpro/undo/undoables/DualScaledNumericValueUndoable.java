package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.math.MathUtils;
import com.ray3k.particleparkpro.undo.Undoable;
import com.ray3k.particleparkpro.widgets.LineGraph;
import com.ray3k.particleparkpro.widgets.ToggleWidget;
import com.ray3k.stripe.Spinner;
import lombok.Builder;

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
        if (data.spinnerHigh != null) data.spinnerHigh.setValue(data.xValue.getHighMin());
        if (data.spinnerHighMin != null) data.spinnerHighMin.setValue(data.xValue.getHighMin());
        if (data.spinnerHighMax != null) data.spinnerHighMax.setValue(data.xValue.getHighMax());
        if (data.toggleWidgetHigh != null && !MathUtils.isEqual(data.xValue.getHighMin(), data.xValue.getHighMax())) data.toggleWidgetHigh.showTable2();

        if (data.spinnerLow != null) data.spinnerLow.setValue(data.xValue.getLowMin());
        if (data.spinnerLowMin != null) data.spinnerLowMin.setValue(data.xValue.getLowMin());
        if (data.spinnerLowMax != null) data.spinnerLowMax.setValue(data.xValue.getLowMax());
        if (data.toggleWidgetLow != null && !MathUtils.isEqual(data.xValue.getLowMin(), data.xValue.getLowMax())) data.toggleWidgetLow.showTable2();

        if (data.graph != null) data.graph.setNodes(data.xValue.getTimeline(), data.xValue.getScaling());
        if (data.graphExpanded != null) data.graphExpanded.setNodes(data.xValue.getTimeline(), data.xValue.getScaling());
    }

    @Builder(toBuilder = true)
    public static class DualScaledNumericValueUndoableData {
        public final ScaledNumericValue newXvalue = new ScaledNumericValue();
        public final ScaledNumericValue oldXvalue = new ScaledNumericValue();
        private ScaledNumericValue xValue;
        private Spinner spinnerLow;
        private Spinner spinnerLowMin;
        private Spinner spinnerLowMax;
        private Spinner spinnerHigh;
        private Spinner spinnerHighMin;
        private Spinner spinnerHighMax;
        private ToggleWidget toggleWidgetLow;
        private ToggleWidget toggleWidgetHigh;
        private LineGraph graph;
        private LineGraph graphExpanded;

        public final ScaledNumericValue newYvalue = new ScaledNumericValue();
        public final ScaledNumericValue oldYvalue = new ScaledNumericValue();
        private ScaledNumericValue yValue;

        private String description;
    }
}
