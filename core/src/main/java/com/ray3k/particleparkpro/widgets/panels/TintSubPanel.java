package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.ray3k.particleparkpro.widgets.ColorGraph;
import com.ray3k.particleparkpro.widgets.LineGraph;
import com.ray3k.particleparkpro.widgets.Panel;

import static com.ray3k.particleparkpro.Core.*;

public class TintSubPanel extends Panel {
    public TintSubPanel() {
        setTouchable(Touchable.enabled);

        tabTable.padRight(4);

        tabTable.left();
        var label = new Label("Tint", skin, "header");
        tabTable.add(label).spaceRight(3);

        var button = new Button(skin, "close");
        tabTable.add(button);
        addHandListener(button);

        var colorGraph = new ColorGraph(colorGraphStyle);
        bodyTable.add(colorGraph).growX();
        colorGraph.setNodeListener(handListener);
    }
}
