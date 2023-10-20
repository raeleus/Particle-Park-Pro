package com.ray3k.particleparkpro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;

import static com.ray3k.particleparkpro.Core.*;

public class Settings {
    public static final String NAME_MAXIMUM_UNDOS = "Maximum undos";
    public static final String NAME_CHECK_FOR_UPDATES = "Check for updates";
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
    public static final boolean DEFAULT_CHECK_FOR_UPDATES = true;
    public static final String DEFAULT_OPEN_TO_SCREEN = "Welcome";
    public static final int[] DEFAULT_PRIMARY_UNDO_MODIFIERS = new int[] {Keys.CONTROL_LEFT};
    public static final int DEFAULT_PRIMARY_UNDO_SHORTCUT = Keys.Z;
    public static final int[] DEFAULT_SECONDARY_UNDO_MODIFIERS = new int[] {};
    public static final int DEFAULT_SECONDARY_UNDO_SHORTCUT = Keys.ANY_KEY;
    public static final int[] DEFAULT_PRIMARY_REDO_MODIFIERS = new int[] {Keys.CONTROL_LEFT};
    public static final int DEFAULT_PRIMARY_REDO_SHORTCUT = Keys.Y;
    public static final int[] DEFAULT_SECONDARY_REDO_MODIFIERS = new int[] {Keys.CONTROL_LEFT, Keys.SHIFT_LEFT};
    public static final int DEFAULT_SECONDARY_REDO_SHORTCUT = Keys.Z;

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
}
