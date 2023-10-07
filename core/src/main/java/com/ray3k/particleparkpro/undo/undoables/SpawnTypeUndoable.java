package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShapeValue;
import com.ray3k.particleparkpro.undo.Undoable;
import com.ray3k.particleparkpro.widgets.subpanels.SpawnSubPanel;
import com.ray3k.particleparkpro.widgets.subpanels.SpawnSubPanel.SpawnType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpawnTypeUndoable implements Undoable {
    private SpawnShapeValue value;
    private SpawnType spawnType;
    private SpawnType spawnTypeOld;
    private SpawnSubPanel spawnSubPanel;
    private String description;

    @Override
    public void undo() {
        value.setShape(spawnTypeOld.spawnShape);
        refreshDisplay();
    }

    @Override
    public void redo() {
        value.setShape(spawnType.spawnShape);
        refreshDisplay();
    }

    @Override
    public void start() {
        value.setShape(spawnType.spawnShape);
    }

    public void refreshDisplay() {
        if (spawnSubPanel != null) spawnSubPanel.populate();
    }

    @Override
    public String getDescription() {
        return description;
    }
}
