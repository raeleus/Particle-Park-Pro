package com.ray3k.particleparkpro;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ray3k.particleparkpro.undo.UndoManager;
import com.ray3k.particleparkpro.widgets.NoCaptureKeyboardFocusListener;
import com.ray3k.particleparkpro.widgets.styles.Styles;
import com.ray3k.particleparkpro.widgets.tables.ClassicTable;
import com.ray3k.particleparkpro.widgets.tables.WelcomeTable;
import com.ray3k.particleparkpro.widgets.tables.WizardTable;
import com.ray3k.stripe.ViewportWidget;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static com.ray3k.particleparkpro.PresetActions.welcomeAction;
import static com.ray3k.particleparkpro.Settings.*;
import static com.ray3k.particleparkpro.Utils.*;

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
    public static String versionRaw;
    private Color bgColor = new Color();
    public static ShapeDrawer shapeDrawer;
    public static ViewportWidget viewportWidget;
    public static ParticlePreview particlePreview;
    public static Preferences preferences;
    public static ParticleEffect particleEffect;
    public static OrderedMap<ParticleEmitter, Boolean> activeEmitters;
    public static ParticleEmitter selectedEmitter;
    public static FileHandle vertShaderFile;
    public static FileHandle fragShaderFile;
    public static ShaderProgram shaderProgram;
    public static float time;
    public static ObjectMap<String, FileHandle> fileHandles;
    public static ObjectMap<String, Sprite> sprites;
    public static String defaultFileName;
    public static NoCaptureKeyboardFocusListener noCaptureKeyboardFocusListener;

    @Override
    public void create() {
        sizeWindowToScreenHeight(950/1080f, 1000/950f);

        versionRaw = Gdx.files.classpath("version").readString();
        version = "ver " + versionRaw;
        defaultFileName = "particle.p";

        preferences = Gdx.app.getPreferences("Particle Park Pro");
        fileHandles = new ObjectMap<>();
        sprites = new ObjectMap<>();

        logFile = Gdx.files.external(".particleparkpro/log.txt");
        logFile.mkdirs();
        logFile.delete();
        Gdx.app.setApplicationLogger(new TextFileApplicationLogger(logFile));

        Settings.initializeSettings();

        viewport = new ScreenViewport();
        previewCamera = new OrthographicCamera();
        previewViewport = new ScreenViewport(previewCamera);
        viewportWidget = new ViewportWidget(previewViewport);
        spriteBatch = new SpriteBatch();
        stage = new Stage(viewport, spriteBatch);
        foregroundStage = new Stage(viewport, spriteBatch);

        updateViewportScale(valueToUIscale(preferences.getFloat(NAME_SCALE, DEFAULT_SCALE)));

        skin = new Skin(Gdx.files.internal("skin/particleparkpro.json"));
        shapeDrawer = new ShapeDrawer(spriteBatch, skin.getRegion("white-pixel"));
        particlePreview = new ParticlePreview();
        Styles.initializeStyles();

        noCaptureKeyboardFocusListener = new NoCaptureKeyboardFocusListener();

        activeEmitters = new OrderedMap<>();
        loadParticle(Gdx.files.internal("flame.p"));
        selectedEmitter = particleEffect.getEmitters().first();

        Gdx.input.setInputProcessor(stage);

        Listeners.initializeListeners();

        bgColor.set(skin.getColor("bg"));

        root = new Container<>();
        root.setFillParent(true);
        root.fill();
        stage.addActor(root);

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (event.getTarget().getListeners().contains(noCaptureKeyboardFocusListener, true)) return false;
                if (stage.getKeyboardFocus() == null) return false;
                if (event.getTarget() == stage.getKeyboardFocus()) return false;
                if (event.getTarget().isDescendantOf(stage.getKeyboardFocus())) return false;

                stage.setKeyboardFocus(null);
                return false;
            }
        });

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Settings.undoPrimaryShortcut && holdingModifiers(Settings.undoPrimaryModifiers) && UndoManager.hasUndo()) {
                    UndoManager.undo();
                } else if (keycode == Settings.undoSecondaryShortcut && holdingModifiers(Settings.undoSecondaryModifiers) && UndoManager.hasUndo()) {
                    UndoManager.undo();
                }

                if (keycode == Settings.redoPrimaryShortcut && holdingModifiers(Settings.redoPrimaryModifiers) && UndoManager.hasRedo()) {
                    UndoManager.redo();
                } else if (keycode == Settings.redoSecondaryShortcut && holdingModifiers(Settings.redoSecondaryModifiers) && UndoManager.hasRedo()) {
                    UndoManager.redo();
                }
                return false;
            }
        });

        switch (preferences.getString(NAME_OPEN_TO_SCREEN, DEFAULT_OPEN_TO_SCREEN)) {
            case "Welcome":
                var welcomeTable = new WelcomeTable();
                root.setActor(welcomeTable);
                welcomeTable.addAction(welcomeAction(welcomeTable));
                break;
            case "Classic":
                var classicTable = new ClassicTable();
                root.setActor(classicTable);
                classicTable.addAction(welcomeAction(classicTable));
                break;
            case "Wizard":
                var wizardTable = new WizardTable();
                root.setActor(wizardTable);
                wizardTable.addAction(welcomeAction(wizardTable));
                break;
        }

        initShaderProgram();
    }

    private boolean holdingModifiers(IntArray modifiers) {
        var doNotInclude = new IntArray(new int[]{Keys.SHIFT_LEFT, Keys.CONTROL_LEFT, Keys.ALT_LEFT});
        for (int i = 0; i < modifiers.size; i++) {
            var modifier = modifiers.get(i);
            if (!Gdx.input.isKeyPressed(modifier)) return false;
            doNotInclude.removeValue(modifier);
        }

        for (int i = 0; i < doNotInclude.size; i++) {
            var modifier = doNotInclude.get(i);
            if (Gdx.input.isKeyPressed(modifier)) return false;
        }
        return true;
    }

    @Override
    public void resize(int width, int height) {
        if (width + height > 0) {
            viewport.update(width, height, true);
            windowResized = true;
        }
    }

    @Override
    public void render() {
        ScreenUtils.clear(bgColor);

        stage.act();
        viewport.apply();
        stage.draw();

        time += Gdx.graphics.getDeltaTime();
        spriteBatch.setShader(shaderProgram);
        if (shaderProgram != null) {
            shaderProgram.bind();
            shaderProgram.setUniformf("u_amount", 10);
            shaderProgram.setUniformf("u_speed", .5f);
            shaderProgram.setUniformf("u_time", time);
        }
        viewportWidget.updateViewport(false);
        particlePreview.render();
        spriteBatch.setShader(null);

        foregroundStage.act();
        viewport.apply();
        foregroundStage.draw();

        windowResized = false;
    }
}
