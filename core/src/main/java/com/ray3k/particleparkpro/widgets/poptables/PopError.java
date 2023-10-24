package com.ray3k.particleparkpro.widgets.poptables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.ray3k.particleparkpro.Core;
import com.ray3k.stripe.PopTable;
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Settings.*;

public class PopError extends PopTable {
    private String message;
    private String error;

    public PopError(String message, String error) {
        super(skin.get(WindowStyle.class));

        this.message = message;
        this.error = error;

        populate();
    }

    private void populate() {
        clearChildren();
        pad(20);
        defaults().space(10);

        var label = new Label("Particle Park Pro encountered an error:", skin, "bold");
        add(label);

        row();
        label = new Label(message, skin);
        label.setWrap(true);
        add(label).growX();

        row();
        var scrollPane = new ScrollPane(null, skin);
        add(scrollPane).grow();

        label = new Label(error, skin);
        label.setColor(Color.RED);
        scrollPane.setActor(label);

        row();
        var table = new Table();
        table.defaults().uniformX().fillX().space(10);
        add(table);

        var textButton = new TextButton("Close", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            hide();
        });

        textButton = new TextButton("Open log", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            try {
                Core.openFileExplorer(logFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            hide();
        });
    }
}