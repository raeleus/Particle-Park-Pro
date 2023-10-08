package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.IndependentScaledNumericValue;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.ray3k.particleparkpro.undo.Undoable;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.ray3k.particleparkpro.Core.emitterPropertiesPanel;

@Data
@AllArgsConstructor
public class ScaledNumericValueIndependentUndoable implements Undoable {
    private IndependentScaledNumericValue value;
    private boolean active;
    private String description;

    @Override
    public void undo() {
        value.setIndependent(!active);
        refreshDisplay();
    }

    @Override
    public void redo() {
        value.setIndependent(active);
        refreshDisplay();
    }

    @Override
    public void start() {
        value.setIndependent(active);
    }

    @Override
    public String getDescription() {
        return description;
    }

    private void refreshDisplay() {
        emitterPropertiesPanel.populateScrollTable(null);
    }
}