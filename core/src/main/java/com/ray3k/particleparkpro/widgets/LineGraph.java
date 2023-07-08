package com.ray3k.particleparkpro.widgets;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;

import java.util.Comparator;

import static com.ray3k.particleparkpro.Core.shapeDrawer;

public class LineGraph extends Table {
    private LineGraphStyle style;
    private String text;
    private Label backgroundLabel;
    private DragListener dragListener;
    private ButtonStyle nodeStyle;
    private EventListener nodeListener;
    private final Array<Button> nodes = new Array<>();
    private ShapeDrawerDrawable shapeDrawerDrawable;
    private boolean createNewNode;
    private static final Vector2 temp = new Vector2();

    public LineGraph(String text, LineGraphStyle style) {
        this.text = text;
        nodeStyle = new ButtonStyle();
        setStyle(style);

        var stack = new Stack();
        add(stack).grow();

        backgroundLabel = new Label(text, this.style.backgroundLabelStyle);
        var container = new Container<>(backgroundLabel);
        stack.add(container);

        shapeDrawerDrawable = createShapeDrawerDrawable();
        var image = new Image(shapeDrawerDrawable);
        stack.add(image);

        initialize();
    }

    public LineGraph(String text, Skin skin) {
        this(text, skin.get(LineGraphStyle.class));
    }

    public LineGraph(String text, Skin skin, String style) {
        this(text, skin.get(style, LineGraphStyle.class));
    }

    public void setStyle(LineGraphStyle style) {
        this.style = style;
        setBackground(style.background);

        if (backgroundLabel != null) {
            backgroundLabel.setStyle(style.backgroundLabelStyle);
            add(backgroundLabel);
        }

        nodeStyle.up = style.nodeUp;
        nodeStyle.down = style.nodeDown;
        nodeStyle.over = style.nodeOver;
    }

    public void initialize() {
        setTouchable(Touchable.enabled);
        addListener(dragListener = createDragListener());

        createNode(getPadLeft(), getPadBottom(), true);
    }

    @Override
    public void layout() {
        super.layout();
    }

    private ShapeDrawerDrawable createShapeDrawerDrawable() {
        return new ShapeDrawerDrawable(shapeDrawer) {
            @Override
            public void drawShapes(ShapeDrawer shapeDrawer, float x, float y, float width, float height) {
                for (int i = 0; i < nodes.size; i++) {
                    var node = nodes.get(i);

                    shapeDrawer.setColor(style.lineColor);
                    shapeDrawer.setDefaultLineWidth(style.lineWidth);

                    if (i == nodes.size - 1) shapeDrawer.line(x + node.getX(), y + node.getY(), x + width, y + node.getY());
                    else {
                        var nextNode = nodes.get(i + 1);
                        shapeDrawer.line(x + node.getX(), y + node.getY(), x + nextNode.getX(), y + nextNode.getY());
                    }
                }
            }
        };
    }

    private DragListener createDragListener() {
        return new DragListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                createNewNode = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (createNewNode &&
                    x >= getPadLeft() &&
                    x <= getWidth() - getPadRight() &&
                    y >= getPadBottom() &&
                    y <= getHeight() - getPadTop()) {

                    createNode(x, y, false);
                }
            }

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                createNewNode = false;
            }
        };
    }

    private void createNode(float x, float y, boolean onlyDragY) {
        var node = new Button(nodeStyle);
        if (nodeListener != null) node.addListener(nodeListener);
        addActor(node);
        nodes.add(node);
        node.setPosition(MathUtils.round(x), MathUtils.round(y), Align.center);
        sortNodes();

        var clickListener = new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                createNewNode = false;
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Buttons.RIGHT || getTapCount() >= 2) {
                    node.remove();
                    nodes.removeValue(node, true);
                }
            }
        };
        clickListener.setButton(-1);
        node.addListener(clickListener);

        var dragListener = new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                temp.set(x, y);
                node.localToActorCoordinates(LineGraph.this, temp);
                float newX = MathUtils.clamp(onlyDragY ? node.getX(Align.center) : temp.x, getPadLeft(), getWidth() - getPadRight());
                float newY = MathUtils.clamp(temp.y, getPadBottom(), getHeight() - getPadTop());
                node.setPosition(newX, newY, Align.center);

                sortNodes();
            }
        };
        dragListener.setTapSquareSize(5);
        node.addListener(dragListener);
    }

    private void sortNodes() {
        nodes.sort((o1, o2) -> Float.compare(o1.getX(), o2.getX()));
    }

    public EventListener getNodeListener() {
        return nodeListener;
    }

    public void setNodeListener(EventListener nodeListener) {
        if (this.nodeListener != null) {
            for (int i = 0; i < nodes.size; i++) {
                var node = nodes.get(i);
                node.removeListener(this.nodeListener);
            }
        }
        this.nodeListener = nodeListener;
        for (int i = 0; i < nodes.size; i++) {
            var node = nodes.get(i);
            node.addListener(nodeListener);
        }
    }

    public static class LineGraphStyle {
        public Drawable background;
        public LabelStyle backgroundLabelStyle;
        public LabelStyle knobLabelStyle;
        public Drawable nodeUp;
        public Drawable nodeOver;
        public Drawable nodeDown;
        public Color lineColor;
        public int lineWidth;
    }
}
