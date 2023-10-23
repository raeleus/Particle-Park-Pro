package com.ray3k.particleparkpro.widgets.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;
import com.ray3k.particleparkpro.Core;
import com.ray3k.particleparkpro.FileDialogs;
import com.ray3k.particleparkpro.Settings;
import com.ray3k.particleparkpro.widgets.Panel;
import com.ray3k.particleparkpro.widgets.poptables.PopError;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Settings.*;

public class SummaryPanel extends Panel {
    private Table scrollTable;
    public static SummaryPanel summaryPanel;
    private ScrollPane scrollPane;
    private static final Vector2 temp = new Vector2();

    public SummaryPanel() {
        summaryPanel = this;
        setTouchable(Touchable.enabled);

        var label = new Label("Summary", skin, "header");
        tabTable.add(label);

        bodyTable.defaults().space(5);
        scrollTable = new Table();
        scrollPane = new ScrollPane(scrollTable, skin, "emitter-properties");
        scrollPane.setFlickScroll(false);
        bodyTable.add(scrollPane).grow();
        addScrollFocusListener(scrollPane);

        populateScrollTable();
    }

    public void populateScrollTable() {
        scrollTable.clearChildren(true);
        scrollTable.defaults().space(10);

        var label = new Label("Save Particle Effect?", skin, "bold");
        scrollTable.add(label);

        scrollTable.row();
        var textButton = new TextButton("Save", skin);
        scrollTable.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            var useFileExtension = preferences.getBoolean(NAME_PRESUME_FILE_EXTENSION, DEFAULT_PRESUME_FILE_EXTENSION);
            var filterPatterns = useFileExtension ? new String[] {"p"} : new String[0];
            var saveHandle = FileDialogs.saveDialog(getDefaultSavePath(), defaultFileName, filterPatterns, new String[] {"Particle Files"});

            if (saveHandle != null) {
                Settings.setDefaultSavePath(saveHandle.parent());
                defaultFileName = saveHandle.name();

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
            }
        });
    }
}
