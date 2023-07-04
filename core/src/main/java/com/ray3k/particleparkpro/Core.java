package com.ray3k.particleparkpro;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ray3k.particleparkpro.tables.WelcomeTable;
import com.ray3k.particleparkpro.widgets.styles.PPcolorPickerStyle;
import com.ray3k.stripe.PopColorPicker.PopColorPickerStyle;
import com.ray3k.stripe.ScrollFocusListener;

import static com.ray3k.particleparkpro.PresetActions.welcomeAction;

public class Core extends ApplicationAdapter {
    public static Skin skin;
    public static Stage stage;
    public static boolean windowResized;
    public static ScreenViewport viewport;
    public static Container<Actor> root;
    public static String version;
    private Color bgColor = new Color();
    public static PopColorPickerStyle popColorPickerStyle;

    @Override
    public void create() {
        version = "ver " + Gdx.files.classpath("version").readString();

        viewport = new ScreenViewport();
        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("skin/particleparkpro.json"));
        popColorPickerStyle = new PPcolorPickerStyle();

        Gdx.input.setInputProcessor(stage);

        handListener = new HandListener();
        ibeamListener = new IbeamListener();
        scrollFocusListener = new ScrollFocusListener(stage);

        bgColor.set(skin.getColor("bg"));

        root = new Container<>();
        root.setFillParent(true);
        root.fill();
        stage.addActor(root);

        var welcomeTable = new WelcomeTable();
        root.setActor(welcomeTable);
        welcomeTable.addAction(welcomeAction(welcomeTable));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        windowResized = true;
    }

    @Override
    public void render() {
        ScreenUtils.clear(bgColor);

        stage.act();
        stage.draw();
        windowResized = false;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    public static void onChange(Actor actor, Runnable runnable) {
        actor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runnable.run();
            }
        });
    }

    public static void onClick(Actor actor, Runnable runnable) {
        actor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runnable.run();
            }
        });
    }

    public static HandListener handListener;
    public static IbeamListener ibeamListener;

    public static void addHandListener(Actor actor) {
        actor.addListener(handListener);
    }

    public static void addIbeamListener(Actor actor) {
        actor.addListener(ibeamListener);
    }

    private static ScrollFocusListener scrollFocusListener;

    public static void addScrollFocusListener(Actor actor) {
        actor.addListener(scrollFocusListener);
    }
}
