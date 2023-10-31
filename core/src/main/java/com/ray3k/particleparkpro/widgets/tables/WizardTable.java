package com.ray3k.particleparkpro.widgets.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.undo.UndoManager;
import com.ray3k.particleparkpro.widgets.Carousel;
import com.ray3k.particleparkpro.widgets.panels.*;
import com.ray3k.particleparkpro.widgets.poptables.PopEditorSettings;
import com.ray3k.particleparkpro.widgets.styles.Styles;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.PresetActions.transition;
import static com.ray3k.particleparkpro.undo.UndoManager.*;
import static com.ray3k.particleparkpro.widgets.styles.Styles.*;

public class WizardTable extends Table {

    public static WizardTable wizardTable;
    private Table undoTable;
    public WizardTable() {
        wizardTable = this;
        pad(20).padBottom(5);

        var startPanel = new StartPanel();
        var previewPanel = new PreviewPanel();
        var effectEmittersPanel = new EffectEmittersPanel();
        var emitterPropertiesPanel = new EmitterPropertiesPanel();
        var summaryPanel = new SummaryPanel();

        var pager = new Carousel(startPanel, effectEmittersPanel, emitterPropertiesPanel, summaryPanel);
        pager.setTouchable(Touchable.enabled);
        addHandListener(pager.previousButton);
        addHandListener(pager.nextButton);
        for (var button : pager.buttonGroup.getButtons()) addHandListener(button);
        pager.buttonTable.padTop(10).padBottom(20);

        var verticalSplitPane = new SplitPane(previewPanel, pager, true, skin);
        add(verticalSplitPane).grow();
        verticalSplitPane.setSplitAmount(.5f);
        addSplitPaneVerticalSystemCursorListener(verticalSplitPane);

        row();
        var table = new Table();
        add(table).growX().padTop(5);

        var label = new Label(version, skin);
        table.add(label);

        var textButton = new TextButton("-Update Available-", skin, "no-bg");
        textButton.setVisible(false);
        table.add(textButton).spaceLeft(10);
        addHandListener(textButton);
        addTooltip(textButton, "Open browser to download page", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(textButton, () -> Gdx.net.openURI("https://github.com/raeleus/Particle-Park-Pro/releases"));
        checkVersion((String newVersion) -> {
            if (!versionRaw.equals(newVersion)) textButton.setVisible(true);
        });

        var button = new Button(skin, "home");
        table.add(button).expandX().right();
        addHandListener(button);
        onChange(button, () -> transition(this, new WelcomeTable(), Align.bottom));

        button = new Button(skin, "settings");
        table.add(button);
        addHandListener(button);
        onChange(button, () -> {
            Gdx.input.setInputProcessor(foregroundStage);
            Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
            var pop = new PopEditorSettings();
            pop.show(foregroundStage);
        });

        undoTable = new Table();
        table.add(undoTable);

        refreshUndo();

        stage.setKeyboardFocus(this);
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
