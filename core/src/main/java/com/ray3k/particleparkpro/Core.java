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
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ray3k.particleparkpro.runnables.OpenRunnable;
import com.ray3k.particleparkpro.runnables.SaveAsRunnable;
import com.ray3k.particleparkpro.runnables.SaveRunnable;
import com.ray3k.particleparkpro.shortcuts.KeyMap;
import com.ray3k.particleparkpro.shortcuts.Shortcut;
import com.ray3k.particleparkpro.shortcuts.ShortcutManager;
import com.ray3k.particleparkpro.runnables.RedoShortcutRunnable;
import com.ray3k.particleparkpro.runnables.UndoShortcutRunnable;
import com.ray3k.particleparkpro.widgets.NoCaptureKeyboardFocusListener;
import com.ray3k.particleparkpro.undo.UndoManager;
import com.ray3k.particleparkpro.widgets.tables.ClassicTable;
import com.ray3k.particleparkpro.widgets.tables.WelcomeTable;
import com.ray3k.particleparkpro.widgets.tables.WizardTable;
import com.ray3k.stripe.ViewportWidget;
import space.earlygrey.shapedrawer.ShapeDrawer;
import static com.ray3k.particleparkpro.PresetActions.welcomeAction;
import static com.ray3k.particleparkpro.Settings.*;
import static com.ray3k.particleparkpro.Utils.*;

/**
 * The primary ApplicationAdapter for the app. It also holds static variables for the utility objects shared by all
 * classes.
 */
public class Core extends ApplicationAdapter {
    public static Skin skin;

    /**
     * The stage used for all UI elements except for pop up dialogs and tooltips. This is to ensure that anything that
     * can overlap the ParticleEffect preview is rendered above it correctly.
     */
    public static Stage stage;

    /**
     * The stage used for pop up dialogs and tooltips. This is to ensure that anything that can overlap the
     * ParticleEffect preview is rendered above it correctly.
     */
    public static Stage foregroundStage;

    public static SpriteBatch spriteBatch;

    /***
     * Indicates if a screen resize has happened during this frame. Used for transitions to ensure positions are correct.
     */
    public static boolean windowResized;

    /**
     * Viewport used for the UI encompassing the entire window.
     */
    public static ScreenViewport viewport;

    /**
     * Used for the ParticleEffect preview. This viewport is repositioned and scaled based on the viewport widget
     * embedded in the UI.
     */
    public static ScreenViewport previewViewport;

    /**
     * Camera for the ParticleEffect preview. This is repositionable by the user.
     */
    public static OrthographicCamera previewCamera;

    public static Container<Actor> root;

    /**
     * The version number prepended with "ver " to be used throughout the app.
     */
    public static String version;

    /**
     *  The version number read directly from file.
     */
    public static String versionRaw;

    /**
     * The background color of the app.
     */
    private final Color bgColor = new Color();

    public static ShapeDrawer shapeDrawer;
    public static ViewportWidget viewportWidget;

    /**
     * The object that handles the drawing of the ParticleEffect preview.
     */
    public static ParticlePreview particlePreview;
    public static Preferences preferences;
    public static ParticleEffect particleEffect;

    /**
     * The collection of all emitters for the particle effect. When an emitter is disabled, it is marked as false in
     * this map and removed from the particleEffect. All active emitters are exported when the user clicks "Save".
     */
    public static OrderedMap<ParticleEmitter, Boolean> activeEmitters;

    /**
     * The currently selected emitter. The user can edit the properties of this emitter in the EmitterPropertiesPanel.
     */
    public static ParticleEmitter selectedEmitter;

    /**
     * The currently loaded vert shader for the ParticleEffect preview.
     */
    public static FileHandle vertShaderFile;

    /**
     * The currently loaded frag shader for the ParticleEffect preview.
     */
    public static FileHandle fragShaderFile;

    /**
     * The shader program for the ParticleEffect preview.
     */
    public static ShaderProgram shaderProgram;

    /**
     * The time that has passed since the last frame. This is passed to the shader.
     */
    public static float time;

    /**
     * The collection of all the image fileHandles loaded for the ParticleEffect. The FileHandles are keyed by the name
     * of the file.
     */
    public static ObjectMap<String, FileHandle> fileHandles;

    /**
     * The collection of all sprites created for the ParticleEffect. A sprite is created for every image loaded. Each
     * sprite is keyed by its associated file name.
     */
    public static ObjectMap<String, Sprite> sprites;

    /**
     * The default name to be used when the user saves the ParticleEffect.
     */
    public static String defaultFileName;
    public static FileHandle openFileFileHandle;
    public static NoCaptureKeyboardFocusListener noCaptureKeyboardFocusListener;
    public static ShortcutManager shortcutManager;
    public static KeyMap keyMap;

    public static SaveAsRunnable saveAsRunnable;
    public static SaveRunnable saveRunnable;
    public static OpenRunnable openRunnable;

    /**
     * The maximum number of particles recorded in 5 second intervals. Used for the preview stats and the SummaryPanel.
     */
    public static int maxParticleCount;

    /**
     * The number of decimal places used in the spinners used throughout the app.
     */
    public static final int SPINNER_DECIMAL_PLACES = 2;

    public static String openTable;

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

        PreviewSettings.initializeSettings();

        viewport = new ScreenViewport();
        previewCamera = new OrthographicCamera();
        previewViewport = new ScreenViewport(previewCamera);
        viewportWidget = new ViewportWidget(previewViewport);
        spriteBatch = new SpriteBatch();
        stage = new Stage(viewport, spriteBatch);
        foregroundStage = new Stage(viewport, spriteBatch);

        openRunnable = new OpenRunnable();
        saveAsRunnable = new SaveAsRunnable();
        saveRunnable = new SaveRunnable();
        saveAsRunnable.setSaveRunnable(saveRunnable);
        saveRunnable.setSaveAsRunnable(saveAsRunnable);

        initKeyMap();
        shortcutManager = new ShortcutManager();
        shortcutManager.setKeyMap(keyMap);

        updateViewportScale(valueToUIscale(preferences.getFloat(NAME_SCALE, DEFAULT_SCALE)));

        SkinLoader.loadSkin();
        shapeDrawer = new ShapeDrawer(spriteBatch, skin.getRegion("white-pixel"));
        particlePreview = new ParticlePreview();

        activeEmitters = new OrderedMap<>();
        loadParticle(Gdx.files.internal("flame.p"));
        selectedEmitter = particleEffect.getEmitters().first();

        Listeners.initializeListeners();

        bgColor.set(skin.getColor("bg"));

        populate(null);

        initShaderProgram();
    }

    public static void populate(String openTable) {
        stage.clear();
        foregroundStage.clear();

        root = new Container<>();
        root.setFillParent(true);
        root.fill();
        stage.addActor(root);

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (event.getTarget().getListeners().contains(Listeners.noCaptureKeyboardFocusListener, true)) return false;
                if (stage.getKeyboardFocus() == null) return false;
                if (event.getTarget() == stage.getKeyboardFocus()) return false;
                if (event.getTarget().isDescendantOf(stage.getKeyboardFocus())) return false;

                stage.setKeyboardFocus(null);
                return false;
            }
        });

        stage.addListener(shortcutManager);

        if (openTable == null) openTable = preferences.getString(NAME_OPEN_TO_SCREEN, DEFAULT_OPEN_TO_SCREEN);

        switch (openTable) {
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

        Gdx.input.setInputProcessor(stage);
    }

    private void initKeyMap () {
        keyMap = new KeyMap();
        Array<Shortcut> shortcuts = new Array<>();

        shortcuts.add(createShortcut("Undo", "Keyboard shortcut for the Undo action.", DEFAULT_UNDO_PRIMARY_KEYBIND, null, GLOBAL_SCOPE, new UndoShortcutRunnable()));
        shortcuts.add(createShortcut("Redo", "Keyboard shortcut for the Redo action.", DEFAULT_REDO_PRIMARY_KEYBIND, DEFAULT_REDO_SECONDARY_KEYBIND, GLOBAL_SCOPE, new RedoShortcutRunnable()));
        shortcuts.add(createShortcut("Save As", "Keyboard shortcut for the Save As action.", DEFAULT_SAVE_AS_PRIMARY_KEYBIND, null, GLOBAL_SCOPE, saveAsRunnable));
        shortcuts.add(createShortcut("Save", "Keyboard shortcut for the Save action.", DEFAULT_SAVE_PRIMARY_KEYBIND, null, GLOBAL_SCOPE, saveRunnable));
        shortcuts.add(createShortcut("Open", "Keyboard shortcut for the Open action.", DEFAULT_OPEN_PRIMARY_KEYBIND, null, GLOBAL_SCOPE, openRunnable));

        // Classic only keybinds
//        shortcuts.add(createShortcut("(Classic) Classic", "Hello Classic", primaryKeybind, secondaryKeybind, CLASSIC_SCOPE, () -> System.out.println("Hello Classic")));

        // Wizard only keybinds
//        shortcuts.add(createShortcut("(Wizard) Hello", "Save things", primaryKeybind, secondaryKeybind, WIZARD_SCOPE, () -> System.out.println("Hello Wizard")));

        keyMap.addAll(shortcuts);
    }

    public static void initShaderProgram() {
        var vertex = vertShaderFile == null ? spriteBatch.getShader().getVertexShaderSource() : vertShaderFile.readString();
        var frag = fragShaderFile == null ? spriteBatch.getShader().getFragmentShaderSource() : fragShaderFile.readString();
        shaderProgram = new ShaderProgram(vertex, frag);
        ShaderProgram.pedantic = false;
    }

    private static boolean holdingModifiers(IntArray modifiers) {
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
