package com.ray3k.particleparkpro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static com.ray3k.particleparkpro.Core.*;

public class ParticlePreview {
    public static float pixelsPerMeter;
    public static float deltaMultiplier;
    public static final Color backgroundColor = new Color(Color.BLACK);
    public static boolean statisticsEnabled;
    public static Texture previewImageTexture;
    public static boolean showResizeInterface;
    public static float previewImageX;
    public static float previewImageY;
    public static float previewImageWidth;
    public static float previewImageHeight;
    public static boolean axesEnabled;
    public static final Color axesColor = new Color(Color.BLUE);
    public static boolean gridEnabled;
    public static float gridMajorGridlines = 100;
    public static float gridMinorGridlines = 25;
    public static final Color gridColor = new Color(Color.LIGHT_GRAY);
    private static final Vector2 temp = new Vector2();
    public static boolean pause;

    public void render() {
        spriteBatch.setProjectionMatrix(previewViewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.setColor(Color.WHITE);

        //draw preview
        if (previewImageTexture != null) spriteBatch.draw(previewImageTexture, previewImageX, previewImageY, previewImageWidth, previewImageHeight);

        //calculate world coordinates
        temp.set(previewViewport.getScreenX(), Gdx.graphics.getHeight() - previewViewport.getScreenY());
        previewViewport.unproject(temp);
        float left = temp.x;
        float bottom = temp.y;

        temp.set(previewViewport.getScreenX() + previewViewport.getScreenWidth(), Gdx.graphics.getHeight() - (previewViewport.getScreenY() + previewViewport.getScreenHeight()));
        previewViewport.unproject(temp);
        float right = temp.x;
        float top = temp.y;

        if (gridEnabled) {
            //draw major grid
            shapeDrawer.setColor(gridColor);
            shapeDrawer.setDefaultLineWidth(2 * previewViewport.getUnitsPerPixel());
            for (float x = left - left % gridMajorGridlines; x < right; x += gridMajorGridlines) {
                shapeDrawer.line(x, bottom, x, top);
            }
            for (float y = bottom - bottom % gridMajorGridlines; y < top; y += gridMajorGridlines) {
                shapeDrawer.line(left, y, right, y);
            }

            //draw minor grid
            shapeDrawer.setColor(gridColor);
            shapeDrawer.setDefaultLineWidth(1 * previewViewport.getUnitsPerPixel());
            for (float x = left - left % gridMinorGridlines; x < right; x += gridMinorGridlines) {
                shapeDrawer.line(x, bottom, x, top);
            }
            for (float y = bottom - bottom % gridMinorGridlines; y < top; y += gridMinorGridlines) {
                shapeDrawer.line(left, y, right, y);
            }
        }

        if (axesEnabled) {
            //draw axes
            shapeDrawer.setColor(axesColor);
            shapeDrawer.setDefaultLineWidth(3 * previewViewport.getUnitsPerPixel());
            if (bottom <= 0) shapeDrawer.line(left, 0, right, 0);
            if (left <= 0) shapeDrawer.line(0, bottom, 0, top);
        }

        if (!pause) {
            particleEffect.update(Gdx.graphics.getDeltaTime());
            particleEffect.draw(spriteBatch);
        }

        spriteBatch.end();
    }
}
