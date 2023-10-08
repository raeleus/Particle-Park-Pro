package com.ray3k.particleparkpro.undo.undoables;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.ray3k.particleparkpro.undo.Undoable;
import lombok.AllArgsConstructor;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Core.selectedEmitter;

@AllArgsConstructor
public class ImagesAddUndoable implements Undoable {
    private Array<FileHandle> selectedFileHandles;
    private String description;
    private final Array<String> newImagePaths = new Array<>();
    private final ObjectMap<String, FileHandle> newFileHandles = new ObjectMap<>();
    private final ObjectMap<String, Sprite> newSpriteMap = new ObjectMap<>();
    private final Array<Sprite> newSprites = new Array<>();

    @Override
    public void undo() {
        selectedEmitter.getImagePaths().removeRange(selectedEmitter.getImagePaths().size - newImagePaths.size, selectedEmitter.getImagePaths().size - 1);
        removeUnusedImageFiles();
        for (var newSprite : newSpriteMap) {
            sprites.remove(newSprite.key);
        }
        selectedEmitter.getSprites().removeRange(selectedEmitter.getSprites().size - newSprites.size, selectedEmitter.getSprites().size - 1);
        refreshDisplay();
    }

    @Override
    public void redo() {
        selectedEmitter.getImagePaths().addAll(newImagePaths);
        fileHandles.putAll(newFileHandles);
        sprites.putAll(newSpriteMap);
        selectedEmitter.getSprites().addAll(newSprites);
        refreshDisplay();
    }

    @Override
    public void start() {
        for (var fileHandle : selectedFileHandles) {
            var path = fileHandle.name();
            newImagePaths.add(path);
            newFileHandles.put(path, fileHandle);
            var sprite = new Sprite(new Texture(fileHandle));
            newSpriteMap.put(path, sprite);
            newSprites.add(sprite);
        }

        selectedEmitter.getImagePaths().addAll(newImagePaths);
        fileHandles.putAll(newFileHandles);
        sprites.putAll(newSpriteMap);
        selectedEmitter.getSprites().addAll(newSprites);
    }

    @Override
    public String getDescription() {
        return description;
    }

    private void refreshDisplay() {
        emitterPropertiesPanel.populateScrollTable(null);
    }
}
