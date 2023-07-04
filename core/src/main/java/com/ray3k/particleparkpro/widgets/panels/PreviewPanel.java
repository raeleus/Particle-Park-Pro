package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import static com.ray3k.particleparkpro.Core.skin;

public class PreviewPanel extends Panel {
    public PreviewPanel() {
        setTouchable(Touchable.enabled);

        var label = new Label("Preview", skin, "header");
        tabTable.add(label);

        var image = new Image(skin, "black");
        bodyTable.add(image).grow();
    }
}
