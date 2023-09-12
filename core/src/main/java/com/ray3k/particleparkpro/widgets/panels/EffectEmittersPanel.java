package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.StreamUtils;
import com.ray3k.particleparkpro.Core;
import com.ray3k.particleparkpro.FileDialogs;
import com.ray3k.particleparkpro.Settings;
import com.ray3k.particleparkpro.widgets.EditableLabel;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.stripe.DraggableList;
import com.ray3k.stripe.DraggableList.DraggableListListener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Settings.getDefaultSavePath;

public class EffectEmittersPanel extends Panel {
    private DraggableList emittersDraggableList;
    private final int col1Width = 40;
    private final int col1PadLeft = 5;
    private final int defaultHorizontalSpacing = 10;
    private TextButton deleteButton;

    public EffectEmittersPanel() {
        setTouchable(Touchable.enabled);

        var label = new Label("Effect Emitters", skin, "header");
        tabTable.add(label);

        bodyTable.defaults().space(10);
        var table = new Table();
        bodyTable.add(table).grow();

        var headerTable = new Table();
        headerTable.setBackground(skin.getDrawable("subpanel2-header-10"));
        table.add(headerTable).growX();

        headerTable.defaults().space(defaultHorizontalSpacing);
        label = new Label("Active", skin);
        headerTable.add(label).width(col1Width).padLeft(col1PadLeft);

        var image = new Image(skin, "subpanel2-divider");
        image.setScaling(Scaling.none);
        headerTable.add(image).growY();

        label = new Label("Emitter", skin);
        headerTable.add(label).expandX().left();

        table.row();
        emittersDraggableList = new DraggableList(true, draggableListStyle);
        emittersDraggableList.align(Align.top);
        emittersDraggableList.addListener(new DraggableListListener() {
            @Override
            public void removed(Actor actor, int index) {
                if (particleEffect.getEmitters().size > 1) {
                    var emitterIndex = particleEffect.getEmitters().indexOf(activeEmitters.orderedKeys().get(index),
                        true);
                    particleEffect.getEmitters().removeIndex(emitterIndex);
                    activeEmitters.removeIndex(index);
                    selectedEmitter = particleEffect.getEmitters().get(
                        Math.min(index, activeEmitters.orderedKeys().size - 1));
                }

                populateEmitters();
                updateDeleteButton();
                emitterPropertiesPanel.populateScrollTable(null);
            }

            @Override
            public void reordered(Actor actor, int indexBefore, int indexAfter) {
                var emitter = activeEmitters.orderedKeys().get(indexBefore);
                activeEmitters.orderedKeys().removeIndex(indexBefore);
                activeEmitters.orderedKeys().insert(indexAfter, emitter);

                particleEffect.getEmitters().clear();
                for (var entry : activeEmitters.entries()) {
                    if (entry.value) particleEffect.getEmitters().add(entry.key);
                }

                selectedEmitter = emitter;
                populateEmitters();
                emitterPropertiesPanel.populateScrollTable(null);
            }

            @Override
            public void selected(Actor actor) {

            }
        });

        var scrollPane = new ScrollPane(emittersDraggableList, skin, "subpanel2");
        scrollPane.setFlickScroll(false);
        scrollPane.setScrollingDisabled(true, false);
        table.add(scrollPane).grow();
        addScrollFocusListener(scrollPane);

        populateEmitters();

        table = new Table();
        bodyTable.add(table).padRight(5).growY();

        table.defaults().space(5);
        var textButton = new TextButton("New", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            var emitter = createNewEmitter();

            particleEffect.getEmitters().add(emitter);
            activeEmitters.put(emitter, true);
            selectedEmitter = emitter;

            populateEmitters();
            updateDeleteButton();
            emitterPropertiesPanel.populateScrollTable(null);
        });

        table.row();
        textButton = new TextButton("Duplicate", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            var emitter = new ParticleEmitter(selectedEmitter);

            particleEffect.getEmitters().add(emitter);
            activeEmitters.put(emitter, activeEmitters.get(selectedEmitter));
            selectedEmitter = emitter;

            populateEmitters();
            updateDeleteButton();
            emitterPropertiesPanel.populateScrollTable(null);
        });

        table.row();
        deleteButton = new TextButton("Delete", skin);
        updateDeleteButton();
        table.add(deleteButton);
        addHandListener(deleteButton);
        onChange(deleteButton, () -> {
            var index = particleEffect.getEmitters().indexOf(selectedEmitter, true);
            particleEffect.getEmitters().removeIndex(index);
            activeEmitters.remove(selectedEmitter);
            selectedEmitter = particleEffect.getEmitters().get(Math.min(index, activeEmitters.orderedKeys().size - 1));

            populateEmitters();
            updateDeleteButton();
            emitterPropertiesPanel.populateScrollTable(null);
        });

        table.row();
        image = new Image(skin, "divider-10");
        image.setScaling(Scaling.stretchX);
        table.add(image).fillX();

        table.row();
        textButton = new TextButton("Save", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            var saveHandle = FileDialogs.saveDialog(getDefaultSavePath(), defaultFileName, new String[] {"p"}, new String[] {"Particle Files"});

            if (saveHandle != null) {
                Settings.setDefaultSavePath(saveHandle.parent());
                defaultFileName = saveHandle.name();

                Writer fileWriter = null;
                try {
                    fileWriter = new FileWriter(saveHandle.file());
                    particleEffect.save(fileWriter);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    StreamUtils.closeQuietly(fileWriter);
                }

                for (var fileHandle : fileHandles.values()) {
                    if (fileHandle.parent().equals(saveHandle.parent())) break;
                    try {
                        fileHandle.copyTo(saveHandle.parent().child(fileHandle.name()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        table.row();
        textButton = new TextButton("Open", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            var fileHandle = FileDialogs.openDialog(getDefaultSavePath(), new String[] {"p"}, new String[] {"Particle Files"});

            if (fileHandle != null) {
                defaultFileName = fileHandle.name();
                Settings.setDefaultSavePath(fileHandle.parent());
                loadParticle(fileHandle);
                selectedEmitter = particleEffect.getEmitters().first();

                populateEmitters();
                updateDeleteButton();
                emitterPropertiesPanel.populateScrollTable(null);
            }
        });

        table.row();
        textButton = new TextButton("Merge", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            var fileHandle = FileDialogs.openDialog(getDefaultSavePath(), new String[] {"p"}, new String[] {"Particle Files"});

            if (fileHandle != null) {
                defaultFileName = fileHandle.name();
                Settings.setDefaultSavePath(fileHandle.parent());
                mergeParticle(fileHandle);

                populateEmitters();
                updateDeleteButton();
                emitterPropertiesPanel.populateScrollTable(null);
            }
        });

        table.row();
        textButton = new TextButton("Up", skin);
        table.add(textButton).expandY().bottom();
        addHandListener(textButton);
        onChange(textButton, () -> {
            var index = activeEmitters.orderedKeys().indexOf(selectedEmitter, true);
            if (index > 0) {
                activeEmitters.orderedKeys().removeIndex(index);
                index--;
                activeEmitters.orderedKeys().insert(index, selectedEmitter);
            }

            particleEffect.getEmitters().clear();
            for (var entry : activeEmitters.entries()) {
                if (entry.value) particleEffect.getEmitters().add(entry.key);
            }

            populateEmitters();
        });

        table.row();
        textButton = new TextButton("Down", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            var index = activeEmitters.orderedKeys().indexOf(selectedEmitter, true);
            if (index < activeEmitters.orderedKeys().size - 1) {
                activeEmitters.orderedKeys().removeIndex(index);
                index++;
                activeEmitters.orderedKeys().insert(index, selectedEmitter);
            }

            particleEffect.getEmitters().clear();
            for (var entry : activeEmitters.entries()) {
                if (entry.value) particleEffect.getEmitters().add(entry.key);
            }

            populateEmitters();
        });
    }

    private void updateDeleteButton() {
        deleteButton.setDisabled(particleEffect.getEmitters().size <= 1);
    }

    private void populateEmitters() {
        emittersDraggableList.clearChildren();

        var backgroundImages = new Array<Image>();

        for (int i = 0; i < activeEmitters.orderedKeys().size; i++) {
            var emitter = activeEmitters.orderedKeys().get(i);

            var stack = new Stack();

            var backgroundImage = new Image();
            backgroundImage.setDrawable(skin, selectedEmitter == emitter ? "selected-emitter" : "clear");
            stack.add(backgroundImage);
            backgroundImages.add(backgroundImage);

            var table = new Table();
            table.setTouchable(Touchable.enabled);
            stack.add(table);
            onClick(table, () -> {
                for (var bg : backgroundImages) bg.setDrawable(skin, "clear");
                backgroundImage.setDrawable(skin, "selected-emitter");
                selectedEmitter = emitter;
                emitterPropertiesPanel.populateScrollTable(null);
            });

            table.defaults().spaceLeft(defaultHorizontalSpacing).spaceRight(defaultHorizontalSpacing);
            var container = new Container<>();
            container.right();
            table.add(container).width(col1Width).padLeft(col1PadLeft);

            var button = new Button(skin, "checkbox");
            container.setActor(button);
            addHandListener(button);
            button.setChecked(activeEmitters.get(emitter));
            onChange(button, () -> {
                activeEmitters.put(emitter, button.isChecked());

                particleEffect.getEmitters().clear();
                for (var entry : activeEmitters.entries()) {
                    if (entry.value) particleEffect.getEmitters().add(entry.key);
                }
            });

            var image = new Image(skin, "subpanel2-divider-invisible");
            image.setScaling(Scaling.none);
            table.add(image).fillY();

            var editableLabel = new EditableLabel(emitter.getName(), Core.editableLabelStyle) {
                @Override
                public void unfocused() {
                    if (getText().equals("")) {
                        setText("Untitled");
                        emitter.setName(getText());
                    }
                }
            };
            table.add(editableLabel).growX();
            addIbeamListener(editableLabel.textField);
            addIbeamListener(editableLabel.label);
            onChange(editableLabel, () -> emitter.setName(editableLabel.getText()));

            var dragLabel = new Label(emitter.getName(), skin, "emitter-drag");
            var removeLabel = new Label(emitter.getName(), skin, "emitter-remove");

            emittersDraggableList.add(stack, removeLabel, dragLabel, removeLabel);
        }
    }
}
