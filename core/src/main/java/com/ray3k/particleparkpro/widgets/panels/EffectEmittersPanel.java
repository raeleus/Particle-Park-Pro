package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Scaling;
import com.ray3k.particleparkpro.Core;
import com.ray3k.particleparkpro.FileDialogs;
import com.ray3k.particleparkpro.Settings;
import com.ray3k.particleparkpro.widgets.EditableLabel;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.particleparkpro.widgets.styles.PPeditableLabelStyle;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Settings.*;

public class EffectEmittersPanel extends Panel {
    private Table emittersTable;
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
        emittersTable = new Table();
        emittersTable.top();

        var scrollPane = new ScrollPane(emittersTable, skin, "subpanel2");
        scrollPane.setFlickScroll(false);
        scrollPane.setScrollingDisabled(true, false);
        table.add(scrollPane).grow();
        addScrollFocusListener(scrollPane);

        populateEmittersTable();

        table = new Table();
        bodyTable.add(table).padRight(5).growY();

        table.defaults().space(5);
        var textButton = new TextButton("New", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            var emitter = new ParticleEmitter();
            emitter.setName("Untitled");
            emitter.setMaxParticleCount(100);

            particleEffect.getEmitters().add(emitter);
            activeEmitters.put(emitter, true);
            selectedEmitter = emitter;

            populateEmittersTable();
            updateDeleteButton();
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

            populateEmittersTable();
            updateDeleteButton();
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
            selectedEmitter = particleEffect.getEmitters().get(Math.min(index, particleEffect.getEmitters().size - 1));

            populateEmittersTable();
            updateDeleteButton();
        });

        table.row();
        image = new Image(skin, "divider-10");
        image.setScaling(Scaling.stretchX);
        table.add(image).fillX();

        table.row();
        textButton = new TextButton("Save", skin);
        table.add(textButton);
        addHandListener(textButton);

        table.row();
        textButton = new TextButton("Open", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            var fileHandle = FileDialogs.openDialog(getDefaultSavePath(), new String[] {"p"}, new String[] {"Particle Files"});

            if (fileHandle != null) {
                Settings.setDefaultSavePath(fileHandle);
                loadParticle(fileHandle);
                selectedEmitter = particleEffect.getEmitters().first();

                populateEmittersTable();
                updateDeleteButton();
            }
        });

        table.row();
        textButton = new TextButton("Merge", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            var fileHandle = FileDialogs.openDialog(getDefaultSavePath(), new String[] {"p"}, new String[] {"Particle Files"});

            if (fileHandle != null) {
                Settings.setDefaultSavePath(fileHandle);
                mergeParticle(fileHandle);

                populateEmittersTable();
                updateDeleteButton();
            }
        });

        table.row();
        textButton = new TextButton("Up", skin);
        table.add(textButton).expandY().bottom();
        addHandListener(textButton);
        onChange(textButton, () -> {
            var index = particleEffect.getEmitters().indexOf(selectedEmitter, true);
            if (index <= 0) return;
            particleEffect.getEmitters().removeIndex(index);
            index--;
            particleEffect.getEmitters().insert(index, selectedEmitter);

            populateEmittersTable();
        });

        table.row();
        textButton = new TextButton("Down", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            var index = particleEffect.getEmitters().indexOf(selectedEmitter, true);
            if (index >= particleEffect.getEmitters().size -1) return;
            particleEffect.getEmitters().removeIndex(index);
            index++;
            particleEffect.getEmitters().insert(index, selectedEmitter);

            populateEmittersTable();
        });
    }

    private void updateDeleteButton() {
        deleteButton.setDisabled(particleEffect.getEmitters().size <= 1);
    }

    private void populateEmittersTable() {
        emittersTable.clear();

        var backgroundImages = new Array<Image>();

        for (int i = 0; i < particleEffect.getEmitters().size; i++) {
            var emitter = particleEffect.getEmitters().get(i);

            var stack = new Stack();
            emittersTable.add(stack).growX();

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
            });

            table.defaults().spaceLeft(defaultHorizontalSpacing).spaceRight(defaultHorizontalSpacing);
            var container = new Container<>();
            container.right();
            table.add(container).width(col1Width).padLeft(col1PadLeft);

            var button = new Button(skin, "checkbox");
            container.setActor(button);
            addHandListener(button);
            button.setChecked(activeEmitters.get(emitter));
            onChange(button, () -> activeEmitters.put(emitter, button.isChecked()));

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

            emittersTable.row();
        }
    }
}
