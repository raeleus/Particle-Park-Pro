package com.ray3k.particleparkpro.shortcuts;

import static com.ray3k.particleparkpro.Utils.*;

public class Shortcut {

    private int[] primaryKeybind;
    private int[] secondaryKeybind;
    private int primaryKeybindPacked;
    private int secondaryKeybindPacked;
    private final Runnable runnable;
    private final String name;
    private final String description;
    private int scope;

    public Shortcut (String name, String description, Runnable runnable) {
       this.name = name;
       this.description = description;
       this.runnable = runnable;
    }

    public Shortcut setScope (int scope) {
        this.scope = scope;
        return this;
    }

    public int getScope() {
       return scope;
    }

    public Runnable getRunnable() {
         return runnable;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Shortcut setPrimaryKeybind(int[] keybind, int packed) {
        primaryKeybind = keybind;
        primaryKeybindPacked = packed;
        return this;
    }

    public int[] getPrimaryKeybind() {
        return primaryKeybind != null ? primaryKeybind : EMPTY_KEYBIND;
    }

    public int getPrimaryKeybindPacked () {
        return primaryKeybindPacked;
    }

    public Shortcut setSecondaryKeybind (int[] keybind, int packed) {
        secondaryKeybind = keybind;
        secondaryKeybindPacked = packed;
        return this;
    }

    public int[] getSecondaryKeybind() {
        return secondaryKeybind != null ? secondaryKeybind : EMPTY_KEYBIND;
    }

    public int getSecondaryKeybindPacked() {
        return secondaryKeybindPacked;
    }

}
