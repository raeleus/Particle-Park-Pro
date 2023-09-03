package com.ray3k.particleparkpro.widgets.styles;

import com.ray3k.particleparkpro.Core;
import com.ray3k.stripe.DraggableList.DraggableListStyle;

public class PPdraggableListStyle extends DraggableListStyle {
    public PPdraggableListStyle() {
        dividerUp = Core.skin.getDrawable("divider-10");
        dividerOver = Core.skin.getDrawable("divider-10");
        background = Core.skin.getDrawable("select-box-list-10");
    }
}
