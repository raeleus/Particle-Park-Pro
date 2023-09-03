package com.ray3k.particleparkpro.widgets.subpanels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.ray3k.particleparkpro.widgets.ColorGraph;
import com.ray3k.particleparkpro.widgets.ColorGraph.NodeData;
import com.ray3k.particleparkpro.widgets.LineGraph;
import com.ray3k.particleparkpro.widgets.Panel;

import javax.xml.crypto.NodeSetData;

import static com.ray3k.particleparkpro.Core.*;

public class TintSubPanel extends Panel {
    public TintSubPanel() {
        var value = selectedEmitter.getTint();
        setTouchable(Touchable.enabled);

        tabTable.padRight(7);

        tabTable.left();
        var label = new Label("Tint", skin, "header");
        tabTable.add(label).spaceRight(3);

        var colorGraph = new ColorGraph(colorGraphStyle);
        colorGraph.setNodes(value.getTimeline(), value.getColors());
        bodyTable.add(colorGraph).growX();
        colorGraph.setNodeListener(handListener);
        onChange(colorGraph, () -> {
            var nodes = colorGraph.getNodes();
            System.out.println("nodes.size = " + nodes.size);

            var newTimeline = new FloatArray();
            var newColors = new FloatArray();

            for (var node : nodes) {
                var nodeData = (NodeData) node.getUserObject();
                newTimeline.add(nodeData.value);
                newColors.add(nodeData.color.r);
                newColors.add(nodeData.color.g);
                newColors.add(nodeData.color.b);
            }

            value.setTimeline(newTimeline.toArray());
            value.setColors(newColors.toArray());
        });
    }
}
