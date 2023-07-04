package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import static com.ray3k.particleparkpro.Core.skin;

public class Panel extends Table  {
    public Table tabTable;
    public Table topTable;
    public Table bodyTable;

    public Panel() {
        tabTable = new Table();
        add(tabTable);
        tabTable.setBackground(skin.getDrawable("panel-tab-10"));

        topTable = new Table();
        add(topTable).growX();
        topTable.setBackground(skin.getDrawable("panel-top-10"));

        row();
        bodyTable = new Table();
        add(bodyTable).grow().colspan(2);
        bodyTable.setBackground(skin.getDrawable("panel-body-10"));
    }
}
