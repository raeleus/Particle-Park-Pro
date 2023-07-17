package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.ray3k.particleparkpro.widgets.LineGraph;
import com.ray3k.particleparkpro.widgets.Panel;

import static com.ray3k.particleparkpro.Core.*;

public class ImagesSubPanel extends Panel {
    public ImagesSubPanel() {
        setTouchable(Touchable.enabled);

        tabTable.left();
        var label = new Label("Images", skin, "header");
        tabTable.add(label);

        bodyTable.defaults().space(15);
        bodyTable.left();
        var table = new Table();
        bodyTable.add(table);

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
        bodyTable.add(table);

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
        bodyTable.add(list).fill().spaceRight(10);
        addHandListener(list);

        table = new Table();
        bodyTable.add(table).spaceLeft(10);

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
    }
}
