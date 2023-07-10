package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.FloatArray;
import com.ray3k.particleparkpro.Core;
import com.ray3k.particleparkpro.widgets.Panel;

import static com.ray3k.particleparkpro.Core.*;

public class PreviewPanel extends Panel {
    private final FloatArray zoomLevels = new FloatArray(new float[] {1/6f, 1/4f, 1/3f, .5f, 2/3f, 1f, 1.5f, 2f, 3f, 4f, 6f});
    private int zoomLevelIndex = 5;
    private static final Vector2 temp = new Vector2();

    public PreviewPanel() {
        setTouchable(Touchable.enabled);

        var label = new Label("Preview", skin, "header");
        tabTable.add(label);

        var stack = new Stack();
        bodyTable.add(stack).grow();

        var image = new Image(skin, "black");
        stack.add(image);

        stack.add(viewportWidget);

        var table = new Table();
        table.setTouchable(Touchable.enabled);
        stack.add(table);

        table.top().pad(5);
        var button = new Button(skin, "settings");
        table.add(button).expandX().left();
        addHandListener(button);

        button = new Button(skin, "zoom-full");
        table.add(button);
        addHandListener(button);
        onChange(button, () -> {
            zoomLevelIndex = 5;
            previewViewport.setUnitsPerPixel(zoomLevels.get(zoomLevelIndex));
        });

        button = new Button(skin, "zoom-out");
        table.add(button);
        addHandListener(button);
        onChange(button, () -> {
            zoomLevelIndex = MathUtils.clamp(zoomLevelIndex + 1, 0, zoomLevels.size - 1);
            previewViewport.setUnitsPerPixel(zoomLevels.get(zoomLevelIndex));
        });

        button = new Button(skin, "zoom-in");
        table.add(button);
        addHandListener(button);
        onChange(button, () -> {
            zoomLevelIndex = MathUtils.clamp(zoomLevelIndex - 1, 0, zoomLevels.size - 1);
            previewViewport.setUnitsPerPixel(zoomLevels.get(zoomLevelIndex));
        });

        var dragListener = new DragListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Buttons.RIGHT) {
                    temp.set(x, y);
                    stack.localToScreenCoordinates(temp);
                    previewViewport.unproject(temp);
                    particlePreview.particleEffect.setPosition(temp.x, temp.y);
                }

                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                if (amountY > 0) {
                    zoomLevelIndex = MathUtils.clamp(zoomLevelIndex + 1, 0, zoomLevels.size - 1);
                    previewViewport.setUnitsPerPixel(zoomLevels.get(zoomLevelIndex));
                } else if (amountY < 0) {
                    zoomLevelIndex = MathUtils.clamp(zoomLevelIndex - 1, 0, zoomLevels.size - 1);
                    previewViewport.setUnitsPerPixel(zoomLevels.get(zoomLevelIndex));
                }
                return true;
            }

            float startX, startY;
            float cameraStartX, cameraStartY;

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                startX = x;
                startY = y;
                cameraStartX = previewCamera.position.x;
                cameraStartY = previewCamera.position.y;
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                if (Gdx.input.isButtonPressed(Buttons.LEFT) || Gdx.input.isButtonPressed(Buttons.MIDDLE)) {
                    temp.set(startX - x, startY - y);
                    temp.scl(zoomLevels.get(zoomLevelIndex));
                    previewCamera.position.set(cameraStartX + temp.x, cameraStartY + temp.y, 0);
                }

                if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
                    temp.set(x, y);
                    stack.localToScreenCoordinates(temp);
                    previewViewport.unproject(temp);
                    particlePreview.particleEffect.setPosition(temp.x, temp.y);
                }
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {

            }
        };
        dragListener.setButton(-1);
        dragListener.setTapSquareSize(5);
        table.addListener(dragListener);
        addScrollFocusListener(table);
    }
}
