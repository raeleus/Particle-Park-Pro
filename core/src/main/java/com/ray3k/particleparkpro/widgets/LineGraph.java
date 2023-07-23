package com.ray3k.particleparkpro.widgets;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.utils.Scaling;
import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;

import static com.ray3k.particleparkpro.Core.shapeDrawer;

public class LineGraph extends Table {
    private LineGraphStyle style;
    private String text;
    private Label backgroundLabel;
    private DragListener dragListener;
    private ButtonStyle nodeStyle;
    private EventListener nodeListener;
    private final Array<Node> nodes = new Array<>();
    private ShapeDrawerDrawable shapeDrawerDrawable;
    private boolean createNewNode;
    private static final Vector2 temp = new Vector2();
    private Image shapeDrawerImage;

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
        shapeDrawerImage = new Image(shapeDrawerDrawable);
        shapeDrawerImage.setScaling(Scaling.stretch);
        stack.add(shapeDrawerImage);

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

        for (var node : nodes) {
            if (style.knobLabelStyle == null && node.label != null) node.label.remove();
            else if (style.knobLabelStyle != null) {
                if (node.label == null) node.label = new Label(Math.round(node.percentX * 100) + "%, " + Math.round(node.percentY * 100) + "%", style.knobLabelStyle);
                else node.label.setStyle(style.knobLabelStyle);
            }
        }
    }

    public void initialize() {
        setTouchable(Touchable.enabled);
        addListener(dragListener = createDragListener());

        createNode(0, 0, true);
    }

    @Override
    public void layout() {
        super.layout();

        for (int i = 0; i < nodes.size; i++) {
            var node = nodes.get(i);

            node.setPosition(MathUtils.round(getPadLeft() + node.percentX * (getWidth() - getPadLeft() - getPadRight()) - node.getWidth() / 2),
                MathUtils.round(getPadBottom() + node.percentY * (getHeight() - getPadBottom() - getPadTop()) - node.getHeight() / 2));

            if (node.label != null) {
                node.label.pack();
                var labelX = MathUtils.round(node.getWidth() / 2 - node.label.getWidth() / 2);
                var nodeLeft = node.getX() - node.label.getWidth() / 2;
                var nodeRight = node.getX() + node.label.getWidth() / 2;
                if (nodeLeft < getPadLeft()) labelX = MathUtils.round(-node.getX() + getPadLeft());
                else if (nodeRight > getWidth() - getPadRight())
                    labelX = MathUtils.round(getWidth() - getPadRight() - node.getX() - node.label.getWidth());

                var labelY = MathUtils.round(node.getHeight());
                if (node.getY() + node.getHeight() + node.label.getHeight() >= getHeight() - getPadTop())
                    labelY = MathUtils.round(-node.label.getHeight());

                node.label.setPosition(labelX, labelY);
            }
        }
    }

    private ShapeDrawerDrawable createShapeDrawerDrawable() {
        return new ShapeDrawerDrawable(shapeDrawer) {
            @Override
            public void drawShapes(ShapeDrawer shapeDrawer, float x, float y, float width, float height) {
                for (int i = 0; i < nodes.size; i++) {
                    var node = nodes.get(i);

                    shapeDrawer.setColor(style.lineColor);
                    shapeDrawer.setDefaultLineWidth(style.lineWidth);

                    if (i == nodes.size - 1) shapeDrawer.line(x + node.percentX * width, y + node.percentY * height, x + width, y + node.percentY * height);
                    else {
                        var nextNode = nodes.get(i + 1);
                        shapeDrawer.line(x + node.percentX * width, y + node.percentY * height, x + nextNode.percentX * width, y + nextNode.percentY * height);
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

                    createNode((x - getPadLeft()) / (getWidth() - getPadLeft() - getPadRight()),
                        (y - getPadBottom()) / (getHeight() - getPadBottom() - getPadTop()), false);
                }
            }

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                createNewNode = false;
            }
        };
    }

    private void createNode(float percentX, float percentY, boolean onlyDragY) {
        var node = new Node(nodeStyle);
        if (nodeListener != null) node.addListener(nodeListener);
        addActor(node);
        nodes.add(node);
        node.percentX = percentX;
        node.percentY = percentY;
        if (node.label != null) node.label.setText(Math.round(node.percentX * 100) + "%, " + Math.round(node.percentY * 100) + "%");
        sortNodes();

        if (style.knobLabelStyle != null) {
            var label = new Label(Math.round(node.percentX * 100) + "%, " + Math.round(node.percentY * 100) + "%", style.knobLabelStyle);
            node.addActor(label);
            node.label = label;
        }

        var clickListener = new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                createNewNode = false;
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (nodes.indexOf(node, true) != 0 && (event.getButton() == Buttons.RIGHT || getTapCount() >= 2)) {
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
                node.percentX = MathUtils.clamp(onlyDragY ? node.percentX : (temp.x - getPadLeft()) / (getWidth() - getPadLeft() - getPadRight()), 0, 1);
                node.percentY = MathUtils.clamp((temp.y - getPadBottom()) / (getHeight() - getPadBottom() - getPadTop()), 0, 1);
                if (node.label != null) node.label.setText(Math.round(node.percentX * 100) + "%, " + Math.round(node.percentY * 100) + "%");

                invalidate();

                sortNodes();
            }
        };
        dragListener.setTapSquareSize(5);
        node.addListener(dragListener);
    }

    private void sortNodes() {
        nodes.sort((o1, o2) -> Float.compare(o1.percentX, o2.percentX));
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

    public static class Node extends Button{
        public float percentX;
        public float percentY;
        public Label label;

        public Node(ButtonStyle style) {
            super(style);
        }
    }
}
