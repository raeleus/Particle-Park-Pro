package com.ray3k.particleparkpro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.IntArray;

import static com.ray3k.particleparkpro.Core.preferences;

/**
 * This class organizes the various names used in the app preferences, the setting defaults, and provides methods for
 * interpreting the settings saved in the file.
 */
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
    public static final int[] DEFAULT_PRIMARY_UNDO_MODIFIERS = new int[] {Keys.CONTROL_LEFT};
    public static final int DEFAULT_PRIMARY_UNDO_SHORTCUT = Keys.Z;
    public static final int[] DEFAULT_SECONDARY_UNDO_MODIFIERS = new int[] {};
    public static final int DEFAULT_SECONDARY_UNDO_SHORTCUT = Keys.ANY_KEY;
    public static final int[] DEFAULT_PRIMARY_REDO_MODIFIERS = new int[] {Keys.CONTROL_LEFT};
    public static final int DEFAULT_PRIMARY_REDO_SHORTCUT = Keys.Y;
    public static final int[] DEFAULT_SECONDARY_REDO_MODIFIERS = new int[] {Keys.CONTROL_LEFT, Keys.SHIFT_LEFT};
    public static final int DEFAULT_SECONDARY_REDO_SHORTCUT = Keys.Z;
    public static int undoPrimaryShortcut;
    public static int undoSecondaryShortcut;
    public static int redoPrimaryShortcut;
    public static int redoSecondaryShortcut;
    public static final IntArray undoPrimaryModifiers = new IntArray();
    public static final IntArray redoPrimaryModifiers = new IntArray();
    public static final IntArray undoSecondaryModifiers = new IntArray();
    public static final IntArray redoSecondaryModifiers = new IntArray();
    public static FileHandle logFile;

    public static void initializeSettings() {
        undoPrimaryShortcut = preferences.getInteger(NAME_PRIMARY_UNDO_SHORTCUT, DEFAULT_PRIMARY_UNDO_SHORTCUT);

        undoPrimaryModifiers.clear();
        if (preferences.contains(NAME_PRIMARY_UNDO_MODIFIERS)) undoPrimaryModifiers.addAll(readModifierText(preferences.getString(NAME_PRIMARY_UNDO_MODIFIERS)));
        else undoPrimaryModifiers.addAll(DEFAULT_PRIMARY_UNDO_MODIFIERS);

        redoPrimaryShortcut = preferences.getInteger(NAME_PRIMARY_REDO_SHORTCUT, DEFAULT_PRIMARY_REDO_SHORTCUT);

        redoPrimaryModifiers.clear();
        if (preferences.contains(NAME_PRIMARY_REDO_MODIFIERS)) redoPrimaryModifiers.addAll(readModifierText(preferences.getString(NAME_PRIMARY_REDO_MODIFIERS)));
        else redoPrimaryModifiers.addAll(DEFAULT_PRIMARY_REDO_MODIFIERS);

        undoSecondaryShortcut = preferences.getInteger(NAME_SECONDARY_UNDO_SHORTCUT, DEFAULT_SECONDARY_UNDO_SHORTCUT);

        undoSecondaryModifiers.clear();
        if (preferences.contains(NAME_SECONDARY_UNDO_MODIFIERS)) undoSecondaryModifiers.addAll(readModifierText(preferences.getString(NAME_SECONDARY_UNDO_MODIFIERS)));
        else undoSecondaryModifiers.addAll(DEFAULT_SECONDARY_UNDO_MODIFIERS);

        redoSecondaryShortcut = preferences.getInteger(NAME_SECONDARY_REDO_SHORTCUT, DEFAULT_SECONDARY_REDO_SHORTCUT);

        redoSecondaryModifiers.clear();
        if (preferences.contains(NAME_SECONDARY_REDO_MODIFIERS)) redoSecondaryModifiers.addAll(readModifierText(preferences.getString(NAME_SECONDARY_REDO_MODIFIERS)));
        else redoSecondaryModifiers.addAll(DEFAULT_SECONDARY_REDO_MODIFIERS);
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
