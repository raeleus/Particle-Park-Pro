package com.ray3k.particleparkpro.widgets.subpanels;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.widgets.LineGraph;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.particleparkpro.widgets.ToggleWidget;
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.ShownProperty.WIND;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.emitterPropertiesPanel;

public class WindSubPanel extends Panel {
    public WindSubPanel() {
        final int spinnerWidth = 70;
        final int itemSpacing = 5;

        setTouchable(Touchable.enabled);

        tabTable.padRight(4);
        tabTable.left();
        var label = new Label("Wind", skin, "header");
        tabTable.add(label).space(3);

        var button = new Button(skin, "close");
        tabTable.add(button);
        addHandListener(button);
        onChange(button, () -> {
            selectedEmitter.getWind().setActive(false);
            emitterPropertiesPanel.removeProperty(WIND);
        });

        var graphToggleWidget = new ToggleWidget();
        bodyTable.add(graphToggleWidget).grow();

        //Normal view
        graphToggleWidget.table1.defaults().space(itemSpacing);
        graphToggleWidget.table1.left();

        //Relative
        var relativeCheckBox = new CheckBox("Relative", skin);
        graphToggleWidget.table1.add(relativeCheckBox).left();
        addHandListener(relativeCheckBox);
        addTooltip(relativeCheckBox, "If true, the value is in addition to the emitter's value", Align.top, tooltipBottomArrowStyle);

        //High
        graphToggleWidget.table1.row();
        var table = new Table();
        graphToggleWidget.table1.add(table).top();

        table.defaults().space(itemSpacing).left();
        label = new Label("High:", skin);
        table.add(label);

        var highToggleWidget = new ToggleWidget();
        table.add(highToggleWidget);

        //High single
        highToggleWidget.table1.defaults().space(itemSpacing);
        var spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highToggleWidget.table1.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The high value for the wind strength in world units per second", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveright");
        highToggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the high value", Align.top, tooltipBottomArrowStyle);
        onChange(button, highToggleWidget::swap);

        //High range
        highToggleWidget.table2.defaults().space(itemSpacing);
        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highToggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The minimum high value for the wind strength in world units per second", Align.top, tooltipBottomArrowStyle);

        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highToggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The maximum high value for the wind strength in world units per second", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        highToggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single high value", Align.top, tooltipBottomArrowStyle);
        onChange(button, highToggleWidget::swap);

        //Low
        table.row();
        label = new Label("Low:", skin);
        table.add(label);

        var lowToggleWidget = new ToggleWidget();
        table.add(lowToggleWidget);

        //Low single
        lowToggleWidget.table1.defaults().space(itemSpacing);
        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowToggleWidget.table1.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The low value for the wind strength in world units per second", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveright");
        lowToggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the low value", Align.top, tooltipBottomArrowStyle);
        onChange(button, lowToggleWidget::swap);

        //Low range
        lowToggleWidget.table2.defaults().space(itemSpacing);
        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowToggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The minimum low value for the wind strength in world units per second", Align.top, tooltipBottomArrowStyle);

        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowToggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The maximum low value for the wind strength in world units per second", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        lowToggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single low value", Align.top, tooltipBottomArrowStyle);
        onChange(button, lowToggleWidget::swap);

        //Graph small
        var graph = new LineGraph("Life", lineGraphStyle);
        graph.setNodeListener(handListener);
        graphToggleWidget.table1.add(graph);

        button = new Button(skin, "plus");
        graphToggleWidget.table1.add(button).bottom();
        addHandListener(button);
        addTooltip(button, "Expand to large graph view", Align.top, tooltipBottomArrowStyle);
        onChange(button, graphToggleWidget::swap);

        //Expanded graph view
        graphToggleWidget.table2.defaults().space(itemSpacing);
        graph = new LineGraph("Life", lineGraphBigStyle);
        graph.setNodeListener(handListener);
        graphToggleWidget.table2.add(graph).grow();

        button = new Button(skin, "minus");
        graphToggleWidget.table2.add(button).bottom();
        addHandListener(button);
        addTooltip(button, "Collapse to normal view", Align.top, tooltipBottomArrowStyle);
        onChange(button, graphToggleWidget::swap);
    }
}
