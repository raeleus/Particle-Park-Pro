package com.ray3k.particleparkpro.widgets.subpanels;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.IndependentScaledNumericValue;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.widgets.LineGraph;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.particleparkpro.widgets.ToggleWidget;
import com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.ShownProperty;
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.emitterPropertiesPanel;

public class GraphSubPanel extends Panel {
    public GraphSubPanel(String name, ScaledNumericValue value, boolean hasRelative, boolean hasIndependent, String tooltip, String graphText, ShownProperty closeProperty) {
        final int spinnerWidth = 70;
        final int itemSpacing = 5;

        setTouchable(Touchable.enabled);

        tabTable.padRight(7);
        tabTable.left();
        var label = new Label(name, skin, "header");
        tabTable.add(label).space(3);

        if (closeProperty != null) {
            var button = new Button(skin, "close");
            tabTable.add(button);
            addHandListener(button);
            onChange(button, () -> {
                value.setActive(false);
                emitterPropertiesPanel.removeProperty(closeProperty);
            });
        }

        var graphToggleWidget = new ToggleWidget();
        bodyTable.add(graphToggleWidget).grow();

        //Normal view
        graphToggleWidget.table1.defaults().space(itemSpacing);
        graphToggleWidget.table1.left();

        //Relative
        if (hasRelative) {
            var checkBox = new CheckBox("Relative", skin);
            checkBox.setChecked(value.isRelative());
            graphToggleWidget.table1.add(checkBox).left();
            addHandListener(checkBox);
            addTooltip(checkBox, "If true, the value is in addition to the emitter's value", Align.top, Align.top, tooltipBottomArrowStyle);
            onChange(checkBox, () -> value.setRelative(checkBox.isChecked()));
        }

        //Independent
        if (hasIndependent) {
            graphToggleWidget.table1.row();
            var checkBox = new CheckBox("Independent", skin);
            graphToggleWidget.table1.add(checkBox).left();
            addHandListener(checkBox);
            addTooltip(checkBox, "If true, the value is randomly assigned per particle", Align.top, Align.top, tooltipBottomArrowStyle);
            onChange(checkBox, () -> ((IndependentScaledNumericValue) value).setIndependent(checkBox.isChecked()));
        }

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
        var highValueSpinner = new Spinner(value.getHighMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highValueSpinner.setProgrammaticChangeEvents(false);
        highToggleWidget.table1.add(highValueSpinner).width(spinnerWidth);
        addIbeamListener(highValueSpinner.getTextField());
        addHandListener(highValueSpinner.getButtonPlus());
        addHandListener(highValueSpinner.getButtonMinus());
        addTooltip(highValueSpinner, "The high value for " + tooltip, Align.top, Align.top, tooltipBottomArrowStyle);

        var button = new Button(skin, "moveright");
        highToggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the high value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(button, highToggleWidget::swap);

        //High range
        highToggleWidget.table2.defaults().space(itemSpacing);
        var highMinValueSpinner = new Spinner(value.getHighMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highMinValueSpinner.setProgrammaticChangeEvents(false);
        highToggleWidget.table2.add(highMinValueSpinner).width(spinnerWidth);
        addIbeamListener(highMinValueSpinner.getTextField());
        addHandListener(highMinValueSpinner.getButtonPlus());
        addHandListener(highMinValueSpinner.getButtonMinus());
        addTooltip(highMinValueSpinner, "The minimum high value for " + tooltip, Align.top, Align.top, tooltipBottomArrowStyle);

        var highMaxValueSpinner = new Spinner(value.getHighMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highMaxValueSpinner.setProgrammaticChangeEvents(false);
        highToggleWidget.table2.add(highMaxValueSpinner).width(spinnerWidth);
        addIbeamListener(highMaxValueSpinner.getTextField());
        addHandListener(highMaxValueSpinner.getButtonPlus());
        addHandListener(highMaxValueSpinner.getButtonMinus());
        addTooltip(highMaxValueSpinner, "The maximum high value for " + tooltip, Align.top, Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        highToggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single high value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(button, highToggleWidget::swap);

        onChange(highValueSpinner, () -> {
            value.setHigh(highValueSpinner.getValueAsInt());
            highMinValueSpinner.setValue(highValueSpinner.getValueAsInt());
            highMaxValueSpinner.setValue(highValueSpinner.getValueAsInt());
        });

        onChange(highMinValueSpinner, () -> {
            value.setHighMin(highMinValueSpinner.getValueAsInt());
            highValueSpinner.setValue(highMinValueSpinner.getValueAsInt());
        });

        onChange(highMaxValueSpinner, () -> {
            value.setHighMax(highMaxValueSpinner.getValueAsInt());
            highValueSpinner.setValue(highMaxValueSpinner.getValueAsInt());
        });

        onChange(button, () -> {
            value.setHigh(highValueSpinner.getValueAsInt());
            highMinValueSpinner.setValue(highValueSpinner.getValueAsInt());
            highMaxValueSpinner.setValue(highValueSpinner.getValueAsInt());
        });

        if (!MathUtils.isEqual(value.getHighMin(), value.getHighMax())) highToggleWidget.swap();

        //Low
        table.row();
        label = new Label("Low:", skin);
        table.add(label);

        var lowToggleWidget = new ToggleWidget();
        table.add(lowToggleWidget);

        //Low single
        lowToggleWidget.table1.defaults().space(itemSpacing);
        var lowValueSpinner = new Spinner(value.getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowValueSpinner.setProgrammaticChangeEvents(false);
        lowToggleWidget.table1.add(lowValueSpinner).width(spinnerWidth);
        addIbeamListener(lowValueSpinner.getTextField());
        addHandListener(lowValueSpinner.getButtonPlus());
        addHandListener(lowValueSpinner.getButtonMinus());
        addTooltip(lowValueSpinner, "The low value for the " + tooltip, Align.top, Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveright");
        lowToggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the low value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(button, lowToggleWidget::swap);

        //Low range
        lowToggleWidget.table2.defaults().space(itemSpacing);
        var lowMinValueSpinner = new Spinner(value.getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowMinValueSpinner.setProgrammaticChangeEvents(false);
        lowToggleWidget.table2.add(lowMinValueSpinner).width(spinnerWidth);
        addIbeamListener(lowMinValueSpinner.getTextField());
        addHandListener(lowMinValueSpinner.getButtonPlus());
        addHandListener(lowMinValueSpinner.getButtonMinus());
        addTooltip(lowMinValueSpinner, "The minimum low value for " + tooltip, Align.top, Align.top, tooltipBottomArrowStyle);

        var lowMaxValueSpinner = new Spinner(value.getLowMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowMaxValueSpinner.setProgrammaticChangeEvents(false);
        lowToggleWidget.table2.add(lowMaxValueSpinner).width(spinnerWidth);
        addIbeamListener(lowMaxValueSpinner.getTextField());
        addHandListener(lowMaxValueSpinner.getButtonPlus());
        addHandListener(lowMaxValueSpinner.getButtonMinus());
        addTooltip(lowMaxValueSpinner, "The maximum low value for " + tooltip, Align.top, Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        lowToggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single low value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(button, lowToggleWidget::swap);

        onChange(lowValueSpinner, () -> {
            value.setLow(lowValueSpinner.getValueAsInt());
            lowMinValueSpinner.setValue(lowValueSpinner.getValueAsInt());
            lowMaxValueSpinner.setValue(lowValueSpinner.getValueAsInt());
        });

        onChange(lowMinValueSpinner, () -> {
            value.setLowMin(lowMinValueSpinner.getValueAsInt());
            lowValueSpinner.setValue(lowMinValueSpinner.getValueAsInt());
        });

        onChange(lowMaxValueSpinner, () -> {
            value.setLowMax(lowMaxValueSpinner.getValueAsInt());
            lowValueSpinner.setValue(lowMaxValueSpinner.getValueAsInt());
        });

        onChange(button, () -> {
            value.setLow(lowValueSpinner.getValueAsInt());
            lowMinValueSpinner.setValue(lowValueSpinner.getValueAsInt());
            lowMaxValueSpinner.setValue(lowValueSpinner.getValueAsInt());
        });

        if (!MathUtils.isEqual(value.getLowMin(), value.getLowMax())) lowToggleWidget.swap();

        //Graph small
        var graph = new LineGraph(graphText, lineGraphStyle);
        graph.setNodes(value.getTimeline(), value.getScaling());
        graph.setNodeListener(handListener);
        graphToggleWidget.table1.add(graph);

        button = new Button(skin, "plus");
        graphToggleWidget.table1.add(button).bottom();
        addHandListener(button);
        addTooltip(button, "Expand to large graph view", Align.top, Align.top, tooltipBottomArrowStyle);

        //Expanded graph view
        graphToggleWidget.table2.defaults().space(itemSpacing);
        var graphExpanded = new LineGraph(graphText, lineGraphBigStyle);
        graphExpanded.setNodeListener(handListener);
        graphToggleWidget.table2.add(graphExpanded).grow();

        onChange(button, () -> {
            graphToggleWidget.swap();
            graphExpanded.setNodes(value.getTimeline(), value.getScaling());
        });

        onChange(graph, () -> {
            var nodes = graph.getNodes();
            float[] newTimeline = new float[nodes.size];
            float[] newScaling = new float[nodes.size];
            for (int i = 0; i < nodes.size; i++) {
                var node = nodes.get(i);
                newTimeline[i] = node.percentX;
                newScaling[i] = node.percentY;
            }
            value.setTimeline(newTimeline);
            value.setScaling(newScaling);
        });

        onChange(graphExpanded, () -> {
            var nodes = graphExpanded.getNodes();
            float[] newTimeline = new float[nodes.size];
            float[] newScaling = new float[nodes.size];
            for (int i = 0; i < nodes.size; i++) {
                var node = nodes.get(i);
                newTimeline[i] = node.percentX;
                newScaling[i] = node.percentY;
            }
            value.setTimeline(newTimeline);
            value.setScaling(newScaling);
        });

        button = new Button(skin, "minus");
        graphToggleWidget.table2.add(button).bottom();
        addHandListener(button);
        addTooltip(button, "Collapse to normal view", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(button, () -> {
            graphToggleWidget.swap();
            graph.setNodes(value.getTimeline(), value.getScaling());
        });
    }
}
