package com.ray3k.particleparkpro.widgets.subpanels;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.undo.UndoManager;
import com.ray3k.particleparkpro.undo.undoables.DualScaledNumericValueRelativeUndoable;
import com.ray3k.particleparkpro.undo.undoables.DualScaledNumericValueUndoable;
import com.ray3k.particleparkpro.undo.undoables.DualScaledNumericValueUndoable.DualScaledNumericValueUndoableData;
import com.ray3k.particleparkpro.undo.undoables.ScaledNumericValueRelativeUndoable;
import com.ray3k.particleparkpro.undo.undoables.ScaledNumericValueUndoable;
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
    private static final float GRAPH_UNDO_DELAY = .3f;
    private Action graphUndoAction;

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
        addTooltip(splitXYcheckBox, "If true, the X and Y values can be set independently", Align.top, Align.top, tooltipBottomArrowStyle);

        //Split ToggleWidget
        graphToggleWidget.table1.row();
        var splitToggleWidget = new ToggleWidget();
        graphToggleWidget.table1.add(splitToggleWidget);

        //Joined
        //Relative
        splitToggleWidget.table1.defaults().space(itemSpacing);
        var relativeCheckBox = new CheckBox("Relative", skin);
        relativeCheckBox.setProgrammaticChangeEvents(false);
        relativeCheckBox.setChecked(xValue.isRelative());
        splitToggleWidget.table1.add(relativeCheckBox).left();
        addHandListener(relativeCheckBox);
        addTooltip(relativeCheckBox, "If true, the value is in addition to the emitter's value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(relativeCheckBox, () -> UndoManager.addUndoable(new DualScaledNumericValueRelativeUndoable(xValue, yValue, relativeCheckBox.isChecked(), "change Size Relative")));

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
        addTooltip(highSpinner, "The high value for the particle size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var highExpandButton = new Button(skin, "moveright");
        highToggleWidget.table1.add(highExpandButton);
        addHandListener(highExpandButton);
        addTooltip(highExpandButton, "Expand to define a range for the high value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(highExpandButton, highToggleWidget::swap);

        //High range
        highToggleWidget.table2.defaults().space(itemSpacing);
        var highMinSpinner = new Spinner(xValue.getHighMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highMinSpinner.setProgrammaticChangeEvents(false);
        highToggleWidget.table2.add(highMinSpinner).width(spinnerWidth);
        addIbeamListener(highMinSpinner.getTextField());
        addHandListener(highMinSpinner.getButtonPlus());
        addHandListener(highMinSpinner.getButtonMinus());
        addTooltip(highMinSpinner, "The minimum high value for the particle size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var highMaxSpinner = new Spinner(xValue.getHighMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highMaxSpinner.setProgrammaticChangeEvents(false);
        highToggleWidget.table2.add(highMaxSpinner).width(spinnerWidth);
        addIbeamListener(highMaxSpinner.getTextField());
        addHandListener(highMaxSpinner.getButtonPlus());
        addHandListener(highMaxSpinner.getButtonMinus());
        addTooltip(highMaxSpinner, "The maximum high value for the particle size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var highCollapseButton = new Button(skin, "moveleft");
        highToggleWidget.table2.add(highCollapseButton);
        addHandListener(highCollapseButton);
        addTooltip(highCollapseButton, "Collapse to define a single high value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(highCollapseButton, highToggleWidget::swap);

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
        addTooltip(lowSpinner, "The low value for the particle size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var lowExpandButton = new Button(skin, "moveright");
        lowToggleWidget.table1.add(lowExpandButton);
        addHandListener(lowExpandButton);
        addTooltip(lowExpandButton, "Expand to define a range for the low value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(lowExpandButton, lowToggleWidget::swap);

        //Low range
        lowToggleWidget.table2.defaults().space(itemSpacing);
        var lowMinSpinner = new Spinner(xValue.getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowMinSpinner.setProgrammaticChangeEvents(false);
        lowToggleWidget.table2.add(lowMinSpinner).width(spinnerWidth);
        addIbeamListener(lowMinSpinner.getTextField());
        addHandListener(lowMinSpinner.getButtonPlus());
        addHandListener(lowMinSpinner.getButtonMinus());
        addTooltip(lowMinSpinner, "The minimum low value for the particle size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var lowMaxSpinner = new Spinner(xValue.getLowMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowMaxSpinner.setProgrammaticChangeEvents(false);
        lowToggleWidget.table2.add(lowMaxSpinner).width(spinnerWidth);
        addIbeamListener(lowMaxSpinner.getTextField());
        addHandListener(lowMaxSpinner.getButtonPlus());
        addHandListener(lowMaxSpinner.getButtonMinus());
        addTooltip(lowMaxSpinner, "The maximum low value for the particle size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var lowCollapseButton = new Button(skin, "moveleft");
        lowToggleWidget.table2.add(lowCollapseButton);
        addHandListener(lowCollapseButton);
        addTooltip(lowCollapseButton, "Collapse to define a single low value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(lowCollapseButton, lowToggleWidget::swap);

        if (!MathUtils.isEqual(xValue.getLowMin(), xValue.getLowMax())) lowToggleWidget.swap();

        //Graph small
        var graph = new LineGraph("Life", lineGraphStyle);
        graph.setNodes(xValue.getTimeline(), xValue.getScaling());
        graph.setNodeListener(handListener);
        splitToggleWidget.table1.add(graph);

        var graphExpandButton = new Button(skin, "plus");
        splitToggleWidget.table1.add(graphExpandButton).bottom();
        addHandListener(graphExpandButton);
        addTooltip(graphExpandButton, "Expand to large graph view", Align.top, Align.top, tooltipBottomArrowStyle);

        //Expanded graph view
        graphToggleWidget.table2.defaults().space(itemSpacing);
        var graphExpanded = new LineGraph("Life", lineGraphBigStyle);
        graph.setNodes(xValue.getTimeline(), xValue.getScaling());
        graphExpanded.setNodeListener(handListener);
        graphToggleWidget.table2.add(graphExpanded).grow();

        onChange(graphExpandButton, () -> {
            graphToggleWidget.swap();
            graphExpanded.setNodes(xValue.getTimeline(), xValue.getScaling());
            expandedType = ExpandedType.EXPANDED_BOTH;
        });

        var graphCollapseButton = new Button(skin, "minus");
        graphToggleWidget.table2.add(graphCollapseButton).bottom();
        addHandListener(graphCollapseButton);
        addTooltip(graphCollapseButton, "Collapse to normal view", Align.top, Align.top, tooltipBottomArrowStyle);

        //Separate
        //X size
        splitToggleWidget.table2.defaults().space(itemSpacing);
        label = new Label("X Size", skin, "header");
        splitToggleWidget.table2.add(label).left().padTop(sectionPadding);

        //Relative
        splitToggleWidget.table2.row();
        var relativeXcheckBox = new CheckBox("Relative", skin);
        relativeXcheckBox.setProgrammaticChangeEvents(false);
        splitToggleWidget.table2.add(relativeXcheckBox).left();
        addHandListener(relativeXcheckBox);
        addTooltip(relativeXcheckBox, "If true, the value is in addition to the emitter's value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(relativeXcheckBox, () -> UndoManager.addUndoable(new ScaledNumericValueRelativeUndoable(xValue, relativeXcheckBox.isChecked(), "change X Size Relative")));

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
        addTooltip(highXspinner, "The high value for the particle X size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var highXexpandButton = new Button(skin, "moveright");
        highXtoggleWidget.table1.add(highXexpandButton);
        addHandListener(highXexpandButton);
        addTooltip(highXexpandButton, "Expand to define a range for the high value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(highXexpandButton, highXtoggleWidget::swap);

        //High range
        highXtoggleWidget.table2.defaults().space(itemSpacing);
        var highXminSpinner = new Spinner(xValue.getHighMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highXminSpinner.setProgrammaticChangeEvents(false);
        highXtoggleWidget.table2.add(highXminSpinner).width(spinnerWidth);
        addIbeamListener(highXminSpinner.getTextField());
        addHandListener(highXminSpinner.getButtonPlus());
        addHandListener(highXminSpinner.getButtonMinus());
        addTooltip(highXminSpinner, "The minimum high value for the particle X size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var highXmaxSpinner = new Spinner(xValue.getHighMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highXmaxSpinner.setProgrammaticChangeEvents(false);
        highXtoggleWidget.table2.add(highXmaxSpinner).width(spinnerWidth);
        addIbeamListener(highXmaxSpinner.getTextField());
        addHandListener(highXmaxSpinner.getButtonPlus());
        addHandListener(highXmaxSpinner.getButtonMinus());
        addTooltip(highXmaxSpinner, "The maximum high value for the particle X size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var highXcollapseButton = new Button(skin, "moveleft");
        highXtoggleWidget.table2.add(highXcollapseButton);
        addHandListener(highXcollapseButton);
        addTooltip(highXcollapseButton, "Collapse to define a single high value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(highXcollapseButton, highXtoggleWidget::swap);

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
        addTooltip(lowXspinner, "The low value for the particle X size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var lowXexpandButton = new Button(skin, "moveright");
        lowXtoggleWidget.table1.add(lowXexpandButton);
        addHandListener(lowXexpandButton);
        addTooltip(lowXexpandButton, "Expand to define a range for the low value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(lowXexpandButton, lowXtoggleWidget::swap);

        //Low range
        lowXtoggleWidget.table2.defaults().space(itemSpacing);
        var lowXminSpinner = new Spinner(xValue.getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowXminSpinner.setProgrammaticChangeEvents(false);
        lowXtoggleWidget.table2.add(lowXminSpinner).width(spinnerWidth);
        addIbeamListener(lowXminSpinner.getTextField());
        addHandListener(lowXminSpinner.getButtonPlus());
        addHandListener(lowXminSpinner.getButtonMinus());
        addTooltip(lowXminSpinner, "The minimum low value for the particle X size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var lowXmaxSpinner = new Spinner(xValue.getLowMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowXmaxSpinner.setProgrammaticChangeEvents(false);
        lowXtoggleWidget.table2.add(lowXmaxSpinner).width(spinnerWidth);
        addIbeamListener(lowXmaxSpinner.getTextField());
        addHandListener(lowXmaxSpinner.getButtonPlus());
        addHandListener(lowXmaxSpinner.getButtonMinus());
        addTooltip(lowXmaxSpinner, "The maximum low value for the particle X size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var lowXcollapseButton = new Button(skin, "moveleft");
        lowXtoggleWidget.table2.add(lowXcollapseButton);
        addHandListener(lowXcollapseButton);
        addTooltip(lowXcollapseButton, "Collapse to define a single low value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(lowXcollapseButton, lowXtoggleWidget::swap);

        //Graph small
        var graphX = new LineGraph("Life", lineGraphStyle);
        graphX.setNodes(xValue.getTimeline(), xValue.getScaling());
        graphX.setNodeListener(handListener);
        splitToggleWidget.table2.add(graphX);

        var lowXGraphExpandButton = new Button(skin, "plus");
        splitToggleWidget.table2.add(lowXGraphExpandButton).bottom();
        addHandListener(lowXGraphExpandButton);
        addTooltip(lowXGraphExpandButton, "Expand to large graph view", Align.top, Align.top, tooltipBottomArrowStyle);

        onChange(lowXGraphExpandButton, () -> {
            graphToggleWidget.swap();
            graphExpanded.setNodes(xValue.getTimeline(), xValue.getScaling());
            expandedType = ExpandedType.EXPANDED_X;
        });

        //Y size
        splitToggleWidget.table2.row();
        label = new Label("Y Size", skin, "header");
        splitToggleWidget.table2.add(label).left().padTop(sectionPadding);

        //Relative
        splitToggleWidget.table2.row();
        var relativeYcheckBox = new CheckBox("Relative", skin);
        relativeYcheckBox.setProgrammaticChangeEvents(false);
        splitToggleWidget.table2.add(relativeYcheckBox).left();
        addHandListener(relativeYcheckBox);
        addTooltip(relativeYcheckBox, "If true, the value is in addition to the emitter's value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(relativeYcheckBox, () -> UndoManager.addUndoable(new ScaledNumericValueRelativeUndoable(yValue, relativeYcheckBox.isChecked(), "change Y Size Relative")));

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
        addTooltip(highYspinner, "The high value for the particle Y size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var highYexpandButton = new Button(skin, "moveright");
        highYtoggleWidget.table1.add(highYexpandButton);
        addHandListener(highYexpandButton);
        addTooltip(highYexpandButton, "Expand to define a range for the high value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(highYexpandButton, highYtoggleWidget::swap);

        //High range
        highYtoggleWidget.table2.defaults().space(itemSpacing);
        var highYminSpinner = new Spinner(yValue.getHighMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highYminSpinner.setProgrammaticChangeEvents(false);
        highYtoggleWidget.table2.add(highYminSpinner).width(spinnerWidth);
        addIbeamListener(highYminSpinner.getTextField());
        addHandListener(highYminSpinner.getButtonPlus());
        addHandListener(highYminSpinner.getButtonMinus());
        addTooltip(highYminSpinner, "The minimum high value for the particle Y size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var highYmaxSpinner = new Spinner(yValue.getHighMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highYmaxSpinner.setProgrammaticChangeEvents(false);
        highYtoggleWidget.table2.add(highYmaxSpinner).width(spinnerWidth);
        addIbeamListener(highYmaxSpinner.getTextField());
        addHandListener(highYmaxSpinner.getButtonPlus());
        addHandListener(highYmaxSpinner.getButtonMinus());
        addTooltip(highYmaxSpinner, "The maximum high value for the particle Y size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var highYcollapseButton = new Button(skin, "moveleft");
        highYtoggleWidget.table2.add(highYcollapseButton);
        addHandListener(highYcollapseButton);
        addTooltip(highYcollapseButton, "Collapse to define a single high value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(highYcollapseButton, highYtoggleWidget::swap);

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
        addTooltip(lowYspinner, "The low value for the particle Y size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var lowYexpandButton = new Button(skin, "moveright");
        lowYtoggleWidget.table1.add(lowYexpandButton);
        addHandListener(lowYexpandButton);
        addTooltip(lowYexpandButton, "Expand to define a range for the low value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(lowYexpandButton, lowYtoggleWidget::swap);

        //Low range
        lowYtoggleWidget.table2.defaults().space(itemSpacing);
        var lowYminSpinner = new Spinner(yValue.getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowYminSpinner.setProgrammaticChangeEvents(false);
        lowYtoggleWidget.table2.add(lowYminSpinner).width(spinnerWidth);
        addIbeamListener(lowYminSpinner.getTextField());
        addHandListener(lowYminSpinner.getButtonPlus());
        addHandListener(lowYminSpinner.getButtonMinus());
        addTooltip(lowYminSpinner, "The minimum low value for the particle Y size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var lowYmaxSpinner = new Spinner(yValue.getLowMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowYmaxSpinner.setProgrammaticChangeEvents(false);
        lowYtoggleWidget.table2.add(lowYmaxSpinner).width(spinnerWidth);
        addIbeamListener(lowYmaxSpinner.getTextField());
        addHandListener(lowYmaxSpinner.getButtonPlus());
        addHandListener(lowYmaxSpinner.getButtonMinus());
        addTooltip(lowYmaxSpinner, "The maximum low value for the particle Y size in world units.", Align.top, Align.top, tooltipBottomArrowStyle);

        var lowYcollapseButton = new Button(skin, "moveleft");
        lowYtoggleWidget.table2.add(lowYcollapseButton);
        addHandListener(lowYcollapseButton);
        addTooltip(lowYcollapseButton, "Collapse to define a single low value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(lowYcollapseButton, lowYtoggleWidget::swap);

        //Graph small
        var graphY = new LineGraph("Life", lineGraphStyle);
        graphY.setNodes(yValue.getTimeline(), yValue.getScaling());
        graphY.setNodeListener(handListener);
        splitToggleWidget.table2.add(graphY);

        var lowYgraphExpandButton = new Button(skin, "plus");
        splitToggleWidget.table2.add(lowYgraphExpandButton).bottom();
        addHandListener(lowYgraphExpandButton);
        addTooltip(lowYgraphExpandButton, "Expand to large graph view", Align.top, Align.top, tooltipBottomArrowStyle);

        onChange(lowYgraphExpandButton, () -> {
            graphToggleWidget.swap();
            graphExpanded.setNodes(yValue.getTimeline(), yValue.getScaling());
            expandedType = ExpandedType.EXPANDED_Y;
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

        onChange(graphCollapseButton, () -> {
            graph.setNodes(xValue.getTimeline(), xValue.getScaling());
            graphX.setNodes(xValue.getTimeline(), xValue.getScaling());
            graphY.setNodes(yValue.getTimeline(), yValue.getScaling());
            graphToggleWidget.swap();
        });

        var unsplitUndoDataTemplate = DualScaledNumericValueUndoableData
            .builder()
            .xValue(xValue)
            .yValue(yValue)
            .description("change Scale")
            .build();

        onChange(highSpinner, () -> {
            var undoData = unsplitUndoDataTemplate.toBuilder().build();
            undoData.oldXvalue.set(xValue);
            undoData.newXvalue.set(xValue);
            undoData.newXvalue.setHigh(highSpinner.getValueAsInt());

            undoData.oldYvalue.set(yValue);
            undoData.newYvalue.set(yValue);
            undoData.newYvalue.setHigh(highSpinner.getValueAsInt());
            UndoManager.addUndoable(new DualScaledNumericValueUndoable(undoData));

            highMinSpinner.setValue(highSpinner.getValueAsInt());
            highMaxSpinner.setValue(highSpinner.getValueAsInt());
        });

        onChange(highMinSpinner, () -> {
            var undoData = unsplitUndoDataTemplate.toBuilder().build();
            undoData.oldXvalue.set(xValue);
            undoData.newXvalue.set(xValue);
            undoData.newXvalue.setHighMin(highMinSpinner.getValueAsInt());

            undoData.oldYvalue.set(yValue);
            undoData.newYvalue.set(yValue);
            undoData.newYvalue.setHighMin(highMinSpinner.getValueAsInt());
            UndoManager.addUndoable(new DualScaledNumericValueUndoable(undoData));

            highSpinner.setValue(highMinSpinner.getValueAsInt());
        });

        onChange(highMaxSpinner, () -> {
            var undoData = unsplitUndoDataTemplate.toBuilder().build();
            undoData.oldXvalue.set(xValue);
            undoData.newXvalue.set(xValue);
            undoData.newXvalue.setHighMax(highMaxSpinner.getValueAsInt());

            undoData.oldYvalue.set(yValue);
            undoData.newYvalue.set(yValue);
            undoData.newYvalue.setHighMax(highMaxSpinner.getValueAsInt());
            UndoManager.addUndoable(new DualScaledNumericValueUndoable(undoData));

            highSpinner.setValue(highMaxSpinner.getValueAsInt());
        });

        onChange(highCollapseButton, () -> {
            var undoData = unsplitUndoDataTemplate.toBuilder().build();
            undoData.oldXvalue.set(xValue);
            undoData.newXvalue.set(xValue);
            undoData.newXvalue.setHigh(highSpinner.getValueAsInt());

            undoData.oldYvalue.set(yValue);
            undoData.newYvalue.set(yValue);
            undoData.newYvalue.setHigh(highSpinner.getValueAsInt());
            UndoManager.addUndoable(new DualScaledNumericValueUndoable(undoData));

            highMinSpinner.setValue(highSpinner.getValueAsInt());
            highMaxSpinner.setValue(highSpinner.getValueAsInt());
        });

        onChange(lowSpinner, () -> {
            var undoData = unsplitUndoDataTemplate.toBuilder().build();
            undoData.oldXvalue.set(xValue);
            undoData.newXvalue.set(xValue);
            undoData.newXvalue.setLow(lowSpinner.getValueAsInt());

            undoData.oldYvalue.set(yValue);
            undoData.newYvalue.set(yValue);
            undoData.newYvalue.setLow(lowSpinner.getValueAsInt());
            UndoManager.addUndoable(new DualScaledNumericValueUndoable(undoData));

            lowMinSpinner.setValue(lowSpinner.getValueAsInt());
            lowMaxSpinner.setValue(lowSpinner.getValueAsInt());
        });

        onChange(lowMinSpinner, () -> {
            var undoData = unsplitUndoDataTemplate.toBuilder().build();
            undoData.oldXvalue.set(xValue);
            undoData.newXvalue.set(xValue);
            undoData.newXvalue.setLowMin(lowMinSpinner.getValueAsInt());

            undoData.oldYvalue.set(yValue);
            undoData.newYvalue.set(yValue);
            undoData.newYvalue.setLowMin(lowMinSpinner.getValueAsInt());
            UndoManager.addUndoable(new DualScaledNumericValueUndoable(undoData));

            lowSpinner.setValue(lowMinSpinner.getValueAsInt());
        });

        onChange(lowMaxSpinner, () -> {
            var undoData = unsplitUndoDataTemplate.toBuilder().build();
            undoData.oldXvalue.set(xValue);
            undoData.newXvalue.set(xValue);
            undoData.newXvalue.setLowMax(lowMaxSpinner.getValueAsInt());

            undoData.oldYvalue.set(yValue);
            undoData.newYvalue.set(yValue);
            undoData.newYvalue.setLowMax(lowMaxSpinner.getValueAsInt());
            UndoManager.addUndoable(new DualScaledNumericValueUndoable(undoData));

            lowSpinner.setValue(lowMaxSpinner.getValueAsInt());
        });

        onChange(lowCollapseButton, () -> {
            var undoData = unsplitUndoDataTemplate.toBuilder().build();
            undoData.oldXvalue.set(xValue);
            undoData.newXvalue.set(xValue);
            undoData.newXvalue.setLow(lowSpinner.getValueAsInt());

            undoData.oldYvalue.set(yValue);
            undoData.newYvalue.set(yValue);
            undoData.newYvalue.setLow(lowSpinner.getValueAsInt());
            UndoManager.addUndoable(new DualScaledNumericValueUndoable(undoData));

            lowMinSpinner.setValue(lowSpinner.getValueAsInt());
            lowMaxSpinner.setValue(lowSpinner.getValueAsInt());
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

            addDualGraphUpdateAction(xValue, yValue, newTimeline, newScaling, unsplitUndoDataTemplate);
        });

        onChange(highXspinner, () -> {
            var undo = new ScaledNumericValueUndoable(xValue, "change X Scale");
            undo.oldValue.set(xValue);
            undo.newValue.set(xValue);
            undo.newValue.setHigh(highXspinner.getValueAsInt());
            UndoManager.addUndoable(undo);

            highXminSpinner.setValue(highXspinner.getValueAsInt());
            highXmaxSpinner.setValue(highXspinner.getValueAsInt());
        });

        onChange(highXminSpinner, () -> {
            var undo = new ScaledNumericValueUndoable(xValue, "change X Scale");
            undo.oldValue.set(xValue);
            undo.newValue.set(xValue);
            undo.newValue.setHighMin(highXminSpinner.getValueAsInt());
            UndoManager.addUndoable(undo);

            highXspinner.setValue(highXminSpinner.getValueAsInt());
        });

        onChange(highXmaxSpinner, () -> {
            var undo = new ScaledNumericValueUndoable(xValue, "change X Scale");
            undo.oldValue.set(xValue);
            undo.newValue.set(xValue);
            undo.newValue.setHighMax(highXmaxSpinner.getValueAsInt());
            UndoManager.addUndoable(undo);

            highSpinner.setValue(highXmaxSpinner.getValueAsInt());
        });

        onChange(highXcollapseButton, () -> {
            var undo = new ScaledNumericValueUndoable(xValue, "change X Scale");
            undo.oldValue.set(xValue);
            undo.newValue.set(xValue);
            undo.newValue.setHigh(highXspinner.getValueAsInt());
            UndoManager.addUndoable(undo);

            highXmaxSpinner.setValue(highXspinner.getValueAsInt());
            highXmaxSpinner.setValue(highXspinner.getValueAsInt());
        });

        onChange(lowXspinner, () -> {
            var undo = new ScaledNumericValueUndoable(xValue, "change X Scale");
            undo.oldValue.set(xValue);
            undo.newValue.set(xValue);
            undo.newValue.setLow(lowXspinner.getValueAsInt());
            UndoManager.addUndoable(undo);

            lowXminSpinner.setValue(lowXspinner.getValueAsInt());
            lowXmaxSpinner.setValue(lowXspinner.getValueAsInt());
        });

        onChange(lowXminSpinner, () -> {
            var undo = new ScaledNumericValueUndoable(xValue, "change X Scale");
            undo.oldValue.set(xValue);
            undo.newValue.set(xValue);
            undo.newValue.setLowMin(lowXminSpinner.getValueAsInt());
            UndoManager.addUndoable(undo);

            lowXspinner.setValue(lowXminSpinner.getValueAsInt());
        });

        onChange(lowXmaxSpinner, () -> {
            var undo = new ScaledNumericValueUndoable(xValue, "change X Scale");
            undo.oldValue.set(xValue);
            undo.newValue.set(xValue);
            undo.newValue.setLowMax(lowXmaxSpinner.getValueAsInt());
            UndoManager.addUndoable(undo);

            lowSpinner.setValue(lowXmaxSpinner.getValueAsInt());
        });

        onChange(lowXcollapseButton, () -> {
            var undo = new ScaledNumericValueUndoable(xValue, "change X Scale");
            undo.oldValue.set(xValue);
            undo.newValue.set(xValue);
            undo.newValue.setLow(lowXspinner.getValueAsInt());
            UndoManager.addUndoable(undo);

            lowXmaxSpinner.setValue(lowXspinner.getValueAsInt());
            lowXmaxSpinner.setValue(lowXspinner.getValueAsInt());
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

            addGraphUpdateAction(xValue, newTimeline, newScaling, "change X Scale");
        });

        onChange(highYspinner, () -> {
            var undo = new ScaledNumericValueUndoable(yValue, "change Y Scale");
            undo.oldValue.set(yValue);
            undo.newValue.set(yValue);
            undo.newValue.setHigh(highYspinner.getValueAsInt());
            UndoManager.addUndoable(undo);

            highYminSpinner.setValue(highYspinner.getValueAsInt());
            highYmaxSpinner.setValue(highYspinner.getValueAsInt());
        });

        onChange(highYminSpinner, () -> {
            var undo = new ScaledNumericValueUndoable(yValue, "change Y Scale");
            undo.oldValue.set(yValue);
            undo.newValue.set(yValue);
            undo.newValue.setHighMin(highYminSpinner.getValueAsInt());
            UndoManager.addUndoable(undo);

            highYspinner.setValue(highYminSpinner.getValueAsInt());
        });

        onChange(highYmaxSpinner, () -> {
            var undo = new ScaledNumericValueUndoable(yValue, "change Y Scale");
            undo.oldValue.set(yValue);
            undo.newValue.set(yValue);
            undo.newValue.setHighMax(highYmaxSpinner.getValueAsInt());
            UndoManager.addUndoable(undo);

            highSpinner.setValue(highYmaxSpinner.getValueAsInt());
        });

        onChange(highYcollapseButton, () -> {
            var undo = new ScaledNumericValueUndoable(yValue, "change Y Scale");
            undo.oldValue.set(yValue);
            undo.newValue.set(yValue);
            undo.newValue.setHigh(highYspinner.getValueAsInt());
            UndoManager.addUndoable(undo);

            highYmaxSpinner.setValue(highYspinner.getValueAsInt());
            highYmaxSpinner.setValue(highYspinner.getValueAsInt());
        });

        onChange(lowYspinner, () -> {
            var undo = new ScaledNumericValueUndoable(yValue, "change Y Scale");
            undo.oldValue.set(yValue);
            undo.newValue.set(yValue);
            undo.newValue.setLow(lowYspinner.getValueAsInt());
            UndoManager.addUndoable(undo);

            lowYminSpinner.setValue(lowYspinner.getValueAsInt());
            lowYmaxSpinner.setValue(lowYspinner.getValueAsInt());
        });

        onChange(lowYminSpinner, () -> {
            var undo = new ScaledNumericValueUndoable(yValue, "change Y Scale");
            undo.oldValue.set(yValue);
            undo.newValue.set(yValue);
            undo.newValue.setLowMin(lowYminSpinner.getValueAsInt());
            UndoManager.addUndoable(undo);

            lowYspinner.setValue(lowYminSpinner.getValueAsInt());
        });

        onChange(lowYmaxSpinner, () -> {
            var undo = new ScaledNumericValueUndoable(yValue, "change Y Scale");
            undo.oldValue.set(yValue);
            undo.newValue.set(yValue);
            undo.newValue.setLowMax(lowYmaxSpinner.getValueAsInt());
            UndoManager.addUndoable(undo);

            lowSpinner.setValue(lowYmaxSpinner.getValueAsInt());
        });

        onChange(lowYcollapseButton, () -> {
            var undo = new ScaledNumericValueUndoable(yValue, "change Y Scale");
            undo.oldValue.set(yValue);
            undo.newValue.set(yValue);
            undo.newValue.setLow(lowYspinner.getValueAsInt());
            UndoManager.addUndoable(undo);

            lowYmaxSpinner.setValue(lowYspinner.getValueAsInt());
            lowYmaxSpinner.setValue(lowYspinner.getValueAsInt());
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

            addGraphUpdateAction(yValue, newTimeline, newScaling, "change Y scale");
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

            if (expandedType == ExpandedType.EXPANDED_X) {
                addGraphUpdateAction(xValue, newTimeline, newScaling, "change X scale");
            } else if (expandedType == ExpandedType.EXPANDED_Y) {
                addGraphUpdateAction(yValue, newTimeline, newScaling, "change Y scale");
            } else if (expandedType == ExpandedType.EXPANDED_BOTH) {
                addDualGraphUpdateAction(xValue, yValue, newTimeline, newScaling, unsplitUndoDataTemplate);
            }
        });
    }

    private void addGraphUpdateAction(ScaledNumericValue value, float[] newTimeline, float[] newScaling, String description) {
        var oldValue = new ScaledNumericValue();
        oldValue.set(value);

        value.setTimeline(newTimeline);
        value.setScaling(newScaling);

        if (graphUndoAction != null) graphUndoAction.restart();
        else {
            graphUndoAction = new TemporalAction(GRAPH_UNDO_DELAY) {
                @Override
                protected void update(float percent) {
                }

                @Override
                protected void end() {
                    var undo = new ScaledNumericValueUndoable(value, description);
                    undo.oldValue.set(oldValue);
                    undo.newValue.set(value);
                    UndoManager.addUndoable(undo);

                    graphUndoAction = null;
                }
            };
            stage.addAction(graphUndoAction);
        }
    }

    private void addDualGraphUpdateAction(ScaledNumericValue xValue, ScaledNumericValue yValue, float[] newTimeline, float[] newScaling, DualScaledNumericValueUndoableData undoDataTemplate) {
        var oldXvalue = new ScaledNumericValue();
        oldXvalue.set(xValue);

        xValue.setTimeline(newTimeline);
        xValue.setScaling(newScaling);

        var oldYvalue = new ScaledNumericValue();
        oldYvalue.set(yValue);

        yValue.setTimeline(newTimeline);
        yValue.setScaling(newScaling);

        if (graphUndoAction != null) graphUndoAction.restart();
        else {
            graphUndoAction = new TemporalAction(GRAPH_UNDO_DELAY) {
                @Override
                protected void update(float percent) {
                }

                @Override
                protected void end() {
                    var undoData = undoDataTemplate.toBuilder().build();
                    undoData.oldXvalue.set(oldXvalue);
                    undoData.newXvalue.set(xValue);
                    undoData.oldYvalue.set(oldYvalue);
                    undoData.newYvalue.set(yValue);
                    UndoManager.addUndoable(new DualScaledNumericValueUndoable(undoData));

                    graphUndoAction = null;
                }
            };
            stage.addAction(graphUndoAction);
        }
    }
}
