package com.ray3k.particleparkpro.widgets.poptables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.Scaling;
import com.ray3k.particleparkpro.Core;
import com.ray3k.particleparkpro.FileDialogs;
import com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel;
import com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.ShownProperty;
import com.ray3k.stripe.PopColorPicker;
import com.ray3k.stripe.PopColorPicker.PopColorPickerListener;
import com.ray3k.stripe.PopTable;
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.ParticlePreview.*;
import static com.ray3k.particleparkpro.Settings.getDefaultImagePath;
import static com.ray3k.particleparkpro.Settings.setDefaultImagePath;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.*;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.ShownProperty.*;
import static com.ray3k.particleparkpro.widgets.panels.PreviewPanel.*;

public class PopAddProperty extends PopTable {
    public PopAddProperty() {
        super(Core.skin.get(WindowStyle.class));

        setDraggable(false);
        setHideOnUnfocus(true);
        setKeepSizedWithinStage(true);
        addListener(new TableShowHideListener() {
            @Override
            public void tableShown(Event event) {
                Gdx.input.setInputProcessor(foregroundStage);
            }

            @Override
            public void tableHidden(Event event) {
                Gdx.input.setInputProcessor(stage);
            }
        });

        final int itemSpacing = 5;

        var scrollTable = new Table();
        var scrollPane = new ScrollPane(scrollTable, skin);
        scrollPane.setFlickScroll(false);
        add(scrollPane).grow();
        addForegroundScrollFocusListener(scrollPane);

        scrollTable.pad(5);
        scrollTable.defaults().space(itemSpacing).left();
        var checkBox = new CheckBox("Delay", skin);
        scrollTable.add(checkBox);
        addHandListener(checkBox);
        onChange(checkBox, () -> {
            if (checkBox.isChecked()) shownProperties.add(DELAY);
            else shownProperties.remove(DELAY);
        });

        scrollTable.row();
        checkBox = new CheckBox("Life Offset", skin);
        scrollTable.add(checkBox);
        addHandListener(checkBox);

        scrollTable.row();
        checkBox = new CheckBox("X Offset", skin);
        scrollTable.add(checkBox);
        addHandListener(checkBox);

        scrollTable.row();
        checkBox = new CheckBox("Y Offset", skin);
        scrollTable.add(checkBox);
        addHandListener(checkBox);

        scrollTable.row();
        checkBox = new CheckBox("Y Size", skin);
        scrollTable.add(checkBox);
        addHandListener(checkBox);

        scrollTable.row();
        checkBox = new CheckBox("Velocity", skin);
        scrollTable.add(checkBox);
        addHandListener(checkBox);

        scrollTable.row();
        checkBox = new CheckBox("Angle", skin);
        scrollTable.add(checkBox);
        addHandListener(checkBox);

        scrollTable.row();
        checkBox = new CheckBox("Rotation", skin);
        scrollTable.add(checkBox);
        addHandListener(checkBox);

        scrollTable.row();
        checkBox = new CheckBox("Wind", skin);
        scrollTable.add(checkBox);
        addHandListener(checkBox);

        scrollTable.row();
        checkBox = new CheckBox("Gravity", skin);
        scrollTable.add(checkBox);
        addHandListener(checkBox);
    }
}
