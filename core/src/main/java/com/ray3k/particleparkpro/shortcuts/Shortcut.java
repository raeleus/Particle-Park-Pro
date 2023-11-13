package com.ray3k.particleparkpro.shortcuts;

public class Shortcut {

    private static final int[] EMTPTY_KEYBIND = new int[0];

    private int keybindPacked;
    private int[] keybind;
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

    public Shortcut setKeybindPacked (int packed) {
        keybindPacked = packed;
        return this;
    }

    public int getKeybindPacked () {
        return keybindPacked;
    }

    public Shortcut setKeybind (int[] keybind) {
        this.keybind = keybind;
        return this;
    }

    public int[] getKeybind () {
        return keybind == null ? EMTPTY_KEYBIND : keybind;
    }

}
