package com.ray3k.particleparkpro.widgets.poptables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.Scaling;
import com.ray3k.particleparkpro.Core;
import com.ray3k.particleparkpro.FileDialogs;
import com.ray3k.stripe.PopColorPicker;
import com.ray3k.stripe.PopColorPicker.PopColorPickerListener;
import com.ray3k.stripe.PopTable;
import com.ray3k.stripe.Spinner;
import com.ray3k.stripe.Spinner.Orientation;

import static com.ray3k.particleparkpro.Core.*;
import static com.ray3k.particleparkpro.ParticlePreview.*;

public class PopEditorSettings extends PopTable {
    public PopEditorSettings() {
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

        final int sectionPadding = 10;
        final int tabWidth = 5;
        final int itemSpacing = 5;
        final int spinnerWidth = 70;

        var scrollTable = new Table();
        var scrollPane = new ScrollPane(scrollTable, skin);
        scrollPane.setFlickScroll(false);
        add(scrollPane).grow();
        addScrollFocusListener(scrollPane);

        scrollTable.top().pad(5);

        //Pixels per meter
        var label = new Label("Pixels per meter", skin, "header");
        scrollTable.add(label).left();

        scrollTable.row();
        var table = new Table();
        scrollTable.add(table).space(itemSpacing).left();

        label = new Label("Value:", skin);
        table.add(label).padLeft(tabWidth);

        var pixelsPerMeterSpinner = new Spinner(pixelsPerMeter, 1, false, Orientation.RIGHT_STACK, spinnerStyle);
        table.add(pixelsPerMeterSpinner).spaceLeft(itemSpacing).width(spinnerWidth);
        addHandListener(pixelsPerMeterSpinner.getButtonMinus());
        addHandListener(pixelsPerMeterSpinner.getButtonPlus());
        addIbeamListener(pixelsPerMeterSpinner.getTextField());
        onChange(pixelsPerMeterSpinner, () -> pixelsPerMeter = (float) pixelsPerMeterSpinner.getValue());

        scrollTable.row();
        var image = new Image(skin, "divider-10");
        image.setScaling(Scaling.stretchX);
        scrollTable.add(image).growX().padTop(sectionPadding).padBottom(sectionPadding);

        //Delta multiplier
        scrollTable.row();
        label = new Label("Delta multiplier", skin, "header");
        scrollTable.add(label).left();

        scrollTable.row();
        table = new Table();
        scrollTable.add(table).space(itemSpacing).left();

        label = new Label("Value:", skin);
        table.add(label).padLeft(tabWidth);

        var deltaMultiplierSpinner = new Spinner(deltaMultiplier, 1, false, Orientation.RIGHT_STACK, spinnerStyle);
        table.add(deltaMultiplierSpinner).spaceLeft(itemSpacing).width(spinnerWidth);
        addHandListener(deltaMultiplierSpinner.getButtonMinus());
        addHandListener(deltaMultiplierSpinner.getButtonPlus());
        addIbeamListener(deltaMultiplierSpinner.getTextField());
        onChange(deltaMultiplierSpinner, () -> deltaMultiplier = (float) deltaMultiplierSpinner.getValue());

        scrollTable.row();
        image = new Image(skin, "divider-10");
        image.setScaling(Scaling.stretchX);
        scrollTable.add(image).growX().padTop(sectionPadding).padBottom(sectionPadding);

        //Background color
        scrollTable.row();
        label = new Label("Background color", skin, "header");
        scrollTable.add(label).left();

        scrollTable.row();
        var stack = new Stack();
        scrollTable.add(stack).left().padLeft(tabWidth);

        image = new Image(skin, "swatch-bg");
        image.setScaling(Scaling.none);
        stack.add(image);

        var backgroundColorImage = new Image(skin, "swatch-fill");
        backgroundColorImage.setColor(backgroundColor);
        backgroundColorImage.setScaling(Scaling.none);
        stack.add(backgroundColorImage);
        addHandListener(stack);
        onClick(backgroundColorImage, () -> {
            var cp = new PopColorPicker(backgroundColor, popColorPickerStyle);
            cp.setHideOnUnfocus(true);
            cp.setButtonListener(handListener);
            cp.setTextFieldListener(ibeamListener);
            cp.addListener(new TableShowHideListener() {
                @Override
                public void tableShown(Event event) {
                    setHideOnUnfocus(false);
                }

                @Override
                public void tableHidden(Event event) {
                    setHideOnUnfocus(true);
                }
            });
            cp.addListener(new PopColorPickerListener() {
                @Override
                public void picked(Color color) {
                    backgroundColorImage.setColor(color);
                    backgroundColor.set(color);
                }

                @Override
                public void updated(Color color) {
                    backgroundColorImage.setColor(color);
                    backgroundColor.set(color);
                }

                @Override
                public void cancelled(Color oldColor) {
                    backgroundColorImage.setColor(oldColor);
                    backgroundColor.set(oldColor);
                }
            });
            cp.show(foregroundStage);
        });

        scrollTable.row();
        image = new Image(skin, "divider-10");
        image.setScaling(Scaling.stretchX);
        scrollTable.add(image).growX().padTop(sectionPadding).padBottom(sectionPadding);

        //Preview image
        scrollTable.row();
        label = new Label("Preview image", skin, "header");
        scrollTable.add(label).left();

        scrollTable.row();
        table = new Table();
        scrollTable.add(table).space(itemSpacing).left().padLeft(tabWidth);

        table.defaults().space(itemSpacing);
        var textButton = new TextButton("Select preview", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
//            FileDialogs.openDialog(Gdx.files.getLocalStoragePath(), new String[] {"png,jpg,jpeg"}, new String[]{"Image files"});
            FileDialogs.saveDialog(Gdx.files.getLocalStoragePath(), null, new String[] {"png,jpg,jpeg"}, new String[]{"Image files"});
        });

        textButton = new TextButton("Remove preview", skin);
        table.add(textButton);
        addHandListener(textButton);
        onChange(textButton, () -> {
            previewImagePath = null;
            previewImage = null;
        });

        scrollTable.row();
        var showResizeInterfaceCheckBox = new CheckBox("Show resize interface", skin);
        scrollTable.add(showResizeInterfaceCheckBox).left().padLeft(tabWidth).space(itemSpacing);
        addHandListener(showResizeInterfaceCheckBox);
        onChange(showResizeInterfaceCheckBox, () -> showResizeInterface = showResizeInterfaceCheckBox.isChecked());

        scrollTable.row();
        table = new Table();
        scrollTable.add(table).space(itemSpacing).left().padLeft(tabWidth);

        table.defaults().right().space(itemSpacing);

        label = new Label("X:", skin);
        table.add(label).padLeft(tabWidth);

        var xSpinner = new Spinner(previewImageX, 1, false, Orientation.RIGHT_STACK, spinnerStyle);
        table.add(xSpinner).width(spinnerWidth);
        addHandListener(xSpinner.getButtonMinus());
        addHandListener(xSpinner.getButtonPlus());
        addIbeamListener(xSpinner.getTextField());
        onChange(xSpinner, () -> previewImageX = (float) xSpinner.getValue());

        label = new Label("Y:", skin);
        table.add(label).padLeft(tabWidth);

        var ySpinner = new Spinner(previewImageY, 1, false, Orientation.RIGHT_STACK, spinnerStyle);
        table.add(ySpinner).width(spinnerWidth);
        addHandListener(ySpinner.getButtonMinus());
        addHandListener(ySpinner.getButtonPlus());
        addIbeamListener(ySpinner.getTextField());
        onChange(ySpinner, () -> previewImageY = (float) ySpinner.getValue());

        table.row();
        label = new Label("Width:", skin);
        table.add(label).padLeft(tabWidth);

        var widthSpinner = new Spinner(previewImageWidth, 1, false, Orientation.RIGHT_STACK, spinnerStyle);
        table.add(widthSpinner).width(spinnerWidth);
        addHandListener(widthSpinner.getButtonMinus());
        addHandListener(widthSpinner.getButtonPlus());
        addIbeamListener(widthSpinner.getTextField());
        onChange(widthSpinner, () -> previewImageWidth = (float) widthSpinner.getValue());

        label = new Label("Height:", skin);
        table.add(label).padLeft(tabWidth);

        var heightSpinner = new Spinner(previewImageHeight, 1, false, Orientation.RIGHT_STACK, spinnerStyle);
        table.add(heightSpinner).width(spinnerWidth);
        addHandListener(heightSpinner.getButtonMinus());
        addHandListener(heightSpinner.getButtonPlus());
        addIbeamListener(heightSpinner.getTextField());
        onChange(heightSpinner, () -> previewImageHeight = (float) heightSpinner.getValue());

        scrollTable.row();
        image = new Image(skin, "divider-10");
        image.setScaling(Scaling.stretchX);
        scrollTable.add(image).growX().padTop(sectionPadding).padBottom(sectionPadding);

        //Render grid
        scrollTable.row();
        label = new Label("Render Grid", skin, "header");
        scrollTable.add(label).left();

        scrollTable.row();
        table = new Table();
        scrollTable.add(table).left().padLeft(tabWidth);

        table.defaults().space(itemSpacing);
        var renderGridCheckBox = new CheckBox("Grid enabled", skin);
        table.add(renderGridCheckBox);
        addHandListener(renderGridCheckBox);
        onChange(renderGridCheckBox, () -> gridEnabled = renderGridCheckBox.isChecked());

        scrollTable.row();
        var gridTable = new Table();
        scrollTable.add(gridTable).left();

        gridTable.defaults().space(itemSpacing);
        gridTable.columnDefaults(0).right();
        gridTable.columnDefaults(1).left();
        label = new Label("Major gridlines:", skin);
        gridTable.add(label).padLeft(tabWidth);

        var majorGridlinesSpinner = new Spinner(gridMajorGridlines, 1, false, Orientation.RIGHT_STACK, spinnerStyle);
        gridTable.add(majorGridlinesSpinner).width(spinnerWidth);
        addHandListener(majorGridlinesSpinner.getButtonMinus());
        addHandListener(majorGridlinesSpinner.getButtonPlus());
        addIbeamListener(majorGridlinesSpinner.getTextField());
        onChange(xSpinner, () -> previewImageX = (float) xSpinner.getValue());
        onChange(majorGridlinesSpinner, () -> gridMajorGridlines = (float) majorGridlinesSpinner.getValue());

        gridTable.row();
        label = new Label("Minor gridlines:", skin);
        gridTable.add(label).padLeft(tabWidth);

        var minorGridlinesSpinner = new Spinner(gridMinorGridlines, 1, false, Orientation.RIGHT_STACK, spinnerStyle);
        gridTable.add(minorGridlinesSpinner).width(spinnerWidth);
        addHandListener(minorGridlinesSpinner.getButtonMinus());
        addHandListener(minorGridlinesSpinner.getButtonPlus());
        addIbeamListener(minorGridlinesSpinner.getTextField());
        onChange(minorGridlinesSpinner, () -> gridMinorGridlines = (float) minorGridlinesSpinner.getValue());

        gridTable.row();
        label = new Label("Grid color:", skin);
        gridTable.add(label).padLeft(tabWidth);

        stack = new Stack();
        gridTable.add(stack).space(itemSpacing);

        image = new Image(skin, "swatch-bg");
        image.setScaling(Scaling.none);
        stack.add(image);

        var gridColorImage = new Image(skin, "swatch-fill");
        gridColorImage.setColor(gridColor);
        gridColorImage.setScaling(Scaling.none);
        stack.add(gridColorImage);
        addHandListener(stack);
        onClick(gridColorImage, () -> {
            var cp = new PopColorPicker(gridColor, popColorPickerStyle);
            cp.setHideOnUnfocus(true);
            cp.setButtonListener(handListener);
            cp.setTextFieldListener(ibeamListener);
            cp.addListener(new TableShowHideListener() {
                @Override
                public void tableShown(Event event) {
                    setHideOnUnfocus(false);
                }

                @Override
                public void tableHidden(Event event) {
                    setHideOnUnfocus(true);
                }
            });
            cp.addListener(new PopColorPickerListener() {
                @Override
                public void picked(Color color) {
                    gridColorImage.setColor(color);
                    gridColor.set(color);
                }

                @Override
                public void updated(Color color) {
                    gridColorImage.setColor(color);
                    gridColor.set(color);
                }

                @Override
                public void cancelled(Color oldColor) {
                    gridColorImage.setColor(oldColor);
                    gridColor.set(oldColor);
                }
            });
            cp.show(foregroundStage);
        });

        onChange(renderGridCheckBox, () -> {
            if (renderGridCheckBox.isChecked()) {
                gridTable.setTouchable(Touchable.enabled);
                gridTable.setColor(Color.WHITE);
            } else {
                gridTable.setTouchable(Touchable.disabled);
                gridTable.setColor(skin.getColor("disabled"));
                foregroundStage.setKeyboardFocus(null);
            }
        });
        if (!gridEnabled) {
            gridTable.setTouchable(Touchable.disabled);
            gridTable.setColor(skin.getColor("disabled"));
        }

        scrollTable.row();
        image = new Image(skin, "divider-10");
        image.setScaling(Scaling.stretchX);
        scrollTable.add(image).growX().padTop(sectionPadding).padBottom(sectionPadding);

        //Shading
        scrollTable.row();
        label = new Label("Shading", skin, "header");
        scrollTable.add(label).left();

        scrollTable.row();
        table = new Table();
        scrollTable.add(table).space(itemSpacing).left();

        //Vertex Shader
        table.defaults().top().space(itemSpacing);
        var subtable = new Table();
        table.add(subtable);

        subtable.defaults().space(itemSpacing);
        label = new Label("Vertex Shader", skin);
        subtable.add(label).padLeft(tabWidth);

        subtable.row();
        textButton = new TextButton("Default", skin);
        subtable.add(textButton);
        addHandListener(textButton);

        subtable.row();
        textButton = new TextButton("Set", skin);
        subtable.add(textButton);
        addHandListener(textButton);

        subtable.row();
        textButton = new TextButton("Reload", skin);
        subtable.add(textButton);
        addHandListener(textButton);

        subtable.row();
        textButton = new TextButton("Show", skin);
        subtable.add(textButton);
        addHandListener(textButton);

        //Frag. Shader
        subtable = new Table();
        table.add(subtable);

        subtable.defaults().space(itemSpacing);
        label = new Label("Frag. Shader", skin);
        subtable.add(label).padLeft(tabWidth);

        subtable.row();
        textButton = new TextButton("Default", skin);
        subtable.add(textButton);
        addHandListener(textButton);

        subtable.row();
        textButton = new TextButton("Set", skin);
        subtable.add(textButton);
        addHandListener(textButton);

        subtable.row();
        textButton = new TextButton("Reload", skin);
        subtable.add(textButton);
        addHandListener(textButton);

        subtable.row();
        textButton = new TextButton("Show", skin);
        subtable.add(textButton);
        addHandListener(textButton);

        //Extra Texture Units
        subtable = new Table();
        table.add(subtable).padLeft(30);

        subtable.defaults().space(itemSpacing);
        label = new Label("Extra Texture Units", skin);
        subtable.add(label).padLeft(tabWidth).colspan(2);

        subtable.row();
        var list = new List<String>(skin);
        scrollPane = new ScrollPane(list, skin);
        subtable.add(scrollPane).growY().width(100);
        addHandListener(list);

        var textureUnitsTable = new Table();
        subtable.add(textureUnitsTable);

        textureUnitsTable.defaults().space(itemSpacing);
        textButton = new TextButton("Add", skin);
        textureUnitsTable.add(textButton);
        addHandListener(textButton);

        textureUnitsTable.row();
        textButton = new TextButton("Up", skin);
        textureUnitsTable.add(textButton);
        addHandListener(textButton);

        textureUnitsTable.row();
        textButton = new TextButton("Down", skin);
        textureUnitsTable.add(textButton);
        addHandListener(textButton);

        textureUnitsTable.row();
        textButton = new TextButton("Delete", skin);
        textureUnitsTable.add(textButton);
        addHandListener(textButton);

        textureUnitsTable.row();
        textButton = new TextButton("Reload", skin);
        textureUnitsTable.add(textButton);
        addHandListener(textButton);
    }
}
