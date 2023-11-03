package com.ray3k.particleparkpro.shortcuts;

public class Shortcut {

    private static final int[] EMTPTY_KEYBIND = new int[0];

    private int primaryKeybindPacked;
    private int secondaryKeybindPacked;
    private int[] primaryKeybind;
    private int[] secondaryKeybind;
    private final Runnable runnable;
    private final String name;
    private final String description;

    public Shortcut (String name, String description, Runnable runnable) {
       this.name = name;
       this.description = description;
       this.runnable = runnable;
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

    public Shortcut setPrimaryKeybindPacked (int packed) {
        primaryKeybindPacked = packed;
        return this;
    }

    public int getPrimaryKeybindPacked() {
        return primaryKeybindPacked;
    }

    public Shortcut setPrimaryKeybind (int[] keybind) {
        primaryKeybind = keybind;
        return this;
    }

    public int[] getPrimaryKeybind() {
        return primaryKeybind == null ? EMTPTY_KEYBIND : primaryKeybind;
    }

    public Shortcut setSecondaryKeybind (int[] keybind) {
        secondaryKeybind = keybind;
        return this;
    }

    public int[] getSecondaryKeybind() {
        return secondaryKeybind;
    }

}
