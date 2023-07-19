package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.ParticlePreview.pixelsPerMeter;

public class CountSubPanel extends Panel {
    public CountSubPanel() {
        setTouchable(Touchable.enabled);

        final var itemSpacing = 5;
        final var spinnerWidth = 50;

        tabTable.left();
        var label = new Label("Count", skin, "header");
        tabTable.add(label);

        bodyTable.defaults().space(itemSpacing);
        bodyTable.left();

        label = new Label("Min:", skin);
        bodyTable.add(label);

        var minSpinner = new Spinner(0, 1, false, Orientation.RIGHT_STACK, spinnerStyle);
        bodyTable.add(minSpinner).width(spinnerWidth);
        addHandListener(minSpinner.getButtonMinus());
        addHandListener(minSpinner.getButtonPlus());
        addIbeamListener(minSpinner.getTextField());

        label = new Label("Max:", skin);
        bodyTable.add(label);

        var maxSpinner = new Spinner(0, 1, false, Orientation.RIGHT_STACK, spinnerStyle);
        bodyTable.add(maxSpinner).width(spinnerWidth);
        addHandListener(maxSpinner.getButtonMinus());
        addHandListener(maxSpinner.getButtonPlus());
        addIbeamListener(maxSpinner.getTextField());
    }
}
