package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.GradientColorValue;
import com.ray3k.particleparkpro.undo.Undoable;
import com.ray3k.particleparkpro.widgets.ColorGraph;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.ray3k.particleparkpro.Core.emitterPropertiesPanel;

@Data
@AllArgsConstructor
public class TintUndoable implements Undoable {
    private GradientColorValue value;
    private final GradientColorValue oldValue = new GradientColorValue();
    private final GradientColorValue newValue = new GradientColorValue();
    private ColorGraph colorGraph;
    private String description;

    @Override
    public void undo() {
        value.setTimeline(oldValue.getTimeline());
        value.setColors(oldValue.getColors());
        refreshDisplay();
    }

    @Override
    public void redo() {
        value.setTimeline(newValue.getTimeline());
        value.setColors(newValue.getColors());
        refreshDisplay();
    }

    @Override
    public void start() {
        value.setTimeline(newValue.getTimeline());
        value.setColors(newValue.getColors());
    }

    @Override
    public String getDescription() {
        return description;
    }

    private void refreshDisplay() {
        emitterPropertiesPanel.populateScrollTable(null);
    }
}
