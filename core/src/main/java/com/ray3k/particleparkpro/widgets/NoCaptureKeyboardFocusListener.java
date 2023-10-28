package com.ray3k.particleparkpro.widgets;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class NoCaptureKeyboardFocusListener implements EventListener {
    @Override
    public boolean handle(Event event) {
        return false;
    }
}
