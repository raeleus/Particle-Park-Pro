package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.RangedNumericValue;
import com.badlogic.gdx.math.MathUtils;
import com.ray3k.particleparkpro.undo.Undoable;
import com.ray3k.particleparkpro.widgets.ToggleWidget;
import com.ray3k.stripe.Spinner;

import static com.ray3k.particleparkpro.Core.emitterPropertiesPanel;
import static com.ray3k.particleparkpro.Core.selectedEmitter;

public class RangedNumericValueUndoable implements Undoable {
    private RangedNumericValue newValue;
    private RangedNumericValue oldValue;
    private RangedNumericValue value;
    private Spinner spinner;
    private Spinner spinnerMin;
    private Spinner spinnerMax;
    private ToggleWidget toggleWidget;
    private String description;

    public RangedNumericValueUndoable(RangedNumericValue value, RangedNumericValue newValue, Spinner spinnerMin, Spinner spinnerMax, Spinner spinner, ToggleWidget toggleWidget, String description) {
        this.newValue = new RangedNumericValue();
        this.newValue.set(newValue);

        oldValue = new RangedNumericValue();
        oldValue.set(value);

        this.value = value;
        this.spinnerMin = spinnerMin;
        this.spinnerMax = spinnerMax;
        this.spinner = spinner;
        this.toggleWidget = toggleWidget;
        this.description = description;
    }

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
        if (spinner.getStage() != null) spinner.setValue(value.getLowMin());
        if (spinnerMin.getStage() != null) spinnerMin.setValue(value.getLowMin());
        if (spinnerMax.getStage() != null) spinnerMax.setValue(value.getLowMax());
        else emitterPropertiesPanel.populateScrollTable(null);

        if (!MathUtils.isEqual(value.getLowMin(), value.getLowMax())) toggleWidget.showTable2();
    }
}
