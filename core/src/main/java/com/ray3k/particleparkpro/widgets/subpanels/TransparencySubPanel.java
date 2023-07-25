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

public class TransparencySubPanel extends Panel {
    public TransparencySubPanel() {
        final int itemSpacing = 5;

        setTouchable(Touchable.enabled);

        tabTable.padRight(7);
        tabTable.left();
        var label = new Label("Transparency", skin, "header");
        tabTable.add(label);

        var graphToggleWidget = new ToggleWidget();
        bodyTable.add(graphToggleWidget).grow();

        //Normal view
        graphToggleWidget.table1.defaults().space(itemSpacing);
        graphToggleWidget.table1.left();

        //Graph small
        var graph = new LineGraph("Life", lineGraphStyle);
        graph.setNodeListener(handListener);
        graphToggleWidget.table1.add(graph);

        var button = new Button(skin, "plus");
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
