package com.ray3k.particleparkpro.shortcuts;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;

public class KeyMap {

    private final ObjectMap<String, Shortcut> allShortcuts;

    private final IntMap<Shortcut> activeShortcuts;

    public KeyMap () {
        allShortcuts = new ObjectMap<>();
        activeShortcuts = new IntMap<>();
    }

    public Array<Shortcut> getActiveShortcuts () {
        return activeShortcuts.values().toArray();
    }

    public Array<Shortcut> getAllShortcuts() {
        return allShortcuts.values().toArray();
    }

    public Shortcut getShortcut (int keybindPacked) {
        return activeShortcuts.get(keybindPacked);
    }

    public boolean hasKeybind (int keybindPacked) {
       return activeShortcuts.containsKey(keybindPacked);
    }

    public void addAll (Array<Shortcut> arr) {
        for (Shortcut s : arr) {
            add(s);
        }
    }

    public void add (Shortcut shortcut) {
        if (shortcut.getKeybind().length > 0) {
            int packed = ShortcutManager.packKeybindUnsorted(shortcut.getKeybind());
            shortcut.setKeybindPacked(packed);
            activeShortcuts.put(packed, shortcut);
        } else if (shortcut.getKeybindPacked() > 0) {
            int[] unpacked = ShortcutManager.unpacKeybind(shortcut.getKeybindPacked());
            isValidKeybind(unpacked);
            shortcut.setKeybind(unpacked);
            activeShortcuts.put(shortcut.getKeybindPacked(), shortcut);
        }

        allShortcuts.put(shortcut.getName(), shortcut);
    }

    public void removeKeybind (Shortcut shortcut) {
        activeShortcuts.remove(shortcut.getKeybindPacked());
        shortcut.setKeybind(null);
        shortcut.setKeybindPacked(0);
    }

    public void changeKeybind (Shortcut shortcut, int[] newKeybind) {
        activeShortcuts.remove(shortcut.getKeybindPacked());

        int packed = ShortcutManager.packKeybindUnsorted(newKeybind);
        shortcut.setKeybind(newKeybind);
        shortcut.setKeybindPacked(packed);
        activeShortcuts.put(packed, shortcut);
    }

    public void changeKeybind (Shortcut shortcut, int packed) {
        activeShortcuts.remove(shortcut.getKeybindPacked());
        shortcut.setKeybindPacked(packed);
        shortcut.setKeybind(ShortcutManager.unpacKeybind(packed));
        activeShortcuts.put(packed, shortcut);
    }

    public void changeAllKeybinds (ObjectMap<String, int[]> keybinds) {
        Array<Shortcut> shortcuts = getAllShortcuts();
        for (Shortcut s : shortcuts) {
            int[] keybind = keybinds.get(s.getName());
            if (keybind != null) {
                changeKeybind(s, keybind);
            }
        }
    }

    // Only modifiers is invalid
	// Empty is invalid
	// Using a restricted keybind is invalid
	// Only one non modifier key is allowed
	private void isValidKeybind (int[] keys) {
		if (keys == null || keys.length == 0 || keys.length > ShortcutManager.MAX_KEYS) {
			throw new InvalidShortcutException(
				"Keybind must not be null and have a length between 0 and " + ShortcutManager.MAX_KEYS);
		}

		boolean allModifiers = true;
		boolean hasNormalKey = false;
		boolean hasShift = false;
		boolean hasAlt = false;
		boolean hasControl = false;

		for (int i = 0; i < keys.length; i++) {
			// Treat left and right modifier keys the same
			switch (keys[i]) {
			case Keys.ALT_LEFT:
			case Keys.ALT_RIGHT:
            case Keys.SYM:
				if (hasAlt) throw new InvalidShortcutException("Alt key already added.");
				hasAlt = true;
				break;
			case Keys.CONTROL_LEFT:
			case Keys.CONTROL_RIGHT:
				if (hasControl) throw new InvalidShortcutException("Control key already added.");
				hasControl = true;
				break;
			case Keys.SHIFT_LEFT:
			case Keys.SHIFT_RIGHT:
				if (hasShift) throw new InvalidShortcutException("Shift key already added.");
				hasShift = true;
				break;
			default:
				if (hasNormalKey) throw new InvalidShortcutException("Keybind must have a maximum of 1 non modifier key");

				hasNormalKey = true;
				allModifiers = false;
			}
		}

		if (allModifiers) throw new InvalidShortcutException("All modifier keys are not allowed.");
	}
}
