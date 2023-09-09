package com.ray3k.particleparkpro.widgets.subpanels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpriteMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.ray3k.particleparkpro.FileDialogs;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.stripe.DraggableList;
import com.ray3k.stripe.DraggableList.DraggableListListener;
import com.ray3k.stripe.DraggableTextList;
import com.ray3k.stripe.DraggableTextList.DraggableTextListListener;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Settings.getDefaultImagePath;

public class ImagesSubPanel extends Panel {
    private DraggableTextList list;
    private Button removeButton;

    public ImagesSubPanel() {
        var listWidth = 210;
        var listHeight = 100;

        for (int i = 0; i < selectedEmitter.getImagePaths().size; i++) {
            var path = selectedEmitter.getImagePaths().get(i);
            var sprite = selectedEmitter.getSprites().get(i);
        }

        setTouchable(Touchable.enabled);

        tabTable.left();
        var label = new Label("Images", skin, "header");
        tabTable.add(label);

        bodyTable.defaults().space(15);
        bodyTable.left();
        var table = new Table();
        bodyTable.add(table);

        table.defaults().space(5).fillX();
        var textButton = new TextButton("Add", skin, "small");
        table.add(textButton);
        addHandListener(textButton);
        addTooltip(textButton, "Add an image to be used as the texture for the particle", Align.top, tooltipBottomArrowStyle);
        onChange(textButton, () -> {
            var selectedFileHandles = FileDialogs.openMultipleDialog(getDefaultImagePath(), new String[] {"png,jpg,jpeg"}, new String[]{"Image files"});
            if (selectedFileHandles == null) return;
            for (var fileHandle : selectedFileHandles) {
                var path = fileHandle.name();
                selectedEmitter.getImagePaths().add(path);
                addImage(fileHandle);
                var sprite = particleAtlas.createSprite(fileHandle.nameWithoutExtension());
                selectedEmitter.getSprites().add(sprite);
            }
            if (selectedFileHandles.size > 0) updateList();
        });

        table.row();
        textButton = new TextButton("Default", skin, "small");
        table.add(textButton);
        addHandListener(textButton);
        addTooltip(textButton, "Set the image to the default round-faded image", Align.top, tooltipBottomArrowStyle);
        onChange(textButton, () -> {
            var fileHandle = Gdx.files.internal("particle.png");
            var path = fileHandle.name();
            selectedEmitter.getImagePaths().add(path);
            var sprite = particleAtlas.createSprite("particle");
            selectedEmitter.getSprites().add(sprite);
            updateList();
        });

        table.row();
        textButton = new TextButton("Default PMA", skin, "small");
        table.add(textButton);
        addHandListener(textButton);
        addTooltip(textButton, "Set the image to the default for premultiplied alpha", Align.top, tooltipBottomArrowStyle);
        onChange(textButton, () -> {
            var fileHandle = Gdx.files.internal("pre_particle.png");
            var path = fileHandle.name();
            selectedEmitter.getImagePaths().add(path);
            var sprite = particleAtlas.createSprite("pre_particle");
            selectedEmitter.getSprites().add(sprite);
            updateList();
        });

        table = new Table();
        bodyTable.add(table);

        label = new Label("Sprite mode:", skin);
        table.add(label);

        table.row();
        table.defaults().left();
        var buttonGroup = new ButtonGroup<>();
        var checkBoxSingle = new CheckBox("Single", skin, "radio");
        table.add(checkBoxSingle).spaceTop(5);
        buttonGroup.add(checkBoxSingle);
        addHandListener(checkBoxSingle);
        addTooltip(checkBoxSingle, "Only the selected image will be drawn", Align.top, tooltipBottomArrowStyle);
        onChange(checkBoxSingle, () -> selectedEmitter.setSpriteMode(SpriteMode.single));

        table.row();
        var checkBoxRandom = new CheckBox("Random", skin, "radio");
        table.add(checkBoxRandom);
        buttonGroup.add(checkBoxRandom);
        addHandListener(checkBoxRandom);
        addTooltip(checkBoxRandom, "A randomly selected image will be chosen for each particle", Align.top, tooltipBottomArrowStyle);
        onChange(checkBoxRandom, () -> selectedEmitter.setSpriteMode(SpriteMode.random));

        table.row();
        var checkBoxAnimated = new CheckBox("Animated", skin, "radio");
        table.add(checkBoxAnimated);
        buttonGroup.add(checkBoxAnimated);
        addHandListener(checkBoxAnimated);
        addTooltip(checkBoxAnimated, "All images will be displayed in sequence over the life of each particle", Align.top, tooltipBottomArrowStyle);
        onChange(checkBoxAnimated, () -> selectedEmitter.setSpriteMode(SpriteMode.animated));

        list = new DraggableTextList(true, draggableListNoBgStyle);
        list.setProgrammaticChangeEvents(false);
        list.setTextAlignment(Align.left);
        list.align(Align.top);
        addHandListener(list);
        list.addListener(new DraggableTextListListener() {
            @Override
            public void removed(String text, int index) {
                list.setAllowRemoval(list.getTexts().size > 1);
                removeButton.setDisabled(!list.isAllowRemoval());

                selectedEmitter.getSprites().removeIndex(index);

                var paths = selectedEmitter.getImagePaths();
                paths.clear();
                paths.addAll(list.getTexts());
            }

            @Override
            public void reordered(String text, int indexBefore, int indexAfter) {
                var paths = selectedEmitter.getImagePaths();
                paths.clear();
                paths.addAll(list.getTexts());
            }

            @Override
            public void selected(String text) {

            }
        });

        var scrollPane = new ScrollPane(list, skin, "draggable-list");
        scrollPane.setFlickScroll(false);
        scrollPane.setFadeScrollBars(false);
        bodyTable.add(scrollPane).size(listWidth, listHeight).spaceRight(10);
        addScrollFocusListener(scrollPane);

        table = new Table();
        bodyTable.add(table).spaceLeft(10);

        table.defaults().space(5);
        var button = new Button(skin, "moveup");
        table.add(button);
        addHandListener(button);
        onChange(button, () -> {
            var paths = selectedEmitter.getImagePaths();
            var sprites = selectedEmitter.getSprites();
            var index = list.getSelectedIndex();
            if (index > 0) {
                var path = paths.get(index);
                paths.removeIndex(index);
                var sprite = sprites.get(index);
                sprites.removeIndex(index);
                paths.insert(--index, path);
                sprites.insert(index, sprite);

                list.clearChildren();
                list.addAllTexts(paths);
                list.setSelected(index);
            }
        });

        table.row();
        button = new Button(skin, "movedown");
        table.add(button);
        addHandListener(button);
        onChange(button, () -> {
            var paths = selectedEmitter.getImagePaths();
            var sprites = selectedEmitter.getSprites();
            var index = list.getSelectedIndex();
            if (index < paths.size - 1) {
                var path = paths.get(index);
                paths.removeIndex(index);
                var sprite = sprites.get(index);
                sprites.removeIndex(index);
                paths.insert(++index, path);
                sprites.insert(index, sprite);

                list.clearChildren();
                list.addAllTexts(paths);
                list.setSelected(index);
            }
        });

        table.row();
        removeButton = new Button(skin, "cancel");
        table.add(removeButton);
        addHandListener(removeButton);
        onChange(removeButton, () -> {
            var paths = selectedEmitter.getImagePaths();
            if (paths.size <= 1) return;
            var index = list.getSelectedIndex();
            paths.removeIndex(index);

            selectedEmitter.getSprites().removeIndex(index);

            list.clearChildren();
            list.addAllTexts(paths);
            list.setSelected(index < list.getTexts().size ? index : list.getTexts().size - 1);
            list.setAllowRemoval(list.getTexts().size > 1);
            removeButton.setDisabled(!list.isAllowRemoval());
        });

        updateList();
    }

    private void updateList() {
        var paths = selectedEmitter.getImagePaths();
        list.clearChildren();
        for (var path : paths) {
            list.addText(path);
        }
        list.setAllowRemoval(list.getTexts().size > 1);
        removeButton.setDisabled(!list.isAllowRemoval());
    }
}
