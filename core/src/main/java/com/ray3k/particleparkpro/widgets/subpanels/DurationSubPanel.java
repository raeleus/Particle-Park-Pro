package com.ray3k.particleparkpro.widgets.subpanels;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.particleparkpro.widgets.ToggleWidget;
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

import static com.ray3k.particleparkpro.Core.*;

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
        var valueSpinner = new Spinner(selectedEmitter.getDuration().getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        valueSpinner.setProgrammaticChangeEvents(false);
        highToggleWidget.table1.add(valueSpinner).width(spinnerWidth);
        addIbeamListener(valueSpinner.getTextField());
        addHandListener(valueSpinner.getButtonPlus());
        addHandListener(valueSpinner.getButtonMinus());
        addTooltip(valueSpinner, "The time particles will be emitted in milliseconds", Align.top, tooltipBottomArrowStyle);

        var button = new Button(skin, "moveright");
        highToggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the duration", Align.top, tooltipBottomArrowStyle);
        onChange(button, highToggleWidget::swap);

        //Value range
        highToggleWidget.table2.defaults().space(itemSpacing);
        var valueMinSpinner = new Spinner(selectedEmitter.getDuration().getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        valueMinSpinner.setProgrammaticChangeEvents(false);
        highToggleWidget.table2.add(valueMinSpinner).width(spinnerWidth);
        addIbeamListener(valueMinSpinner.getTextField());
        addHandListener(valueMinSpinner.getButtonPlus());
        addHandListener(valueMinSpinner.getButtonMinus());
        addTooltip(valueMinSpinner, "The minimum time particles will be emitted in milliseconds", Align.top, tooltipBottomArrowStyle);

        var valueMaxSpinner = new Spinner(selectedEmitter.getDuration().getLowMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        valueMaxSpinner.setProgrammaticChangeEvents(false);
        highToggleWidget.table2.add(valueMaxSpinner).width(spinnerWidth);
        addIbeamListener(valueMaxSpinner.getTextField());
        addHandListener(valueMaxSpinner.getButtonPlus());
        addHandListener(valueMaxSpinner.getButtonMinus());
        addTooltip(valueMaxSpinner, "The maximum time particles will be emitted in milliseconds", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        highToggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single duration", Align.top, tooltipBottomArrowStyle);
        onChange(button, highToggleWidget::swap);

        onChange(valueSpinner, () -> {
            selectedEmitter.getDuration().setLow(valueSpinner.getValueAsInt());
            valueMinSpinner.setValue(valueSpinner.getValueAsInt());
            valueMaxSpinner.setValue(valueSpinner.getValueAsInt());
        });

        onChange(valueMinSpinner, () -> {
            selectedEmitter.getDuration().setLowMin(valueMinSpinner.getValueAsInt());
            valueSpinner.setValue(valueMinSpinner.getValueAsInt());
        });

        onChange(valueMaxSpinner, () -> {
            selectedEmitter.getDuration().setLowMax(valueMaxSpinner.getValueAsInt());
            valueSpinner.setValue(valueMaxSpinner.getValueAsInt());
        });

        onChange(button, () -> {
            selectedEmitter.getDuration().setLow(valueSpinner.getValueAsInt());
            valueMinSpinner.setValue(valueSpinner.getValueAsInt());
            valueMaxSpinner.setValue(valueSpinner.getValueAsInt());
        });
    }
}
