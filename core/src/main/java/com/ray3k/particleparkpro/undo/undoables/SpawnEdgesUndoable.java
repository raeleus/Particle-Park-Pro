package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShapeValue;
import com.ray3k.particleparkpro.undo.Undoable;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.ray3k.particleparkpro.Core.emitterPropertiesPanel;

@Data
@AllArgsConstructor
public class SpawnEdgesUndoable implements Undoable {
    private SpawnShapeValue value;
    private boolean active;
    private String description;

    @Override
    public void undo() {
        value.setEdges(!active);
        refreshDisplay();
    }

    @Override
    public void redo() {
        value.setEdges(active);
        refreshDisplay();
    }

    @Override
    public void start() {
        value.setEdges(active);
    }

    @Override
    public String getDescription() {
        return description;
    }

    private void refreshDisplay() {
        emitterPropertiesPanel.populateScrollTable(null);
    }
}
