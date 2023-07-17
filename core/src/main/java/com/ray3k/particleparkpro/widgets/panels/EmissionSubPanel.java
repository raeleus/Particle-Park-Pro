package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.widgets.ColorGraph;
import com.ray3k.particleparkpro.widgets.LineGraph;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.particleparkpro.widgets.poptables.PopAddProperty;

import static com.ray3k.particleparkpro.Core.*;

public class EmissionSubPanel extends Panel {
    public EmissionSubPanel() {
        final int spinnerWidth = 70;

        setTouchable(Touchable.enabled);

        tabTable.padRight(4);
        tabTable.left();
        var label = new Label("Emission", skin, "header");
        tabTable.add(label).space(3);

        var button = new Button(skin, "close");
        tabTable.add(button);
        addHandListener(button);

        bodyTable.defaults().space(5);
        bodyTable.left();
        var table = new Table();
        bodyTable.add(table);

        table.defaults().space(3);
        label = new Label("High:", skin);
        table.add(label);

        var textField = new TextField("250", skin, "spinner");
        table.add(textField).spaceRight(0).width(spinnerWidth);
        addIbeamListener(textField);

        var subTable = new Table();
        table.add(subTable).spaceLeft(0);

        button = new Button(skin, "spinner-top");
        subTable.add(button);
        addHandListener(button);

        subTable.row();
        button = new Button(skin, "spinner-bottom");
        subTable.add(button);
        addHandListener(button);

        button = new Button(skin, "moveright");
        table.add(button);
        addHandListener(button);

        table.row().spaceTop(5);
        label = new Label("Low:", skin);
        table.add(label);

        textField = new TextField("250", skin, "spinner");
        table.add(textField).spaceRight(0).width(spinnerWidth);
        addIbeamListener(textField);

        subTable = new Table();
        table.add(subTable).spaceLeft(0);

        button = new Button(skin, "spinner-top");
        subTable.add(button);
        addHandListener(button);

        subTable.row();
        button = new Button(skin, "spinner-bottom");
        subTable.add(button);
        addHandListener(button);

        button = new Button(skin, "moveright");
        table.add(button);
        addHandListener(button);

        var graph = new LineGraph("text", lineGraphStyle);
        graph.setNodeListener(handListener);
        bodyTable.add(graph);

        table = new Table();
        bodyTable.add(table).bottom();

        table.defaults().space(3);
        button = new Button(skin, "plus");
        table.add(button);
        addHandListener(button);
    }
}
