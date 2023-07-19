package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.stripe.PopTable;
import com.ray3k.stripe.PopTable.PopTableStyle;
import com.ray3k.stripe.PopTable.TableShowHideListener;
import com.ray3k.stripe.PopTableHoverListener;
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

import static com.ray3k.particleparkpro.Core.*;

public class OptionsSubPanel extends Panel {
    private static PopTable.PopTableStyle popTableStyle;
    public OptionsSubPanel() {
        setTouchable(Touchable.enabled);

        final var itemSpacing = 5;
        popTableStyle = new PopTable.PopTableStyle();

        tabTable.left();
        var label = new Label("Options", skin, "header");
        tabTable.add(label);

        bodyTable.defaults().space(itemSpacing).left();
        bodyTable.left();

        //Additive
        var checkBox = new CheckBox("Additive", skin);
        bodyTable.add(checkBox);
        addHandListener(checkBox);
        addHoverListener(checkBox, "Additive blending is used when the particle emitter is drawn. This causes overlapping colors to approach white.");

        //Attached
        bodyTable.row();
        checkBox = new CheckBox("Attached", skin);
        bodyTable.add(checkBox);
        addHandListener(checkBox);
        addHoverListener(checkBox,  "An attached particle emitter draws its particles relative to its origin. This makes existing particles move with the emitter when the particle effect's position is changed.");

        //Continuous
        bodyTable.row();
        checkBox = new CheckBox("Continuous", skin);
        bodyTable.add(checkBox);
        addHandListener(checkBox);
        addHoverListener(checkBox, "A continuous particle emitter will keep emitting particles even after the duration has expired.");

        //Aligned
        bodyTable.row();
        checkBox = new CheckBox("Aligned", skin);
        bodyTable.add(checkBox);
        addHandListener(checkBox);
        addHoverListener(checkBox,"An aligned particle emitter will rotate it's particles relative to the angle of the particle effect. If the particle effect rotates, the particles rotate as well.");

        //Behind
        bodyTable.row();
        checkBox = new CheckBox("Behind", skin);
        bodyTable.add(checkBox);
        addHandListener(checkBox);
        addHoverListener(checkBox, "Behind has no practical application in the current libGDX API, but is included for backwards compatibility.");

        //Premultiplied alpha
        bodyTable.row();
        checkBox = new CheckBox("Premultiplied Alpha", skin);
        bodyTable.add(checkBox);
        addHandListener(checkBox);
        addHoverListener(checkBox, "Premultiplied alpha is an alternative blending mode that expects RGB values to be multiplied by their transparency. Enable this value if your texture atlas is also set to premultiplied alpha.");
    }

    private void addHoverListener(Actor actor, String text) {
        var inputListener = new InputListener() {
            PopTable popTable;

            {
                popTable = new PopTable(popTableStyle);
                popTable.setModal(false);
                popTable.setHideOnUnfocus(true);
                popTable.setTouchable(Touchable.disabled);

                var label = new Label(text, skin);
                label.setWrap(true);
                popTable.add(label).width(250);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (popTable.isHidden()) {
                    if (fromActor == null || !event.getListenerActor().isAscendantOf(fromActor)) {
                        Actor actor = event.getListenerActor();

                        if (actor instanceof Disableable) {
                            if (((Disableable) actor).isDisabled()) return;
                        }

                        popTable.show(foregroundStage);
                        popTable.attachToActor(actor, Align.top, Align.top);


                        popTable.moveToInsideStage();
                    }
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (!popTable.isHidden()) {
                    if (toActor == null || !event.getListenerActor().isAscendantOf(toActor)) {
                        popTable.hide();
                    }
                }
            }
        };
        actor.addListener(inputListener);
    }
}
