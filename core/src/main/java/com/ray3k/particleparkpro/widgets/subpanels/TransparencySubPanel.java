package com.ray3k.particleparkpro.widgets.subpanels;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.undo.UndoManager;
import com.ray3k.particleparkpro.undo.undoables.ScaledNumericValueUndoable;
import com.ray3k.particleparkpro.undo.undoables.ScaledNumericValueUndoable.ScaledNumericValueUndoableData;
import com.ray3k.particleparkpro.widgets.LineGraph;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.particleparkpro.widgets.ToggleWidget;
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

import static com.ray3k.particleparkpro.Core.*;

public class TransparencySubPanel extends Panel {
    private static final float GRAPH_UNDO_DELAY = .3f;
    private Action graphUndoAction;

    public TransparencySubPanel() {
        final int itemSpacing = 5;

        var value = selectedEmitter.getTransparency();

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
        graph.setNodes(value.getTimeline(), value.getScaling());
        graph.setNodeListener(handListener);
        graphToggleWidget.table1.add(graph);

        var button = new Button(skin, "plus");
        graphToggleWidget.table1.add(button).bottom();
        addHandListener(button);
        addTooltip(button, "Expand to large graph view", Align.top, Align.top, tooltipBottomArrowStyle);

        //Expanded graph view
        graphToggleWidget.table2.defaults().space(itemSpacing);
        var graphExpanded = new LineGraph("Life", lineGraphBigStyle);
        graphExpanded.setNodeListener(handListener);
        graphToggleWidget.table2.add(graphExpanded).grow();

        onChange(button, () -> {
            graphToggleWidget.swap();
            graphExpanded.setNodes(value.getTimeline(), value.getScaling());
        });

        button = new Button(skin, "minus");
        graphToggleWidget.table2.add(button).bottom();
        addHandListener(button);
        addTooltip(button, "Collapse to normal view", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(button, () -> {
            graphToggleWidget.swap();
            graph.setNodes(value.getTimeline(), value.getScaling());
        });

        var undoDataTemplate = ScaledNumericValueUndoableData
            .builder()
            .value(value)
            .description("change Transparency")
            .build();

        onChange(graph, () -> {
            var nodes = graph.getNodes();
            float[] newTimeline = new float[nodes.size];
            float[] newScaling = new float[nodes.size];
            for (int i = 0; i < nodes.size; i++) {
                var node = nodes.get(i);
                newTimeline[i] = node.percentX;
                newScaling[i] = node.percentY;
            }

            addGraphUpdateAction(value, newTimeline, newScaling, undoDataTemplate);
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

            addGraphUpdateAction(value, newTimeline, newScaling, undoDataTemplate);
        });
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
