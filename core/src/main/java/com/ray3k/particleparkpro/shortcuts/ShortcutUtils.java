package com.ray3k.particleparkpro.shortcuts;

import com.badlogic.gdx.Input.Keys;
import com.ray3k.particleparkpro.Core;

import static com.ray3k.particleparkpro.Core.*;

public class ShortcutUtils {

    private static final String PREF_ID = "Shortcut";

    public static int getPackedKeybind (Shortcut shortcut, int[] defaultKeybind) {
        return preferences.getInteger(shortcut.getName() + PREF_ID, ShortcutManager.packKeybindUnsorted(defaultKeybind));
    }

    public static Shortcut createShortcut (String name, String toolTipDesc, int[] defaultKeybind, int scope, Runnable runnable) {
        Shortcut s = new Shortcut(name, toolTipDesc, runnable);
        s.setScope(scope);
        s.setPrimaryKeybindPacked(getPackedKeybind(s, defaultKeybind));
        return s;
    }

    public static void clearKeybind(KeyMap keyMap, Shortcut s, boolean flush) {
        if (keyMap == null) return;
        preferences.putInteger(s.getName() + PREF_ID, 0);
        if (flush) preferences.flush();
    }

    public static void setKeybind(KeyMap keyMap, Shortcut s, int[] unsortedKeybind, boolean flush) {
        keyMap.changeKeybind(s, unsortedKeybind);
        preferences.putInteger(s.getName() + PREF_ID, s.getPrimaryKeybindPacked());
        if (flush) preferences.flush();
    }

    public static void setKeybind(String name, int[] unsortedKeybind, boolean flush) {
        preferences.putInteger(name + PREF_ID, ShortcutManager.packKeybindUnsorted(unsortedKeybind)) ;
        if (flush) preferences.flush();
    }

}
