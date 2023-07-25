package com.ray3k.particleparkpro.widgets.subpanels;

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

        tabTable.padRight(7);

        tabTable.left();
        var label = new Label("Tint", skin, "header");
        tabTable.add(label).spaceRight(3);

        var colorGraph = new ColorGraph(colorGraphStyle);
        bodyTable.add(colorGraph).growX();
        colorGraph.setNodeListener(handListener);
    }
}
