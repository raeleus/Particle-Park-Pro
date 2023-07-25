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

public class SizeSubPanel extends Panel {
    public SizeSubPanel() {
        final int spinnerWidth = 70;
        final int itemSpacing = 5;
        final int sectionPadding = 10;

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

        onChange(splitXYcheckBox, splitToggleWidget::swap);

        //Joined
        //Relative
        splitToggleWidget.table1.defaults().space(itemSpacing);
        var relativeCheckBox = new CheckBox("Relative", skin);
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
        var spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highToggleWidget.table1.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The high value for the particle size in world units.", Align.top, tooltipBottomArrowStyle);

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
        addTooltip(spinner, "The minimum high value for the particle size in world units.", Align.top, tooltipBottomArrowStyle);

        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highToggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The maximum high value for the particle size in world units.", Align.top, tooltipBottomArrowStyle);

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
        addTooltip(spinner, "The low value for the particle size in world units.", Align.top, tooltipBottomArrowStyle);

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
        addTooltip(spinner, "The minimum low value for the particle size in world units.", Align.top, tooltipBottomArrowStyle);

        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowToggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The maximum low value for the particle size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        lowToggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single low value", Align.top, tooltipBottomArrowStyle);
        onChange(button, lowToggleWidget::swap);

        //Graph small
        var graph = new LineGraph("Life", lineGraphStyle);
        graph.setNodeListener(handListener);
        splitToggleWidget.table1.add(graph);

        button = new Button(skin, "plus");
        splitToggleWidget.table1.add(button).bottom();
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
        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highXtoggleWidget.table1.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The high value for the particle X size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveright");
        highXtoggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the high value", Align.top, tooltipBottomArrowStyle);
        onChange(button, highXtoggleWidget::swap);

        //High range
        highXtoggleWidget.table2.defaults().space(itemSpacing);
        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highXtoggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The minimum high value for the particle X size in world units.", Align.top, tooltipBottomArrowStyle);

        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highXtoggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The maximum high value for the particle X size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        highXtoggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single high value", Align.top, tooltipBottomArrowStyle);
        onChange(button, highXtoggleWidget::swap);

        //Low
        table.row();
        label = new Label("Low:", skin);
        table.add(label);

        var lowXtoggleWidget = new ToggleWidget();
        table.add(lowXtoggleWidget);

        //Low single
        lowXtoggleWidget.table1.defaults().space(itemSpacing);
        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowXtoggleWidget.table1.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The low value for the particle X size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveright");
        lowXtoggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the low value", Align.top, tooltipBottomArrowStyle);
        onChange(button, lowXtoggleWidget::swap);

        //Low range
        lowXtoggleWidget.table2.defaults().space(itemSpacing);
        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowXtoggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The minimum low value for the particle X size in world units.", Align.top, tooltipBottomArrowStyle);

        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowXtoggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The maximum low value for the particle X size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        lowXtoggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single low value", Align.top, tooltipBottomArrowStyle);
        onChange(button, lowXtoggleWidget::swap);

        //Graph small
        graph = new LineGraph("Life", lineGraphStyle);
        graph.setNodeListener(handListener);
        splitToggleWidget.table2.add(graph);

        button = new Button(skin, "plus");
        splitToggleWidget.table2.add(button).bottom();
        addHandListener(button);
        addTooltip(button, "Expand to large graph view", Align.top, tooltipBottomArrowStyle);
        onChange(button, graphToggleWidget::swap);

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
        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highYtoggleWidget.table1.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The high value for the particle Y size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveright");
        highYtoggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the high value", Align.top, tooltipBottomArrowStyle);
        onChange(button, highYtoggleWidget::swap);

        //High range
        highYtoggleWidget.table2.defaults().space(itemSpacing);
        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highYtoggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The minimum high value for the particle Y size in world units.", Align.top, tooltipBottomArrowStyle);

        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        highYtoggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The maximum high value for the particle Y size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        highYtoggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single high value", Align.top, tooltipBottomArrowStyle);
        onChange(button, highYtoggleWidget::swap);

        //Low
        table.row();
        label = new Label("Low:", skin);
        table.add(label);

        var lowYtoggleWidget = new ToggleWidget();
        table.add(lowYtoggleWidget);

        //Low single
        lowYtoggleWidget.table1.defaults().space(itemSpacing);
        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowYtoggleWidget.table1.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The low value for the particle Y size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveright");
        lowYtoggleWidget.table1.add(button);
        addHandListener(button);
        addTooltip(button, "Expand to define a range for the low value", Align.top, tooltipBottomArrowStyle);
        onChange(button, lowYtoggleWidget::swap);

        //Low range
        lowYtoggleWidget.table2.defaults().space(itemSpacing);
        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowYtoggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The minimum low value for the particle Y size in world units.", Align.top, tooltipBottomArrowStyle);

        spinner = new Spinner(250, 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        lowYtoggleWidget.table2.add(spinner).width(spinnerWidth);
        addIbeamListener(spinner.getTextField());
        addHandListener(spinner.getButtonPlus());
        addHandListener(spinner.getButtonMinus());
        addTooltip(spinner, "The maximum low value for the particle Y size in world units.", Align.top, tooltipBottomArrowStyle);

        button = new Button(skin, "moveleft");
        lowYtoggleWidget.table2.add(button);
        addHandListener(button);
        addTooltip(button, "Collapse to define a single low value", Align.top, tooltipBottomArrowStyle);
        onChange(button, lowYtoggleWidget::swap);

        //Graph small
        graph = new LineGraph("Life", lineGraphStyle);
        graph.setNodeListener(handListener);
        splitToggleWidget.table2.add(graph);

        button = new Button(skin, "plus");
        splitToggleWidget.table2.add(button).bottom();
        addHandListener(button);
        addTooltip(button, "Expand to large graph view", Align.top, tooltipBottomArrowStyle);
        onChange(button, graphToggleWidget::swap);
    }
}
