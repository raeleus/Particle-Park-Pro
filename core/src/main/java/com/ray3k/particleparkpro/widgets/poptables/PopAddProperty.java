package com.ray3k.particleparkpro.widgets.poptables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.Core;
import com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel;
import com.ray3k.stripe.PopTable;
import com.ray3k.stripe.PopTableHoverListener;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.ShownProperty.*;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.shownProperties;

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

        //Delay
        scrollTable.pad(5);
        scrollTable.defaults().space(itemSpacing).left();
        var delayCheckBox = new CheckBox("Delay", skin);
        delayCheckBox.setChecked(shownProperties.contains(DELAY));
        scrollTable.add(delayCheckBox);
        addHandListener(delayCheckBox);
        onChange(delayCheckBox, () -> {
            if (delayCheckBox.isChecked()) shownProperties.add(DELAY);
            else shownProperties.remove(DELAY);
            EmitterPropertiesPanel.emitterPropertiesPanel.populateScrollTable();
        });

        var popHoverListener = new PopTableHoverListener(Align.left, Align.left, tooltipRightArrowStyle);
        popHoverListener.getPopTable().setAttachOffsetX(-10);
        popHoverListener.getPopTable().setKeepSizedWithinStage(false);
        delayCheckBox.addListener(popHoverListener);
        var label = new Label("Time from beginning of effect to emission start, in milliseconds.", skin);
        popHoverListener.getPopTable().add(label);

        //Life Offset
        scrollTable.row();
        var lifeOffsetCheckbox = new CheckBox("Life Offset", skin);
        lifeOffsetCheckbox.setChecked(shownProperties.contains(LIFE_OFFSET));
        scrollTable.add(lifeOffsetCheckbox);
        addHandListener(lifeOffsetCheckbox);
        onChange(lifeOffsetCheckbox, () -> {
            if (lifeOffsetCheckbox.isChecked()) shownProperties.add(LIFE_OFFSET);
            else shownProperties.remove(LIFE_OFFSET);
        });

        popHoverListener = new PopTableHoverListener(Align.left, Align.left, tooltipRightArrowStyle);
        lifeOffsetCheckbox.addListener(popHoverListener);
        popHoverListener.getPopTable().setAttachOffsetX(-10);
        popHoverListener.getPopTable().setKeepSizedWithinStage(false);
        label = new Label("Particle starting life consumed, in milliseconds.", skin);
        popHoverListener.getPopTable().add(label);

        //X Offset
        scrollTable.row();
        var xOffsetCheckBox = new CheckBox("X Offset", skin);
        xOffsetCheckBox.setChecked(shownProperties.contains(X_OFFSET));
        scrollTable.add(xOffsetCheckBox);
        addHandListener(xOffsetCheckBox);
        onChange(xOffsetCheckBox, () -> {
            if (xOffsetCheckBox.isChecked()) shownProperties.add(X_OFFSET);
            else shownProperties.remove(X_OFFSET);
        });

        popHoverListener = new PopTableHoverListener(Align.left, Align.left, tooltipRightArrowStyle);
        xOffsetCheckBox.addListener(popHoverListener);
        popHoverListener.getPopTable().setAttachOffsetX(-10);
        popHoverListener.getPopTable().setKeepSizedWithinStage(false);
        label = new Label("Amount to offset a particle's starting X location, in world units.", skin);
        popHoverListener.getPopTable().add(label);

        //Y Offset
        scrollTable.row();
        var yOffsetCheckBox = new CheckBox("Y Offset", skin);
        yOffsetCheckBox.setChecked(shownProperties.contains(Y_OFFSET));
        scrollTable.add(yOffsetCheckBox);
        addHandListener(yOffsetCheckBox);
        onChange(yOffsetCheckBox, () -> {
            if (yOffsetCheckBox.isChecked()) shownProperties.add(Y_OFFSET);
            else shownProperties.remove(Y_OFFSET);
        });

        popHoverListener = new PopTableHoverListener(Align.left, Align.left, tooltipRightArrowStyle);
        yOffsetCheckBox.addListener(popHoverListener);
        popHoverListener.getPopTable().setAttachOffsetX(-10);
        popHoverListener.getPopTable().setKeepSizedWithinStage(false);
        label = new Label("Amount to offset a particle's starting y location, in world units.", skin);
        popHoverListener.getPopTable().add(label);

        //Y Size
        scrollTable.row();
        var ySizeCheckBox = new CheckBox("Y Size", skin);
        ySizeCheckBox.setChecked(shownProperties.contains(Y_SIZE));
        scrollTable.add(ySizeCheckBox);
        addHandListener(ySizeCheckBox);
        onChange(ySizeCheckBox, () -> {
            if (ySizeCheckBox.isChecked()) shownProperties.add(Y_SIZE);
            else shownProperties.remove(Y_SIZE);
        });

        popHoverListener = new PopTableHoverListener(Align.left, Align.left, tooltipRightArrowStyle);
        ySizeCheckBox.addListener(popHoverListener);
        popHoverListener.getPopTable().setAttachOffsetX(-10);
        popHoverListener.getPopTable().setKeepSizedWithinStage(false);
        label = new Label("Particle y size, in world units.", skin);
        popHoverListener.getPopTable().add(label);

        //Velocity
        scrollTable.row();
        var velocityCheckBox = new CheckBox("Velocity", skin);
        velocityCheckBox.setChecked(shownProperties.contains(VELOCITY));
        scrollTable.add(velocityCheckBox);
        addHandListener(velocityCheckBox);
        onChange(velocityCheckBox, () -> {
            if (velocityCheckBox.isChecked()) shownProperties.add(VELOCITY);
            else shownProperties.remove(VELOCITY);
        });

        popHoverListener = new PopTableHoverListener(Align.left, Align.left, tooltipRightArrowStyle);
        velocityCheckBox.addListener(popHoverListener);
        popHoverListener.getPopTable().setAttachOffsetX(-10);
        popHoverListener.getPopTable().setKeepSizedWithinStage(false);
        label = new Label("Particle speed, in world units per second.", skin);
        popHoverListener.getPopTable().add(label);

        //Angle
        scrollTable.row();
        var angleCheckBox = new CheckBox("Angle", skin);
        angleCheckBox.setChecked(shownProperties.contains(ANGLE));
        scrollTable.add(angleCheckBox);
        addHandListener(angleCheckBox);
        onChange(angleCheckBox, () -> {
            if (angleCheckBox.isChecked()) shownProperties.add(ANGLE);
            else shownProperties.remove(ANGLE);
        });

        popHoverListener = new PopTableHoverListener(Align.left, Align.left, tooltipRightArrowStyle);
        angleCheckBox.addListener(popHoverListener);
        popHoverListener.getPopTable().setAttachOffsetX(-10);
        popHoverListener.getPopTable().setKeepSizedWithinStage(false);
        label = new Label("Particle emission angle, in degrees.", skin);
        popHoverListener.getPopTable().add(label);

        //Rotation
        scrollTable.row();
        var rotationCheckBox = new CheckBox("Rotation", skin);
        rotationCheckBox.setChecked(shownProperties.contains(ROTATION));
        scrollTable.add(rotationCheckBox);
        addHandListener(rotationCheckBox);
        onChange(rotationCheckBox, () -> {
            if (rotationCheckBox.isChecked()) shownProperties.add(ROTATION);
            else shownProperties.remove(ROTATION);
        });

        popHoverListener = new PopTableHoverListener(Align.left, Align.left, tooltipRightArrowStyle);
        rotationCheckBox.addListener(popHoverListener);
        popHoverListener.getPopTable().setAttachOffsetX(-10);
        popHoverListener.getPopTable().setKeepSizedWithinStage(false);
        label = new Label("Particle rotation, in degrees.", skin);
        popHoverListener.getPopTable().add(label);

        //Wind
        scrollTable.row();
        var windCheckBox = new CheckBox("Wind", skin);
        windCheckBox.setChecked(shownProperties.contains(WIND));
        scrollTable.add(windCheckBox);
        addHandListener(windCheckBox);
        onChange(windCheckBox, () -> {
            if (windCheckBox.isChecked()) shownProperties.add(WIND);
            else shownProperties.remove(WIND);
        });

        popHoverListener = new PopTableHoverListener(Align.left, Align.left, tooltipRightArrowStyle);
        windCheckBox.addListener(popHoverListener);
        popHoverListener.getPopTable().setAttachOffsetX(-10);
        popHoverListener.getPopTable().setKeepSizedWithinStage(false);
        label = new Label("Wind strength, in world units per second.", skin);
        popHoverListener.getPopTable().add(label);

        //Graviity
        scrollTable.row();
        var gravityCheckBox = new CheckBox("Gravity", skin);
        gravityCheckBox.setChecked(shownProperties.contains(GRAVITY));
        scrollTable.add(gravityCheckBox);
        addHandListener(gravityCheckBox);
        onChange(gravityCheckBox, () -> {
            if (gravityCheckBox.isChecked()) shownProperties.add(GRAVITY);
            else shownProperties.remove(GRAVITY);
        });

        popHoverListener = new PopTableHoverListener(Align.left, Align.left, tooltipRightArrowStyle);
        gravityCheckBox.addListener(popHoverListener);
        popHoverListener.getPopTable().setAttachOffsetX(-10);
        popHoverListener.getPopTable().setKeepSizedWithinStage(false);
        label = new Label("Gravity strength, in world units per second.", skin);
        popHoverListener.getPopTable().add(label);
    }
}
