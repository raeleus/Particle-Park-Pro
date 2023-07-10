package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Scaling;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.stripe.PopColorPicker;
import com.ray3k.stripe.PopColorPicker.PopColorPickerListener;
import com.ray3k.stripe.PopTable.TableShowHideListener;

import static com.ray3k.particleparkpro.Core.*;

public class EditorPropertiesPanel extends Panel {
    public EditorPropertiesPanel() {
        setTouchable(Touchable.enabled);

        final int topBorder = 10;
        final int tabWidth = 5;
        final int itemSpacing = 5;
        final int spinnerWidth = 70;

        var label = new Label("Editor Properties", skin, "header");
        tabTable.add(label);

        var scrollTable = new Table();
        var scrollPane = new ScrollPane(scrollTable, skin);
        scrollPane.setFlickScroll(false);
        bodyTable.add(scrollPane).grow();
        addScrollFocusListener(scrollPane);

        scrollTable.top().pad(1);
        label = new Label("Pixels per meter", skin, "header");
        scrollTable.add(label).left().padTop(topBorder);

        scrollTable.row();
        var table = new Table();
        scrollTable.add(table).space(itemSpacing).left();

        label = new Label("Value:", skin);
        table.add(label).padLeft(tabWidth);

        var textField = new TextField("", skin, "spinner");
        table.add(textField).spaceLeft(itemSpacing).width(spinnerWidth);
        addIbeamListener(textField);

        var subTable = new Table();
        table.add(subTable);

        var button = new Button(skin, "spinner-top");
        subTable.add(button);
        addHandListener(button);

        subTable.row();
        button = new Button(skin, "spinner-bottom");
        subTable.add(button);
        addHandListener(button);

        scrollTable.row();
        var image = new Image(skin, "divider-10");
        image.setScaling(Scaling.stretchX);
        scrollTable.add(image).growX();

        scrollTable.row();
        table = new Table();
        scrollTable.add(table).space(itemSpacing).left();

        label = new Label("Background Color:", skin);
        table.add(label).padLeft(tabWidth);

        var stack = new Stack();
        table.add(stack).space(itemSpacing);

        image = new Image(skin, "swatch-bg");
        image.setScaling(Scaling.none);
        stack.add(image);

        image = new Image(skin, "swatch-fill");
        image.setColor(Color.RED);
        image.setScaling(Scaling.none);
        stack.add(image);
        addHandListener(stack);
        onClick(image, () -> {
            Gdx.input.setInputProcessor(foregroundStage);
            var cp = new PopColorPicker(Color.RED, popColorPickerStyle);
            cp.setHideOnUnfocus(true);
            cp.setButtonListener(handListener);
            cp.setTextFieldListener(ibeamListener);
            cp.show(foregroundStage);
            cp.addListener(new TableShowHideListener() {
                @Override
                public void tableShown(Event event) {

                }

                @Override
                public void tableHidden(Event event) {
                    Gdx.input.setInputProcessor(stage);
                }
            });
        });
    }
}
