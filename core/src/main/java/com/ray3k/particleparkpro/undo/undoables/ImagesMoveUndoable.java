package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.ray3k.particleparkpro.undo.Undoable;
import lombok.AllArgsConstructor;

import static com.ray3k.particleparkpro.Core.*;

@AllArgsConstructor
public class ImagesMoveUndoable implements Undoable {
    private int oldIndex;
    private int newIndex;
    private String description;

    @Override
    public void undo() {
        var paths = selectedEmitter.getImagePaths();
        var sprites = selectedEmitter.getSprites();
        var path = paths.get(newIndex);
        var sprite = sprites.get(newIndex);
        paths.removeIndex(newIndex);
        sprites.removeIndex(newIndex);
        paths.insert(oldIndex, path);
        sprites.insert(oldIndex, sprite);
        refreshDisplay();
    }

    @Override
    public void redo() {
        var paths = selectedEmitter.getImagePaths();
        var sprites = selectedEmitter.getSprites();
        var path = paths.get(oldIndex);
        var sprite = sprites.get(oldIndex);
        paths.removeIndex(oldIndex);
        sprites.removeIndex(oldIndex);
        paths.insert(newIndex, path);
        sprites.insert(newIndex, sprite);
        refreshDisplay();
    }

    @Override
    public void start() {
        var paths = selectedEmitter.getImagePaths();
        var sprites = selectedEmitter.getSprites();
        var path = paths.get(oldIndex);
        var sprite = sprites.get(oldIndex);
        paths.removeIndex(oldIndex);
        sprites.removeIndex(oldIndex);
        paths.insert(newIndex, path);
        sprites.insert(newIndex, sprite);
    }

    @Override
    public String getDescription() {
        return description;
    }

    private void refreshDisplay() {
        emitterPropertiesPanel.populateScrollTable(null);
    }
}
