package com.ray3k.particleparkpro.undo.undoables;

import com.ray3k.particleparkpro.undo.Undoable;
import com.ray3k.stripe.Spinner;

import static com.ray3k.particleparkpro.Core.emitterPropertiesPanel;
import static com.ray3k.particleparkpro.Core.selectedEmitter;

public class CountMaxUndoable implements Undoable {
    private int newValue;
    private int oldValue;
    private Spinner spinner;

    public CountMaxUndoable(Spinner spinner) {
        this.newValue = spinner.getValueAsInt();
        this.oldValue = selectedEmitter.getMaxParticleCount();
        this.spinner = spinner;
    }

    @Override
    public void undo() {
        selectedEmitter.setMaxParticleCount(oldValue);
        refreshDisplay();
    }

    @Override
    public void redo() {
        selectedEmitter.setMaxParticleCount(newValue);
        refreshDisplay();
    }

    @Override
    public void start() {
        selectedEmitter.setMaxParticleCount(newValue);
    }

    @Override
    public String getDescription() {
        return "change Count max";
    }

    private void refreshDisplay() {
        if (spinner.getStage() != null) spinner.setValue(selectedEmitter.getMaxParticleCount());
        else emitterPropertiesPanel.populateScrollTable(null);
    }
}
