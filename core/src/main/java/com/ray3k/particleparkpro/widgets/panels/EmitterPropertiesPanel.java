package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.widgets.ColorGraph;
import com.ray3k.particleparkpro.widgets.LineGraph;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.particleparkpro.widgets.poptables.PopAddProperty;
import com.ray3k.stripe.PopTableClickListener;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Core.skin;

public class EmitterPropertiesPanel extends Panel {
    public EmitterPropertiesPanel() {
        setTouchable(Touchable.enabled);

        var label = new Label("Emitter Properties", skin, "header");
        tabTable.add(label);

        var scrollTable = new Table();
        scrollTable.top();
        var scrollPane = new ScrollPane(scrollTable, skin);
        scrollPane.setFlickScroll(false);
        bodyTable.add(scrollPane).grow();
        addScrollFocusListener(scrollPane);
        scrollTable.defaults().space(10);

        //Images
        var imagesSubPanel = new ImagesSubPanel();
        scrollTable.add(imagesSubPanel).growX();

        //Emission
        scrollTable.row();
        var emissionSubPanel = new EmissionSubPanel();
        scrollTable.add(emissionSubPanel).growX();

        //Tint
        scrollTable.row();
        var tintSubPanel = new TintSubPanel();
        scrollTable.add(tintSubPanel).growX();

        bodyTable.row();
        var addPropertyTextButton = new TextButton("Add Property", skin, "add");
        bodyTable.add(addPropertyTextButton).right();
        addHandListener(addPropertyTextButton);
        onChange(addPropertyTextButton, () -> {
            var pop = new PopAddProperty();
            pop.attachToActor(addPropertyTextButton, Align.top, Align.top);
            pop.show(foregroundStage);
        });
    }
}
