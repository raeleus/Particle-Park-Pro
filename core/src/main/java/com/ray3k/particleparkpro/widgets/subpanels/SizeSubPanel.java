package com.ray3k.particleparkpro.widgets.subpanels;

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
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

import static com.ray3k.particleparkpro.Core.*;

public class SizeSubPanel extends Panel {
    private enum ExpandedType {
        EXPANDED_X, EXPANDED_Y, EXPANDED_BOTH
    }
    private ExpandedType expandedType;

    public SizeSubPanel() {
        final int spinnerWidth = 70;
        final int itemSpacing = 5;
        final int sectionPadding = 10;

        var xValue = selectedEmitter.getXScale();
        var yValue = selectedEmitter.getYScale();

        setTouchable(Touchable.enabled);

        tabTable.padRight(7);
        tabTable.left();
        var label = new Label("Size", skin, "header");
        tabTable.add(label);

        var graphToggleWidget = new ToggleWidget();
        bodyTable.add(graphToggleWidget).grow();

        //Normal view
        graphToggleWidget.table1.defaults().space(itemSpacing);
        graphToggleWidget.table1.left();

        //Split X and Y
        var splitXYcheckBox = new CheckBox("Split X and Y", skin);
        graphToggleWidget.table1.add(splitXYcheckBox).left();
        addHandListener(splitXYcheckBox);
        addTooltip(splitXYcheckBox, "If true, the X and Y values can be set independently", Align.top, tooltipBottomArrowStyle);

        //Split ToggleWidget
        graphToggleWidget.table1.row();
        var splitToggleWidget = new ToggleWidget();
        graphToggleWidget.table1.add(splitToggleWidget);

        //Joined
        //Relative
        splitToggleWidget.table1.defaults().space(itemSpacing);
        var relativeCheckBox = new CheckBox("Relative", skin);
        relativeCheckBox.setChecked(xValue.isRelative());
        splitToggleWidget.table1.add(relativeCheckBox).left();
        addHandListener(relativeCheckBox);
        addTooltip(relativeCheckBox, "If true, the value is in addition to the emitter's value", Align.top, tooltipBottomArrowStyle);

        //High
        splitToggleWidget.table1.row();
        var table = new Table();
        splitToggleWidget.table1.add(table).top();

        table.defaults().space(itemSpacing).left();
        label = new Label("High:", skin);
        table.add(label);

        var highToggleWidget = new ToggleWidget();
        table.add(highToggleWidget);

        //High single
        highToggleWidget.table1.defaults().space(itemSpacing);
        var highSpinner = new Spinner(xValue.getHighMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highSpinner.setProgrammaticChangeEvents(false);
        highToggleWidget.table1.add(highSpinner).width(spinnerWidth);
        addIbeamListener(highSpinner.getTextField());
        addHandListener(highSpinner.getButtonPlus());
        addHandListener(highSpinner.getButtonMinus());
        addTooltip(highSpinner, "The high value for the particle size in world units.", Align.top, tooltipBottomArrowStyle);

        var button = new Button(skin, "moveright");
        highToggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the high value", Align.top, tooltipBottomArrowStyle);
        onChange(button, highToggleWidget::swap);

        //High range
        highToggleWidget.table2.defaults().space(itemSpacing);
        var highMinSpinner = new Spinner(xValue.getHighMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highMinSpinner.setProgrammaticChangeEvents(false);
        highToggleWidget.table2.add(highMinSpinner).width(spinnerWidth);
        addIbeamListener(highMinSpinner.getTextField());
        addHandListener(highMinSpinner.getButtonPlus());
        addHandListener(highMinSpinner.getButtonMinus());
        addTooltip(highMinSpinner, "The minimum high value for the particle size in world units.", Align.top, tooltipBottomArrowStyle);

        var highMaxSpinner = new Spinner(xValue.getHighMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highMaxSpinner.setProgrammaticChangeEvents(false);
        highToggleWidget.table2.add(highMaxSpinner).width(spinnerWidth);
        addIbeamListener(highMaxSpinner.getTextField());
        addHandListener(highMaxSpinner.getButtonPlus());
        addHandListener(highMaxSpinner.getButtonMinus());
        addTooltip(highMaxSpinner, "The maximum high value for the particle size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        highToggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single high value", Align.top, tooltipBottomArrowStyle);
        onChange(button, highToggleWidget::swap);

        onChange(highSpinner, () -> {
            xValue.setHigh(highSpinner.getValueAsInt());
            yValue.setHigh(highSpinner.getValueAsInt());
            highMinSpinner.setValue(highSpinner.getValueAsInt());
            highMaxSpinner.setValue(highSpinner.getValueAsInt());
        });

        onChange(highMinSpinner, () -> {
            xValue.setHighMin(highMinSpinner.getValueAsInt());
            yValue.setHighMin(highMinSpinner.getValueAsInt());
            highSpinner.setValue(highMinSpinner.getValueAsInt());
        });

        onChange(highMaxSpinner, () -> {
            xValue.setHighMax(highMaxSpinner.getValueAsInt());
            yValue.setHighMax(highMaxSpinner.getValueAsInt());
            highSpinner.setValue(highMaxSpinner.getValueAsInt());
        });

        onChange(button, () -> {
            xValue.setHigh(highSpinner.getValueAsInt());
            yValue.setHigh(highSpinner.getValueAsInt());
            highMinSpinner.setValue(highSpinner.getValueAsInt());
            highMaxSpinner.setValue(highSpinner.getValueAsInt());
        });

        if (!MathUtils.isEqual(xValue.getHighMin(), xValue.getHighMax())) highToggleWidget.swap();

        //Low
        table.row();
        label = new Label("Low:", skin);
        table.add(label);

        var lowToggleWidget = new ToggleWidget();
        table.add(lowToggleWidget);

        //Low single
        lowToggleWidget.table1.defaults().space(itemSpacing);
        var lowSpinner = new Spinner(xValue.getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowSpinner.setProgrammaticChangeEvents(false);
        lowToggleWidget.table1.add(lowSpinner).width(spinnerWidth);
        addIbeamListener(lowSpinner.getTextField());
        addHandListener(lowSpinner.getButtonPlus());
        addHandListener(lowSpinner.getButtonMinus());
        addTooltip(lowSpinner, "The low value for the particle size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveright");
        lowToggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the low value", Align.top, tooltipBottomArrowStyle);
        onChange(button, lowToggleWidget::swap);

        //Low range
        lowToggleWidget.table2.defaults().space(itemSpacing);
        var lowMinSpinner = new Spinner(xValue.getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowMinSpinner.setProgrammaticChangeEvents(false);
        lowToggleWidget.table2.add(lowMinSpinner).width(spinnerWidth);
        addIbeamListener(lowMinSpinner.getTextField());
        addHandListener(lowMinSpinner.getButtonPlus());
        addHandListener(lowMinSpinner.getButtonMinus());
        addTooltip(lowMinSpinner, "The minimum low value for the particle size in world units.", Align.top, tooltipBottomArrowStyle);

        var lowMaxSpinner = new Spinner(xValue.getLowMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowMaxSpinner.setProgrammaticChangeEvents(false);
        lowToggleWidget.table2.add(lowMaxSpinner).width(spinnerWidth);
        addIbeamListener(lowMaxSpinner.getTextField());
        addHandListener(lowMaxSpinner.getButtonPlus());
        addHandListener(lowMaxSpinner.getButtonMinus());
        addTooltip(lowMaxSpinner, "The maximum low value for the particle size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        lowToggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single low value", Align.top, tooltipBottomArrowStyle);
        onChange(button, lowToggleWidget::swap);

        onChange(lowSpinner, () -> {
            xValue.setLow(lowSpinner.getValueAsInt());
            yValue.setLow(lowSpinner.getValueAsInt());
            lowMinSpinner.setValue(lowSpinner.getValueAsInt());
            lowMaxSpinner.setValue(lowSpinner.getValueAsInt());
        });

        onChange(lowMinSpinner, () -> {
            xValue.setLowMin(lowMinSpinner.getValueAsInt());
            yValue.setLowMin(lowMinSpinner.getValueAsInt());
            lowSpinner.setValue(lowMinSpinner.getValueAsInt());
        });

        onChange(lowMaxSpinner, () -> {
            xValue.setLowMax(lowMaxSpinner.getValueAsInt());
            yValue.setLowMax(lowMaxSpinner.getValueAsInt());
            lowSpinner.setValue(lowMaxSpinner.getValueAsInt());
        });

        onChange(button, () -> {
            xValue.setLow(lowSpinner.getValueAsInt());
            yValue.setLow(lowSpinner.getValueAsInt());
            lowMinSpinner.setValue(lowSpinner.getValueAsInt());
            lowMaxSpinner.setValue(lowSpinner.getValueAsInt());
        });

        if (!MathUtils.isEqual(xValue.getLowMin(), xValue.getLowMax())) lowToggleWidget.swap();

        //Graph small
        var graph = new LineGraph("Life", lineGraphStyle);
        graph.setNodes(xValue.getTimeline(), xValue.getScaling());
        graph.setNodeListener(handListener);
        splitToggleWidget.table1.add(graph);

        button = new Button(skin, "plus");
        splitToggleWidget.table1.add(button).bottom();
        addHandListener(button);
        addTooltip(button, "Expand to large graph view", Align.top, tooltipBottomArrowStyle);

        //Expanded graph view
        graphToggleWidget.table2.defaults().space(itemSpacing);
        var graphExpanded = new LineGraph("Life", lineGraphBigStyle);
        graph.setNodes(xValue.getTimeline(), xValue.getScaling());
        graphExpanded.setNodeListener(handListener);
        graphToggleWidget.table2.add(graphExpanded).grow();

        onChange(button, () -> {
            graphToggleWidget.swap();
            graphExpanded.setNodes(xValue.getTimeline(), xValue.getScaling());
            expandedType = ExpandedType.EXPANDED_BOTH;
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
            xValue.setTimeline(newTimeline);
            xValue.setScaling(newScaling);
            yValue.setTimeline(newTimeline);
            yValue.setScaling(newScaling);
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

            if (expandedType == ExpandedType.EXPANDED_X || expandedType == ExpandedType.EXPANDED_BOTH) {
                xValue.setTimeline(newTimeline);
                xValue.setScaling(newScaling);
            }

            if (expandedType == ExpandedType.EXPANDED_Y || expandedType == ExpandedType.EXPANDED_BOTH) {
                yValue.setTimeline(newTimeline);
                yValue.setScaling(newScaling);
            }
        });

        var expandMinusButton = new Button(skin, "minus");
        graphToggleWidget.table2.add(expandMinusButton).bottom();
        addHandListener(expandMinusButton);
        addTooltip(expandMinusButton, "Collapse to normal view", Align.top, tooltipBottomArrowStyle);

        //Separate
        //X size
        splitToggleWidget.table2.defaults().space(itemSpacing);
        label = new Label("X Size", skin, "header");
        splitToggleWidget.table2.add(label).left().padTop(sectionPadding);

        //Relative
        splitToggleWidget.table2.row();
        var relativeXcheckBox = new CheckBox("Relative", skin);
        splitToggleWidget.table2.add(relativeXcheckBox).left();
        addHandListener(relativeXcheckBox);
        addTooltip(relativeXcheckBox, "If true, the value is in addition to the emitter's value", Align.top, tooltipBottomArrowStyle);

        //High
        splitToggleWidget.table2.row();
        table = new Table();
        splitToggleWidget.table2.add(table).top();

        table.defaults().space(itemSpacing).left();
        label = new Label("High:", skin);
        table.add(label);

        var highXtoggleWidget = new ToggleWidget();
        table.add(highXtoggleWidget);

        //High single
        highXtoggleWidget.table1.defaults().space(itemSpacing);
        var highXspinner = new Spinner(xValue.getHighMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highXspinner.setProgrammaticChangeEvents(false);
        highXtoggleWidget.table1.add(highXspinner).width(spinnerWidth);
        addIbeamListener(highXspinner.getTextField());
        addHandListener(highXspinner.getButtonPlus());
        addHandListener(highXspinner.getButtonMinus());
        addTooltip(highXspinner, "The high value for the particle X size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveright");
        highXtoggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the high value", Align.top, tooltipBottomArrowStyle);
        onChange(button, highXtoggleWidget::swap);

        //High range
        highXtoggleWidget.table2.defaults().space(itemSpacing);
        var highXminSpinner = new Spinner(xValue.getHighMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highXminSpinner.setProgrammaticChangeEvents(false);
        highXtoggleWidget.table2.add(highXminSpinner).width(spinnerWidth);
        addIbeamListener(highXminSpinner.getTextField());
        addHandListener(highXminSpinner.getButtonPlus());
        addHandListener(highXminSpinner.getButtonMinus());
        addTooltip(highXminSpinner, "The minimum high value for the particle X size in world units.", Align.top, tooltipBottomArrowStyle);

        var highXmaxSpinner = new Spinner(xValue.getHighMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highXmaxSpinner.setProgrammaticChangeEvents(false);
        highXtoggleWidget.table2.add(highXmaxSpinner).width(spinnerWidth);
        addIbeamListener(highXmaxSpinner.getTextField());
        addHandListener(highXmaxSpinner.getButtonPlus());
        addHandListener(highXmaxSpinner.getButtonMinus());
        addTooltip(highXmaxSpinner, "The maximum high value for the particle X size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        highXtoggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single high value", Align.top, tooltipBottomArrowStyle);
        onChange(button, highXtoggleWidget::swap);

        onChange(highXspinner, () -> {
            xValue.setHigh(highXspinner.getValueAsInt());
            highXminSpinner.setValue(highXspinner.getValueAsInt());
            highXmaxSpinner.setValue(highXspinner.getValueAsInt());
        });

        onChange(highXminSpinner, () -> {
            xValue.setHighMin(highXminSpinner.getValueAsInt());
            yValue.setHighMin(highXminSpinner.getValueAsInt());
            highXspinner.setValue(highXminSpinner.getValueAsInt());
        });

        onChange(highXmaxSpinner, () -> {
            xValue.setHighMax(highXmaxSpinner.getValueAsInt());
            yValue.setHighMax(highXmaxSpinner.getValueAsInt());
            highSpinner.setValue(highXmaxSpinner.getValueAsInt());
        });

        onChange(button, () -> {
            xValue.setHigh(highXspinner.getValueAsInt());
            yValue.setHigh(highXspinner.getValueAsInt());
            highXmaxSpinner.setValue(highXspinner.getValueAsInt());
            highXmaxSpinner.setValue(highXspinner.getValueAsInt());
        });

        //Low
        table.row();
        label = new Label("Low:", skin);
        table.add(label);

        var lowXtoggleWidget = new ToggleWidget();
        table.add(lowXtoggleWidget);

        //Low single
        lowXtoggleWidget.table1.defaults().space(itemSpacing);
        var lowXspinner = new Spinner(xValue.getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowXspinner.setProgrammaticChangeEvents(false);
        lowXtoggleWidget.table1.add(lowXspinner).width(spinnerWidth);
        addIbeamListener(lowXspinner.getTextField());
        addHandListener(lowXspinner.getButtonPlus());
        addHandListener(lowXspinner.getButtonMinus());
        addTooltip(lowXspinner, "The low value for the particle X size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveright");
        lowXtoggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the low value", Align.top, tooltipBottomArrowStyle);
        onChange(button, lowXtoggleWidget::swap);

        //Low range
        lowXtoggleWidget.table2.defaults().space(itemSpacing);
        var lowXminSpinner = new Spinner(xValue.getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowXminSpinner.setProgrammaticChangeEvents(false);
        lowXtoggleWidget.table2.add(lowXminSpinner).width(spinnerWidth);
        addIbeamListener(lowXminSpinner.getTextField());
        addHandListener(lowXminSpinner.getButtonPlus());
        addHandListener(lowXminSpinner.getButtonMinus());
        addTooltip(lowXminSpinner, "The minimum low value for the particle X size in world units.", Align.top, tooltipBottomArrowStyle);

        var lowXmaxSpinner = new Spinner(xValue.getLowMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowXmaxSpinner.setProgrammaticChangeEvents(false);
        lowXtoggleWidget.table2.add(lowXmaxSpinner).width(spinnerWidth);
        addIbeamListener(lowXmaxSpinner.getTextField());
        addHandListener(lowXmaxSpinner.getButtonPlus());
        addHandListener(lowXmaxSpinner.getButtonMinus());
        addTooltip(lowXmaxSpinner, "The maximum low value for the particle X size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        lowXtoggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single low value", Align.top, tooltipBottomArrowStyle);
        onChange(button, lowXtoggleWidget::swap);

        onChange(lowXspinner, () -> {
            xValue.setLow(lowXspinner.getValueAsInt());
            lowXminSpinner.setValue(lowXspinner.getValueAsInt());
            lowXmaxSpinner.setValue(lowXspinner.getValueAsInt());
        });

        onChange(lowXminSpinner, () -> {
            xValue.setLowMin(lowXminSpinner.getValueAsInt());
            yValue.setLowMin(lowXminSpinner.getValueAsInt());
            lowXspinner.setValue(lowXminSpinner.getValueAsInt());
        });

        onChange(lowXmaxSpinner, () -> {
            xValue.setLowMax(lowXmaxSpinner.getValueAsInt());
            yValue.setLowMax(lowXmaxSpinner.getValueAsInt());
            lowSpinner.setValue(lowXmaxSpinner.getValueAsInt());
        });

        onChange(button, () -> {
            xValue.setLow(lowXspinner.getValueAsInt());
            yValue.setLow(lowXspinner.getValueAsInt());
            lowXmaxSpinner.setValue(lowXspinner.getValueAsInt());
            lowXmaxSpinner.setValue(lowXspinner.getValueAsInt());
        });

        //Graph small
        var graphX = new LineGraph("Life", lineGraphStyle);
        graphX.setNodes(xValue.getTimeline(), xValue.getScaling());
        graphX.setNodeListener(handListener);
        splitToggleWidget.table2.add(graphX);

        button = new Button(skin, "plus");
        splitToggleWidget.table2.add(button).bottom();
        addHandListener(button);
        addTooltip(button, "Expand to large graph view", Align.top, tooltipBottomArrowStyle);

        onChange(button, () -> {
            graphToggleWidget.swap();
            graphExpanded.setNodes(xValue.getTimeline(), xValue.getScaling());
            expandedType = ExpandedType.EXPANDED_X;
        });

        onChange(graphX, () -> {
            var nodes = graphX.getNodes();
            float[] newTimeline = new float[nodes.size];
            float[] newScaling = new float[nodes.size];
            for (int i = 0; i < nodes.size; i++) {
                var node = nodes.get(i);
                newTimeline[i] = node.percentX;
                newScaling[i] = node.percentY;
            }
            xValue.setTimeline(newTimeline);
            xValue.setScaling(newScaling);
        });

        //Y size
        splitToggleWidget.table2.row();
        label = new Label("Y Size", skin, "header");
        splitToggleWidget.table2.add(label).left().padTop(sectionPadding);

        //Relative
        splitToggleWidget.table2.row();
        var relativeYcheckBox = new CheckBox("Relative", skin);
        splitToggleWidget.table2.add(relativeYcheckBox).left();
        addHandListener(relativeYcheckBox);
        addTooltip(relativeYcheckBox, "If true, the value is in addition to the emitter's value", Align.top, tooltipBottomArrowStyle);

        //High
        splitToggleWidget.table2.row();
        table = new Table();
        splitToggleWidget.table2.add(table).top();

        table.defaults().space(itemSpacing).left();
        label = new Label("High:", skin);
        table.add(label);

        var highYtoggleWidget = new ToggleWidget();
        table.add(highYtoggleWidget);

        //High single
        highYtoggleWidget.table1.defaults().space(itemSpacing);
        var highYspinner = new Spinner(yValue.getHighMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highYspinner.setProgrammaticChangeEvents(false);
        highYtoggleWidget.table1.add(highYspinner).width(spinnerWidth);
        addIbeamListener(highYspinner.getTextField());
        addHandListener(highYspinner.getButtonPlus());
        addHandListener(highYspinner.getButtonMinus());
        addTooltip(highYspinner, "The high value for the particle Y size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveright");
        highYtoggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the high value", Align.top, tooltipBottomArrowStyle);
        onChange(button, highYtoggleWidget::swap);

        //High range
        highYtoggleWidget.table2.defaults().space(itemSpacing);
        var highYminSpinner = new Spinner(yValue.getHighMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highYminSpinner.setProgrammaticChangeEvents(false);
        highYtoggleWidget.table2.add(highYminSpinner).width(spinnerWidth);
        addIbeamListener(highYminSpinner.getTextField());
        addHandListener(highYminSpinner.getButtonPlus());
        addHandListener(highYminSpinner.getButtonMinus());
        addTooltip(highYminSpinner, "The minimum high value for the particle Y size in world units.", Align.top, tooltipBottomArrowStyle);

        var highYmaxSpinner = new Spinner(yValue.getHighMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highYmaxSpinner.setProgrammaticChangeEvents(false);
        highYtoggleWidget.table2.add(highYmaxSpinner).width(spinnerWidth);
        addIbeamListener(highYmaxSpinner.getTextField());
        addHandListener(highYmaxSpinner.getButtonPlus());
        addHandListener(highYmaxSpinner.getButtonMinus());
        addTooltip(highYmaxSpinner, "The maximum high value for the particle Y size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        highYtoggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single high value", Align.top, tooltipBottomArrowStyle);
        onChange(button, highYtoggleWidget::swap);

        onChange(highYspinner, () -> {
            yValue.setHigh(highYspinner.getValueAsInt());
            highYminSpinner.setValue(highYspinner.getValueAsInt());
            highYmaxSpinner.setValue(highYspinner.getValueAsInt());
        });

        onChange(highYminSpinner, () -> {
            yValue.setHighMin(highYminSpinner.getValueAsInt());
            yValue.setHighMin(highYminSpinner.getValueAsInt());
            highYspinner.setValue(highYminSpinner.getValueAsInt());
        });

        onChange(highYmaxSpinner, () -> {
            yValue.setHighMax(highYmaxSpinner.getValueAsInt());
            yValue.setHighMax(highYmaxSpinner.getValueAsInt());
            highSpinner.setValue(highYmaxSpinner.getValueAsInt());
        });

        onChange(button, () -> {
            yValue.setHigh(highYspinner.getValueAsInt());
            yValue.setHigh(highYspinner.getValueAsInt());
            highYmaxSpinner.setValue(highYspinner.getValueAsInt());
            highYmaxSpinner.setValue(highYspinner.getValueAsInt());
        });

        //Low
        table.row();
        label = new Label("Low:", skin);
        table.add(label);

        var lowYtoggleWidget = new ToggleWidget();
        table.add(lowYtoggleWidget);

        //Low single
        lowYtoggleWidget.table1.defaults().space(itemSpacing);
        var lowYspinner = new Spinner(yValue.getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowYspinner.setProgrammaticChangeEvents(false);
        lowYtoggleWidget.table1.add(lowYspinner).width(spinnerWidth);
        addIbeamListener(lowYspinner.getTextField());
        addHandListener(lowYspinner.getButtonPlus());
        addHandListener(lowYspinner.getButtonMinus());
        addTooltip(lowYspinner, "The low value for the particle Y size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveright");
        lowYtoggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the low value", Align.top, tooltipBottomArrowStyle);
        onChange(button, lowYtoggleWidget::swap);

        //Low range
        lowYtoggleWidget.table2.defaults().space(itemSpacing);
        var lowYminSpinner = new Spinner(yValue.getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowYminSpinner.setProgrammaticChangeEvents(false);
        lowYtoggleWidget.table2.add(lowYminSpinner).width(spinnerWidth);
        addIbeamListener(lowYminSpinner.getTextField());
        addHandListener(lowYminSpinner.getButtonPlus());
        addHandListener(lowYminSpinner.getButtonMinus());
        addTooltip(lowYminSpinner, "The minimum low value for the particle Y size in world units.", Align.top, tooltipBottomArrowStyle);

        var lowYmaxSpinner = new Spinner(yValue.getLowMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowYmaxSpinner.setProgrammaticChangeEvents(false);
        lowYtoggleWidget.table2.add(lowYmaxSpinner).width(spinnerWidth);
        addIbeamListener(lowYmaxSpinner.getTextField());
        addHandListener(lowYmaxSpinner.getButtonPlus());
        addHandListener(lowYmaxSpinner.getButtonMinus());
        addTooltip(lowYmaxSpinner, "The maximum low value for the particle Y size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        lowYtoggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single low value", Align.top, tooltipBottomArrowStyle);
        onChange(button, lowYtoggleWidget::swap);

        onChange(lowYspinner, () -> {
            yValue.setLow(lowYspinner.getValueAsInt());
            lowYminSpinner.setValue(lowYspinner.getValueAsInt());
            lowYmaxSpinner.setValue(lowYspinner.getValueAsInt());
        });

        onChange(lowYminSpinner, () -> {
            yValue.setLowMin(lowYminSpinner.getValueAsInt());
            yValue.setLowMin(lowYminSpinner.getValueAsInt());
            lowYspinner.setValue(lowYminSpinner.getValueAsInt());
        });

        onChange(lowYmaxSpinner, () -> {
            yValue.setLowMax(lowYmaxSpinner.getValueAsInt());
            yValue.setLowMax(lowYmaxSpinner.getValueAsInt());
            lowSpinner.setValue(lowYmaxSpinner.getValueAsInt());
        });

        onChange(button, () -> {
            yValue.setLow(lowYspinner.getValueAsInt());
            yValue.setLow(lowYspinner.getValueAsInt());
            lowYmaxSpinner.setValue(lowYspinner.getValueAsInt());
            lowYmaxSpinner.setValue(lowYspinner.getValueAsInt());
        });

        //Graph small
        var graphY = new LineGraph("Life", lineGraphStyle);
        graphY.setNodes(yValue.getTimeline(), yValue.getScaling());
        graphY.setNodeListener(handListener);
        splitToggleWidget.table2.add(graphY);

        button = new Button(skin, "plus");
        splitToggleWidget.table2.add(button).bottom();
        addHandListener(button);
        addTooltip(button, "Expand to large graph view", Align.top, tooltipBottomArrowStyle);

        onChange(button, () -> {
            graphToggleWidget.swap();
            graphExpanded.setNodes(yValue.getTimeline(), yValue.getScaling());
            expandedType = ExpandedType.EXPANDED_Y;
        });

        onChange(graphY, () -> {
            var nodes = graphY.getNodes();
            float[] newTimeline = new float[nodes.size];
            float[] newScaling = new float[nodes.size];
            for (int i = 0; i < nodes.size; i++) {
                var node = nodes.get(i);
                newTimeline[i] = node.percentX;
                newScaling[i] = node.percentY;
            }
            yValue.setTimeline(newTimeline);
            yValue.setScaling(newScaling);
        });

        onChange(splitXYcheckBox, () -> {
            if (splitXYcheckBox.isChecked()) {
                yValue.setActive(true);
                highXspinner.setValue(xValue.getHighMin());
                highXminSpinner.setValue(xValue.getHighMin());
                highXmaxSpinner.setValue(xValue.getHighMax());
                if (MathUtils.isEqual(xValue.getHighMin(), xValue.getHighMax())) highXtoggleWidget.showTable1();
                else highXtoggleWidget.showTable2();

                lowXspinner.setValue(xValue.getLowMin());
                lowXminSpinner.setValue(xValue.getLowMin());
                lowXmaxSpinner.setValue(xValue.getLowMax());
                if (MathUtils.isEqual(xValue.getLowMin(), xValue.getLowMax())) lowXtoggleWidget.showTable1();
                else lowXtoggleWidget.showTable2();

                highYspinner.setValue(yValue.getHighMin());
                highYminSpinner.setValue(yValue.getHighMin());
                highYmaxSpinner.setValue(yValue.getHighMax());
                if (MathUtils.isEqual(yValue.getHighMin(), yValue.getHighMax())) highYtoggleWidget.showTable1();
                else highYtoggleWidget.showTable2();

                lowYspinner.setValue(yValue.getLowMin());
                lowYminSpinner.setValue(yValue.getLowMin());
                lowYmaxSpinner.setValue(yValue.getLowMax());
                if (MathUtils.isEqual(yValue.getLowMin(), yValue.getLowMax())) lowYtoggleWidget.showTable1();
                else lowYtoggleWidget.showTable2();
            } else {
                yValue.setActive(false);

                highSpinner.setValue(xValue.getHighMin());
                highMinSpinner.setValue(xValue.getHighMin());
                highMaxSpinner.setValue(xValue.getHighMax());
                if (MathUtils.isEqual(xValue.getHighMin(), xValue.getHighMax())) highToggleWidget.showTable1();
                else highToggleWidget.showTable2();

                lowSpinner.setValue(xValue.getLowMin());
                lowMinSpinner.setValue(xValue.getLowMin());
                lowMaxSpinner.setValue(xValue.getLowMax());
                if (MathUtils.isEqual(xValue.getLowMin(), xValue.getLowMax())) lowToggleWidget.showTable1();
                else lowToggleWidget.showTable2();
            }

            graph.setNodes(xValue.getTimeline(), xValue.getScaling());
            graphX.setNodes(xValue.getTimeline(), xValue.getScaling());
            graphY.setNodes(yValue.getTimeline(), yValue.getScaling());

            splitToggleWidget.swap();
        });

        onChange(expandMinusButton, () -> {
            graph.setNodes(xValue.getTimeline(), xValue.getScaling());
            graphX.setNodes(xValue.getTimeline(), xValue.getScaling());
            graphY.setNodes(yValue.getTimeline(), yValue.getScaling());
            graphToggleWidget.swap();
        });
    }
}
