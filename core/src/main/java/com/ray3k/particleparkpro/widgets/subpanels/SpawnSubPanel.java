package com.ray3k.particleparkpro.widgets.subpanels;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.widgets.LineGraph;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.particleparkpro.widgets.ToggleWidget;
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.widgets.subpanels.SpawnSubPanel.SpawnType.*;

public class SpawnSubPanel extends Panel {
    public enum SpawnType {
        POINT("point"), LINE("line"), SQUARE("square"), ELLIPSE("ellipse");

        String name;
        SpawnType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public SpawnSubPanel() {
        final int spinnerWidth = 70;
        final int itemSpacing = 5;
        final int sectionPadding = 10;

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
        table.add(shapeSelectBox).width(spinnerWidth);
        addHandListener(shapeSelectBox);
        addHandListener(shapeSelectBox.getList());
        addTooltip(shapeSelectBox, "The shape used to spawn particles", Align.top, tooltipBottomArrowStyle);

        //Edges
        graphToggleWidget.table1.row();
        var ellipseToggleWidget = new ToggleWidget();
        graphToggleWidget.table1.add(ellipseToggleWidget);

        ellipseToggleWidget.table2.defaults().space(itemSpacing);
        var checkBox = new CheckBox("Edges", skin);
        ellipseToggleWidget.table2.add(checkBox).colspan(2).left();
        addHandListener(checkBox);
        addTooltip(checkBox, "If true, particles will spawn on the edges of the ellipse", Align.top, tooltipBottomArrowStyle);

        //Side
        ellipseToggleWidget.table2.row();
        table = new Table();
        ellipseToggleWidget.table2.add(table);

        table.defaults().space(itemSpacing);
        label = new Label("Side:", skin);
        table.add(label);

        var selectBox = new SelectBox<>(skin);
        selectBox.setItems("both", "top", "bottom");
        table.add(selectBox).width(spinnerWidth);
        addHandListener(selectBox);
        addHandListener(selectBox.getList());
        addTooltip(selectBox, "The side of the ellipse where particles will spawn", Align.top, tooltipBottomArrowStyle);

        //Shape specific widgets
        graphToggleWidget.table1.row();

        var shapeToggleWidget = new ToggleWidget();
        graphToggleWidget.table1.add(shapeToggleWidget);

        onChange(shapeSelectBox, () -> {
            var selected = shapeSelectBox.getSelected();
            if (selected == ELLIPSE) ellipseToggleWidget.showTable2();
            else ellipseToggleWidget.showTable1();

            if (selected == POINT) shapeToggleWidget.showTable1();
            else shapeToggleWidget.showTable2();
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

        var highToggleWidget = new ToggleWidget();
        table.add(highToggleWidget);

        //High single
        highToggleWidget.table1.defaults().space(itemSpacing);
        var spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highToggleWidget.table1.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The high value for the number of particles emitted per second", Align.top, tooltipBottomArrowStyle);

        var button = new Button(skin, "moveright");
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
        addTooltip(spinner, "The minimum high value for the number of particles emitted per second", Align.top, tooltipBottomArrowStyle);

        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highToggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The maximum high value for the number of particles emitted per second", Align.top, tooltipBottomArrowStyle);

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
        addTooltip(spinner, "The low value for the number of particles emitted per second", Align.top, tooltipBottomArrowStyle);

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
        addTooltip(spinner, "The minimum low value for the number of particles emitted per second", Align.top, tooltipBottomArrowStyle);

        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowToggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The maximum low value for the number of particles emitted per second", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        lowToggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single low value", Align.top, tooltipBottomArrowStyle);
        onChange(button, lowToggleWidget::swap);

        //Graph small
        var graph = new LineGraph("Duration", lineGraphStyle);
        graph.setNodeListener(handListener);
        shapeToggleWidget.table2.add(graph);

        button = new Button(skin, "plus");
        shapeToggleWidget.table2.add(button).bottom();
        addHandListener(button);
        addTooltip(button, "Expand to large graph view", Align.top, tooltipBottomArrowStyle);
        onChange(button, graphToggleWidget::swap);

        //Expanded graph view
        graphToggleWidget.table2.defaults().space(itemSpacing);
        graph = new LineGraph("Duration", lineGraphBigStyle);
        graph.setNodeListener(handListener);
        graphToggleWidget.table2.add(graph).grow();

        button = new Button(skin, "minus");
        graphToggleWidget.table2.add(button).bottom();
        addHandListener(button);
        addTooltip(button, "Collapse to normal view", Align.top, tooltipBottomArrowStyle);
        onChange(button, graphToggleWidget::swap);

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

        highToggleWidget = new ToggleWidget();
        table.add(highToggleWidget);

        //High single
        highToggleWidget.table1.defaults().space(itemSpacing);
        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highToggleWidget.table1.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The high value for the number of particles emitted per second", Align.top, tooltipBottomArrowStyle);

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
        addTooltip(spinner, "The minimum high value for the number of particles emitted per second", Align.top, tooltipBottomArrowStyle);

        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highToggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The maximum high value for the number of particles emitted per second", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        highToggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single high value", Align.top, tooltipBottomArrowStyle);
        onChange(button, highToggleWidget::swap);

        //Low
        table.row();
        label = new Label("Low:", skin);
        table.add(label);

        lowToggleWidget = new ToggleWidget();
        table.add(lowToggleWidget);

        //Low single
        lowToggleWidget.table1.defaults().space(itemSpacing);
        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowToggleWidget.table1.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The low value for the number of particles emitted per second", Align.top, tooltipBottomArrowStyle);

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
        addTooltip(spinner, "The minimum low value for the number of particles emitted per second", Align.top, tooltipBottomArrowStyle);

        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowToggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The maximum low value for the number of particles emitted per second", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        lowToggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single low value", Align.top, tooltipBottomArrowStyle);
        onChange(button, lowToggleWidget::swap);

        //Graph small
        graph = new LineGraph("Life", lineGraphStyle);
        graph.setNodeListener(handListener);
        shapeToggleWidget.table2.add(graph);

        button = new Button(skin, "plus");
        shapeToggleWidget.table2.add(button).bottom();
        addHandListener(button);
        addTooltip(button, "Expand to large graph view", Align.top, tooltipBottomArrowStyle);
        onChange(button, graphToggleWidget::swap);
    }
}
