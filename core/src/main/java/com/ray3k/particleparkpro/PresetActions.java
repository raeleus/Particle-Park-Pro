package com.ray3k.particleparkpro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.ray3k.particleparkpro.Core.*;

public class PresetActions {
    private final static Color CLEAR_WHITE = new Color(1, 1, 1, 0);

    public static Action moveOutAction(Actor actor, int alignDirection) {
        float targetX = actor.getX();
        float targetY = actor.getY();

        if (Align.isLeft(alignDirection)) targetX -= stage.getWidth();
        else if (Align.isRight(alignDirection)) targetX += stage.getWidth();

        if (Align.isBottom(alignDirection)) targetY -= stage.getHeight();
        else if (Align.isTop(alignDirection)) targetY += stage.getHeight();

        var movementAction = moveTo(targetX, targetY, 1, Interpolation.fade);

        var resizeCheckAction = new Action() {
            @Override
            public boolean act(float delta) {
                if (windowResized) {
                    movementAction.setActor(null);
                    return true;
                }
                return movementAction.isComplete();
            }
        };

        return sequence(parallel(movementAction, resizeCheckAction), removeActor());
    }

    public static Action moveInAction(Actor actor, int alignDirection) {
        float startX = actor.getX();
        float startY = actor.getY();

        if (Align.isLeft(alignDirection)) startX += stage.getWidth();
        else if (Align.isRight(alignDirection)) startX -= stage.getWidth();

        if (Align.isBottom(alignDirection)) startY += stage.getHeight();
        else if (Align.isTop(alignDirection)) startY -= stage.getHeight();

        var initializeAction = moveTo(startX, startY);

        var movementAction = moveTo(actor.getX(), actor.getY(), 1, Interpolation.fade);

        var resizeCheckAction = new Action() {
            @Override
            public boolean act(float delta) {
                if (windowResized) {
                    movementAction.setActor(null);
                    return true;
                }
                return movementAction.isComplete();
            }
        };

        return sequence(initializeAction, parallel(movementAction, resizeCheckAction));
    }

    public static Action welcomeAction(Table table) {
        Gdx.app.postRunnable(() -> {
            table.setTransform(true);
            table.setOrigin(Align.center);
        });
        return Actions.sequence(
            Actions.color(CLEAR_WHITE),
            Actions.scaleTo(1.1f, 1.1f),
            Actions.delay(.5f),
            Actions.parallel(Actions.fadeIn(1f), Actions.scaleTo(1f, 1f, .75f, Interpolation.smooth)),
            Actions.run(() -> table.setTransform(false)));
    }

    public static void transition(Actor first, Actor second, int alignDirection) {
        stage.addActor(first);
        first.addAction(moveOutAction(first, alignDirection));

        root.setActor(second);
        root.validate();

        second.addAction(moveInAction(second, alignDirection));
    }
}
