package com.ray3k.particleparkpro.tables;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ray3k.particleparkpro.ResizeArrowListener;
import com.ray3k.particleparkpro.widgets.panels.EditorPropertiesPanel;
import com.ray3k.particleparkpro.widgets.panels.EffectEmittersPanel;
import com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel;
import com.ray3k.particleparkpro.widgets.panels.PreviewPanel;

import static com.ray3k.particleparkpro.Core.*;

public class ClassicTable extends Table {
    public ClassicTable() {
        pad(20).padBottom(5);

        var effectEmittersPanel = new EffectEmittersPanel();
        var previewPanel = new PreviewPanel();
        var leftSplitPane = new SplitPane(effectEmittersPanel, previewPanel, true, skin);
        leftSplitPane.setSplitAmount(.7f);
        leftSplitPane.addListener(new ResizeArrowListener(true));

        var editorPropertiesPanel = new EditorPropertiesPanel();
        var emitterPropertiesPanel = new EmitterPropertiesPanel();
        var rightSplitPane = new SplitPane(editorPropertiesPanel, emitterPropertiesPanel, true, skin);
        rightSplitPane.setSplitAmount(.3f);
        rightSplitPane.addListener(new ResizeArrowListener(true));

        var horizontalSplitPane = new SplitPane(leftSplitPane, rightSplitPane, false, skin);
        add(horizontalSplitPane).grow();
        horizontalSplitPane.addListener(new ResizeArrowListener(false));

        row();
        var table = new Table();
        add(table).growX().padTop(5);

        var label = new Label(version, skin);
        table.add(label);

        var button = new Button(skin, "home");
        table.add(button).expandX().right();
        addHandListener(button);

        button = new Button(skin, "settings");
        table.add(button);
        addHandListener(button);
    }
}
