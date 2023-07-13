package com.ray3k.particleparkpro.widgets.tables;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.widgets.WelcomeCard;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.PresetActions.transition;

public class WelcomeTable extends Table {
    public WelcomeTable() {
        var table = new Table();
        add(table);

        var image = new Image(skin, "title");
        table.add(image);

        table.row();
        var label = new Label(version, skin);
        table.add(label).expandX().right().padRight(10).padTop(5);

        row();
        table = new Table();
        add(table).spaceTop(100);

        table.row().spaceRight(70);
        var card = new WelcomeCard("Classic", "The classic Particle Editor experience", skin.getDrawable("thumb-classic"), "Open Classic Mode");
        table.add(card);
        addHandListener(card);
        onChange(card, () -> transition(this, new ClassicTable(), Align.top));

        card = new WelcomeCard("Wizard", "Simple with a large preview", skin.getDrawable("thumb-wizard"), "Open Wizard Mode");
        table.add(card);
        addHandListener(card);
        onChange(card, () -> transition(this, new WizardTable(), Align.top));

        table.row();
        var checkbox = new CheckBox("Remember my choice", skin);
        table.add(checkbox).space(25).right().colspan(2);
        addHandListener(checkbox);
    }
}
