package com.ray3k.particleparkpro.shortcuts;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Logger;

import java.util.Arrays;

public class ShortcutManager extends InputListener {

    public static final Logger LOGGER = new Logger("ShortcutManager");
    public static final int MAX_KEYS = 4;
    private static final int[] TEMP = new int[4];

    private int currentlyPressedKeysPacked;
    private int[] pressedKeys;
    private static int scope;
    private boolean dirty;
    private boolean disabled;
    private KeyMap keyMap;
    private ShortcutManagerFilter filter;

    public static void setScope (int scope) {
        ShortcutManager.scope = scope;
    }

    public ShortcutManager() {
        pressedKeys = new int[MAX_KEYS];
    }

    public void setKeyMap (KeyMap keyMap) {
        this.keyMap = keyMap;
    }

    public void setFilter (ShortcutManagerFilter filter) {
        this.filter = filter;
    }

    public int getCurrentlyPressedKeysPacked() {
        return currentlyPressedKeysPacked;
    }

    public void setDisabled (boolean disabled) {
        this.disabled = disabled;
    }

    private static boolean setKey (int[] keys, int keycode) {
		boolean dirty = false;

		if (keycode == 0) return dirty;

		switch (keycode) {
		case Input.Keys.CONTROL_LEFT:
		case Input.Keys.CONTROL_RIGHT:
			if (keys[0] == 0) {
				keys[0] = Input.Keys.CONTROL_LEFT;
				dirty = true;
			}
			break;
		case Input.Keys.SHIFT_LEFT:
		case Input.Keys.SHIFT_RIGHT:
			if (keys[1] == 0) {
				keys[1] = Input.Keys.SHIFT_LEFT;
				dirty = true;
			}
			break;
		case Input.Keys.ALT_LEFT:
		case Input.Keys.ALT_RIGHT:
        case Input.Keys.SYM:
			if (keys[2] == 0) {
				keys[2] = Input.Keys.ALT_LEFT;
				dirty = true;
			}
			break;
		default:
			if (keys[3] == 0) {
				keys[3] = keycode;
				dirty = true;
			}
			break;
		}

		return dirty;
	}

    private void clearKey (int keycode) {
		switch (keycode) {
		case Input.Keys.CONTROL_LEFT:
		case Input.Keys.CONTROL_RIGHT:
			if (pressedKeys[0] > 0) {
				pressedKeys[0] = 0;
				dirty = true;
			}
			break;
		case Input.Keys.SHIFT_LEFT:
		case Input.Keys.SHIFT_RIGHT:
			if (pressedKeys[1] > 0) {
				pressedKeys[1] = 0;
				dirty = true;
			}
			break;
		case Input.Keys.ALT_LEFT:
		case Input.Keys.ALT_RIGHT:
			if (pressedKeys[2] > 0) {
				pressedKeys[2] = 0;
				dirty = true;
			}
			break;
		default:
			if (pressedKeys[3] > 0) {
				pressedKeys[3] = 0;
				dirty = true;
			}
			break;
		}
	}

    @Override
    public boolean keyUp (InputEvent event, int keycode) {
        if (keyMap == null) return false;
        clearKey(keycode);
        pack();
        return false;
    }

    @Override
    public boolean keyDown (InputEvent event, int keycode) {
        if (keyMap == null || disabled || pressedKeys[3] != 0) return false;

        if (event.getStage().getKeyboardFocus() != null) {
           return false;
        }

        if (filter != null && !filter.acceptKeycode(keycode)) {
            return false;
        }

        dirty = setKey(pressedKeys, keycode);
        pack();

        Shortcut shortcut = keyMap.getShortcut(currentlyPressedKeysPacked);

        if (shortcut == null) return false;

        if (shortcut.getScope() > 0 && scope != shortcut.getScope()) {
           return false;
        }

        shortcut.getRunnable().run();

        return true;
    }

    private void pack() {
        if (!dirty) return;
        dirty = false;
        currentlyPressedKeysPacked = packKeybindSorted(pressedKeys);
    }

    public static int[] sortKeybind (int[] keybind) {
        if (keybind.length > MAX_KEYS) throw new InvalidShortcutException("Keybind can't be longer than " + MAX_KEYS + " keys");

        int[] helper = new int[MAX_KEYS];
        for (int i : keybind) {
            setKey(helper, i);
        }
        return helper;
    }

    public static int packKeybindUnsorted (int[] keybind) {
        return packKeybindSorted(sortKeybind(keybind));
    }

    public static int packKeybindSorted (int[] keybind) {
        int idx = 0;
		int packed = 0;

		for (int i = 0; i < keybind.length; i++) {
			if (keybind[i] == 0) continue;
			packed |= (keybind[i] & 0xFF) << (idx++ << 3);
		}
		return packed;
    }

    public static int[] unpacKeybind(int packed) {
//        int[] keybind = new int[4];
        int size = 0;
        Arrays.fill(TEMP, 0);
        for (int i = 0; i < MAX_KEYS; i++) {
			int key = (packed >> (i << 3)) & 0xFF;

            if (key == Keys.ANY_KEY || key == Keys.UNKNOWN) continue;
            TEMP[i] = key;
            size++;
		}

        int[] keybind = new int[size];
        for (int i = 0, idx = 0; i < MAX_KEYS; i++) {
            int key = TEMP[i];
            if (key == Keys.ANY_KEY || key == Keys.UNKNOWN) continue;
            keybind[idx++] = TEMP[i];

            if (idx == size) break;
        }

        return keybind;
    }

    public static boolean isValidKeybind (int[] keys) {
		if (keys == null || keys.length == 0 || keys.length > ShortcutManager.MAX_KEYS) {
            return false;
//			throw new InvalidShortcutException(
//				"Keybind must not be null and have a length between 0 and " + ShortcutManager.MAX_KEYS);
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
//				if (hasAlt) throw new InvalidShortcutException("Alt key already added.");
                if (hasAlt) return false;
				hasAlt = true;
				break;
			case Keys.CONTROL_LEFT:
			case Keys.CONTROL_RIGHT:
//				if (hasControl) throw new InvalidShortcutException("Control key already added.");
                if (hasControl) return false;
				hasControl = true;
				break;
			case Keys.SHIFT_LEFT:
			case Keys.SHIFT_RIGHT:
//				if (hasShift) throw new InvalidShortcutException("Shift key already added.");
                if (hasShift) return false;
				hasShift = true;
				break;
			default:
//				if (hasNormalKey) throw new InvalidShortcutException("Keybind must have a maximum of 1 non modifier key");
                if (hasNormalKey) return false;

				hasNormalKey = true;
				allModifiers = false;
			}
		}

//		if (allModifiers) throw new InvalidShortcutException("All modifier keys are not allowed.");
        if (allModifiers) return false;

        return true;
	}

    public static String keybindToString (int[] keybind) {
		StringBuilder builder = new StringBuilder();
		builder.append('[');

		for (int i = 0; i < keybind.length; i++) {

			switch (keybind[i]) {
			case Keys.GRAVE:
				builder.append("Grave");
				break;
			default:
				builder.append(Keys.toString(keybind[i]));
				break;
			}

			if (i < keybind.length - 1) builder.append(" ,");
		}

		builder.append("]");
		return builder.toString();
	}

    public interface ShortcutManagerFilter {
        boolean acceptKeycode (int keycode);
    }
}
