package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.*;
import com.ray3k.particleparkpro.Core;
import com.ray3k.particleparkpro.FileDialogs;
import com.ray3k.particleparkpro.Settings;
import com.ray3k.particleparkpro.undo.UndoManager;
import com.ray3k.particleparkpro.undo.undoables.*;
import com.ray3k.particleparkpro.widgets.CollapsibleGroup;
import com.ray3k.particleparkpro.widgets.EditableLabel;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.stripe.DraggableList;
import com.ray3k.stripe.DraggableList.DraggableListListener;
import com.ray3k.stripe.PopTable;
import com.ray3k.stripe.PopTable.TableShowHideListener;

import java.io.FileWriter;
import java.io.Writer;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Settings.getDefaultSavePath;

public class EffectEmittersPanel extends Panel {
    private DraggableList emittersDraggableList;
    private final int col1Width = 40;
    private final int col1PadLeft = 5;
    private final int defaultHorizontalSpacing = 10;
    private final Array<TextButton> disableableButtons = new Array<>();
    public static EffectEmittersPanel effectEmittersPanel;
    private static final float DELAYED_UNDO_DELAY = .3f;
    private Action delayedUndoAction;
    private static final float TAP_SQUARE = 5;
    private static final float TAP_WIDTH_RENAMING = 2000;
    private static final float TAP_HEIGHT_RENAMING = 12;

    public EffectEmittersPanel() {
        effectEmittersPanel = this;
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

        //Draggable List
        table.row();
        emittersDraggableList = new DraggableList(true, draggableListStyle);
        emittersDraggableList.getDragAndDrop().setTapSquareSize(TAP_SQUARE);
        emittersDraggableList.align(Align.top);
        emittersDraggableList.addListener(new DraggableListListener() {
            @Override
            public void removed(Actor actor, int index) {
                if (particleEffect.getEmitters().size <= 1) return;

                UndoManager.add(new DeleteEmitterUndoable(activeEmitters.orderedKeys().get(index), index, "Delete Emitter"));

                populateEmitters();
                updateDisableableWidgets();
                emitterPropertiesPanel.populateScrollTable(null);
            }

            @Override
            public void reordered(Actor actor, int indexBefore, int indexAfter) {
                if (particleEffect.getEmitters().size <= 1) return;

                var emitter = activeEmitters.orderedKeys().get(indexBefore);
                UndoManager.add(new MoveEmitterUndoable(emitter, indexBefore, indexAfter, "Move Emitter"));

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

        var collapsibleWidget = new CollapsibleGroup(false);
        bodyTable.add(collapsibleWidget).padRight(5).growY();

        //Expanded
        table = new Table();
        collapsibleWidget.addActor(table);

        addEmitterButtons(table);

        //Collapsed
        table = new Table();
        table.bottom();
        collapsibleWidget.addActor(table);

        var emittersButton = new TextButton("Emitters...", skin);
        table.add(emittersButton);
        addHandListener(emittersButton);
        onChange(emittersButton, () -> {
            showPopEmitterControls(emittersButton);
        });
    }

    private void showPopEmitterControls(Actor attachToActor) {
        var pop = new PopTable(skin.get("side-pop", WindowStyle.class));
        pop.attachToActor(attachToActor, Align.topRight, Align.bottomRight);
        pop.setHideOnUnfocus(true);
        addEmitterButtons(pop);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        pop.addListener(new TableShowHideListener() {
            @Override
            public void tableShown(Event event) {
                Gdx.input.setInputProcessor(foregroundStage);
            }

            @Override
            public void tableHidden(Event event) {
                Gdx.input.setInputProcessor(stage);
            }
        });
        pop.show(foregroundStage);
    }

    private void addEmitterButtons(Table table) {
        //New
        table.defaults().space(5);
        var textButton = new TextButton("New", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            UndoManager.add(new NewEmitterUndoable(createNewEmitter(), "New Emitter"));

            populateEmitters();
            updateDisableableWidgets();
            emitterPropertiesPanel.populateScrollTable(null);
        });

        //Duplicate
        table.row();
        textButton = new TextButton("Duplicate", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            UndoManager.add(new NewEmitterUndoable(new ParticleEmitter(selectedEmitter), "Duplicate Emitter"));

            populateEmitters();
            updateDisableableWidgets();
            emitterPropertiesPanel.populateScrollTable(null);
        });

        //Delete
        table.row();
        var deleteButton = new TextButton("Delete", skin);
        table.add(deleteButton);
        disableableButtons.add(deleteButton);
        addHandListener(deleteButton);
        onChange(deleteButton, () -> {
            UndoManager.add(new DeleteEmitterUndoable(selectedEmitter, activeEmitters.orderedKeys().indexOf(selectedEmitter, true), "Delete Emitter"));

            populateEmitters();
            updateDisableableWidgets();
            emitterPropertiesPanel.populateScrollTable(null);
        });

        table.row();
        var image = new Image(skin, "divider-10");
        image.setScaling(Scaling.stretchX);
        table.add(image).fillX();

        //Save
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

        //Open
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
                updateDisableableWidgets();
                emitterPropertiesPanel.populateScrollTable(null);

                UndoManager.clear();
            }
        });

        //Merge
        table.row();
        textButton = new TextButton("Merge", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            var fileHandle = FileDialogs.openDialog(getDefaultSavePath(), new String[] {"p"}, new String[] {"Particle Files"});

            if (fileHandle != null) {
                defaultFileName = fileHandle.name();
                Settings.setDefaultSavePath(fileHandle.parent());

                var oldEmitters = new Array<>(particleEffect.getEmitters());
                var oldActiveEmitters = new ObjectMap<>(activeEmitters);
                var oldFileHandles = new ObjectMap<>(fileHandles);
                var oldSprites = new ObjectMap<>(sprites);
                var oldSelectedIndex = oldEmitters.indexOf(selectedEmitter, true);

                mergeParticle(fileHandle);

                UndoManager.add(MergeEmitterUndoable
                    .builder()
                    .oldEmitters(oldEmitters)
                    .oldActiveEmitters(oldActiveEmitters)
                    .oldFileHandles(oldFileHandles)
                    .oldSprites(oldSprites)
                    .oldSelectedIndex(oldSelectedIndex)
                    .newEmitters(new Array<>(particleEffect.getEmitters()))
                    .newActiveEmitters(new ObjectMap<>(activeEmitters))
                    .newFileHandles(new ObjectMap<>(fileHandles))
                    .newSprites(new ObjectMap<>(sprites))
                    .newSelectedIndex(oldEmitters.indexOf(selectedEmitter, true))
                    .description("Merge Particle Effect")
                    .build());
                populateEmitters();
                updateDisableableWidgets();
                emitterPropertiesPanel.populateScrollTable(null);
            }
        });

        //Up
        table.row();
        textButton = new TextButton("Up", skin);
        table.add(textButton).expandY().bottom();
        disableableButtons.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            var oldIndex = activeEmitters.orderedKeys().indexOf(selectedEmitter, true);
            if (oldIndex <= 0) return;
            UndoManager.add(new MoveEmitterUndoable(selectedEmitter, oldIndex, oldIndex - 1, "Move Up Emitter"));
            populateEmitters();
        });

        //Down
        table.row();
        textButton = new TextButton("Down", skin);
        table.add(textButton);
        disableableButtons.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            var oldIndex = activeEmitters.orderedKeys().indexOf(selectedEmitter, true);
            if (oldIndex >= activeEmitters.orderedKeys().size - 1) return;
            UndoManager.add(new MoveEmitterUndoable(selectedEmitter, oldIndex, oldIndex + 1, "Move Down Emitter"));
            populateEmitters();
        });

        updateDisableableWidgets();
    }

    public void updateDisableableWidgets() {
        for (var button : disableableButtons) {
            button.setDisabled(particleEffect.getEmitters().size <= 1);
        }

        emittersDraggableList.setAllowRemoval(particleEffect.getEmitters().size > 1);
    }

    public void populateEmitters() {
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

            var dragLabel = new Label(emitter.getName(), skin, "emitter-drag");
            var removeLabel = new Label(emitter.getName(), skin, "emitter-remove");
            var editableLabel = new EditableLabel(emitter.getName(), Core.editableLabelStyle) {
                @Override
                public void unfocused() {
                    emittersDraggableList.getDragAndDrop().setTapSquareSize(TAP_SQUARE);

                    if (getText().equals("")) {
                        setText("Untitled");
                        UndoManager.add(new RenameEmitterUndoable(emitter, emitter.getName(), getText(), "Rename Emitter"));
                        dragLabel.setText(emitter.getName());
                        dragLabel.pack();
                        removeLabel.setText(emitter.getName());
                        removeLabel.pack();
                    }
                }
            };

            editableLabel.textField.addListener(new InputListener() {
                @Override
                public boolean keyDown(InputEvent event, int keycode) {
                    if (keycode == Keys.ENTER || keycode == Keys.NUMPAD_ENTER) {
                        stage.setKeyboardFocus(null);
                        editableLabel.showTable1();
                        editableLabel.unfocused();
                    }
                    return false;
                }
            });

            table.add(editableLabel).growX();
            addIbeamListener(editableLabel.textField);
            addIbeamListener(editableLabel.label);

            onChange(editableLabel, () -> {
                addDelayedUndoAction(() -> {
                    UndoManager.add(new RenameEmitterUndoable(emitter, emitter.getName(), editableLabel.getText(), "Rename Emitter"));
                    dragLabel.setText(emitter.getName());
                    dragLabel.pack();
                    removeLabel.setText(emitter.getName());
                    removeLabel.pack();
                });
            });

            onClick(editableLabel, () -> {
                emittersDraggableList.getDragAndDrop().setTapWidth(TAP_WIDTH_RENAMING);
                emittersDraggableList.getDragAndDrop().setTapHeight(TAP_HEIGHT_RENAMING);
            });

            emittersDraggableList.add(stack, removeLabel, dragLabel, removeLabel);
        }
    }

    private void addDelayedUndoAction(Runnable runnable) {
        if (delayedUndoAction != null) delayedUndoAction.restart();
        else {
            delayedUndoAction = Actions.sequence(Actions.delay(DELAYED_UNDO_DELAY), Actions.run(runnable), Actions.run(() -> delayedUndoAction = null));
            stage.addAction(delayedUndoAction);
        }
    }
}
