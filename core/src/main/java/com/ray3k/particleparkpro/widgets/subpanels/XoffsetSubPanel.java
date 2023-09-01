package com.ray3k.particleparkpro.widgets.subpanels;

import com.badlogic.gdx.math.MathUtils;
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
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.ShownProperty.X_OFFSET;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.emitterPropertiesPanel;

public class XoffsetSubPanel extends Panel {
    public XoffsetSubPanel() {
        final int spinnerWidth = 70;
        final int itemSpacing = 5;

        setTouchable(Touchable.enabled);

        tabTable.padRight(4);
        tabTable.left();
        var label = new Label("X Offset", skin, "header");
        tabTable.add(label).space(3);

        var button = new Button(skin, "close");
        tabTable.add(button);
        addHandListener(button);
        onChange(button, () -> {
            selectedEmitter.getXOffsetValue().setActive(false);
            emitterPropertiesPanel.removeProperty(X_OFFSET);
        });

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
        var valueSpinner = new Spinner(selectedEmitter.getXOffsetValue().getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        valueSpinner.setProgrammaticChangeEvents(false);
        highToggleWidget.table1.add(valueSpinner).width(spinnerWidth);
        addIbeamListener(valueSpinner.getTextField());
        addHandListener(valueSpinner.getButtonPlus());
        addHandListener(valueSpinner.getButtonMinus());
        addTooltip(valueSpinner, "The amount to offset a particle's starting X location in world units", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveright");
        highToggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the X offset", Align.top, tooltipBottomArrowStyle);
        onChange(button, highToggleWidget::swap);

        //Value range
        highToggleWidget.table2.defaults().space(itemSpacing);
        var valueMinSpinner = new Spinner(selectedEmitter.getXOffsetValue().getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        valueMinSpinner.setProgrammaticChangeEvents(false);
        highToggleWidget.table2.add(valueMinSpinner).width(spinnerWidth);
        addIbeamListener(valueMinSpinner.getTextField());
        addHandListener(valueMinSpinner.getButtonPlus());
        addHandListener(valueMinSpinner.getButtonMinus());
        addTooltip(valueMinSpinner, "The minimum amount to offset a particle's starting X location in world units", Align.top, tooltipBottomArrowStyle);

        var valueMaxSpinner = new Spinner(selectedEmitter.getXOffsetValue().getLowMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        valueMaxSpinner.setProgrammaticChangeEvents(false);
        highToggleWidget.table2.add(valueMaxSpinner).width(spinnerWidth);
        addIbeamListener(valueMaxSpinner.getTextField());
        addHandListener(valueMaxSpinner.getButtonPlus());
        addHandListener(valueMaxSpinner.getButtonMinus());
        addTooltip(valueMaxSpinner, "The maximum amount to offset a particle's starting X location in world units", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        highToggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single X offset", Align.top, tooltipBottomArrowStyle);
        onChange(button, highToggleWidget::swap);

        onChange(valueSpinner, () -> {
            selectedEmitter.getXOffsetValue().setLow(valueSpinner.getValueAsInt());
            valueMinSpinner.setValue(valueSpinner.getValueAsInt());
            valueMaxSpinner.setValue(valueSpinner.getValueAsInt());
        });

        onChange(valueMinSpinner, () -> {
            selectedEmitter.getXOffsetValue().setLowMin(valueMinSpinner.getValueAsInt());
            valueSpinner.setValue(valueMinSpinner.getValueAsInt());
        });

        onChange(valueMaxSpinner, () -> {
            selectedEmitter.getXOffsetValue().setLowMax(valueMaxSpinner.getValueAsInt());
            valueSpinner.setValue(valueMaxSpinner.getValueAsInt());
        });

        onChange(button, () -> {
            selectedEmitter.getXOffsetValue().setLow(valueSpinner.getValueAsInt());
            valueMinSpinner.setValue(valueSpinner.getValueAsInt());
            valueMaxSpinner.setValue(valueSpinner.getValueAsInt());
        });

        if (!MathUtils.isEqual(selectedEmitter.getXOffsetValue().getLowMin(), selectedEmitter.getXOffsetValue().getLowMax())) highToggleWidget.swap();
    }
}
