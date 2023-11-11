package com.ray3k.particleparkpro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.ray3k.particleparkpro.shortcuts.ShortcutUtils;

import java.util.Map;

import static com.ray3k.particleparkpro.Core.preferences;

public class Settings {
    public static final String NAME_MAXIMUM_UNDOS = "Maximum undos";
    public static final String NAME_CHECK_FOR_UPDATES = "Check for updates";
    public static final String NAME_PRESUME_FILE_EXTENSION = "Presume file extension";
    public static final String NAME_OPEN_TO_SCREEN = "Open to screen";
    public static final String NAME_SCALE = "Scale";
    public static final String NAME_PRIMARY_UNDO_MODIFIERS = "Primary undo modifiers";
    public static final String NAME_PRIMARY_UNDO_SHORTCUT = "Primary undo shortcut";
    public static final String NAME_SECONDARY_UNDO_MODIFIERS = "Secondary undo modifiers";
    public static final String NAME_SECONDARY_UNDO_SHORTCUT = "Secondary undo shortcut";
    public static final String NAME_PRIMARY_REDO_MODIFIERS = "Primary redo modifiers";
    public static final String NAME_PRIMARY_REDO_SHORTCUT = "Primary redo shortcut";
    public static final String NAME_SECONDARY_REDO_MODIFIERS = "Secondary redo modifiers";
    public static final String NAME_SECONDARY_REDO_SHORTCUT = "Secondary redo shortcut";
    public static final int DEFAULT_MAXIMUM_UNDOS = 100;
    public static final boolean DEFAULT_CHECK_FOR_UPDATES = true;
    public static final boolean DEFAULT_PRESUME_FILE_EXTENSION = true;
    public static final String DEFAULT_OPEN_TO_SCREEN = "Welcome";
    public static final float DEFAULT_SCALE = 1f;

    public static FileHandle logFile;
    public static Map<String, ?> preferenceMap;
    public static final int GLOBAL_SCOPE = 0;
    public static final int CLASSIC_SCOPE = 1;
    public static final int WIZARD_SCOPE = 2;
    public static final ObjectMap<String, int[]> DEFAULT_KEYBINDS = new ObjectMap<>();

    static {

    }

    public static void initialize() {
        DEFAULT_KEYBINDS.put("Undo", new int[] {Keys.CONTROL_LEFT, Keys.Z});
        DEFAULT_KEYBINDS.put("Redo", new int[] {Keys.CONTROL_LEFT, Keys.Y});
        DEFAULT_KEYBINDS.put("Save", new int[] {Keys.CONTROL_LEFT, Keys.SHIFT_LEFT, Keys.S});
        DEFAULT_KEYBINDS.put("SaveAs", new int[] {Keys.CONTROL_LEFT, Keys.S});
        DEFAULT_KEYBINDS.put("Open", new int[] {Keys.CONTROL_LEFT, Keys.O});

        preferenceMap = preferences.get();
//        undoPrimaryShortcut = preferences.getInteger(NAME_PRIMARY_UNDO_SHORTCUT, DEFAULT_PRIMARY_UNDO_SHORTCUT);
//
//        undoPrimaryModifiers.clear();
//        if (preferences.contains(NAME_PRIMARY_UNDO_MODIFIERS)) undoPrimaryModifiers.addAll(readModifierText(preferences.getString(NAME_PRIMARY_UNDO_MODIFIERS)));
//        else undoPrimaryModifiers.addAll(DEFAULT_PRIMARY_UNDO_MODIFIERS);
//
//        redoPrimaryShortcut = preferences.getInteger(NAME_PRIMARY_REDO_SHORTCUT, DEFAULT_PRIMARY_REDO_SHORTCUT);
//
//        redoPrimaryModifiers.clear();
//        if (preferences.contains(NAME_PRIMARY_REDO_MODIFIERS)) redoPrimaryModifiers.addAll(readModifierText(preferences.getString(NAME_PRIMARY_REDO_MODIFIERS)));
//        else redoPrimaryModifiers.addAll(DEFAULT_PRIMARY_REDO_MODIFIERS);
//
//        undoSecondaryShortcut = preferences.getInteger(NAME_SECONDARY_UNDO_SHORTCUT, DEFAULT_SECONDARY_UNDO_SHORTCUT);
//
//        undoSecondaryModifiers.clear();
//        if (preferences.contains(NAME_SECONDARY_UNDO_MODIFIERS)) undoSecondaryModifiers.addAll(readModifierText(preferences.getString(NAME_SECONDARY_UNDO_MODIFIERS)));
//        else undoSecondaryModifiers.addAll(DEFAULT_SECONDARY_UNDO_MODIFIERS);
//
//        redoSecondaryShortcut = preferences.getInteger(NAME_SECONDARY_REDO_SHORTCUT, DEFAULT_SECONDARY_REDO_SHORTCUT);
//
//        redoSecondaryModifiers.clear();
//        if (preferences.contains(NAME_SECONDARY_REDO_MODIFIERS)) redoSecondaryModifiers.addAll(readModifierText(preferences.getString(NAME_SECONDARY_REDO_MODIFIERS)));
//        else redoSecondaryModifiers.addAll(DEFAULT_SECONDARY_REDO_MODIFIERS);
    }

    public static void resetKeybinds(boolean flush) {
        for (Entry<String, int[]> e : DEFAULT_KEYBINDS) {
            ShortcutUtils.setKeybind(e.key, e.value, flush);
        }
    }

    public static int getInt(String key) {
        return (int) preferenceMap.get(key);
    }

    public static String getDefaultImagePath() {
        return preferences.getString("defaultImagePath", getDefaultSavePath());
    }

    public static void setDefaultImagePath(FileHandle defaultImagePath) {
        preferences.putString("defaultImagePath", defaultImagePath.path());
        preferences.flush();
    }

    public static String getDefaultShaderPath() {
        return preferences.getString("defaultShaderPath", getDefaultSavePath());
    }

    public static void setDefaultShaderPath(FileHandle defaultShaderPath) {
        preferences.putString("defaultShaderPath", defaultShaderPath.path());
        preferences.flush();
    }

    public static String getDefaultSavePath() {
        return preferences.getString("defaultSavePath", Gdx.files.getLocalStoragePath());
    }

    public static void setDefaultSavePath(FileHandle defaultSavePath) {
        preferences.putString("defaultSavePath", defaultSavePath.path());
        preferences.flush();
    }

    public static int[] readModifierText(String modifiersText) {
        if (modifiersText.isEmpty()) return new int[0];

        var returnValue = new IntArray();
        var strings = modifiersText.split(",");
        for (var string : strings) {
            returnValue.add(Integer.parseInt(string));
        }
        return returnValue.toArray();
    }

    public static String modifiersToText(int[] modifiers) {
        var stringBuilder = new StringBuilder();
        for (int i = 0; i < modifiers.length; i++) {
            var modifier = modifiers[i];
            stringBuilder.append(modifier);
            if (i < modifiers.length - 1) stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }
}
