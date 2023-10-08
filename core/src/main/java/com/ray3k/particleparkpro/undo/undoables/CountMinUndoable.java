package com.ray3k.particleparkpro.undo.undoables;

import com.ray3k.particleparkpro.undo.Undoable;
import com.ray3k.stripe.Spinner;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.ray3k.particleparkpro.Core.emitterPropertiesPanel;
import static com.ray3k.particleparkpro.Core.selectedEmitter;

@AllArgsConstructor
public class CountMinUndoable implements Undoable {
    private int newValue;
    private int oldValue;

    @Override
    public void undo() {
        selectedEmitter.setMinParticleCount(oldValue);
        refreshDisplay();
    }

    @Override
    public void redo() {
        selectedEmitter.setMinParticleCount(newValue);
        refreshDisplay();
    }

    @Override
    public void start() {
        selectedEmitter.setMinParticleCount(newValue);
    }

    @Override
    public String getDescription() {
        return "change Count min";
    }

    private void refreshDisplay() {
        emitterPropertiesPanel.populateScrollTable(null);
    }
}
