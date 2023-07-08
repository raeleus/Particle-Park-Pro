package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.ray3k.particleparkpro.Core;
import com.ray3k.particleparkpro.widgets.LineGraph;
import com.ray3k.particleparkpro.widgets.styles.PPlineGraphStyle;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Core.skin;

public class EmitterPropertiesPanel extends Panel {
    public EmitterPropertiesPanel() {
        final int spinnerWidth = 70;

        setTouchable(Touchable.enabled);

        var label = new Label("Emitter Properties", skin, "header");
        tabTable.add(label);

        var scrollTable = new Table();
        scrollTable.top();
        var scrollPane = new ScrollPane(scrollTable, skin);
        scrollPane.setFlickScroll(false);
        bodyTable.add(scrollPane).grow();
        addScrollFocusListener(scrollPane);

        var panel = new Panel();
        scrollTable.add(panel).growX();

        scrollTable.defaults().space(10);
        panel.tabTable.left();
        label = new Label("Images", skin, "header");
        panel.tabTable.add(label);

        panel.bodyTable.defaults().space(15);
        panel.bodyTable.left();
        var table = new Table();
        panel.bodyTable.add(table);

        table.defaults().space(5).fillX();
        var textButton = new TextButton("Add", skin, "small");
        table.add(textButton);
        addHandListener(textButton);

        table.row();
        textButton = new TextButton("Default", skin, "small");
        table.add(textButton);
        addHandListener(textButton);

        table.row();
        textButton = new TextButton("Default (Premultiplied Alpha)", skin, "small");
        table.add(textButton);
        addHandListener(textButton);

        table = new Table();
        panel.bodyTable.add(table);

        label = new Label("Sprite mode:", skin);
        table.add(label);

        table.row();
        table.defaults().left();
        var buttonGroup = new ButtonGroup<>();
        var checkBox = new CheckBox("Single", skin, "radio");
        table.add(checkBox).spaceTop(5);
        buttonGroup.add(checkBox);
        addHandListener(checkBox);

        table.row();
        checkBox = new CheckBox("Random", skin, "radio");
        table.add(checkBox);
        buttonGroup.add(checkBox);
        addHandListener(checkBox);

        table.row();
        checkBox = new CheckBox("Animated", skin, "radio");
        table.add(checkBox);
        buttonGroup.add(checkBox);
        addHandListener(checkBox);

        var list = new List<String>(skin);
        list.setItems("particle.png", "particle1.png", "particle2.png", "particle3.png", "particle4.png");
        panel.bodyTable.add(list).fill().spaceRight(10);
        addHandListener(list);

        table = new Table();
        panel.bodyTable.add(table).spaceLeft(10);

        table.defaults().space(5);
        var button = new Button(skin, "moveup");
        table.add(button);
        addHandListener(button);

        table.row();
        button = new Button(skin, "movedown");
        table.add(button);
        addHandListener(button);

        table.row();
        button = new Button(skin, "cancel");
        table.add(button);
        addHandListener(button);

        scrollTable.row();
        panel = new Panel();
        scrollTable.add(panel).growX();

        panel.tabTable.padRight(4);

        panel.tabTable.left();
        label = new Label("Emission", skin, "header");
        panel.tabTable.add(label).space(3);

        button = new Button(skin, "close");
        panel.tabTable.add(button);
        addHandListener(button);

        panel.bodyTable.defaults().space(5);
        panel.bodyTable.left();
        table = new Table();
        panel.bodyTable.add(table);

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
        panel.bodyTable.add(graph);

        table = new Table();
        panel.bodyTable.add(table).bottom();

        table.defaults().space(3);
        button = new Button(skin, "plus");
        table.add(button);
        addHandListener(button);

        scrollTable.row();
        panel = new Panel();
        scrollTable.add(panel).growX();

        panel.tabTable.padRight(4);

        panel.tabTable.left();
        label = new Label("Tint", skin, "header");
        panel.tabTable.add(label).spaceRight(3);

        button = new Button(skin, "close");
        panel.tabTable.add(button);
        addHandListener(button);

        var image = new Image(skin, "colorbar-bg-10");
        panel.bodyTable.add(image).growX();
        addHandListener(image);

        bodyTable.row();
        textButton = new TextButton("Add Property", skin, "add");
        bodyTable.add(textButton).right();
        addHandListener(textButton);
    }
}
