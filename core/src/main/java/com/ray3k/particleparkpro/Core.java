package com.ray3k.particleparkpro;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ray3k.particleparkpro.widgets.ColorGraph.ColorGraphStyle;
import com.ray3k.particleparkpro.widgets.EditableLabel.EditableLabelStyle;
import com.ray3k.particleparkpro.widgets.LineGraph.LineGraphStyle;
import com.ray3k.particleparkpro.widgets.styles.*;
import com.ray3k.particleparkpro.widgets.tables.WelcomeTable;
import com.ray3k.stripe.DraggableList.DraggableListStyle;
import com.ray3k.stripe.DraggableTextList.DraggableTextListStyle;
import com.ray3k.stripe.PopColorPicker.PopColorPickerStyle;
import com.ray3k.stripe.PopTable;
import com.ray3k.stripe.PopTable.PopTableStyle;
import com.ray3k.stripe.ResizeWidget.ResizeWidgetStyle;
import com.ray3k.stripe.ScrollFocusListener;
import com.ray3k.stripe.Spinner.SpinnerStyle;
import com.ray3k.stripe.ViewportWidget;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static com.ray3k.particleparkpro.PresetActions.welcomeAction;

public class Core extends ApplicationAdapter {
    public static Skin skin;
    public static Stage stage;
    public static Stage foregroundStage;
    public static SpriteBatch spriteBatch;
    public static boolean windowResized;
    public static ScreenViewport viewport;
    public static ScreenViewport previewViewport;
    public static OrthographicCamera previewCamera;
    public static Container<Actor> root;
    public static String version;
    private Color bgColor = new Color();
    public static PopColorPickerStyle popColorPickerStyle;
    public static LineGraphStyle lineGraphStyle;
    public static LineGraphStyle lineGraphBigStyle;
    public static ShapeDrawer shapeDrawer;
    public static ColorGraphStyle colorGraphStyle;
    public static ViewportWidget viewportWidget;
    public static ParticlePreview particlePreview;
    public static SpinnerStyle spinnerStyle;
    public static Preferences preferences;
    public static ResizeWidgetStyle resizeWidgetStyle;
    public static DraggableTextListStyle draggableListStyle;
    public static DraggableTextListStyle draggableListNoBgStyle;
    public static SystemCursorListener handListener;
    public static SystemCursorListener ibeamListener;
    public static SystemCursorListener horizontalResizeListener;
    public static SystemCursorListener verticalResizeListener;
    public static SystemCursorListener neswResizeListener;
    public static SystemCursorListener nwseResizeListener;
    public static SystemCursorListener allResizeListener;
    public static SplitPaneSystemCursorListener splitPaneHorizontalSystemCursorListener;
    public static SplitPaneSystemCursorListener splitPaneVerticalSystemCursorListener;
    public static PopTableStyle tooltipBottomArrowStyle;
    public static PopTableStyle tooltipTopArrowStyle;
    public static PopTableStyle tooltipRightArrowStyle;
    public static PopTableStyle tooltipLeftArrowStyle;
    public static EditableLabelStyle editableLabelStyle;
    public static ParticleEffect particleEffect;
    public static ObjectMap<ParticleEmitter, Boolean> activeEmitters;
    public static ParticleEmitter selectedEmitter;
    public static ShaderProgram shader;
//    public static float time;

    @Override
    public void create() {
        version = "ver " + Gdx.files.classpath("version").readString();

        preferences = Gdx.app.getPreferences("Particle Park Pro");

        viewport = new ScreenViewport();
        previewCamera = new OrthographicCamera();
        previewViewport = new ScreenViewport(previewCamera);
        viewportWidget = new ViewportWidget(previewViewport);
        spriteBatch = new SpriteBatch();
        stage = new Stage(viewport, spriteBatch);
        foregroundStage = new Stage(viewport, spriteBatch);
        skin = new Skin(Gdx.files.internal("skin/particleparkpro.json"));
        popColorPickerStyle = new PPcolorPickerStyle();
        lineGraphStyle = new PPlineGraphStyle();
        lineGraphBigStyle = new PPlineGraphBigStyle();
        shapeDrawer = new ShapeDrawer(spriteBatch, skin.getRegion("white-pixel"));
        colorGraphStyle = new PPcolorGraphStyle();
        particlePreview = new ParticlePreview();
        spinnerStyle = new PPspinnerStyle();
        resizeWidgetStyle = new PPresizeWidgetStyle();
        draggableListStyle = new PPdraggableTextListStyle();
        draggableListNoBgStyle = new PPdraggableTextListNoBGStyle();
        tooltipBottomArrowStyle = new PopTableStyle(skin.get("tooltip-bottom-arrow", WindowStyle.class));
        tooltipTopArrowStyle = new PopTableStyle(skin.get("tooltip-top-arrow", WindowStyle.class));
        tooltipRightArrowStyle = new PopTableStyle(skin.get("tooltip-right-arrow", WindowStyle.class));
        tooltipLeftArrowStyle = new PopTableStyle(skin.get("tooltip-left-arrow", WindowStyle.class));
        editableLabelStyle = new PPeditableLabelStyle();

        activeEmitters = new ObjectMap<>();
        loadParticle(Gdx.files.internal("flame.p"));
        selectedEmitter = particleEffect.getEmitters().first();

        Gdx.input.setInputProcessor(stage);

        handListener = new SystemCursorListener(SystemCursor.Hand);
        ibeamListener = new SystemCursorListener(SystemCursor.Ibeam);
        neswResizeListener = new SystemCursorListener(SystemCursor.NESWResize);
        nwseResizeListener = new SystemCursorListener(SystemCursor.NWSEResize);
        horizontalResizeListener = new SystemCursorListener(SystemCursor.HorizontalResize);
        verticalResizeListener = new SystemCursorListener(SystemCursor.VerticalResize);
        allResizeListener = new SystemCursorListener(SystemCursor.AllResize);
        splitPaneHorizontalSystemCursorListener = new SplitPaneSystemCursorListener(SystemCursor.HorizontalResize);
        splitPaneVerticalSystemCursorListener = new SplitPaneSystemCursorListener(SystemCursor.VerticalResize);
        scrollFocusListener = new ScrollFocusListener(stage);
        foregroundScrollFocusListener = new ScrollFocusListener(foregroundStage);

        bgColor.set(skin.getColor("bg"));

        root = new Container<>();
        root.setFillParent(true);
        root.fill();
        stage.addActor(root);

        var welcomeTable = new WelcomeTable();
        root.setActor(welcomeTable);
        welcomeTable.addAction(welcomeAction(welcomeTable));

//        shader = new ShaderProgram(spriteBatch.getShader().getVertexShaderSource(), Gdx.files.internal("test.frag").readString());
//        if (!shader.isCompiled()){
//            System.out.println(shader.getLog());
//        }
//        ShaderProgram.pedantic = false;
    }

    public static void loadParticle(FileHandle fileHandle) {
        particleEffect = new ParticleEffect();
        particleEffect.load(fileHandle, skin.getAtlas());
        particleEffect.setPosition(0, 0);

        activeEmitters.clear();
        for (var emitter : particleEffect.getEmitters()) {
            activeEmitters.put(emitter, true);
        }
    }

    public static void mergeParticle(FileHandle fileHandle) {
        var oldEmitters = new Array<ParticleEmitter>();
        var oldActiveEmitters = new ObjectMap<ParticleEmitter, Boolean>();
        for (var emitter : particleEffect.getEmitters()) {
            var oldEmitter = new ParticleEmitter(emitter);
            oldEmitters.add(oldEmitter);
            oldActiveEmitters.put(oldEmitter, activeEmitters.get(emitter));
        }

        particleEffect.load(fileHandle, skin.getAtlas());

        activeEmitters.clear();
        for (var emitter : particleEffect.getEmitters()) {
            activeEmitters.put(emitter, true);
        }

        particleEffect.getEmitters().addAll(oldEmitters);
        for (var emitter : oldEmitters) {
            activeEmitters.put(emitter, oldActiveEmitters.get(emitter));
        }
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
        viewport.apply();
        stage.draw();

//        time += Gdx.graphics.getDeltaTime();
//        spriteBatch.setShader(shader);
//        shader.bind();
//        shader.setUniformf("u_amount", 10);
//        shader.setUniformf("u_speed", .5f);
//        shader.setUniformf("u_time", time);
        viewportWidget.updateViewport(false);
        particlePreview.render();
//        spriteBatch.setShader(null);

        foregroundStage.act();
        viewport.apply();
        foregroundStage.draw();

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


    public static void addHandListener(Actor actor) {
        actor.addListener(handListener);
    }

    public static void addIbeamListener(Actor actor) {
        actor.addListener(ibeamListener);
    }

    private static ScrollFocusListener scrollFocusListener;
    private static ScrollFocusListener foregroundScrollFocusListener;

    public static void addScrollFocusListener(Actor actor) {
        actor.addListener(scrollFocusListener);
    }

    public static void addForegroundScrollFocusListener(Actor actor) {
        actor.addListener(foregroundScrollFocusListener);
    }

    public static void addHorizontalResizeListener(Actor actor) {
        actor.addListener(horizontalResizeListener);
    }

    public static void addVerticalResizeListener(Actor actor) {
        actor.addListener(verticalResizeListener);
    }

    public static void addNESWresizeListener(Actor actor) {
        actor.addListener(neswResizeListener);
    }

    public static void addNWSEresizeListener(Actor actor) {
        actor.addListener(nwseResizeListener);
    }

    public static void addAllResizeListener(Actor actor) {
        actor.addListener(allResizeListener);
    }

    public static void addSplitPaneHorizontalSystemCursorListener(Actor actor) {
        actor.addListener(splitPaneHorizontalSystemCursorListener);
    }

    public static void addSplitPaneVerticalSystemCursorListener(Actor actor) {
        actor.addListener(splitPaneVerticalSystemCursorListener);
    }

    public static PopTable addTooltip(Actor actor, String text, int align, float width, PopTableStyle popTableStyle) {
        return addTooltip(actor, text, align, width, true, popTableStyle);
    }

    public static PopTable addTooltip(Actor actor, String text, int align, PopTableStyle popTableStyle) {
        return addTooltip(actor, text, align, 0, false, popTableStyle);
    }

    private static PopTable addTooltip(Actor actor, String text, int align, float width, boolean defineWidth, PopTableStyle popTableStyle) {
        PopTable popTable = new PopTable(popTableStyle);
        var inputListener = new ClickListener() {
            boolean dismissed;
            Action showTableAction;

            {
                popTable.setModal(false);
                popTable.setHideOnUnfocus(true);
                popTable.setTouchable(Touchable.disabled);

                var label = new Label(text, skin);
                if (defineWidth) {
                    label.setWrap(true);
                    popTable.add(label).width(width);
                } else {
                    popTable.add(label);
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && popTable.isHidden() && !dismissed) {
                    if (fromActor == null || !event.getListenerActor().isAscendantOf(fromActor)) {
                        if (showTableAction == null) {
                            showTableAction = Actions.delay(.5f,
                                Actions.run(() -> {
                                    showTable(actor);
                                    showTableAction = null;
                                }));
                            actor.addAction(showTableAction);
                        }
                    }
                }
            }

            private void showTable(Actor actor) {
                if (actor instanceof Disableable) {
                    if (((Disableable) actor).isDisabled()) return;
                }

                popTable.show(foregroundStage);
                popTable.attachToActor(actor, align, align);

                popTable.moveToInsideStage();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    if (toActor == null || !toActor.isDescendantOf(event.getListenerActor())) {
                        if (!popTable.isHidden()) popTable.hide();
                        dismissed = false;
                        if (showTableAction != null) {
                            actor.removeAction(showTableAction);
                            showTableAction = null;
                        }
                    }

                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                dismissed = true;
                popTable.hide();
                if (showTableAction != null) {
                    actor.removeAction(showTableAction);
                    showTableAction = null;
                }
            }
        };
        actor.addListener(inputListener);
        return popTable;
    }
}
