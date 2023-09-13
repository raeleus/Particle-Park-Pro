package com.ray3k.particleparkpro.widgets.tables;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.undo.UndoManager;
import com.ray3k.particleparkpro.widgets.panels.EffectEmittersPanel;
import com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel;
import com.ray3k.particleparkpro.widgets.panels.PreviewPanel;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.undo.UndoManager.*;

public class ClassicTable extends Table {
    public static ClassicTable classicTable;
    private Table undoTable;
    public ClassicTable() {
        classicTable = this;
        pad(20).padBottom(5);

        var effectEmittersPanel = new EffectEmittersPanel();
        var previewPanel = new PreviewPanel();
        var leftSplitPane = new SplitPane(effectEmittersPanel, previewPanel, true, skin);
        leftSplitPane.setSplitAmount(.7f);
        addSplitPaneVerticalSystemCursorListener(leftSplitPane);

        emitterPropertiesPanel = new EmitterPropertiesPanel();
        var horizontalSplitPane = new SplitPane(leftSplitPane, emitterPropertiesPanel, false, skin);
        add(horizontalSplitPane).grow();
        horizontalSplitPane.setSplitAmount(.4f);
        addSplitPaneHorizontalSystemCursorListener(horizontalSplitPane);

        row();
        var table = new Table();
        add(table).growX().padTop(5);

        var label = new Label(version, skin);
        table.add(label);

        var textButton = new TextButton("-Update Available-", skin, "no-bg");
        table.add(textButton).spaceLeft(10);
        addHandListener(textButton);
        addTooltip(textButton, "Open browser to download page", Align.top, Align.top, tooltipBottomArrowStyle);

        var button = new Button(skin, "home");
        table.add(button).expandX().right();
        addHandListener(button);

        button = new Button(skin, "settings");
        table.add(button);
        addHandListener(button);

        undoTable = new Table();
        table.add(undoTable);

        refreshUndo();
    }

    public void refreshUndo() {
        undoTable.clearChildren();

        var button = new Button(skin, hasUndo() ? "undo-active" : "undo");
        button.setDisabled(!hasUndo());
        undoTable.add(button);
        addHandListener(button);
        if (hasUndo()) {
            var pop = addTooltip(button, "Undo " + getUndoDescription(), Align.top, Align.topLeft, tooltipBottomRightArrowStyle, false);
            pop.setAttachOffsetX(8);
        }
        onChange(button, UndoManager::undo);

        button = new Button(skin, hasRedo() ? "redo-active" : "redo");
        button.setDisabled(!hasRedo());
        undoTable.add(button);
        addHandListener(button);
        if (hasRedo()) {
            var pop = addTooltip(button, "Redo " + getRedoDescription(), Align.top, Align.topLeft, tooltipBottomRightArrowStyle, false);
            pop.setAttachOffsetX(8);
        }
        onChange(button, UndoManager::redo);
    }
}
