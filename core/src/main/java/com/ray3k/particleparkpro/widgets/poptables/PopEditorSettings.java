package com.ray3k.particleparkpro.widgets.poptables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.ray3k.stripe.PopTable;
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static com.ray3k.particleparkpro.Core.*;

public class PopEditorSettings extends PopTable {
    public static final String NAME_MAXIMUM_UNDOS = "Maximum undos";
    public static final String NAME_OPEN_TO_SCREEN = "Open to screen";
    public static final String NAME_PRIMARY_UNDO_MODIFIERS = "Primary undo modifiers";
    public static final String NAME_PRIMARY_UNDO_SHORTCUT = "Primary undo shortcut";
    public static final String NAME_SECONDARY_UNDO_MODIFIERS = "Secondary undo modifiers";
    public static final String NAME_SECONDARY_UNDO_SHORTCUT = "Secondary undo shortcut";
    public static final String NAME_PRIMARY_REDO_MODIFIERS = "Primary redo modifiers";
    public static final String NAME_PRIMARY_REDO_SHORTCUT = "Primary redo shortcut";
    public static final String NAME_SECONDARY_REDO_MODIFIERS = "Secondary redo modifiers";
    public static final String NAME_SECONDARY_REDO_SHORTCUT = "Secondary redo shortcut";
    public static final int DEFAULT_MAXIMUM_UNDOS = 100;
    public static final String DEFAULT_OPEN_TO_SCREEN = "Welcome";
    public static final int[] DEFAULT_PRIMARY_UNDO_MODIFIERS = new int[] {Keys.CONTROL_LEFT};
    public static final int DEFAULT_PRIMARY_UNDO_SHORTCUT = Keys.Z;
    public static final int[] DEFAULT_SECONDARY_UNDO_MODIFIERS = new int[] {};
    public static final int DEFAULT_SECONDARY_UNDO_SHORTCUT = Keys.ANY_KEY;
    public static final int[] DEFAULT_PRIMARY_REDO_MODIFIERS = new int[] {Keys.CONTROL_LEFT};
    public static final int DEFAULT_PRIMARY_REDO_SHORTCUT = Keys.Y;
    public static final int[] DEFAULT_SECONDARY_REDO_MODIFIERS = new int[] {Keys.CONTROL_LEFT, Keys.SHIFT_LEFT};
    public static final int DEFAULT_SECONDARY_REDO_SHORTCUT = Keys.Z;

    private static final Array<TextField> textFields = new Array<>();

    public PopEditorSettings() {
        super(skin.get(WindowStyle.class));

        populate();
    }

    private void populate() {
        textFields.clear();
        clearChildren();

        pad(20).padTop(10);
        setHideOnUnfocus(true);
        setKeepCenteredInWindow(true);
        addListener(new TableShowHideListener() {
            @Override
            public void tableShown(Event event) {

            }

            @Override
            public void tableHidden(Event event) {
                Gdx.input.setInputProcessor(stage);
            }
        });

        var subLabel = new Label("SETTINGS", skin, "bold");
        add(subLabel).padBottom(10);

        row();
        var settingsTable = new Table();
        settingsTable.columnDefaults(0).right().uniformX();
        settingsTable.columnDefaults(1).left().uniformX().width(80);
        settingsTable.defaults().space(5);
        add(settingsTable);

        subLabel = new Label("Maximum Undos:", skin);
        settingsTable.add(subLabel);

        var spinner = new Spinner(0, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        spinner.setValue(preferences.getInteger(NAME_MAXIMUM_UNDOS, DEFAULT_MAXIMUM_UNDOS));
        spinner.setProgrammaticChangeEvents(false);
        settingsTable.add(spinner);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonMinus());
        addHandListener(spinner.getButtonPlus());
        addTooltip(spinner, "The maximum number of undos that will be kept in memory.", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(spinner, () -> {
            preferences.putInteger(NAME_MAXIMUM_UNDOS, spinner.getValueAsInt());
            preferences.flush();
        });

        settingsTable.row();
        subLabel = new Label("Open to screen:", skin);
        settingsTable.add(subLabel);

        var selectBox = new SelectBox<String>(skin);
        selectBox.setItems("Welcome", "Classic", "Wizard");
        selectBox.setSelected(preferences.getString(NAME_OPEN_TO_SCREEN, DEFAULT_OPEN_TO_SCREEN));
        settingsTable.add(selectBox);
        addHandListener(selectBox);
        addHandListener(selectBox.getList());
        onChange(selectBox, () -> {
            preferences.putString(NAME_OPEN_TO_SCREEN, selectBox.getSelected());
            preferences.flush();
        });

        row();
        var shortcutTable = new Table();
        shortcutTable.columnDefaults(0).right().uniformX();
        shortcutTable.columnDefaults(1).width(90);
        shortcutTable.columnDefaults(2).width(90);
        shortcutTable.columnDefaults(3).uniformX();
        shortcutTable.defaults().space(5);
        add(shortcutTable).padTop(20);

        subLabel = new Label("SHORTCUTS", skin, "header");
        shortcutTable.add(subLabel).colspan(4).align(Align.center);

        shortcutTable.row();
        shortcutTable.add();

        subLabel = new Label("Primary:", skin);
        subLabel.setAlignment(Align.center);
        shortcutTable.add(subLabel);

        subLabel = new Label("Secondary:", skin);
        subLabel.setAlignment(Align.center);
        shortcutTable.add(subLabel);

        shortcutTable.add();

        shortcutTable.row();
        subLabel = new Label("Undo:", skin);
        shortcutTable.add(subLabel);

        var primaryUndoTextField = new TextField("", skin);
        setShortcutTextFieldText(primaryUndoTextField, NAME_PRIMARY_UNDO_MODIFIERS, NAME_PRIMARY_UNDO_SHORTCUT, DEFAULT_PRIMARY_UNDO_MODIFIERS, DEFAULT_PRIMARY_UNDO_SHORTCUT);
        shortcutTable.add(primaryUndoTextField);
        textFields.add(primaryUndoTextField);
        addIbeamListener(primaryUndoTextField);
        addTooltip(primaryUndoTextField, "Primary keyboard shortcut for the Undo action.", Align.top, Align.top, tooltipBottomArrowStyle);
        onTouchDown(primaryUndoTextField, () -> {
            getStage().setKeyboardFocus(null);
            showKeyBindPop(primaryUndoTextField, "UNDO SHORTCUT", getStage(), NAME_PRIMARY_UNDO_MODIFIERS, NAME_PRIMARY_UNDO_SHORTCUT);
        });

        var secondaryUndoTextField = new TextField("", skin);
        setShortcutTextFieldText(secondaryUndoTextField, NAME_SECONDARY_UNDO_MODIFIERS, NAME_SECONDARY_UNDO_SHORTCUT, DEFAULT_SECONDARY_UNDO_MODIFIERS, DEFAULT_SECONDARY_UNDO_SHORTCUT);
        shortcutTable.add(secondaryUndoTextField);
        textFields.add(secondaryUndoTextField);
        addIbeamListener(secondaryUndoTextField);
        addTooltip(secondaryUndoTextField, "Secondary keyboard shortcut for the Undo action.", Align.top, Align.top, tooltipBottomArrowStyle);
        onTouchDown(secondaryUndoTextField, () -> {
            getStage().setKeyboardFocus(null);
            showKeyBindPop(secondaryUndoTextField, "UNDO SHORTCUT", getStage(), NAME_SECONDARY_UNDO_MODIFIERS, NAME_SECONDARY_UNDO_SHORTCUT);
        });

        shortcutTable.row();
        subLabel = new Label("Redo:", skin);
        shortcutTable.add(subLabel);

        var primaryRedoTextField = new TextField("", skin);
        setShortcutTextFieldText(primaryRedoTextField, NAME_PRIMARY_REDO_MODIFIERS, NAME_PRIMARY_REDO_SHORTCUT, DEFAULT_PRIMARY_REDO_MODIFIERS, DEFAULT_PRIMARY_REDO_SHORTCUT);
        shortcutTable.add(primaryRedoTextField);
        textFields.add(primaryRedoTextField);
        addIbeamListener(primaryRedoTextField);
        addTooltip(primaryRedoTextField, "Primary keyboard shortcut for the Redo action.", Align.top, Align.top, tooltipBottomArrowStyle);
        onTouchDown(primaryRedoTextField, () -> {
            getStage().setKeyboardFocus(null);
            showKeyBindPop(primaryRedoTextField, "REDO SHORTCUT", getStage(), NAME_PRIMARY_REDO_MODIFIERS, NAME_PRIMARY_REDO_SHORTCUT);
        });

        var secondaryRedoTextField = new TextField("", skin);
        setShortcutTextFieldText(secondaryRedoTextField, NAME_SECONDARY_REDO_MODIFIERS, NAME_SECONDARY_REDO_SHORTCUT, DEFAULT_SECONDARY_REDO_MODIFIERS, DEFAULT_SECONDARY_REDO_SHORTCUT);
        shortcutTable.add(secondaryRedoTextField);
        textFields.add(secondaryRedoTextField);
        addIbeamListener(secondaryRedoTextField);
        addTooltip(secondaryRedoTextField, "Secondary keyboard shortcut for the Redo action.", Align.top, Align.top, tooltipBottomArrowStyle);
        onTouchDown(secondaryRedoTextField, () -> {
            getStage().setKeyboardFocus(null);
            showKeyBindPop(secondaryRedoTextField, "REDO SHORTCUT", getStage(), NAME_SECONDARY_REDO_MODIFIERS, NAME_SECONDARY_REDO_SHORTCUT);
        });

        row();
        var buttonTable = new Table();
        buttonTable.defaults().space(5);
        add(buttonTable).padTop(20);

        var subButton = new TextButton("Open Preferences Directory", skin);
        buttonTable.add(subButton);
        addHandListener(subButton);
        addTooltip(subButton, "Open the preferences directory where Particle Park Pro saves its settings.", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(subButton, () -> {
            try {
                openFileExplorer(Gdx.files.external(".prefs/"));
            } catch (Exception e) {
                Gdx.app.error(getClass().getName(), "Error opening preferences folder", e);
            }
        });

        buttonTable.row();
        subButton = new TextButton("Reset to Defaults", skin);
        buttonTable.add(subButton);
        addHandListener(subButton);
        addTooltip(subButton, "Reset all settings to their defaults.", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(subButton, this::resetSettingsToDefaults);

        buttonTable.row();
        subButton = new TextButton("Open GitHub Page", skin);
        buttonTable.add(subButton);
        addHandListener(subButton);
        addTooltip(subButton, "Open the GitHub page for Particle Park Pro.", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(subButton, () -> {
            Gdx.net.openURI("https://github.com/raeleus/Particle-Park-Pro");
        });

        checkForDuplicateShortcuts();
    }

    public static void openFileExplorer(FileHandle startDirectory) throws IOException {
        if (startDirectory.exists()) {
            File file = startDirectory.file();
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } else {
            throw new IOException("Directory doesn't exist: " + startDirectory.path());
        }
    }

    private static String constructShortcutText(int[] modifiers, int shortcut) {
        if (shortcut == Keys.ANY_KEY) return "";

        var stringBuilder = new StringBuilder();
        for (var modifier : modifiers) {
            var text = Keys.toString(modifier);
            if (text.startsWith("L-")) text = text.substring(2);
            stringBuilder.append(text);
            stringBuilder.append("+");
        }
        stringBuilder.append(Keys.toString(shortcut));
        return stringBuilder.toString();
    }

    private static int[] readModifierText(String modifiersText) {
        if (modifiersText.isEmpty()) return new int[0];

        var returnValue = new IntArray();
        var strings = modifiersText.split(",");
        for (var string : strings) {
            returnValue.add(Integer.parseInt(string));
        }
        return returnValue.toArray();
    }

    private static String modifiersToText(int[] modifiers) {
        var stringBuilder = new StringBuilder();
        for (int i = 0; i < modifiers.length; i++) {
            var modifier = modifiers[i];
            stringBuilder.append(modifier);
            if (i < modifiers.length - 1) stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }

    public void resetSettingsToDefaults() {
        preferences.putInteger(NAME_MAXIMUM_UNDOS, DEFAULT_MAXIMUM_UNDOS);
        preferences.putString(NAME_OPEN_TO_SCREEN, DEFAULT_OPEN_TO_SCREEN);
        preferences.putInteger(NAME_PRIMARY_UNDO_SHORTCUT, DEFAULT_PRIMARY_UNDO_SHORTCUT);
        preferences.putString(NAME_PRIMARY_UNDO_MODIFIERS, modifiersToText(DEFAULT_PRIMARY_UNDO_MODIFIERS));
        preferences.putInteger(NAME_SECONDARY_UNDO_SHORTCUT, DEFAULT_SECONDARY_UNDO_SHORTCUT);
        preferences.putString(NAME_SECONDARY_UNDO_MODIFIERS, modifiersToText(DEFAULT_SECONDARY_UNDO_MODIFIERS));
        preferences.putInteger(NAME_PRIMARY_REDO_SHORTCUT, DEFAULT_PRIMARY_REDO_SHORTCUT);
        preferences.putString(NAME_PRIMARY_REDO_MODIFIERS, modifiersToText(DEFAULT_PRIMARY_REDO_MODIFIERS));
        preferences.putInteger(NAME_SECONDARY_REDO_SHORTCUT, DEFAULT_SECONDARY_REDO_SHORTCUT);
        preferences.putString(NAME_SECONDARY_REDO_MODIFIERS, modifiersToText(DEFAULT_SECONDARY_REDO_MODIFIERS));
        preferences.flush();

        populate();
    }

    private void setShortcutTextFieldText(TextField textField, String modifiersKey, String shortcutKey, int[] defaultModifiers, int defaultShortcut) {
        if (preferences.contains(modifiersKey) && preferences.contains(shortcutKey)) {
            textField.setText(constructShortcutText(readModifierText(preferences.getString(modifiersKey)), preferences.getInteger(shortcutKey)));
        } else {
            textField.setText(constructShortcutText(defaultModifiers, defaultShortcut));
        }
    }

    private void showKeyBindPop(TextField textField, String name, Stage stage, String modifiersKey, String shortcutKey) {
        var pop = new PopTable(skin.get("key-bind", WindowStyle.class));
        pop.setHideOnUnfocus(true);

        var label = new Label("Press a key combination for\n" + name + "\nPress ESCAPE to clear", skin, "bold");
        label.setAlignment(Align.center);
        pop.add(label);
        label.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.CONTROL_LEFT || keycode == Keys.CONTROL_RIGHT) return false;
                if (keycode == Keys.SHIFT_LEFT || keycode == Keys.SHIFT_RIGHT) return false;
                if (keycode == Keys.ALT_LEFT || keycode == Keys.ALT_RIGHT) return false;

                var intArray = new IntArray();
                if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) intArray.add(Keys.CONTROL_LEFT);
                if (Gdx.input.isKeyPressed(Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Keys.ALT_RIGHT)) intArray.add(Keys.ALT_LEFT);
                if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) intArray.add(Keys.SHIFT_LEFT);

                if (keycode == Keys.ESCAPE) {
                    preferences.putInteger(shortcutKey, Keys.ANY_KEY);
                    preferences.putString(modifiersKey, "");
                    preferences.flush();
                    textField.setText(constructShortcutText(readModifierText(preferences.getString(modifiersKey)), preferences.getInteger(shortcutKey)));

                    pop.hide();
                    checkForDuplicateShortcuts();
                    return false;
                }

                preferences.putInteger(shortcutKey, keycode);
                preferences.putString(modifiersKey, modifiersToText(intArray.toArray()));
                preferences.flush();
                textField.setText(constructShortcutText(readModifierText(preferences.getString(modifiersKey)), preferences.getInteger(shortcutKey)));

                pop.hide();
                checkForDuplicateShortcuts();
                return false;
            }
        });

        pop.show(stage);
        stage.setKeyboardFocus(label);
    }

    private void checkForDuplicateShortcuts() {
        var texts = new Array<String>();
        for (int i = 0; i < textFields.size; i++) {
            var textField = textFields.get(i);
            var text = textField.getText();

            var index = texts.indexOf(text, false);
            if (!text.equals("") && index != -1) {
                textFields.get(index).setColor(Color.RED);
                textField.setColor(Color.RED);
            } else {
                textField.setColor(Color.WHITE);
            }

            texts.add(text);
        }
    }
}
