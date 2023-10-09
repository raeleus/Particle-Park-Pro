package com.ray3k.particleparkpro.undo.undoables;

import com.ray3k.particleparkpro.undo.Undoable;
import lombok.AllArgsConstructor;

import static com.ray3k.particleparkpro.Core.selectedEmitter;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.ShownProperty;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.emitterPropertiesPanel;

@AllArgsConstructor
public class SetPropertyUndoable implements Undoable {
    private ShownProperty property;
    private boolean active;
    private String description;

    @Override
    public void undo() {
        activateProperty(!active);
        if (active) emitterPropertiesPanel.removeProperty(property);
        else emitterPropertiesPanel.populateScrollTable(property);
    }

    @Override
    public void redo() {
        activateProperty(active);
        if (active) emitterPropertiesPanel.populateScrollTable(property);
        else emitterPropertiesPanel.removeProperty(property);
    }

    @Override
    public void start() {
        activateProperty(active);
        if (active) emitterPropertiesPanel.populateScrollTable(property);
        else emitterPropertiesPanel.removeProperty(property);
    }

    public void activateProperty(boolean active) {
        switch (property) {
            case DELAY:
                selectedEmitter.getDelay().setActive(active);
                break;
            case LIFE_OFFSET:
                selectedEmitter.getLifeOffset().setActive(active);
                break;
            case X_OFFSET:
                selectedEmitter.getXOffsetValue().setActive(active);
                break;
            case Y_OFFSET:
                selectedEmitter.getYOffsetValue().setActive(active);
                break;
            case VELOCITY:
                selectedEmitter.getVelocity().setActive(active);
                break;
            case ANGLE:
                selectedEmitter.getAngle().setActive(active);
                break;
            case ROTATION:
                selectedEmitter.getRotation().setActive(active);
                break;
            case WIND:
                selectedEmitter.getWind().setActive(active);
                break;
            case GRAVITY:
                selectedEmitter.getGravity().setActive(active);
                break;
        }
    }

    @Override
    public String getDescription() {
        return description;
    }
}