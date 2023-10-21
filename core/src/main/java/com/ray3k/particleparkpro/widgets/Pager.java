package com.ray3k.particleparkpro.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;

import static com.ray3k.particleparkpro.Core.skin;

public class Pager extends Table {
    public CardGroup cardGroup;
    public TextButton previousButton;
    public TextButton nextButton;
    public Table buttonTable;
    public ButtonGroup<Button> buttonGroup;
    private int index;
    private float buttonSpacing = 5f;
    public static final Vector2 temp = new Vector2();
    private Interpolation interpolation = Interpolation.smoother;
    private float transitionDuration = .5f;
    private boolean transitioning;
    private final Array<Button> queuedButtons = new Array<>();

    public Pager(Actor... actors) {
        initialize(actors);
    }

    private void initialize(Actor... actors) {
        queuedButtons.clear();
        cardGroup = new CardGroup(actors);
        add(cardGroup).grow();

        row();
        buttonTable = new Table();
        buttonTable.defaults().space(buttonSpacing);
        add(buttonTable).growX();

        previousButton = new TextButton("Previous", skin, "small");
        buttonTable.add(previousButton).uniformX().fill();
        previousButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (transitioning) queuedButtons.add(previousButton);
                else changeIndex(index - 1);
            }
        });

        buttonGroup = new ButtonGroup<>();
        if (actors.length > 0) {
            for (int i = 0; i < actors.length; i++) {
                final int newIndex = i;
                var button = new Button(skin, "pager-dot");
                button.setProgrammaticChangeEvents(false);
                buttonTable.add(button);
                buttonGroup.add(button);
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (transitioning) queuedButtons.add(button);
                        else changeIndex(newIndex);
                    }
                });
            }
            buttonGroup.getButtons().get(index).setChecked(true);
        }

        nextButton = new TextButton("Next", skin, "small");
        buttonTable.add(nextButton).uniformX().fill();
        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (transitioning) queuedButtons.add(nextButton);
                else changeIndex(index + 1);
            }
        });
    }

    private void changeIndex(int index) {
        var size = cardGroup.actors.size - 1;
        if (index < 0) index = 0;
        if (index > size) index = size;

        if (this.index == index) {
            queuedButtons.clear();
            return;
        }

        boolean goLeft = index < this.index;

        var currentActor = cardGroup.actors.get(this.index);
        var nextActor = cardGroup.actors.get(index);

        this.index = index;
        setClip(true);
        transitioning = true;
        buttonGroup.getButtons().get(this.index).setChecked(true);

        currentActor.addAction(Actions.sequence(Actions.moveBy((goLeft ? 1 : -1) * currentActor.getWidth(), 0, transitionDuration, interpolation)));

        temp.set(0, 0);
        currentActor.localToActorCoordinates(this, temp);
        addActor(nextActor);
        nextActor.setBounds(temp.x + (goLeft ? -1 : 1) * currentActor.getWidth(),  temp.y, currentActor.getWidth(), currentActor.getHeight());

        nextActor.addAction(Actions.sequence(Actions.moveTo(temp.x, temp.y, transitionDuration, interpolation), Actions.run(() -> {
            if (cardGroup.actors.size > 0) cardGroup.showIndex(this.index);
            setClip(false);
            transitioning = false;


            Gdx.app.postRunnable(() -> {
                if (queuedButtons.size > 0) {
                    var button = queuedButtons.first();
                    queuedButtons.removeIndex(0);
                    button.fire(new ChangeEvent());
                }
            });
        })));
    }
}
