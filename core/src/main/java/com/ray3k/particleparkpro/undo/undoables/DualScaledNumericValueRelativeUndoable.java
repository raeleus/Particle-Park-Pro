package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.ray3k.particleparkpro.undo.Undoable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DualScaledNumericValueRelativeUndoable implements Undoable {
    private ScaledNumericValue xValue;
    private ScaledNumericValue yValue;
    private CheckBox checkBox;
    private boolean active;
    private String description;

    @Override
    public void undo() {
        xValue.setRelative(!active);
        yValue.setRelative(!active);
        refreshDisplay();
    }

    @Override
    public void redo() {
        xValue.setRelative(active);
        yValue.setRelative(active);
        refreshDisplay();
    }

    @Override
    public void start() {
        xValue.setRelative(active);
        yValue.setRelative(active);
    }

    @Override
    public String getDescription() {
        return description;
    }

    private void refreshDisplay() {
        if (checkBox !=null) checkBox.setChecked(xValue.isRelative());
    }
}
