package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.particleparkpro.widgets.poptables.PopAddProperty;
import com.ray3k.particleparkpro.widgets.subpanels.*;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Core.skin;
import static com.ray3k.particleparkpro.PresetActions.*;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.ShownProperty.*;

public class EmitterPropertiesPanel extends Panel {
    public enum ShownProperty {
        DELAY, LIFE_OFFSET, X_OFFSET, Y_OFFSET, VELOCITY, ANGLE, ROTATION, WIND, GRAVITY
    }
    private Table scrollTable;
    public static EmitterPropertiesPanel emitterPropertiesPanel;
    private ScrollPane scrollPane;
    private static final Vector2 temp = new Vector2();

    public EmitterPropertiesPanel() {
        emitterPropertiesPanel = this;
        setTouchable(Touchable.enabled);

        var label = new Label("Emitter Properties", skin, "header");
        tabTable.add(label);

        bodyTable.defaults().space(5);
        scrollTable = new Table();
        scrollTable.top();
        scrollPane = new ScrollPane(scrollTable, skin, "emitter-properties");
        scrollPane.setFlickScroll(false);
        bodyTable.add(scrollPane).grow();
        addScrollFocusListener(scrollPane);

        populateScrollTable(null);

        bodyTable.row();
        var addPropertyTextButton = new TextButton("Add Property", skin, "add");
        bodyTable.add(addPropertyTextButton).right();
        addHandListener(addPropertyTextButton);
        onChange(addPropertyTextButton, () -> {
            var pop = new PopAddProperty();
            pop.attachToActor(addPropertyTextButton, Align.top, Align.top);
            pop.show(foregroundStage);
        });
        addTooltip(addPropertyTextButton, "Activate an optional emitter property", Align.left, Align.left, tooltipRightArrowStyle);
    }

    public void populateScrollTable(ShownProperty newProperty) {
        Actor scrollToActor = null;

        scrollTable.clearChildren(true);
        scrollTable.defaults().growX().space(10);

        //Images
        var imagesSubPanel = new ImagesSubPanel();
        scrollTable.add(imagesSubPanel);

        //Count
        scrollTable.row();
        var countSubPanel = new CountSubPanel();
        scrollTable.add(countSubPanel);

        //Delay
        if (selectedEmitter.getDelay().isActive()) {
            scrollTable.row();
            var delaySubPanel = new RangeSubPanel("Delay", selectedEmitter.getDelay(), "time from beginning of the effect to emission start in milliseconds", DELAY);
            delaySubPanel.setUserObject(DELAY);
            scrollTable.add(delaySubPanel);
            if (newProperty == DELAY) scrollToActor = delaySubPanel;
        }

        //Duration
        scrollTable.row();
        var durationSubPanel = new RangeSubPanel("Duration", selectedEmitter.getDuration(), "time particles will be emitted in milliseconds", null);
        scrollTable.add(durationSubPanel);

        //Emission
        scrollTable.row();
        var emissionSubPanel = new GraphSubPanel("Emission", selectedEmitter.getEmission(), true, false, "the number of particles emitted per second", "Duration", null);
        scrollTable.add(emissionSubPanel);

        //Life
        scrollTable.row();
        var lifeSubPanel = new GraphSubPanel("Life", selectedEmitter.getLife(), true, true, "the time particles will live in milliseconds", "Duration", null);
        scrollTable.add(lifeSubPanel);

        //Life Offset
        if (selectedEmitter.getLifeOffset().isActive()) {
            scrollTable.row();
            var lifeOffsetSubPanel = new GraphSubPanel("Life Offset", selectedEmitter.getLifeOffset(), true, true, "the life duration consumed upon particle emission in milliseconds", "Duration", LIFE_OFFSET);
            lifeOffsetSubPanel.setUserObject(LIFE_OFFSET);
            scrollTable.add(lifeOffsetSubPanel);
            if (newProperty == LIFE_OFFSET) scrollToActor = lifeOffsetSubPanel;
        }

        //X Offset
        if (selectedEmitter.getXOffsetValue().isActive()) {
            scrollTable.row();
            var xOffsetSubPanel = new RangeSubPanel("X Offset", selectedEmitter.getXOffsetValue(), "amount to offset a particle's starting X location in world units", X_OFFSET);
            xOffsetSubPanel.setUserObject(X_OFFSET);
            scrollTable.add(xOffsetSubPanel);
            if (newProperty == X_OFFSET) scrollToActor = xOffsetSubPanel;
        }

        //Y Offset
        if (selectedEmitter.getYOffsetValue().isActive()) {
            scrollTable.row();
            var yOffsetSubPanel = new RangeSubPanel("Y Offset", selectedEmitter.getYOffsetValue(), "amount to offset a particle's starting Y location in world units", Y_OFFSET);
            yOffsetSubPanel.setUserObject(Y_OFFSET);
            scrollTable.add(yOffsetSubPanel);
            if (newProperty == Y_OFFSET) scrollToActor = yOffsetSubPanel;
        }

        //Spawn
        scrollTable.row();
        var spawnSubPanel = new SpawnSubPanel();
        scrollTable.add(spawnSubPanel);

        //Size
        scrollTable.row();
        var sizeSubPanel = new SizeSubPanel();
        scrollTable.add(sizeSubPanel);

        //Velocity
        if (selectedEmitter.getVelocity().isActive()) {
            scrollTable.row();
            var velocitySubPanel = new GraphSubPanel("Velocity", selectedEmitter.getVelocity(), true, false, "the particle speed in world units per second", "Life", VELOCITY);
            velocitySubPanel.setUserObject(VELOCITY);
            scrollTable.add(velocitySubPanel);
            if (newProperty == VELOCITY) scrollToActor = velocitySubPanel;
        }

        //Angle
        if (selectedEmitter.getAngle().isActive()) {
            scrollTable.row();
            var angleSubPanel = new GraphSubPanel("Angle", selectedEmitter.getAngle(), true, false, "the particle emission angle in degrees", "Life", ANGLE);
            angleSubPanel.setUserObject(ANGLE);
            scrollTable.add(angleSubPanel);
            if (newProperty == ANGLE) scrollToActor = angleSubPanel;
        }

        //Rotation
        if (selectedEmitter.getRotation().isActive()) {
            scrollTable.row();
            var rotationSubPanel = new GraphSubPanel("Rotation", selectedEmitter.getRotation(), true, false, "the particle rotation in degrees", "Life", ROTATION);
            rotationSubPanel.setUserObject(ROTATION);
            scrollTable.add(rotationSubPanel);
            if (newProperty == ROTATION) scrollToActor = rotationSubPanel;
        }

        //Wind
        if (selectedEmitter.getWind().isActive()) {
            scrollTable.row();
            var windSubPanel = new GraphSubPanel("Wind", selectedEmitter.getWind(), true, false, "the wind strength in world units per second", "Life", WIND);
            windSubPanel.setUserObject(WIND);
            scrollTable.add(windSubPanel);
            if (newProperty == WIND) scrollToActor = windSubPanel;
        }

        //Gravity
        if (selectedEmitter.getGravity().isActive()) {
            scrollTable.row();
            var gravitySubPanel = new GraphSubPanel("Gravity", selectedEmitter.getGravity(), true, false, "the gravity strength in world units per second", "Life", GRAVITY);
            gravitySubPanel.setUserObject(GRAVITY);
            scrollTable.add(gravitySubPanel);
            if (newProperty == GRAVITY) scrollToActor = gravitySubPanel;
        }

        //Tint
        scrollTable.row();
        var tintSubPanel = new TintSubPanel();
        scrollTable.add(tintSubPanel);

        //Transparency
        scrollTable.row();
        var transparencySubPanel = new TransparencySubPanel();
        scrollTable.add(transparencySubPanel);

        //Options
        scrollTable.row();
        var optionsSubPanel = new OptionsSubPanel();
        scrollTable.add(optionsSubPanel);

        if (scrollToActor != null) {
            scrollTable.layout();
            scrollPane.layout();
            temp.set(0, 0);
            scrollToActor.localToActorCoordinates(scrollTable, temp);
            scrollPane.scrollTo(0, temp.y + scrollToActor.getHeight(), scrollToActor.getWidth(), scrollToActor.getHeight());

            scrollToActor.addAction(fadeInEmitterProperty(scrollToActor));
        }
    }

    public void removeProperty(ShownProperty shownProperty) {
        for (var child : scrollTable.getChildren()) {
            if (child.getUserObject() == shownProperty) {
                child.addAction(Actions.sequence(hideEmitterProperty(child), Actions.run(() -> populateScrollTable(null))));
                break;
            }
        }
    }
}
