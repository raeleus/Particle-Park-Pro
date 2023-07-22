package com.ray3k.particleparkpro.widgets.subpanels;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.widgets.ColorGraph;
import com.ray3k.particleparkpro.widgets.LineGraph;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.particleparkpro.widgets.poptables.PopAddProperty;
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

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

        var spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        table.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The high value for the number of particles emitted per second", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveright");
        table.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the high value", Align.top, tooltipBottomArrowStyle);

        table.row();
        label = new Label("Low:", skin);
        table.add(label);

        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        table.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The low value for the number of particles emitted per second", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveright");
        table.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the low value", Align.top, tooltipBottomArrowStyle);

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
