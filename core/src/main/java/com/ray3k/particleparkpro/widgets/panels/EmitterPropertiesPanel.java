package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectSet;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.particleparkpro.widgets.poptables.PopAddProperty;
import com.ray3k.particleparkpro.widgets.subpanels.*;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Core.skin;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.ShownProperty.*;

public class EmitterPropertiesPanel extends Panel {
    public enum ShownProperty {
        DELAY, LIFE_OFFSET, X_OFFSET, Y_OFFSET, VELOCITY, ANGLE, ROTATION, WIND, GRAVITY
    }
    public static ObjectSet<ShownProperty> shownProperties;
    private Table scrollTable;
    public static EmitterPropertiesPanel emitterPropertiesPanel;

    public EmitterPropertiesPanel() {
        emitterPropertiesPanel = this;
        shownProperties = new ObjectSet<>();
        setTouchable(Touchable.enabled);

        var label = new Label("Emitter Properties", skin, "header");
        tabTable.add(label);

        bodyTable.defaults().space(5);
        scrollTable = new Table();
        scrollTable.top();
        var scrollPane = new ScrollPane(scrollTable, skin);
        scrollPane.setFlickScroll(false);
        bodyTable.add(scrollPane).grow();
        addScrollFocusListener(scrollPane);

        populateScrollTable();

        bodyTable.row();
        var addPropertyTextButton = new TextButton("Add Property", skin, "add");
        bodyTable.add(addPropertyTextButton).right();
        addHandListener(addPropertyTextButton);
        onChange(addPropertyTextButton, () -> {
            var pop = new PopAddProperty();
            pop.attachToActor(addPropertyTextButton, Align.top, Align.top);
            pop.show(foregroundStage);
        });
        addTooltip(addPropertyTextButton, "Activate an optional emitter property", Align.left, tooltipRightArrowStyle);
    }

    public void populateScrollTable() {
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
        if (shownProperties.contains(DELAY)) {
            scrollTable.row();
            var delaySubPanel = new DelaySubPanel();
            scrollTable.add(delaySubPanel);
        }

        //Duration
        scrollTable.row();
        var durationSubPanel = new DurationSubPanel();
        scrollTable.add(durationSubPanel);

        //Emission
        scrollTable.row();
        var emissionSubPanel = new EmissionSubPanel();
        scrollTable.add(emissionSubPanel);

        //Life
        scrollTable.row();
        var lifeSubPanel = new LifeSubPanel();
        scrollTable.add(lifeSubPanel);

        //Life Offset
        if (shownProperties.contains(LIFE_OFFSET)) {
            scrollTable.row();
            var lifeOffsetSubPanel = new LifeOffsetSubPanel();
            scrollTable.add(lifeOffsetSubPanel);
        }

        //X Offset
        if (shownProperties.contains(X_OFFSET)) {
            scrollTable.row();
            var xOffsetSubPanel = new XoffsetSubPanel();
            scrollTable.add(xOffsetSubPanel);
        }

        //Y Offset
        if (shownProperties.contains(Y_OFFSET)) {
            scrollTable.row();
            var yOffsetSubPanel = new YoffsetSubPanel();
            scrollTable.add(yOffsetSubPanel);
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
        if (shownProperties.contains(VELOCITY)) {
            scrollTable.row();
            var velocitySubPanel = new VelocitySubPanel();
            scrollTable.add(velocitySubPanel);
        }

        //Angle
        if (shownProperties.contains(ANGLE)) {
            scrollTable.row();
            var angleSubPanel = new AngleSubPanel();
            scrollTable.add(angleSubPanel);
        }

        //Rotation
        if (shownProperties.contains(ROTATION)) {
            scrollTable.row();
            var rotationSubPanel = new RotationSubPanel();
            scrollTable.add(rotationSubPanel);
        }

        //Wind
        if (shownProperties.contains(WIND)) {
            scrollTable.row();
            var windSubPanel = new WindSubPanel();
            scrollTable.add(windSubPanel);
        }

        //Gravity
        if (shownProperties.contains(GRAVITY)) {
            scrollTable.row();
            var gravitySubPanel = new GravitySubPanel();
            scrollTable.add(gravitySubPanel);
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
    }
}
