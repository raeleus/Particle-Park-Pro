package com.ray3k.particleparkpro.widgets.subpanels;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.particleparkpro.widgets.ToggleWidget;
import com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.ShownProperty;
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.ShownProperty.DELAY;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.emitterPropertiesPanel;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.shownProperties;

public class DurationSubPanel extends Panel {
    public DurationSubPanel() {
        final int spinnerWidth = 70;
        final int itemSpacing = 5;

        setTouchable(Touchable.enabled);

        tabTable.padRight(7);
        tabTable.left();
        var label = new Label("Duration", skin, "header");
        tabTable.add(label);

        //Value
        var table = new Table();
        bodyTable.add(table).expandX().left();
        table.defaults().space(itemSpacing).left();
        label = new Label("Value:", skin);
        table.add(label);

        var highToggleWidget = new ToggleWidget();
        table.add(highToggleWidget);

        //Value single
        highToggleWidget.table1.defaults().space(itemSpacing);
        var spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highToggleWidget.table1.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The time particles will be emitted in milliseconds", Align.top, tooltipBottomArrowStyle);

        var button = new Button(skin, "moveright");
        highToggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the duration", Align.top, tooltipBottomArrowStyle);
        onChange(button, highToggleWidget::swap);

        //Value range
        highToggleWidget.table2.defaults().space(itemSpacing);
        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highToggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The minimum time particles will be emitted in milliseconds", Align.top, tooltipBottomArrowStyle);

        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highToggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The maximum time particles will be emitted in milliseconds", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        highToggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single duration", Align.top, tooltipBottomArrowStyle);
        onChange(button, highToggleWidget::swap);
    }
}
