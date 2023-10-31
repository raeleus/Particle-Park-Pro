package com.ray3k.particleparkpro.undo;

import com.badlogic.gdx.utils.Array;
import com.ray3k.particleparkpro.Utils;

public class UndoManager {
    public final static Array<Undoable> undoables = new Array<>();
    public static int undoIndex = -1;

    public static void add(Undoable undoable) {
        if (hasRedo()) undoables.removeRange(undoIndex + 1, undoables.size - 1);

        undoables.add(undoable);
        undoable.start();
        undoIndex = undoables.size - 1;
        Utils.refreshUndoButtons();
    }

    public static String getUndoDescription() {
        return hasUndo() ? undoables.get(undoIndex).getDescription() : "";
    }

    public static String getRedoDescription() {
        return hasRedo() ? undoables.get(undoIndex + 1).getDescription() : "";
    }

    public static boolean hasUndo() {
        return undoIndex >= 0;
    }

    public static boolean hasRedo() {
        return undoables.size > 0 && undoIndex < undoables.size - 1;
    }

    public static void undo() {
        if (hasUndo()) undoables.get(undoIndex--).undo();
        Utils.refreshUndoButtons();
    }

    public static void redo() {
        if (hasRedo()) undoables.get(++undoIndex).redo();
        Utils.refreshUndoButtons();
    }

    public static void clear() {
        undoables.clear();
        undoIndex = -1;
        Utils.refreshUndoButtons();
    }
}
