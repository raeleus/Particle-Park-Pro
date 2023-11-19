package com.ray3k.particleparkpro.shortcuts;

import static com.ray3k.particleparkpro.Utils.*;

public class KeybindReference {

    private final int[] primaryKeybind;
    private final int[] secondaryKeybind;

    public KeybindReference (int[] primaryKeybind, int[] secondaryKeybind) {
        this.primaryKeybind = primaryKeybind;
        this.secondaryKeybind = secondaryKeybind;
    }

    public int[] getPrimaryKeybind() {
        return primaryKeybind != null ? primaryKeybind : EMPTY_KEYBIND;
    }

    public int[] getSecondaryKeybind() {
        return secondaryKeybind != null ? secondaryKeybind : EMPTY_KEYBIND;
    }

}
