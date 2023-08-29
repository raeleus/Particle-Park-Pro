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
        var additiveCheckBox = new CheckBox("Additive", skin);
        additiveCheckBox.setChecked(selectedEmitter.isAdditive());
        bodyTable.add(additiveCheckBox);
        addHandListener(additiveCheckBox);
        addTooltip(additiveCheckBox, "Additive blending is used when the particle emitter is drawn. This causes overlapping colors to approach white.",
            Align.top, tooltipWidth, tooltipBottomArrowStyle);
        onChange(additiveCheckBox, () -> selectedEmitter.setAdditive(additiveCheckBox.isChecked()));

        //Attached
        bodyTable.row();
        var attachedCheckBox = new CheckBox("Attached", skin);
        attachedCheckBox.setChecked(selectedEmitter.isAttached());
        bodyTable.add(attachedCheckBox);
        addHandListener(attachedCheckBox);
        addTooltip(attachedCheckBox,  "An attached particle emitter draws its particles relative to its origin. This makes existing particles move with the emitter when the particle effect's position is changed.", Align.top, tooltipWidth, tooltipBottomArrowStyle);
        onChange(attachedCheckBox, () -> selectedEmitter.setAttached(attachedCheckBox.isChecked()));

        //Continuous
        bodyTable.row();
        var continuousCheckBox = new CheckBox("Continuous", skin);
        continuousCheckBox.setChecked(selectedEmitter.isContinuous());
        bodyTable.add(continuousCheckBox);
        addHandListener(continuousCheckBox);
        addTooltip(continuousCheckBox, "A continuous particle emitter will keep emitting particles even after the duration has expired.", Align.top, tooltipWidth, tooltipBottomArrowStyle);
        onChange(continuousCheckBox, () -> selectedEmitter.setContinuous(continuousCheckBox.isChecked()));

        //Aligned
        bodyTable.row();
        var alignedCheckBox = new CheckBox("Aligned", skin);
        alignedCheckBox.setChecked(selectedEmitter.isAligned());
        bodyTable.add(alignedCheckBox);
        addHandListener(alignedCheckBox);
        addTooltip(alignedCheckBox,"An aligned particle emitter will rotate it's particles relative to the angle of the particle effect. If the particle effect rotates, the particles rotate as well.", Align.top, tooltipWidth, tooltipBottomArrowStyle);
        onChange(alignedCheckBox, () -> selectedEmitter.setAligned(alignedCheckBox.isChecked()));

        //Behind
        bodyTable.row();
        var behindCheckBox = new CheckBox("Behind", skin);
        behindCheckBox.setChecked(selectedEmitter.isBehind());
        bodyTable.add(behindCheckBox);
        addHandListener(behindCheckBox);
        addTooltip(behindCheckBox, "Behind has no practical application in the current libGDX API, but is included for backwards compatibility.", Align.top, tooltipWidth, tooltipBottomArrowStyle);
        onChange(behindCheckBox, () -> selectedEmitter.setBehind(behindCheckBox.isChecked()));

        //Premultiplied alpha
        bodyTable.row();
        var premultipliedAlphaCheckBox = new CheckBox("Premultiplied Alpha", skin);
        premultipliedAlphaCheckBox.setChecked(selectedEmitter.isPremultipliedAlpha());
        bodyTable.add(premultipliedAlphaCheckBox);
        addHandListener(premultipliedAlphaCheckBox);
        addTooltip(premultipliedAlphaCheckBox, "Premultiplied alpha is an alternative blending mode that expects RGB values to be multiplied by their transparency. Enable this value if your texture atlas is also set to premultiplied alpha.", Align.top, tooltipWidth, tooltipBottomArrowStyle);
        onChange(premultipliedAlphaCheckBox, () -> selectedEmitter.setPremultipliedAlpha(premultipliedAlphaCheckBox.isChecked()));
    }
}
