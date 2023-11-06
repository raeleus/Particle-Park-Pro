package com.ray3k.particleparkpro.widgets.tables;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.widgets.WelcomeCard;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Listeners.addHandListener;
import static com.ray3k.particleparkpro.Listeners.onChange;
import static com.ray3k.particleparkpro.PresetActions.transition;
import static com.ray3k.particleparkpro.Settings.DEFAULT_OPEN_TO_SCREEN;
import static com.ray3k.particleparkpro.Settings.NAME_OPEN_TO_SCREEN;

/**
 * The introductory table which highlights the app title and buttons to open the classic or wizard modes.
 */
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
        var classicCard = new WelcomeCard("Classic", "The classic Particle Editor experience", skin.getDrawable("thumb-classic"), "Open Classic Mode");
        table.add(classicCard);
        addHandListener(classicCard);

        var wizardCard = new WelcomeCard("Wizard", "Simple with a large preview", skin.getDrawable("thumb-wizard"), "Open Wizard Mode");
        table.add(wizardCard);
        addHandListener(wizardCard);

        table.row();
        var checkbox = new CheckBox("Remember my choice", skin);
        checkbox.setChecked(!preferences.getString(NAME_OPEN_TO_SCREEN, DEFAULT_OPEN_TO_SCREEN).equals(DEFAULT_OPEN_TO_SCREEN));
        table.add(checkbox).space(25).right().colspan(2);
        addHandListener(checkbox);
        onChange(checkbox, () -> {
            if (!checkbox.isChecked()) {
                preferences.putString(NAME_OPEN_TO_SCREEN, "Welcome");
                preferences.flush();
            }
        });

        onChange(classicCard, () -> {
            if (checkbox.isChecked()) {
                preferences.putString(NAME_OPEN_TO_SCREEN, "Classic");
                preferences.flush();
            }
            transition(this, new ClassicTable(), Align.top);
        });

        onChange(wizardCard, () -> {
            if (checkbox.isChecked()) {
                preferences.putString(NAME_OPEN_TO_SCREEN, "Wizard");
                preferences.flush();
            }
            transition(this, new WizardTable(), Align.top);
        });
    }
}
