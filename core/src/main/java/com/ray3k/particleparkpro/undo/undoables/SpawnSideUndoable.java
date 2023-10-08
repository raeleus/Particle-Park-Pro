package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnEllipseSide;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShapeValue;
import com.ray3k.particleparkpro.undo.Undoable;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.ray3k.particleparkpro.Core.emitterPropertiesPanel;

@Data
@AllArgsConstructor
public class SpawnSideUndoable implements Undoable {
    private SpawnShapeValue value;
    private SpawnEllipseSide side;
    private SpawnEllipseSide oldSide;
    private String description;

    @Override
    public void undo() {
        value.setSide(oldSide);
        refreshDisplay();
    }

    @Override
    public void redo() {
        value.setSide(side);
        refreshDisplay();
    }

    @Override
    public void start() {
        value.setSide(side);
    }

    @Override
    public String getDescription() {
        return description;
    }

    private void refreshDisplay() {
        emitterPropertiesPanel.populateScrollTable(null);
    }
}
