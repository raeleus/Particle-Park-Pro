package com.ray3k.particleparkpro.undo.undoables;

import com.ray3k.particleparkpro.undo.Undoable;
import com.ray3k.stripe.Spinner;

import static com.ray3k.particleparkpro.Core.emitterPropertiesPanel;
import static com.ray3k.particleparkpro.Core.selectedEmitter;

public class CountMaxUndoable implements Undoable {
    private int newValue;
    private int oldValue;

    public CountMaxUndoable(int newValue) {
        this.newValue = newValue;
        this.oldValue = selectedEmitter.getMaxParticleCount();
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
        emitterPropertiesPanel.populateScrollTable(null);
    }
}
