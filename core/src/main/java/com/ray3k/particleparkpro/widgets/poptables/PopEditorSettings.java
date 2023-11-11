package com.ray3k.particleparkpro.widgets.poptables;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.ray3k.particleparkpro.Core;
import com.ray3k.particleparkpro.shortcuts.KeyMap;
import com.ray3k.particleparkpro.shortcuts.Shortcut;
import com.ray3k.particleparkpro.shortcuts.ShortcutManager;
import com.ray3k.particleparkpro.shortcuts.ShortcutUtils;
import com.ray3k.stripe.PopTable;
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Settings.*;

public class PopEditorSettings extends PopTable {
    private static final Array<TextField> textFields = new Array<>();
    private boolean displayCommandInsteadOfAlt;
    private UIscale uiScale;
    private Table shortcutTable;

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

        var label = new Label("SETTINGS", skin, "bold");
        add(label).padBottom(10);

        //misc settings
        row();
        var settingsTable = new Table();
        settingsTable.columnDefaults(0).right().uniformX();
        settingsTable.columnDefaults(1).left().uniformX().width(80);
        settingsTable.defaults().space(5);
        add(settingsTable);

        label = new Label("Maximum Undos:", skin);
        settingsTable.add(label);

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
        label = new Label("Open to screen:", skin);
        settingsTable.add(label);

        var selectBox = new SelectBox<String>(skin);
        selectBox.setItems("Welcome", "Classic", "Wizard");
        selectBox.setSelected(preferences.getString(NAME_OPEN_TO_SCREEN, DEFAULT_OPEN_TO_SCREEN));
        settingsTable.add(selectBox);
        addHandListener(selectBox);
        addHandListener(selectBox.getList());
        addTooltip(spinner, "The default screen that the app opens to.", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(selectBox, () -> {
            preferences.putString(NAME_OPEN_TO_SCREEN, selectBox.getSelected());
            preferences.flush();
        });

        //checkboxes
        settingsTable.row();
        var checkBoxTable = new Table();
        checkBoxTable.defaults().left().space(5);
        settingsTable.add(checkBoxTable).colspan(2).center();

        var checkForUpdatesCheckBox = new CheckBox("Check for updates", skin);
        checkForUpdatesCheckBox.setChecked(preferences.getBoolean(NAME_CHECK_FOR_UPDATES, DEFAULT_CHECK_FOR_UPDATES));
        checkBoxTable.add(checkForUpdatesCheckBox);
        addHandListener(checkForUpdatesCheckBox);
        addTooltip(checkForUpdatesCheckBox, "Whether or not the app checks to see if there is an update available at startup", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(checkForUpdatesCheckBox, () -> {
            preferences.putBoolean(NAME_CHECK_FOR_UPDATES, checkForUpdatesCheckBox.isChecked());
            preferences.flush();
        });

        checkBoxTable.row();
        var presumeFileExtensionCheckBox = new CheckBox("Presume file extension is .p", skin);
        presumeFileExtensionCheckBox.setChecked(preferences.getBoolean(NAME_PRESUME_FILE_EXTENSION, DEFAULT_PRESUME_FILE_EXTENSION));
        checkBoxTable.add(presumeFileExtensionCheckBox);
        addHandListener(presumeFileExtensionCheckBox);
        addTooltip(presumeFileExtensionCheckBox, "Whether or not the app defaults all particle file dialogs to use the \".p\" file extension", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(presumeFileExtensionCheckBox, () -> {
            preferences.putBoolean(NAME_PRESUME_FILE_EXTENSION, presumeFileExtensionCheckBox.isChecked());
            preferences.flush();
        });

        //sliders
        settingsTable.row();
        var sliderTable = new Table();
        sliderTable.defaults().space(5);
        settingsTable.add(sliderTable).colspan(2).center();

        label = new Label("UI Scale:", skin);
        sliderTable.add(label);

        var slider = new Slider(0, 4, 1, false, skin);
        uiScale = valueToUIscale(preferences.getFloat(NAME_SCALE, DEFAULT_SCALE));
        var scaleArray = new Array<>(UIscale.values());
        slider.setValue(scaleArray.indexOf(uiScale, true));
        sliderTable.add(slider).width(80);
        addHandListener(slider);
        addTooltip(slider, "Increase the UI Scale for high DPI displays.", Align.top, Align.top, tooltipBottomArrowStyle);

        var scaleLabel = new Label(uiScale.text, skin);
        sliderTable.add(scaleLabel).padRight(5).width(20);
        onChange(slider, () -> {
            var index = MathUtils.round(slider.getValue());
            uiScale = UIscale.values()[index];
            scaleLabel.setText(uiScale.text);
        });

        var textButton = new TextButton("Apply", skin);
        sliderTable.add(textButton);
        addHandListener(textButton);
        addTooltip(textButton, "Apply the UI Scale for high DPI displays.", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(textButton, () -> {
            updateViewportScale(uiScale);
            showConfirmScalePop();
        });

        // Shortcuts
        row();

         label = new Label("SHORTCUTS", skin, "header");
         add(label).padBottom(10).padTop(20);
        row();

        final String prefName = "Display command instead of alt";
        var useCommandInsteadAlt = new CheckBox("(MacOs) Display command instead of alt", skin);
        useCommandInsteadAlt.setChecked(preferences.getBoolean(prefName, false));
        displayCommandInsteadOfAlt = useCommandInsteadAlt.isChecked();
        addHandListener(useCommandInsteadAlt);
        addTooltip(useCommandInsteadAlt, "Displays command instead of alt", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(useCommandInsteadAlt, () -> {
            displayCommandInsteadOfAlt = useCommandInsteadAlt.isChecked();
            preferences.putBoolean(prefName, displayCommandInsteadOfAlt);
            preferences.flush();
            refreshShortcutTable();
        });
        add(useCommandInsteadAlt).center().padBottom(10);
        row();

        shortcutTable = new Table();
        shortcutTable.columnDefaults(0).right();
        shortcutTable.columnDefaults(1).left().uniformX();
        shortcutTable.defaults().space(5);
        add(shortcutTable).padTop(0);

        shortcutTable.row();
        shortcutTable.row();

        refreshShortcutTable();

        //buttons
        row();
        var buttonTable = new Table();
        buttonTable.defaults().space(5).uniformX().fillX();
        add(buttonTable).padTop(20);

        var subButton = new TextButton("Open Preferences Directory", skin);
        buttonTable.add(subButton);
        addHandListener(subButton);
        addTooltip(subButton, "Open the preferences directory where Particle Park Pro saves its settings.", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(subButton, () -> {
            try {
                openFileExplorer(Gdx.files.external(".prefs/"));
            } catch (IOException e) {
                var error = "Error opening preferences directory.";
                var pop = new PopError(error, e.getMessage());
                pop.show(stage);

                Gdx.app.error(Core.class.getName(), error, e);
            }
        });

        buttonTable.row();
        subButton = new TextButton("Open Log Directory", skin);
        buttonTable.add(subButton);
        addHandListener(subButton);
        addTooltip(subButton, "Open the log directory where Particle Park Pro saves errors.", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(subButton, () -> {
            try {
                openFileExplorer(Gdx.files.external(".particleparkpro/"));
            } catch (IOException e) {
                var error = "Error opening log directory.";
                var pop = new PopError(error, e.getMessage());
                pop.show(stage);

                Gdx.app.error(Core.class.getName(), error, e);
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

    int confirmTime;

    private void showConfirmScalePop() {
        var pop = new PopTable(skin.get("confirm-scale", WindowStyle.class));
        pop.setModal(true);

         confirmTime = 5;

        var label = new Label("", skin, "confirm-scale");
        label.setAlignment(Align.center);
        pop.add(label).size(200, 100);
        label.addAction(Actions.sequence(
            Actions.repeat(confirmTime, Actions.sequence(Actions.run(() -> label.setText("Click to confirm scale.\nResetting in " + (--confirmTime + 1) + "...")), Actions.delay(1f))),
            Actions.run(() -> {
                pop.hide();
                label.remove();
                uiScale = valueToUIscale(preferences.getFloat(NAME_SCALE, DEFAULT_SCALE));
                updateViewportScale(uiScale);
                populate();
            })));

        pop.show(foregroundStage);

        onClick(pop.getParentGroup(), () -> {
            preferences.putFloat(NAME_SCALE, uiScale.multiplier);
            preferences.flush();
            label.remove();
            pop.hide();
        });

    }

    private void refreshShortcutTable() {
        shortcutTable.clearChildren();
        for (Shortcut s : keyMap.getAllShortcuts()) {
            createShortcutSettingsOption(s, shortcutTable);
        }
    }

    private void createShortcutSettingsOption(Shortcut shortcut, Table shortcutTable) {
        shortcutTable.row();

        var label = new Label(shortcut.getName() + ":", skin);
        shortcutTable.add(label);

        var textField = new TextField("", skin);
        textField.setText(constructShortcutText(shortcut.getPrimaryKeybind()));
        shortcutTable.add(textField);
        textFields.add(textField);
        addIbeamListener(textField);
        addTooltip(textField, shortcut.getDescription(), Align.top, Align.top, tooltipBottomArrowStyle);
        onTouchDown(textField, () -> {
            getStage().setKeyboardFocus(null);
            showKeyBindPop(textField, shortcut, getStage());
        });

        var clearButton = new Button(skin, "cancel");
        shortcutTable.add(clearButton);
        addHandListener(clearButton);
        onTouchDown(clearButton, () -> {
            textField.setText("");
            ShortcutUtils.clearKeybind(keyMap, shortcut, true);
        });
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

    private String constructShortcutText(int[] keybind) {
       var stringBuilder = new StringBuilder();
       for (int i = 0; i < keybind.length; i++) {
            int keycode = keybind[i] ;

            if (keycode <= 0) continue;

           var text = Keys.toString(keybind[i]);
           if (text.startsWith("L-")) text = text.substring(2);
           if (text.equals("Alt") && displayCommandInsteadOfAlt) {
              text = "Cmd";
           }
            stringBuilder.append(text);
            if (i < keybind.length - 1) stringBuilder.append(" + ");
       }
       return stringBuilder.toString();
    }

    public void resetSettingsToDefaults() {
        resetKeybinds(false);
        keyMap.changeAllKeybinds(DEFAULT_KEYBINDS);
        preferences.putInteger(NAME_MAXIMUM_UNDOS, DEFAULT_MAXIMUM_UNDOS);
        preferences.putString(NAME_OPEN_TO_SCREEN, DEFAULT_OPEN_TO_SCREEN);
        preferences.putBoolean(NAME_CHECK_FOR_UPDATES, DEFAULT_CHECK_FOR_UPDATES);
        preferences.putBoolean(NAME_PRESUME_FILE_EXTENSION, DEFAULT_PRESUME_FILE_EXTENSION);
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

    private void showKeyBindPop(TextField textField, Shortcut shortcut, Stage stage) {
        var pop = new PopTable(skin.get("key-bind", WindowStyle.class));
        pop.setHideOnUnfocus(true);

        var label = new Label("Press a key combination for\n" + shortcut.getName() + "\nPress ESCAPE to clear", skin, "bold");
        label.setAlignment(Align.center);
        pop.add(label);
        label.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.CONTROL_LEFT || keycode == Keys.CONTROL_RIGHT) return false;
                if (keycode == Keys.SHIFT_LEFT || keycode == Keys.SHIFT_RIGHT) return false;
                if (keycode == Keys.ALT_LEFT || keycode == Keys.ALT_RIGHT) return false;
                if (keycode == Keys.SYM) return false;

                var intArray = new IntArray();
                if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) intArray.add(Keys.CONTROL_LEFT);
                if (Gdx.input.isKeyPressed(Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Keys.ALT_RIGHT)) intArray.add(Keys.ALT_LEFT);
                if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) intArray.add(Keys.SHIFT_LEFT);
                if (Gdx.input.isKeyPressed(Keys.SYM)) intArray.add(Keys.SYM);

                boolean clearShortcut = keycode == Keys.ESCAPE;

                if (clearShortcut) {
                    textField.setText("");
                    ShortcutUtils.clearKeybind(keyMap, shortcut, true);
                    checkForDuplicateShortcuts();
                } else {
                    intArray.add(keycode);
                    int[] keybind = ShortcutManager.sortKeybind(intArray.toArray());
                    int packed = ShortcutManager.packKeybindSorted(keybind);
                    textField.setText(constructShortcutText(keybind));

                    if (keyMap.hasKeybind(packed)) {
                        checkForDuplicateShortcuts();
                    } else {
                        ShortcutUtils.setKeybind(keyMap, shortcut, keybind, true);
                    }
                }

                pop.hide();
                initialize();
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
