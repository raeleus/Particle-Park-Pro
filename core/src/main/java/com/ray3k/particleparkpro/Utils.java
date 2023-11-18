package com.ray3k.particleparkpro;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpriteMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.*;
import com.ray3k.particleparkpro.widgets.poptables.PopError;
import com.ray3k.particleparkpro.widgets.poptables.PopImageError;
import com.ray3k.particleparkpro.widgets.tables.ClassicTable;
import com.ray3k.particleparkpro.widgets.tables.WizardTable;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Settings.*;
import static com.ray3k.particleparkpro.widgets.panels.EffectEmittersPanel.effectEmittersPanel;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.emitterPropertiesPanel;

/**
 * A convenience class with various static methods that perform various utility tasks throughout Particle Park Pro.
 */
public class Utils {
    public static void openFileExplorer(FileHandle startDirectory) throws IOException {
        if (startDirectory.exists()) {
            File file = startDirectory.file();
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } else {
            throw new IOException("Directory doesn't exist: " + startDirectory.path());
        }
    }

    public static UIscale valueToUIscale(float value) {
        for (var scale : UIscale.values()) {
            if (MathUtils.isEqual(scale.multiplier, value)) return scale;
        }
        return UIscale.SCALE_1X;
    }

    public static void updateViewportScale(UIscale uiScale) {
        viewport.setUnitsPerPixel(uiScale.multiplier);
        previewViewport.setUnitsPerPixel(uiScale.multiplier);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        viewportWidget.updateViewport(false);
    }

    public static void checkVersion(VersionUpdateRunnable updater) {
        if (!preferences.getBoolean(Settings.NAME_CHECK_FOR_UPDATES, Settings.DEFAULT_CHECK_FOR_UPDATES)) return;

        Thread thread = new Thread(() -> {
            HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
            HttpRequest httpRequest =
                requestBuilder.newRequest()
                              .method(HttpMethods.GET)
                              .url("https://raw.githubusercontent.com/raeleus/Particle-Park-Pro/master/core/src/main/resources/version")
                              .build();

            Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    var newVersion = httpResponse.getResultAsString();
                    Gdx.app.postRunnable(() -> updater.versionUpdateAvailable(newVersion));
                }

                @Override
                public void failed(Throwable t) {
                    updater.versionUpdateAvailable(version);
                }

                @Override
                public void cancelled() {
                    updater.versionUpdateAvailable(version);
                }
            });
        });

        thread.start();
    }

    public static void initShaderProgram() {
        var vertex = vertShaderFile == null ? spriteBatch.getShader().getVertexShaderSource() : vertShaderFile.readString();
        var frag = fragShaderFile == null ? spriteBatch.getShader().getFragmentShaderSource() : fragShaderFile.readString();
        shaderProgram = new ShaderProgram(vertex, frag);
        ShaderProgram.pedantic = false;
    }

    public static boolean loadParticle(FileHandle fileHandle) {
        var newParticleEffect = new ParticleEffect();
        try {
            if (fileHandle.type() != FileType.Internal) newParticleEffect.load(fileHandle, fileHandle.parent());
            else {
                var textureAtlas = new TextureAtlas(Gdx.files.internal("default/default.atlas"));
                newParticleEffect.load(fileHandle, textureAtlas);
                var defaultImageHandle = Gdx.files.internal("particle.png");
                fileHandles.put(defaultImageHandle.name(), defaultImageHandle);
            }
            newParticleEffect.setPosition(0, 0);
        } catch (Exception e) {
            Gdx.input.setInputProcessor(foregroundStage);
            Gdx.graphics.setSystemCursor(SystemCursor.Arrow);

            var pop = new PopImageError("Error loading particle file. Ensure that all associated images are saved locally.", e.getMessage(), fileHandle, false);
            pop.show(foregroundStage);

            Gdx.app.error(Core.class.getName(), "Error loading particle file.", e);
            return false;
        }

        disposeParticleEffect();
        sprites.clear();
        fileHandles.clear();
        activeEmitters.clear();

        particleEffect = newParticleEffect;

        for (var emitter : particleEffect.getEmitters()) {
            activeEmitters.put(emitter, true);
            for (int i = 0; i < emitter.getImagePaths().size; i++) {
                var path = emitter.getImagePaths().get(i);
                var imageHandle = fileHandle.parent().child(path);
                fileHandles.put(path, imageHandle);
                if (i < emitter.getSprites().size) sprites.put(path, emitter.getSprites().get(i));
            }
        }
        return true;
    }

    public static void disposeParticleEffect() {
        if (particleEffect == null) return;
        for (int i = 0, n = particleEffect.getEmitters().size; i < n; i++) {
            ParticleEmitter emitter = particleEffect.getEmitters().get(i);
            for (Sprite sprite : emitter.getSprites()) {
                sprite.getTexture().dispose();
            }
        }
    }

    public static boolean mergeParticle(FileHandle fileHandle) {
        var newParticleEffect = new ParticleEffect();
        var oldEmitters = new Array<ParticleEmitter>();
        var oldActiveEmitters = new ObjectMap<ParticleEmitter, Boolean>();
        try {
            for (var emitter : particleEffect.getEmitters()) {
                var oldEmitter = new ParticleEmitter(emitter);
                oldEmitters.add(oldEmitter);
                oldActiveEmitters.put(oldEmitter, activeEmitters.get(emitter));
            }

            if (fileHandle.type() != FileType.Internal) newParticleEffect.load(fileHandle, fileHandle.parent());
            else {
                var textureAtlas = new TextureAtlas(Gdx.files.internal("default/default.atlas"));
                newParticleEffect.load(fileHandle, textureAtlas);
                var defaultImageHandle = Gdx.files.internal("particle.png");
                fileHandles.put(defaultImageHandle.name(), defaultImageHandle);
            }
        } catch (Exception e) {
            Gdx.input.setInputProcessor(foregroundStage);
            Gdx.graphics.setSystemCursor(SystemCursor.Arrow);

            var pop = new PopImageError("Error merging particle file. Ensure that all associated images are saved locally.", e.getMessage(), fileHandle, true);
            pop.show(foregroundStage);

            Gdx.app.error(Core.class.getName(), "Error merging particle file.", e);
            return false;
        }

        activeEmitters.clear();
        particleEffect = newParticleEffect;

        for (var emitter : particleEffect.getEmitters()) {
            emitter.setPosition(oldEmitters.first().getX(), oldEmitters.first().getY());
            activeEmitters.put(emitter, true);

            for (int i = 0; i < emitter.getImagePaths().size; i++) {
                var path = emitter.getImagePaths().get(i);
                var imageHandle = fileHandle.parent().child(path);
                fileHandles.put(path, imageHandle);
                if (i < emitter.getSprites().size) sprites.put(path, emitter.getSprites().get(i));
            }
        }

        particleEffect.getEmitters().addAll(oldEmitters);
        for (var emitter : oldEmitters) {
            activeEmitters.put(emitter, oldActiveEmitters.get(emitter));
        }

        effectEmittersPanel.populateEmitters();
        effectEmittersPanel.updateDisableableWidgets();
        emitterPropertiesPanel.populateScrollTable(null);

        return true;
    }

    public static ParticleEmitter createNewEmitter() {
        var emitter = new ParticleEmitter();
        emitter.setName("Untitled");

        var fileHandle = Gdx.files.internal("particle.png");
        var path = fileHandle.name();
        emitter.getImagePaths().add(path);
        fileHandles.put(path, fileHandle);
        var sprite = new Sprite(new Texture(fileHandle));
        sprites.put(path, sprite);
        emitter.getSprites().add(sprite);

        emitter.setMaxParticleCount(200);

        emitter.getDuration().setActive(true);
        emitter.getDuration().setLow(3000);

        emitter.getEmission().setActive(true);
        emitter.getEmission().setHigh(40);

        emitter.getLife().setActive(true);
        emitter.getLife().setHigh(500);

        emitter.getSpawnShape().setActive(true);
        emitter.getSpawnWidth().setActive(true);
        emitter.getSpawnHeight().setActive(true);

        emitter.getXScale().setActive(true);
        emitter.getXScale().setHigh(32);
        emitter.getYScale().setActive(false);

        emitter.getVelocity().setActive(true);
        emitter.getVelocity().setHigh(100);

        emitter.getAngle().setActive(true);
        emitter.getAngle().setHighMin(0);
        emitter.getAngle().setHighMax(360);

        emitter.getTint().setActive(true);
        emitter.getTint().getColors()[0] = 1;
        emitter.getTint().getColors()[1] = 0;
        emitter.getTint().getColors()[2] = 0;

        emitter.getTransparency().setActive(true);
        emitter.getTransparency().setHigh(1);
        emitter.getTransparency().setTimeline(new float[] {0, 1});
        emitter.getTransparency().setScaling(new float[] {1, 0});

        emitter.setAdditive(true);
        emitter.setContinuous(true);
        emitter.setSpriteMode(SpriteMode.single);
        emitter.setPosition(selectedEmitter.getX(), selectedEmitter.getY());

        return emitter;
    }

    public static int calcParticleCount() {
        var count = 0;
        for (var emitter : particleEffect.getEmitters()) {
            count += emitter.getActiveCount();
        }
        return count;
    }

    public static void removeUnusedImageFiles() {
        var names = new ObjectSet<String>();
        for (var emitter : particleEffect.getEmitters()) {
            names.addAll(emitter.getImagePaths());
        }

        var iter = fileHandles.iterator();
        while (iter.hasNext) {
            var entry = iter.next();
            if (!names.contains(entry.value.name())) iter.remove();
        }
    }

    public static void refreshUndoButtons() {
        if (ClassicTable.classicTable != null) ClassicTable.classicTable.refreshUndo();
        if (WizardTable.wizardTable != null) WizardTable.wizardTable.refreshUndo();
    }

    public enum UIscale {
        SCALE_1X("1x", 1f), SCALE_1_5X("1.5x", 1/1.5f), SCALE_2X("2x", 1/2f), SCALE_3X("3x", 1/3f), SCALE_4X("4x", 1/4f);

        public String text;
        public float multiplier;

        UIscale(String text, float multiplier) {
            this.text = text;
            this.multiplier = multiplier;
        }
    }

    public interface VersionUpdateRunnable {
        void versionUpdateAvailable(String newVersion);
    }

    public static void centerWindow() {
        var graphics = (Lwjgl3Graphics) Gdx.graphics;
        var displayMode = graphics.getDisplayMode();
        var window = graphics.getWindow();
        var monitor = graphics.getMonitor();
        window.setPosition(monitor.virtualX + displayMode.width / 2 - graphics.getWidth() / 2, monitor.virtualY + displayMode.height / 2 - graphics.getHeight() / 2);
    }

    public static void sizeWindowToFit(int maxWidth, int maxHeight, int displayBorder) {
        var displayMode = Gdx.graphics.getDisplayMode();
        int width = Math.min(displayMode.width - displayBorder * 2, maxWidth);
        int height = Math.min(displayMode.height - displayBorder * 2, maxHeight);
        Gdx.graphics.setWindowedMode(width, height);
        centerWindow();
    }

    public static void sizeWindowToScreenHeight(float percentageOfScreenHeight, float widthRatio) {
        var displayMode = Gdx.graphics.getDisplayMode();
        var height = MathUtils.floor(displayMode.height * percentageOfScreenHeight);
        var width = MathUtils.floor(widthRatio * height);
        sizeWindowToFit(Math.min(width, displayMode.width), Math.min(height, displayMode.height), 0);
    }

    public static boolean isWindowFocused() {
        var window = ((Lwjgl3Graphics)Gdx.graphics).getWindow();
        return GLFW.glfwGetWindowAttrib(window.getWindowHandle(), GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE;
    }

    public static void saveParticleEffect() {
        var useFileExtension = preferences.getBoolean(NAME_PRESUME_FILE_EXTENSION, DEFAULT_PRESUME_FILE_EXTENSION);
        var filterPatterns = useFileExtension ? new String[] {"p"} : null;
        var saveHandle = FileDialogs.saveDialog("Save", getDefaultSavePath(), defaultFileName, filterPatterns, "Particle Files (*.p)");

        if (saveHandle != null) {
            if (preferences.getBoolean(NAME_PRESUME_FILE_EXTENSION, DEFAULT_PRESUME_FILE_EXTENSION) && !saveHandle.extension().equals("p"))
                saveHandle = saveHandle.sibling(saveHandle.name() + ".p");

            Settings.setDefaultSavePath(saveHandle.parent());
            defaultFileName = saveHandle.name();

            //enable all emitters for export
            particleEffect.getEmitters().clear();
            for (var entry : activeEmitters.entries()) {
                particleEffect.getEmitters().add(entry.key);
            }

            Writer fileWriter = null;
            try {
                fileWriter = new FileWriter(saveHandle.file());
                particleEffect.save(fileWriter);
            } catch (IOException e) {
                var error = "Error saving particle file.";
                var pop = new PopError(error, e.getMessage());
                pop.show(stage);

                Gdx.app.error(Core.class.getName(), error, e);
            } finally {
                StreamUtils.closeQuietly(fileWriter);
            }

            for (var fileHandle : fileHandles.values()) {
                if (fileHandle.parent().equals(saveHandle.parent())) break;
                try {
                    fileHandle.copyTo(saveHandle.parent().child(fileHandle.name()));
                } catch (GdxRuntimeException e) {
                    var error = "Error copying files to save location.";
                    var pop = new PopError(error, e.getMessage());
                    pop.show(stage);

                    Gdx.app.error(Core.class.getName(), error, e);
                }
            }

            //reset enabled/disabled emitters
            particleEffect.getEmitters().clear();
            for (var entry : activeEmitters.entries()) {
                if (entry.value) particleEffect.getEmitters().add(entry.key);
            }
        }
    }
}
