package com.ray3k.particleparkpro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import static com.ray3k.particleparkpro.Core.*;

public class ParticlePreview {
    public ParticleEffect particleEffect;

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
