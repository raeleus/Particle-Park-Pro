package com.ray3k.particleparkpro.shortcuts;

import static com.ray3k.particleparkpro.Core.*;

public class ShortcutUtils {

    private static final int[] EMPTY_KEYBIND = new int[] {0};
    private static final String PREF_ID = "Shortcut";

    public static int getPackedKeybind (Shortcut shortcut, int[] defaultKeybind) {
        return preferences.getInteger(shortcut.getName() + PREF_ID, ShortcutManager.packKeybindUnsorted(defaultKeybind));
    }

    public static Shortcut createShortcut (String name, String toolTipDesc, int[] defaultKeybind, int scope, Runnable runnable) {
        Shortcut s = new Shortcut(name, toolTipDesc, runnable);
        s.setScope(scope);
        s.setKeybindPacked(getPackedKeybind(s, defaultKeybind));
        return s;
    }

    public static void clearKeybind(KeyMap keyMap, Shortcut s, boolean flush) {
        keyMap.changeKeybind(s, EMPTY_KEYBIND);
        preferences.putInteger(s.getName() + PREF_ID, 0);
        if (flush) preferences.flush();
    }

    public static void setKeybind(KeyMap keyMap, Shortcut s, int[] unsortedKeybind, boolean flush) {
        keyMap.changeKeybind(s, unsortedKeybind);
        preferences.putInteger(s.getName() + PREF_ID, s.getKeybindPacked());
        if (flush) preferences.flush();
    }

    public static void setKeybind(KeyMap keyMap, Shortcut s, int packedKeybind, boolean flush) {
        keyMap.changeKeybind(s, packedKeybind);
        preferences.putInteger(s.getName() + PREF_ID, s.getKeybindPacked());
        if (flush) preferences.flush();
    }

    public static void setKeybindPreference (String name, int[] unsortedKeybind, boolean flush) {
        preferences.putInteger(name + PREF_ID, ShortcutManager.packKeybindUnsorted(unsortedKeybind)) ;
        if (flush) preferences.flush();
    }

}
