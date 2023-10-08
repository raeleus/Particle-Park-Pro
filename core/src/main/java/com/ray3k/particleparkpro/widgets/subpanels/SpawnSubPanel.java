package com.ray3k.particleparkpro.widgets.subpanels;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnEllipseSide;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShape;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.undo.UndoManager;
import com.ray3k.particleparkpro.undo.Undoable;
import com.ray3k.particleparkpro.undo.undoables.ScaledNumericValueUndoable;
import com.ray3k.particleparkpro.undo.undoables.ScaledNumericValueUndoable.ScaledNumericValueUndoableData;
import com.ray3k.particleparkpro.undo.undoables.SpawnTypeUndoable;
import com.ray3k.particleparkpro.widgets.LineGraph;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.particleparkpro.widgets.ToggleWidget;
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.widgets.subpanels.SpawnSubPanel.SpawnType.*;

public class SpawnSubPanel extends Panel {
    private SpawnType spawnType;
    private ToggleWidget ellipseToggleWidget;
    private ToggleWidget shapeToggleWidget;
    private static final float GRAPH_UNDO_DELAY = .3f;
    private Action graphUndoAction;

    public enum SpawnType {
        POINT("point", SpawnShape.point), LINE("line", SpawnShape.line), SQUARE("square", SpawnShape.square), ELLIPSE("ellipse", SpawnShape.ellipse);

        String name;
        public SpawnShape spawnShape;

        SpawnType(String name, SpawnShape spawnShape) {
            this.name = name;
            this.spawnShape = spawnShape;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private enum ExpandedType {
        EXPANDED_WIDTH, EXPANDED_HEIGHT
    }
    private ExpandedType expandedType;

    private SpawnType shapeToType(SpawnShape spawnShape) {
        if (spawnShape == SpawnShape.point) return POINT;
        else if (spawnShape == SpawnShape.line) return LINE;
        else if (spawnShape == SpawnShape.square) return SQUARE;
        else return ELLIPSE;
    }

    public SpawnSubPanel() {
        populate();
    }

    public void populate() {
        tabTable.clearChildren();
        bodyTable.clearChildren();

        final int spinnerWidth = 70;
        final int itemSpacing = 5;
        final int sectionPadding = 10;

        var value = selectedEmitter.getSpawnShape();
        var valueWidth = selectedEmitter.getSpawnWidth();
        var valueHeight = selectedEmitter.getSpawnHeight();

        setTouchable(Touchable.enabled);

        tabTable.padRight(7);
        tabTable.left();
        var label = new Label("Spawn", skin, "header");
        tabTable.add(label).space(3);

        var graphToggleWidget = new ToggleWidget();
        bodyTable.add(graphToggleWidget).grow();

        //Value
        graphToggleWidget.table1.defaults().space(itemSpacing).left();
        graphToggleWidget.table1.left().top();
        var table = new Table();
        graphToggleWidget.table1.add(table);

        //Shape
        table.defaults().space(itemSpacing);
        label = new Label("Shape:", skin);
        table.add(label);

        var shapeSelectBox = new SelectBox<SpawnType>(skin);
        shapeSelectBox.setItems(POINT, LINE, SQUARE, ELLIPSE);
        spawnType = shapeToType(value.getShape());
        shapeSelectBox.setSelected(spawnType);
        table.add(shapeSelectBox).width(spinnerWidth);
        addHandListener(shapeSelectBox);
        addHandListener(shapeSelectBox.getList());
        addTooltip(shapeSelectBox, "The shape used to spawn particles", Align.top, Align.top, tooltipBottomArrowStyle);

        //Edges
        graphToggleWidget.table1.row();
        ellipseToggleWidget = new ToggleWidget();
        graphToggleWidget.table1.add(ellipseToggleWidget);

        ellipseToggleWidget.table2.defaults().space(itemSpacing);
        var checkBox = new CheckBox("Edges", skin);
        checkBox.setChecked(value.isEdges());
        ellipseToggleWidget.table2.add(checkBox).colspan(2).left();
        addHandListener(checkBox);
        addTooltip(checkBox, "If true, particles will spawn on the edges of the ellipse", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(checkBox, () -> {
            value.setEdges(checkBox.isChecked());
        });

        //Side
        ellipseToggleWidget.table2.row();
        table = new Table();
        ellipseToggleWidget.table2.add(table);

        table.defaults().space(itemSpacing);
        label = new Label("Side:", skin);
        table.add(label);

        var selectBox = new SelectBox<String>(skin);
        selectBox.setItems("both", "top", "bottom");
        selectBox.setSelectedIndex(value.getSide() == SpawnEllipseSide.both ? 0 : value.getSide() == SpawnEllipseSide.top ? 1 : 2);
        table.add(selectBox).width(spinnerWidth);
        addHandListener(selectBox);
        addHandListener(selectBox.getList());
        addTooltip(selectBox, "The side of the ellipse where particles will spawn", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(selectBox, () -> {
            switch (selectBox.getSelectedIndex()) {
                case 0:
                    value.setSide(SpawnEllipseSide.both);
                    break;
                case 1:
                    value.setSide(SpawnEllipseSide.top);
                    break;
                default:
                    value.setSide(SpawnEllipseSide.bottom);
                    break;
            }
        });

        //Shape specific widgets
        graphToggleWidget.table1.row();

        shapeToggleWidget = new ToggleWidget();
        graphToggleWidget.table1.add(shapeToggleWidget);

        onChange(shapeSelectBox, () -> {
            var spawnTypeOld = spawnType;
            spawnType = shapeSelectBox.getSelected();
            UndoManager.addUndoable(new SpawnTypeUndoable(value, spawnType, spawnTypeOld, "change Spawn Type"));

            updateShownTable();
        });

        //Width
        shapeToggleWidget.table2.defaults().space(itemSpacing);
        label = new Label("Width", skin, "header");
        shapeToggleWidget.table2.add(label).left().padTop(sectionPadding);

        //High
        shapeToggleWidget.table2.row();
        table = new Table();
        shapeToggleWidget.table2.add(table).top();
        table.defaults().space(itemSpacing).left();
        label = new Label("High:", skin);
        table.add(label);

        var widthHighToggleWidget = new ToggleWidget();
        table.add(widthHighToggleWidget);

        //High single
        widthHighToggleWidget.table1.defaults().space(itemSpacing);
        var widthHighSpinner = new Spinner(valueWidth.getHighMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        widthHighSpinner.setProgrammaticChangeEvents(false);
        widthHighToggleWidget.table1.add(widthHighSpinner).width(spinnerWidth);
        addIbeamListener(widthHighSpinner.getTextField());
        addHandListener(widthHighSpinner.getButtonPlus());
        addHandListener(widthHighSpinner.getButtonMinus());
        addTooltip(widthHighSpinner, "The high value for the number of particles emitted per second", Align.top, Align.top, tooltipBottomArrowStyle);

        var widthHighExpandButton = new Button(skin, "moveright");
        widthHighToggleWidget.table1.add(widthHighExpandButton);
        addHandListener(widthHighExpandButton);
        addTooltip(widthHighExpandButton, "Expand to define a range for the high value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(widthHighExpandButton, widthHighToggleWidget::swap);

        //High range
        widthHighToggleWidget.table2.defaults().space(itemSpacing);
        var widthHighMinSpinner = new Spinner(valueWidth.getHighMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        widthHighMinSpinner.setProgrammaticChangeEvents(false);
        widthHighToggleWidget.table2.add(widthHighMinSpinner).width(spinnerWidth);
        addIbeamListener(widthHighMinSpinner.getTextField());
        addHandListener(widthHighMinSpinner.getButtonPlus());
        addHandListener(widthHighMinSpinner.getButtonMinus());
        addTooltip(widthHighMinSpinner, "The minimum high value for the number of particles emitted per second", Align.top, Align.top, tooltipBottomArrowStyle);

        var widthHighMaxSpinner = new Spinner(valueWidth.getHighMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        widthHighMaxSpinner.setProgrammaticChangeEvents(false);
        widthHighToggleWidget.table2.add(widthHighMaxSpinner).width(spinnerWidth);
        addIbeamListener(widthHighMaxSpinner.getTextField());
        addHandListener(widthHighMaxSpinner.getButtonPlus());
        addHandListener(widthHighMaxSpinner.getButtonMinus());
        addTooltip(widthHighMaxSpinner, "The maximum high value for the number of particles emitted per second", Align.top, Align.top, tooltipBottomArrowStyle);

        var widthHighCollapseButton = new Button(skin, "moveleft");
        widthHighToggleWidget.table2.add(widthHighCollapseButton);
        addHandListener(widthHighCollapseButton);
        addTooltip(widthHighCollapseButton, "Collapse to define a single high value", Align.top, Align.top, tooltipBottomArrowStyle);

        if (!MathUtils.isEqual(valueWidth.getHighMin(), valueWidth.getHighMax())) widthHighToggleWidget.swap();

        //Low
        table.row();
        label = new Label("Low:", skin);
        table.add(label);

        var widthLowToggleWidget = new ToggleWidget();
        table.add(widthLowToggleWidget);

        //Low single
        widthLowToggleWidget.table1.defaults().space(itemSpacing);
        var widthLowSpinner = new Spinner(valueWidth.getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        widthLowSpinner.setProgrammaticChangeEvents(false);
        widthLowToggleWidget.table1.add(widthLowSpinner).width(spinnerWidth);
        addIbeamListener(widthLowSpinner.getTextField());
        addHandListener(widthLowSpinner.getButtonPlus());
        addHandListener(widthLowSpinner.getButtonMinus());
        addTooltip(widthLowSpinner, "The low value for the number of particles emitted per second", Align.top, Align.top, tooltipBottomArrowStyle);

        var widthLowExpandButton = new Button(skin, "moveright");
        widthLowToggleWidget.table1.add(widthLowExpandButton);
        addHandListener(widthLowExpandButton);
        addTooltip(widthLowExpandButton, "Expand to define a range for the low value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(widthLowExpandButton, widthLowToggleWidget::swap);

        //Low range
        widthLowToggleWidget.table2.defaults().space(itemSpacing);
        var widthLowMinSpinner = new Spinner(valueWidth.getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        widthLowMinSpinner.setProgrammaticChangeEvents(false);
        widthLowToggleWidget.table2.add(widthLowMinSpinner).width(spinnerWidth);
        addIbeamListener(widthLowMinSpinner.getTextField());
        addHandListener(widthLowMinSpinner.getButtonPlus());
        addHandListener(widthLowMinSpinner.getButtonMinus());
        addTooltip(widthLowMinSpinner, "The minimum low value for the number of particles emitted per second", Align.top, Align.top, tooltipBottomArrowStyle);

        var widthLowMaxSpinner = new Spinner(valueWidth.getLowMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        widthLowMaxSpinner.setProgrammaticChangeEvents(false);
        widthLowToggleWidget.table2.add(widthLowMaxSpinner).width(spinnerWidth);
        addIbeamListener(widthLowMaxSpinner.getTextField());
        addHandListener(widthLowMaxSpinner.getButtonPlus());
        addHandListener(widthLowMaxSpinner.getButtonMinus());
        addTooltip(widthLowMaxSpinner, "The maximum low value for the number of particles emitted per second", Align.top, Align.top, tooltipBottomArrowStyle);

        var widthLowCollapseButton = new Button(skin, "moveleft");
        widthLowToggleWidget.table2.add(widthLowCollapseButton);
        addHandListener(widthLowCollapseButton);
        addTooltip(widthLowCollapseButton, "Collapse to define a single low value", Align.top, Align.top, tooltipBottomArrowStyle);

        if (!MathUtils.isEqual(valueWidth.getLowMin(), valueWidth.getLowMax())) widthLowToggleWidget.swap();

        //Graph small
        var graphWidth = new LineGraph("Duration", lineGraphStyle);
        graphWidth.setNodes(valueWidth.getTimeline(), valueWidth.getScaling());
        graphWidth.setNodeListener(handListener);
        shapeToggleWidget.table2.add(graphWidth);

        var widthExpandGraphButton = new Button(skin, "plus");
        shapeToggleWidget.table2.add(widthExpandGraphButton).bottom();
        addHandListener(widthExpandGraphButton);
        addTooltip(widthExpandGraphButton, "Expand to large graph view", Align.top, Align.top, tooltipBottomArrowStyle);

        //Expanded graph view
        graphToggleWidget.table2.defaults().space(itemSpacing);
        var graphExpanded = new LineGraph("Duration", lineGraphBigStyle);
        graphExpanded.setNodeListener(handListener);
        graphToggleWidget.table2.add(graphExpanded).grow();

        onChange(widthExpandGraphButton, () -> {
            graphToggleWidget.swap();
            graphExpanded.setNodes(valueWidth.getTimeline(), valueWidth.getScaling());
            expandedType = ExpandedType.EXPANDED_WIDTH;
        });

        var collapseExpandedGraphButton = new Button(skin, "minus");
        graphToggleWidget.table2.add(collapseExpandedGraphButton).bottom();
        addHandListener(collapseExpandedGraphButton);
        addTooltip(collapseExpandedGraphButton, "Collapse to normal view", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(collapseExpandedGraphButton, graphToggleWidget::swap);

        //Height
        shapeToggleWidget.table2.row();
        label = new Label("Height", skin, "header");
        shapeToggleWidget.table2.add(label).left().padTop(sectionPadding);

        //High
        shapeToggleWidget.table2.row();
        table = new Table();
        shapeToggleWidget.table2.add(table).top();
        table.defaults().space(itemSpacing).left();
        label = new Label("High:", skin);
        table.add(label);

        var heightHighToggleWidget = new ToggleWidget();
        table.add(heightHighToggleWidget);

        //High single
        heightHighToggleWidget.table1.defaults().space(itemSpacing);
        var heightHighSpinner = new Spinner(valueHeight.getHighMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        heightHighSpinner.setProgrammaticChangeEvents(false);
        heightHighToggleWidget.table1.add(heightHighSpinner).width(spinnerWidth);
        addIbeamListener(heightHighSpinner.getTextField());
        addHandListener(heightHighSpinner.getButtonPlus());
        addHandListener(heightHighSpinner.getButtonMinus());
        addTooltip(heightHighSpinner, "The high value for the number of particles emitted per second", Align.top, Align.top, tooltipBottomArrowStyle);

        var heightHighExpandButton = new Button(skin, "moveright");
        heightHighToggleWidget.table1.add(heightHighExpandButton);
        addHandListener(heightHighExpandButton);
        addTooltip(heightHighExpandButton, "Expand to define a range for the high value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(heightHighExpandButton, heightHighToggleWidget::swap);

        //High range
        heightHighToggleWidget.table2.defaults().space(itemSpacing);
        var heightHighMinSpinner = new Spinner(valueHeight.getHighMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        heightHighMinSpinner.setProgrammaticChangeEvents(false);
        heightHighToggleWidget.table2.add(heightHighMinSpinner).width(spinnerWidth);
        addIbeamListener(heightHighMinSpinner.getTextField());
        addHandListener(heightHighMinSpinner.getButtonPlus());
        addHandListener(heightHighMinSpinner.getButtonMinus());
        addTooltip(heightHighMinSpinner, "The minimum high value for the number of particles emitted per second", Align.top, Align.top, tooltipBottomArrowStyle);

        var heightHighMaxSpinner = new Spinner(valueHeight.getHighMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        heightHighMaxSpinner.setProgrammaticChangeEvents(false);
        heightHighToggleWidget.table2.add(heightHighMaxSpinner).width(spinnerWidth);
        addIbeamListener(heightHighMaxSpinner.getTextField());
        addHandListener(heightHighMaxSpinner.getButtonPlus());
        addHandListener(heightHighMaxSpinner.getButtonMinus());
        addTooltip(heightHighMaxSpinner, "The maximum high value for the number of particles emitted per second", Align.top, Align.top, tooltipBottomArrowStyle);

        var heightHighCollapseButton = new Button(skin, "moveleft");
        heightHighToggleWidget.table2.add(heightHighCollapseButton);
        addHandListener(heightHighCollapseButton);
        addTooltip(heightHighCollapseButton, "Collapse to define a single high value", Align.top, Align.top, tooltipBottomArrowStyle);

        if (!MathUtils.isEqual(valueHeight.getHighMin(), valueHeight.getHighMax())) heightHighToggleWidget.swap();

        //Low
        table.row();
        label = new Label("Low:", skin);
        table.add(label);

        var heightLowToggleWidget = new ToggleWidget();
        table.add(heightLowToggleWidget);

        //Low single
        heightLowToggleWidget.table1.defaults().space(itemSpacing);
        var heightLowSpinner = new Spinner(valueHeight.getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        heightLowSpinner.setProgrammaticChangeEvents(false);
        heightLowToggleWidget.table1.add(heightLowSpinner).width(spinnerWidth);
        addIbeamListener(heightLowSpinner.getTextField());
        addHandListener(heightLowSpinner.getButtonPlus());
        addHandListener(heightLowSpinner.getButtonMinus());
        addTooltip(heightLowSpinner, "The low value for the number of particles emitted per second", Align.top, Align.top, tooltipBottomArrowStyle);

        var heightLowExpandButton = new Button(skin, "moveright");
        heightLowToggleWidget.table1.add(heightLowExpandButton);
        addHandListener(heightLowExpandButton);
        addTooltip(heightLowExpandButton, "Expand to define a range for the low value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(heightLowExpandButton, heightLowToggleWidget::swap);

        //Low range
        heightLowToggleWidget.table2.defaults().space(itemSpacing);
        var heightLowMinSpinner = new Spinner(valueHeight.getLowMin(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        heightLowMinSpinner.setProgrammaticChangeEvents(false);
        heightLowToggleWidget.table2.add(heightLowMinSpinner).width(spinnerWidth);
        addIbeamListener(heightLowMinSpinner.getTextField());
        addHandListener(heightLowMinSpinner.getButtonPlus());
        addHandListener(heightLowMinSpinner.getButtonMinus());
        addTooltip(heightLowMinSpinner, "The minimum low value for the number of particles emitted per second", Align.top, Align.top, tooltipBottomArrowStyle);

        var heightLowMaxSpinner = new Spinner(valueHeight.getLowMax(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        heightLowMaxSpinner.setProgrammaticChangeEvents(false);
        heightLowToggleWidget.table2.add(heightLowMaxSpinner).width(spinnerWidth);
        addIbeamListener(heightLowMaxSpinner.getTextField());
        addHandListener(heightLowMaxSpinner.getButtonPlus());
        addHandListener(heightLowMaxSpinner.getButtonMinus());
        addTooltip(heightLowMaxSpinner, "The maximum low value for the number of particles emitted per second", Align.top, Align.top, tooltipBottomArrowStyle);

        var heightLowCollapseButton = new Button(skin, "moveleft");
        heightLowToggleWidget.table2.add(heightLowCollapseButton);
        addHandListener(heightLowCollapseButton);
        addTooltip(heightLowCollapseButton, "Collapse to define a single low value", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(heightLowCollapseButton, heightLowToggleWidget::swap);

        if (!MathUtils.isEqual(valueHeight.getLowMin(), valueHeight.getLowMax())) heightLowToggleWidget.swap();

        //Graph small
        var graphHeight = new LineGraph("Life", lineGraphStyle);
        graphHeight.setNodes(valueHeight.getTimeline(), valueHeight.getScaling());
        graphHeight.setNodeListener(handListener);
        shapeToggleWidget.table2.add(graphHeight);

        var heightExpandGraphButton = new Button(skin, "plus");
        shapeToggleWidget.table2.add(heightExpandGraphButton).bottom();
        addHandListener(heightExpandGraphButton);
        addTooltip(heightExpandGraphButton, "Expand to large graph view", Align.top, Align.top, tooltipBottomArrowStyle);

        onChange(heightExpandGraphButton, () -> {
            graphToggleWidget.swap();
            graphExpanded.setNodes(valueHeight.getTimeline(), valueHeight.getScaling());
            expandedType = ExpandedType.EXPANDED_HEIGHT;
        });

        updateShownTable();

        var widthUndoDataTemplate = ScaledNumericValueUndoableData
            .builder()
            .value(valueWidth)
            .description("change Shape Width")
            .build();

        onChange(widthHighSpinner, () -> {
            var undoData = widthUndoDataTemplate.toBuilder().build();
            undoData.oldValue.set(valueWidth);
            undoData.newValue.set(valueWidth);
            undoData.newValue.setHigh(widthHighSpinner.getValueAsInt());
            UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

            widthHighMinSpinner.setValue(widthHighSpinner.getValueAsInt());
            widthHighMaxSpinner.setValue(widthHighSpinner.getValueAsInt());
        });

        onChange(widthHighMinSpinner, () -> {
            var undoData = widthUndoDataTemplate.toBuilder().build();
            undoData.oldValue.set(valueWidth);
            undoData.newValue.set(valueWidth);
            undoData.newValue.setHighMin(widthHighMinSpinner.getValueAsInt());
            UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

            widthHighSpinner.setValue(widthHighMinSpinner.getValueAsInt());
        });

        onChange(widthHighMaxSpinner, () -> {
            var undoData = widthUndoDataTemplate.toBuilder().build();
            undoData.oldValue.set(valueWidth);
            undoData.newValue.set(valueWidth);
            undoData.newValue.setHighMax(widthHighMaxSpinner.getValueAsInt());
            UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

            widthHighSpinner.setValue(widthHighMaxSpinner.getValueAsInt());
        });

        onChange(widthHighCollapseButton, () -> {
            var undoData = widthUndoDataTemplate.toBuilder().build();
            undoData.oldValue.set(valueWidth);
            undoData.newValue.set(valueWidth);
            undoData.newValue.setHigh(widthHighSpinner.getValueAsInt());
            UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

            widthHighMinSpinner.setValue(widthHighSpinner.getValueAsInt());
            widthHighMaxSpinner.setValue(widthHighSpinner.getValueAsInt());
        });

        onChange(widthLowSpinner, () -> {
            var undoData = widthUndoDataTemplate.toBuilder().build();
            undoData.oldValue.set(valueWidth);
            undoData.newValue.set(valueWidth);
            undoData.newValue.setLow(widthLowSpinner.getValueAsInt());
            UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

            widthLowMinSpinner.setValue(widthLowSpinner.getValueAsInt());
            widthLowMaxSpinner.setValue(widthLowSpinner.getValueAsInt());
        });

        onChange(widthLowMinSpinner, () -> {
            var undoData = widthUndoDataTemplate.toBuilder().build();
            undoData.oldValue.set(valueWidth);
            undoData.newValue.set(valueWidth);
            undoData.newValue.setLowMin(widthLowMinSpinner.getValueAsInt());
            UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

            widthLowSpinner.setValue(widthLowMinSpinner.getValueAsInt());
        });

        onChange(widthLowMaxSpinner, () -> {
            var undoData = widthUndoDataTemplate.toBuilder().build();
            undoData.oldValue.set(valueWidth);
            undoData.newValue.set(valueWidth);
            undoData.newValue.setLowMax(widthLowMaxSpinner.getValueAsInt());
            UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

            widthLowSpinner.setValue(widthLowMaxSpinner.getValueAsInt());
        });

        onChange(widthLowCollapseButton, () -> {
            var undoData = widthUndoDataTemplate.toBuilder().build();
            undoData.oldValue.set(valueWidth);
            undoData.newValue.set(valueWidth);
            undoData.newValue.setLow(widthLowSpinner.getValueAsInt());
            UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

            widthLowMinSpinner.setValue(widthLowSpinner.getValueAsInt());
            widthLowMaxSpinner.setValue(widthLowSpinner.getValueAsInt());
        });

        onChange(graphWidth, () -> {
            var nodes = graphWidth.getNodes();
            float[] newTimeline = new float[nodes.size];
            float[] newScaling = new float[nodes.size];
            for (int i = 0; i < nodes.size; i++) {
                var node = nodes.get(i);
                newTimeline[i] = node.percentX;
                newScaling[i] = node.percentY;
            }

            addGraphUpdateAction(valueWidth, newTimeline, newScaling, widthUndoDataTemplate);
        });

        var heightUndoDataTemplate = ScaledNumericValueUndoableData
            .builder()
            .value(valueHeight)
            .description("change Shape Height")
            .build();

        onChange(heightHighSpinner, () -> {
            var undoData = heightUndoDataTemplate.toBuilder().build();
            undoData.oldValue.set(valueHeight);
            undoData.newValue.set(valueHeight);
            undoData.newValue.setHigh(heightHighSpinner.getValueAsInt());
            UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

            heightHighMinSpinner.setValue(heightHighSpinner.getValueAsInt());
            heightHighMaxSpinner.setValue(heightHighSpinner.getValueAsInt());
        });

        onChange(heightHighMinSpinner, () -> {
            var undoData = heightUndoDataTemplate.toBuilder().build();
            undoData.oldValue.set(valueHeight);
            undoData.newValue.set(valueHeight);
            undoData.newValue.setHighMin(heightHighMinSpinner.getValueAsInt());
            UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

            heightHighSpinner.setValue(heightHighMinSpinner.getValueAsInt());
        });

        onChange(heightHighMaxSpinner, () -> {
            var undoData = heightUndoDataTemplate.toBuilder().build();
            undoData.oldValue.set(valueHeight);
            undoData.newValue.set(valueHeight);
            undoData.newValue.setHighMax(heightHighMaxSpinner.getValueAsInt());
            UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

            heightHighSpinner.setValue(heightHighMaxSpinner.getValueAsInt());
        });

        onChange(heightHighCollapseButton, () -> {
            var undoData = heightUndoDataTemplate.toBuilder().build();
            undoData.oldValue.set(valueHeight);
            undoData.newValue.set(valueHeight);
            undoData.newValue.setHigh(heightHighSpinner.getValueAsInt());
            UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

            heightHighMinSpinner.setValue(heightHighSpinner.getValueAsInt());
            heightHighMaxSpinner.setValue(heightHighSpinner.getValueAsInt());
        });

        onChange(heightLowSpinner, () -> {
            var undoData = heightUndoDataTemplate.toBuilder().build();
            undoData.oldValue.set(valueHeight);
            undoData.newValue.set(valueHeight);
            undoData.newValue.setLow(heightLowSpinner.getValueAsInt());
            UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

            heightLowMinSpinner.setValue(heightLowSpinner.getValueAsInt());
            heightLowMaxSpinner.setValue(heightLowSpinner.getValueAsInt());
        });

        onChange(heightLowMinSpinner, () -> {
            var undoData = heightUndoDataTemplate.toBuilder().build();
            undoData.oldValue.set(valueHeight);
            undoData.newValue.set(valueHeight);
            undoData.newValue.setLowMin(heightLowMinSpinner.getValueAsInt());
            UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

            heightLowSpinner.setValue(heightLowMinSpinner.getValueAsInt());
        });

        onChange(heightLowMaxSpinner, () -> {
            var undoData = heightUndoDataTemplate.toBuilder().build();
            undoData.oldValue.set(valueHeight);
            undoData.newValue.set(valueHeight);
            undoData.newValue.setLowMax(heightLowMaxSpinner.getValueAsInt());
            UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

            heightLowSpinner.setValue(heightLowMaxSpinner.getValueAsInt());
        });

        onChange(heightLowCollapseButton, () -> {
            var undoData = heightUndoDataTemplate.toBuilder().build();
            undoData.oldValue.set(valueHeight);
            undoData.newValue.set(valueHeight);
            undoData.newValue.setLow(heightLowSpinner.getValueAsInt());
            UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

            heightLowMinSpinner.setValue(heightLowSpinner.getValueAsInt());
            heightLowMaxSpinner.setValue(heightLowSpinner.getValueAsInt());
        });

        onChange(graphHeight, () -> {
            var nodes = graphHeight.getNodes();
            float[] newTimeline = new float[nodes.size];
            float[] newScaling = new float[nodes.size];
            for (int i = 0; i < nodes.size; i++) {
                var node = nodes.get(i);
                newTimeline[i] = node.percentX;
                newScaling[i] = node.percentY;
            }

            addGraphUpdateAction(valueHeight, newTimeline, newScaling, heightUndoDataTemplate);
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

            if (expandedType == ExpandedType.EXPANDED_WIDTH) {
                addGraphUpdateAction(valueWidth, newTimeline, newScaling, widthUndoDataTemplate);
            } else {
                addGraphUpdateAction(valueHeight, newTimeline, newScaling, heightUndoDataTemplate);
            }
        });
    }

    private void updateShownTable() {
        if (spawnType == ELLIPSE) ellipseToggleWidget.showTable2();
        else ellipseToggleWidget.showTable1();

        if (spawnType == POINT) shapeToggleWidget.showTable1();
        else shapeToggleWidget.showTable2();
    }

    private void addGraphUpdateAction(ScaledNumericValue value, float[] newTimeline, float[] newScaling, ScaledNumericValueUndoableData undoDataTemplate) {
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
                    var undoData = undoDataTemplate.toBuilder().build();
                    undoData.oldValue.set(oldValue);
                    undoData.newValue.set(value);
                    UndoManager.addUndoable(new ScaledNumericValueUndoable(undoData));

                    graphUndoAction = null;
                }
            };
            stage.addAction(graphUndoAction);
        }
    }
}
