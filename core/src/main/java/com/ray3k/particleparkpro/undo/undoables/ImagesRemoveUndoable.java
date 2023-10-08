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
public class ImagesRemoveUndoable implements Undoable {
    private String path;
    private FileHandle fileHandle;
    private Sprite sprite;
    private String description;

    @Override
    public void undo() {
        selectedEmitter.getImagePaths().add(path);
        fileHandles.put(path, fileHandle);
        sprites.put(path, sprite);
        selectedEmitter.getSprites().add(sprite);
        refreshDisplay();
    }

    @Override
    public void redo() {
        selectedEmitter.getImagePaths().removeValue(path, false);
        removeUnusedImageFiles();
        sprites.remove(path);
        selectedEmitter.getSprites().removeValue(sprite, true);
        refreshDisplay();
    }

    @Override
    public void start() {
        selectedEmitter.getImagePaths().removeValue(path, false);
        removeUnusedImageFiles();
        sprites.remove(path);
        selectedEmitter.getSprites().removeValue(sprite, true);
    }

    @Override
    public String getDescription() {
        return description;
    }

    private void refreshDisplay() {
        emitterPropertiesPanel.populateScrollTable(null);
    }
}
