package com.ray3k.particleparkpro.widgets;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import lombok.Getter;

public class EditableLabel extends ToggleGroup {
    @Getter
    private String text;
    public Label label;
    public TextField textField;
    public EditableLabelStyle style;

    public EditableLabel(String text, EditableLabelStyle style) {
        this.text = text;
        this.style = style;

        label = new Label(text, style.labelStyle);
        label.setEllipsis("...");
        label.setTouchable(Touchable.enabled);
        table1.add(label).grow().minWidth(0);
        label.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                swap();
                getStage().setKeyboardFocus(textField);
                textField.selectAll();
            }
        });

        textField = new TextField(text, style.textFieldStyle);
        table2.add(textField).grow().minWidth(0);
        textField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                label.setText(textField.getText());
                EditableLabel.this.text = textField.getText();
            }
        });
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage != null) {
            var listener = new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (getStage() != null && event.getTarget() != textField) {
                        if (stage.getKeyboardFocus() == textField) {
                            stage.setKeyboardFocus(null);
                            showTable1();
                            unfocused();
                        }
                    }
                    return false;
                }
            };
            stage.addListener(listener);
        }
    }

    public void setText(String text) {
        this.text = text;
        label.setText(text);
        textField.setText(text);
    }

    public void unfocused() {

    }

    public static class EditableLabelStyle {
        public LabelStyle labelStyle;
        public TextFieldStyle textFieldStyle;
    }
}
