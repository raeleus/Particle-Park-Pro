package com.ray3k.particleparkpro.widgets.styles;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.ray3k.particleparkpro.Core;
import com.ray3k.particleparkpro.widgets.ColorGraph.ColorGraphStyle;
import com.ray3k.particleparkpro.widgets.EditableLabel.EditableLabelStyle;
import com.ray3k.particleparkpro.widgets.InfSlider.InfSliderStyle;
import com.ray3k.particleparkpro.widgets.LineGraph.LineGraphStyle;
import com.ray3k.stripe.DraggableList.DraggableListStyle;
import com.ray3k.stripe.DraggableTextList.DraggableTextListStyle;
import com.ray3k.stripe.PopColorPicker.PopColorPickerStyle;
import com.ray3k.stripe.PopTable.PopTableStyle;
import com.ray3k.stripe.ResizeWidget.ResizeWidgetStyle;
import com.ray3k.stripe.Spinner.SpinnerStyle;

import static com.ray3k.particleparkpro.Core.*;

public class Styles {
    public static LineGraphStyle lineGraphStyle;
    public static LineGraphStyle lineGraphBigStyle;
    public static ColorGraphStyle colorGraphStyle;
    public static SpinnerStyle spinnerStyle;
    public static ResizeWidgetStyle resizeWidgetStyle;
    public static DraggableListStyle draggableListStyle;
    public static DraggableTextListStyle draggableTextListStyle;
    public static DraggableTextListStyle draggableTextListNoBgStyle;
    public static InfSliderStyle infSliderStyle;
    public static PopTableStyle tooltipBottomArrowStyle;
    public static PopTableStyle tooltipBottomRightArrowStyle;
    public static PopTableStyle tooltipTopArrowStyle;
    public static PopTableStyle tooltipRightArrowStyle;
    public static PopTableStyle tooltipLeftArrowStyle;
    public static EditableLabelStyle editableLabelStyle;
    public static PopColorPickerStyle popColorPickerStyle;

    public static void initialize() {
        Styles.popColorPickerStyle = new PPcolorPickerStyle();
        Styles.lineGraphStyle = new PPlineGraphStyle();
        Styles.lineGraphBigStyle = new PPlineGraphBigStyle();
        Styles.colorGraphStyle = new PPcolorGraphStyle();
        Styles.spinnerStyle = new PPspinnerStyle();
        Styles.resizeWidgetStyle = new PPresizeWidgetStyle();
        Styles.draggableListStyle = new PPdraggableListStyle();
        Styles.draggableTextListStyle = new PPdraggableTextListStyle();
        Styles.draggableTextListNoBgStyle = new PPdraggableTextListNoBGStyle();
        Styles.infSliderStyle = new PPinfSliderStyle();
        Styles.tooltipBottomArrowStyle = new PopTableStyle(skin.get("tooltip-bottom-arrow", WindowStyle.class));
        Styles.tooltipBottomRightArrowStyle = new PopTableStyle(skin.get("tooltip-bottom-right-arrow", WindowStyle.class));
        Styles.tooltipTopArrowStyle = new PopTableStyle(skin.get("tooltip-top-arrow", WindowStyle.class));
        Styles.tooltipRightArrowStyle = new PopTableStyle(skin.get("tooltip-right-arrow", WindowStyle.class));
        Styles.tooltipLeftArrowStyle = new PopTableStyle(skin.get("tooltip-left-arrow", WindowStyle.class));
        Styles.editableLabelStyle = new PPeditableLabelStyle();
    }
}
