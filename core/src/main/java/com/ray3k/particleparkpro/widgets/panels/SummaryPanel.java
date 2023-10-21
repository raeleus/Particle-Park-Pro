package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.particleparkpro.widgets.poptables.PopAddProperty;

import static com.ray3k.particleparkpro.Core.*;

public class SummaryPanel extends Panel {
    private Table scrollTable;
    public static SummaryPanel summaryPanel;
    private ScrollPane scrollPane;
    private static final Vector2 temp = new Vector2();

    public SummaryPanel() {
        summaryPanel = this;
        setTouchable(Touchable.enabled);

        var label = new Label("Summary", skin, "header");
        tabTable.add(label);

        bodyTable.defaults().space(5);
        scrollTable = new Table();
        scrollTable.top();
        scrollPane = new ScrollPane(scrollTable, skin, "emitter-properties");
        scrollPane.setFlickScroll(false);
        bodyTable.add(scrollPane).grow();
        addScrollFocusListener(scrollPane);

        populateScrollTable();
    }

    public void populateScrollTable() {
        scrollTable.clearChildren(true);
        scrollTable.defaults().growX().space(10);

        
    }
}
