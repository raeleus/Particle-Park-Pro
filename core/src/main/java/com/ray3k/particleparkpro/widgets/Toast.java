package com.ray3k.particleparkpro.widgets;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.ray3k.stripe.PopTable;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Toast extends PopTable {
    private float hideDelay;

    public Toast(float hideDelay) {
        this.hideDelay = hideDelay;
    }

    public Toast(Skin skin, float hideDelay) {
        super(skin);
        this.hideDelay = hideDelay;
    }

    public Toast(Skin skin, String style, float hideDelay) {
        super(skin, style);
        this.hideDelay = hideDelay;
    }

    public Toast(WindowStyle style, float hideDelay) {
        super(style);
        this.hideDelay = hideDelay;
    }

    public Toast(PopTableStyle style, float hideDelay) {
        super(style);
        this.hideDelay = hideDelay;
    }

    @Override
    public void show(Stage stage) {
        super.show(stage, null);
        setKeepSizedWithinStage(false);
        getParentGroup().setColor(1, 1, 1, 1);
        addAction(sequence(
            moveTo(getStage().getWidth() / 2f - getWidth() / 2f, -getHeight()),
            moveTo(getStage().getWidth() / 2f - getWidth() / 2f, 0, .7f, Interpolation.exp5Out),
            delay(hideDelay),
            run(this::hide)
        ));
    }

    @Override
    public void hide() {
        addAction(sequence(
            moveTo(getStage().getWidth() / 2f - getWidth() / 2f, -getHeight(), .7f, Interpolation.exp5In),
            run(() -> super.hide(null))
        ));
    }
}
