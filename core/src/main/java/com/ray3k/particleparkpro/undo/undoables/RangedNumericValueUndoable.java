package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.RangedNumericValue;
import com.badlogic.gdx.math.MathUtils;
import com.ray3k.particleparkpro.undo.Undoable;
import com.ray3k.particleparkpro.widgets.ToggleWidget;
import com.ray3k.stripe.Spinner;
import lombok.AllArgsConstructor;
import lombok.Builder;

import static com.ray3k.particleparkpro.Core.emitterPropertiesPanel;

@AllArgsConstructor
public class RangedNumericValueUndoable implements Undoable {
    public final RangedNumericValue newValue = new RangedNumericValue();
    public final RangedNumericValue oldValue = new RangedNumericValue();
    private RangedNumericValue value;
    private String description;

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
        emitterPropertiesPanel.populateScrollTable(null);
    }
}
