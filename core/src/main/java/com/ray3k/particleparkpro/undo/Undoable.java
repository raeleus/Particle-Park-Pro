package com.ray3k.particleparkpro.undo;

public interface Undoable {
    public void undo();
    public void redo();
    public void start();
    public String getDescription();
}
