package com.ray3k.particleparkpro.widgets.subpanels;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.undo.UndoManager;
import com.ray3k.particleparkpro.undo.undoables.CountMaxUndoable;
import com.ray3k.particleparkpro.undo.undoables.CountMinUndoable;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

import static com.ray3k.particleparkpro.Core.*;

public class CountSubPanel extends Panel {
    public CountSubPanel() {
        setTouchable(Touchable.enabled);

        final var itemSpacing = 5;
        final var gap = 15;
        final var spinnerWidth = 50;

        tabTable.left();
        var label = new Label("Count", skin, "header");
        tabTable.add(label);

        bodyTable.defaults().space(itemSpacing);
        bodyTable.left();

        label = new Label("Min:", skin);
        bodyTable.add(label);

        var minSpinner = new Spinner(selectedEmitter.getMinParticleCount(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        bodyTable.add(minSpinner).width(spinnerWidth);
        addHandListener(minSpinner.getButtonMinus());
        addHandListener(minSpinner.getButtonPlus());
        addIbeamListener(minSpinner.getTextField());
        addTooltip(minSpinner, "The minimum number of particles at all times", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(minSpinner, () -> UndoManager.addUndoable(new CountMinUndoable(minSpinner)));

        label = new Label("Max:", skin);
        bodyTable.add(label).spaceLeft(gap);

        var maxSpinner = new Spinner(selectedEmitter.getMaxParticleCount(), 1, true, Orientation.RIGHT_STACK, spinnerStyle);
        bodyTable.add(maxSpinner).width(spinnerWidth);
        addHandListener(maxSpinner.getButtonMinus());
        addHandListener(maxSpinner.getButtonPlus());
        addIbeamListener(maxSpinner.getTextField());
        addTooltip(maxSpinner, "The maximum number of particles allowed", Align.top, Align.top, tooltipBottomArrowStyle);
        onChange(maxSpinner, () -> UndoManager.addUndoable(new CountMaxUndoable(maxSpinner)));
    }
}
