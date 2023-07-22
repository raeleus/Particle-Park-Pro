package com.ray3k.particleparkpro.widgets.subpanels;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.widgets.Panel;

import static com.ray3k.particleparkpro.Core.*;

public class OptionsSubPanel extends Panel {
    public OptionsSubPanel() {
        setTouchable(Touchable.enabled);

        final var itemSpacing = 5;
        final var tooltipWidth = 250;

        tabTable.left();
        var label = new Label("Options", skin, "header");
        tabTable.add(label);

        bodyTable.defaults().space(itemSpacing).left();
        bodyTable.left();

        //Additive
        var checkBox = new CheckBox("Additive", skin);
        bodyTable.add(checkBox);
        addHandListener(checkBox);
        addTooltip(checkBox, "Additive blending is used when the particle emitter is drawn. This causes overlapping colors to approach white.",
            Align.top, tooltipWidth, tooltipBottomArrowStyle);

        //Attached
        bodyTable.row();
        checkBox = new CheckBox("Attached", skin);
        bodyTable.add(checkBox);
        addHandListener(checkBox);
        addTooltip(checkBox,  "An attached particle emitter draws its particles relative to its origin. This makes existing particles move with the emitter when the particle effect's position is changed.", Align.top, tooltipWidth, tooltipBottomArrowStyle);

        //Continuous
        bodyTable.row();
        checkBox = new CheckBox("Continuous", skin);
        bodyTable.add(checkBox);
        addHandListener(checkBox);
        addTooltip(checkBox, "A continuous particle emitter will keep emitting particles even after the duration has expired.", Align.top, tooltipWidth, tooltipBottomArrowStyle);

        //Aligned
        bodyTable.row();
        checkBox = new CheckBox("Aligned", skin);
        bodyTable.add(checkBox);
        addHandListener(checkBox);
        addTooltip(checkBox,"An aligned particle emitter will rotate it's particles relative to the angle of the particle effect. If the particle effect rotates, the particles rotate as well.", Align.top, tooltipWidth, tooltipBottomArrowStyle);

        //Behind
        bodyTable.row();
        checkBox = new CheckBox("Behind", skin);
        bodyTable.add(checkBox);
        addHandListener(checkBox);
        addTooltip(checkBox, "Behind has no practical application in the current libGDX API, but is included for backwards compatibility.", Align.top, tooltipWidth, tooltipBottomArrowStyle);

        //Premultiplied alpha
        bodyTable.row();
        checkBox = new CheckBox("Premultiplied Alpha", skin);
        bodyTable.add(checkBox);
        addHandListener(checkBox);
        addTooltip(checkBox, "Premultiplied alpha is an alternative blending mode that expects RGB values to be multiplied by their transparency. Enable this value if your texture atlas is also set to premultiplied alpha.", Align.top, tooltipWidth, tooltipBottomArrowStyle);
    }
}
