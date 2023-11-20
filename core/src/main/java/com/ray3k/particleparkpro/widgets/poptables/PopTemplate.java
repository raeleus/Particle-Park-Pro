package com.ray3k.particleparkpro.widgets.poptables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.Align;
import com.ray3k.particleparkpro.Core;
import com.ray3k.particleparkpro.Utils;
import com.ray3k.particleparkpro.undo.UndoManager;
import com.ray3k.particleparkpro.widgets.panels.EffectEmittersPanel;
import com.ray3k.stripe.PopTable;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.Listeners.*;
import static com.ray3k.particleparkpro.widgets.panels.EmitterPropertiesPanel.emitterPropertiesPanel;
import static com.ray3k.particleparkpro.widgets.styles.Styles.tooltipBottomArrowStyle;

/**
 * PopTable used to open a template file.
 */
public class PopTemplate extends PopTable {
    public PopTemplate() {
        super(Core.skin.get(WindowStyle.class));

        setDraggable(false);
        setHideOnUnfocus(true);
        setKeepSizedWithinStage(true);
        addListener(new TableShowHideListener() {
            @Override
            public void tableShown(Event event) {
                Gdx.input.setInputProcessor(foregroundStage);
            }

            @Override
            public void tableHidden(Event event) {
                Gdx.input.setInputProcessor(stage);
            }
        });

        final int itemSpacing = 5;

        var scrollTable = new Table();
        var scrollPane = new ScrollPane(scrollTable, skin);
        scrollPane.setFlickScroll(false);
        add(scrollPane).grow();
        addForegroundScrollFocusListener(scrollPane);

        //Blank
        scrollTable.pad(5);
        scrollTable.defaults().space(itemSpacing).left();
        var textButton = new TextButton("Blank", skin);
        scrollTable.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> openTemplate("blank.p"));
        var popTable = addTooltip(textButton, "An empty template perfect for starting a new project.", Align.top, Align.top, tooltipBottomArrowStyle);
        popTable.setKeepSizedWithinStage(false);

        //Flame
        scrollTable.row();
        textButton = new TextButton("Flame", skin);
        scrollTable.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> openTemplate("flame.p"));
        popTable = addTooltip(textButton, "The default template implementing the traditional ever-burning flame.", Align.top, Align.top, tooltipBottomArrowStyle);
        popTable.setKeepSizedWithinStage(false);

        //Sparks
        scrollTable.row();
        textButton = new TextButton("Sparks", skin);
        scrollTable.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> openTemplate("sparks.p"));
        popTable = addTooltip(textButton, "A shower of sparks coming from an arc welder.", Align.top, Align.top, tooltipBottomArrowStyle);
        popTable.setKeepSizedWithinStage(false);

        //Smoke
        scrollTable.row();
        textButton = new TextButton("Smoke", skin);
        scrollTable.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> openTemplate("smoke.p"));
        popTable = addTooltip(textButton, "A tower of smoke emanating from a chimney or wreckage.", Align.top, Align.top, tooltipBottomArrowStyle);
        popTable.setKeepSizedWithinStage(false);

        //Demolition
        scrollTable.row();
        textButton = new TextButton("Demolition", skin);
        scrollTable.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> openTemplate("demolition.p"));
        popTable = addTooltip(textButton, "The explosion from a demolished building or self-destructing robot.", Align.top, Align.top, tooltipBottomArrowStyle);
        popTable.setKeepSizedWithinStage(false);

        //Trail
        scrollTable.row();
        textButton = new TextButton("Trail", skin);
        scrollTable.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> openTemplate("trail.p"));
        popTable = addTooltip(textButton, "A simple trail to follow a missile or any other flying object.", Align.top, Align.top, tooltipBottomArrowStyle);
        popTable.setKeepSizedWithinStage(false);

        //Thruster
        scrollTable.row();
        textButton = new TextButton("Thruster", skin);
        scrollTable.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> openTemplate("thruster.p"));
        popTable = addTooltip(textButton, "A simple trail to follow a missile or any other flying object.", Align.top, Align.top, tooltipBottomArrowStyle);
        popTable.setKeepSizedWithinStage(false);
    }

    private void openTemplate(String internalPath) {
        hide();
        Utils.loadParticle(Gdx.files.internal(internalPath));
        selectedEmitter = particleEffect.getEmitters().first();

        EffectEmittersPanel.effectEmittersPanel.populateEmitters();
        EffectEmittersPanel.effectEmittersPanel.updateDisableableWidgets();
        emitterPropertiesPanel.populateScrollTable(null);

        UndoManager.clear();

        openFileFileHandle = null;
        updateWindowTitle();
    }
}
