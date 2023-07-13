package com.ray3k.particleparkpro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.utils.Array;

import static com.ray3k.particleparkpro.Core.*;

public class ParticlePreview {
    public static ParticleEffect particleEffect;
    public static float pixelsPerMeter;
    public static float deltaMultiplier;
    public static final Color backgroundColor = new Color(Color.BLACK);
    public static String previewImagePath;
    public static Texture previewImage;
    public static boolean showResizeInterface;
    public static float previewImageX;
    public static float previewImageY;
    public static float previewImageWidth;
    public static float previewImageHeight;
    public static boolean gridEnabled;
    public static float gridMajorGridlines;
    public static float gridMinorGridlines;
    public static final Color gridColor = new Color(Color.LIGHT_GRAY);
    public static String shaderVertexText;
    public static Shader shaderVertex;
    public static String shaderFragText;
    public static String shaderFrag;
    public static final Array<String> shaderExtraTextureUnits = new Array<>();

    public ParticlePreview() {
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("flame.p"), skin.getAtlas());
        particleEffect.setPosition(0, 0);
    }

    public void render() {
        spriteBatch.setProjectionMatrix(previewViewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.setColor(Color.WHITE);

        shapeDrawer.setColor(Color.RED);
        shapeDrawer.rectangle(-25f, -25f, 50f, 50f);

        particleEffect.update(Gdx.graphics.getDeltaTime());
        particleEffect.draw(spriteBatch);

        spriteBatch.end();
    }
}
