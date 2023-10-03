package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.RangedNumericValue;
import com.badlogic.gdx.math.MathUtils;
import com.ray3k.particleparkpro.undo.Undoable;
import com.ray3k.particleparkpro.widgets.ToggleWidget;
import com.ray3k.stripe.Spinner;
import lombok.Builder;

public class RangedNumericValueUndoable implements Undoable {
    private RangedNumericValueUndoableData data;

    public RangedNumericValueUndoable(RangedNumericValueUndoableData data) {
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
        data.spinner.setValue(data.value.getLowMin());
        data.spinnerMin.setValue(data.value.getLowMin());
        data.spinnerMax.setValue(data.value.getLowMax());

        if (!MathUtils.isEqual(data.value.getLowMin(), data.value.getLowMax())) data.toggleWidget.showTable2();
    }

    @Builder(toBuilder = true)
    public static class RangedNumericValueUndoableData {
        public final RangedNumericValue newValue = new RangedNumericValue();
        public final RangedNumericValue oldValue = new RangedNumericValue();
        private RangedNumericValue value;
        private Spinner spinner;
        private Spinner spinnerMin;
        private Spinner spinnerMax;
        private ToggleWidget toggleWidget;
        private String description;
    }
}
