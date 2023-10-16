package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.Pools;
import com.ray3k.particleparkpro.widgets.NoCaptureKeyboardFocus;

public class InfiniteSlider extends Widget implements NoCaptureKeyboardFocus {
    private static final float STEP_SIZE_DEFAULT = .01f;
    private float stepSize = STEP_SIZE_DEFAULT;
    private float increment;
    private float interval = .2f;
    private float value;
    private Action incrementAction;

    int button = -1;
    int draggingPointer = -1;
    boolean mouseOver;
    private Interpolation visualInterpolationInverse = Interpolation.linear;
    private float[] snapValues;
    private float threshold;

    private SliderStyle style;
    private static final float INTERNAL_MIN = -1, INTERNAL_MAX = 1;
    private float internalValue, animateFromValue;
    float position;
    final boolean vertical;
    private float animateDuration, animateTime;
    private Interpolation animateInterpolation = Interpolation.linear, visualInterpolation = Interpolation.linear;
    boolean disabled;
    private boolean round = true, programmaticChangeEvents = true;

    public InfiniteSlider (float increment, boolean vertical, Skin skin) {
        this(increment, vertical, skin.get("default-" + (vertical ? "vertical" : "horizontal"), Slider.SliderStyle.class));
    }

    public InfiniteSlider (float increment, boolean vertical, Skin skin, String styleName) {
        this(increment, vertical, skin.get(styleName, Slider.SliderStyle.class));
    }

    /** Creates a new slider. If horizontal, its width is determined by the prefWidth parameter, its height is determined by the
     * maximum of the height of either the slider {@link NinePatch} or slider handle {@link TextureRegion}. The min and max values
     * determine the range the values of this slider can take on, the stepSize parameter specifies the distance between individual
     * values. E.g. min could be 4, max could be 10 and stepSize could be 0.2, giving you a total of 30 values, 4.0 4.2, 4.4 and so
     * on.
     * @param style the {@link Slider.SliderStyle} */
    public InfiniteSlider (float increment, boolean vertical, Slider.SliderStyle style) {
        if (stepSize <= 0) throw new IllegalArgumentException("stepSize must be > 0: " + stepSize);
        setStyle(style);
        this.vertical = vertical;
        this.internalValue = 0;
        setSize(getPrefWidth(), getPrefHeight());

        this.increment = increment;

        addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (isDisabled()) return false;
                if (InfiniteSlider.this.button != -1 && InfiniteSlider.this.button != button) return false;
                if (draggingPointer != -1) return false;
                draggingPointer = pointer;
                calculatePositionAndValue(x, y);
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (pointer != draggingPointer) return;
                draggingPointer = -1;
                // The position is invalid when focus is cancelled
                if (event.isTouchFocusCancel() || !calculatePositionAndValue(x, y)) {
                    var adjustedValue = getValue();
                    setInternalValue(0, false);
                    value = adjustedValue;
                    manageIncrementAction();

                    // Fire an event on touchUp even if the value didn't change, so listeners can see when a drag ends via isDragging.
                    ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
                    fire(changeEvent);
                    Pools.free(changeEvent);
                }
            }

            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                calculatePositionAndValue(x, y);
                manageIncrementAction();
            }

            public void enter (InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                if (pointer == -1) mouseOver = true;
            }

            public void exit (InputEvent event, float x, float y, int pointer, @Null Actor toActor) {
                if (pointer == -1) mouseOver = false;
            }
        });
    }

    private void manageIncrementAction() {
        if (MathUtils.isEqual(internalValue, INTERNAL_MIN)) {
            if (incrementAction != null) return;
            incrementAction = Actions.forever(Actions.delay(interval, Actions.run(() -> {
                value -= increment;
                ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
                fire(changeEvent);
                Pools.free(changeEvent);
            })));
            addAction(incrementAction);
        } else if (MathUtils.isEqual(internalValue, INTERNAL_MAX)) {
            if (incrementAction != null) return;
            incrementAction = Actions.forever(Actions.delay(interval, Actions.run(() -> {
                value += increment;
                ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
                fire(changeEvent);
                Pools.free(changeEvent);
            })));
            addAction(incrementAction);
        } else {
            if (incrementAction == null) return;
            removeAction(incrementAction);
            incrementAction = null;
        }
    }

    public void setStyle (SliderStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        invalidateHierarchy();
    }

    /** Returns the slider's style. Modifying the returned style may not have an effect until {@link #setStyle(SliderStyle)}
     * is called. */
    public Slider.SliderStyle getStyle () {
        return style;
    }

    public boolean isOver () {
        return mouseOver;
    }

    protected @Null Drawable getBackgroundDrawable () {
        Slider.SliderStyle style = getStyle();
        if (disabled && style.disabledBackground != null) return style.disabledBackground;
        if (isDragging() && style.backgroundDown != null) return style.backgroundDown;
        if (mouseOver && style.backgroundOver != null) return style.backgroundOver;
        return style.background;
    }

    protected @Null Drawable getKnobDrawable () {
        Slider.SliderStyle style = getStyle();
        if (disabled && style.disabledKnob != null) return style.disabledKnob;
        if (isDragging() && style.knobDown != null) return style.knobDown;
        if (mouseOver && style.knobOver != null) return style.knobOver;
        return style.knob;
    }

    protected Drawable getKnobBeforeDrawable () {
        Slider.SliderStyle style = getStyle();
        if (disabled && style.disabledKnobBefore != null) return style.disabledKnobBefore;
        if (isDragging() && style.knobBeforeDown != null) return style.knobBeforeDown;
        if (mouseOver && style.knobBeforeOver != null) return style.knobBeforeOver;
        return style.knobBefore;
    }

    protected Drawable getKnobAfterDrawable () {
        Slider.SliderStyle style = getStyle();
        if (disabled && style.disabledKnobAfter != null) return style.disabledKnobAfter;
        if (isDragging() && style.knobAfterDown != null) return style.knobAfterDown;
        if (mouseOver && style.knobAfterOver != null) return style.knobAfterOver;
        return style.knobAfter;
    }

    boolean calculatePositionAndValue (float x, float y) {
        Slider.SliderStyle style = getStyle();
        Drawable knob = style.knob;
        Drawable bg = getBackgroundDrawable();

        float value;
        float oldPosition = position;

        if (vertical) {
            float height = getHeight() - bg.getTopHeight() - bg.getBottomHeight();
            float knobHeight = knob == null ? 0 : knob.getMinHeight();
            position = y - bg.getBottomHeight() - knobHeight * 0.5f;
            value = INTERNAL_MIN + (INTERNAL_MAX - INTERNAL_MIN) * visualInterpolationInverse.apply(position / (height - knobHeight));
            position = Math.max(Math.min(0, bg.getBottomHeight()), position);
            position = Math.min(height - knobHeight, position);
        } else {
            float width = getWidth() - bg.getLeftWidth() - bg.getRightWidth();
            float knobWidth = knob == null ? 0 : knob.getMinWidth();
            position = x - bg.getLeftWidth() - knobWidth * 0.5f;
            value = INTERNAL_MIN + (INTERNAL_MAX - INTERNAL_MIN) * visualInterpolationInverse.apply(position / (width - knobWidth));
            position = Math.max(Math.min(0, bg.getLeftWidth()), position);
            position = Math.min(width - knobWidth, position);
        }

        float oldValue = value;
        if (!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) && !Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) value = snap(value);
        boolean valueSet = setInternalValue(value, true);
        if (value == oldValue) position = oldPosition;
        return valueSet;
    }

    /** Returns a snapped value from a value calculated from the mouse position. The default implementation uses
     * {@link #setSnapToValues(float, float...)}. */
    protected float snap (float value) {
        if (snapValues == null || snapValues.length == 0) return value;
        float bestDiff = -1, bestValue = 0;
        for (int i = 0; i < snapValues.length; i++) {
            float snapValue = snapValues[i];
            float diff = Math.abs(value - snapValue);
            if (diff <= threshold) {
                if (bestDiff == -1 || diff < bestDiff) {
                    bestDiff = diff;
                    bestValue = snapValue;
                }
            }
        }
        return bestDiff == -1 ? value : bestValue;
    }

    /** Makes this slider snap to the specified values when the knob is within the threshold.
     * @param values May be null to disable snapping. */
    public void setSnapToValues (float threshold, @Null float... values) {
        if (values != null && values.length == 0) throw new IllegalArgumentException("values cannot be empty.");
        this.snapValues = values;
        this.threshold = threshold;
    }

    /** Makes this progress bar snap to the specified values, if the knob is within the threshold.
     * @param values May be null to disable snapping.
     * @deprecated Use {@link #setSnapToValues(float, float...)}. */
    @Deprecated
    public void setSnapToValues (@Null float[] values, float threshold) {
        setSnapToValues(threshold, values);
    }

    public @Null float[] getSnapToValues () {
        return snapValues;
    }

    public float getSnapToValuesThreshold () {
        return threshold;
    }

    /** Returns true if the slider is being dragged. */
    public boolean isDragging () {
        return draggingPointer != -1;
    }

    /** Sets the mouse button, which can trigger a change of the slider. Is -1, so every button, by default. */
    public void setButton (int button) {
        this.button = button;
    }

    /** Sets the inverse interpolation to use for display. This should perform the inverse of the
     * {@link #setVisualInterpolation(Interpolation) visual interpolation}. */
    public void setVisualInterpolationInverse (Interpolation interpolation) {
        this.visualInterpolationInverse = interpolation;
    }

    /** Sets the value using the specified visual percent.
     * @see #setVisualInterpolation(Interpolation) */
    public void setVisualPercent (float percent) {
        setInternalValue(INTERNAL_MIN + (INTERNAL_MAX - INTERNAL_MIN) * visualInterpolationInverse.apply(percent), true);
    }

    public void act (float delta) {
        super.act(delta);
        if (animateTime > 0) {
            animateTime -= delta;
            Stage stage = getStage();
            if (stage != null && stage.getActionsRequestRendering()) Gdx.graphics.requestRendering();
        }
    }

    public void draw (Batch batch, float parentAlpha) {
        ProgressBar.ProgressBarStyle style = this.style;
        boolean disabled = this.disabled;
        Drawable knob = style.knob, currentKnob = getKnobDrawable();
        Drawable bg = getBackgroundDrawable();
        Drawable knobBefore = getKnobBeforeDrawable();
        Drawable knobAfter = getKnobAfterDrawable();

        Color color = getColor();
        float x = getX(), y = getY();
        float width = getWidth(), height = getHeight();
        float knobHeight = knob == null ? 0 : knob.getMinHeight();
        float knobWidth = knob == null ? 0 : knob.getMinWidth();
        float percent = getVisualPercent();

        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        if (vertical) {
            float bgTopHeight = 0, bgBottomHeight = 0;
            if (bg != null) {
                drawRound(batch, bg, x + (width - bg.getMinWidth()) * 0.5f, y, bg.getMinWidth(), height);
                bgTopHeight = bg.getTopHeight();
                bgBottomHeight = bg.getBottomHeight();
                height -= bgTopHeight + bgBottomHeight;
            }

            float total = height - knobHeight;
            float beforeHeight = MathUtils.clamp(total * percent, 0, total);
            position = bgBottomHeight + beforeHeight;

            float knobHeightHalf = knobHeight * 0.5f;
            if (knobBefore != null) {
                drawRound(batch, knobBefore, //
                    x + (width - knobBefore.getMinWidth()) * 0.5f, //
                    y + bgBottomHeight, //
                    knobBefore.getMinWidth(), beforeHeight + knobHeightHalf);
            }
            if (knobAfter != null) {
                drawRound(batch, knobAfter, //
                    x + (width - knobAfter.getMinWidth()) * 0.5f, //
                    y + position + knobHeightHalf, //
                    knobAfter.getMinWidth(),
                    total - (round ? Math.round(beforeHeight - knobHeightHalf) : beforeHeight - knobHeightHalf));
            }
            if (currentKnob != null) {
                float w = currentKnob.getMinWidth(), h = currentKnob.getMinHeight();
                drawRound(batch, currentKnob, //
                    x + (width - w) * 0.5f, //
                    y + position + (knobHeight - h) * 0.5f, //
                    w, h);
            }
        } else {
            float bgLeftWidth = 0, bgRightWidth = 0;
            if (bg != null) {
                drawRound(batch, bg, x, Math.round(y + (height - bg.getMinHeight()) * 0.5f), width, Math.round(bg.getMinHeight()));
                bgLeftWidth = bg.getLeftWidth();
                bgRightWidth = bg.getRightWidth();
                width -= bgLeftWidth + bgRightWidth;
            }

            float total = width - knobWidth;
            float beforeWidth = MathUtils.clamp(total * percent, 0, total);
            position = bgLeftWidth + beforeWidth;

            float knobWidthHalf = knobWidth * 0.5f;
            if (knobBefore != null) {
                drawRound(batch, knobBefore, //
                    x + bgLeftWidth, //
                    y + (height - knobBefore.getMinHeight()) * 0.5f, //
                    beforeWidth + knobWidthHalf, knobBefore.getMinHeight());
            }
            if (knobAfter != null) {
                drawRound(batch, knobAfter, //
                    x + position + knobWidthHalf, //
                    y + (height - knobAfter.getMinHeight()) * 0.5f, //
                    total - (round ? Math.round(beforeWidth - knobWidthHalf) : beforeWidth - knobWidthHalf), knobAfter.getMinHeight());
            }
            if (currentKnob != null) {
                float w = currentKnob.getMinWidth(), h = currentKnob.getMinHeight();
                drawRound(batch, currentKnob, //
                    x + position + (knobWidth - w) * 0.5f, //
                    y + (height - h) * 0.5f, //
                    w, h);
            }
        }
    }

    private void drawRound (Batch batch, Drawable drawable, float x, float y, float w, float h) {
        if (round) {
            x = Math.round(x);
            y = Math.round(y);
            w = Math.round(w);
            h = Math.round(h);
        }
        drawable.draw(batch, x, y, w, h);
    }

    /** If {@link #setAnimateDuration(float) animating} the progress bar value, this returns the value current displayed. */
    public float getVisualValue () {
        if (animateTime > 0) return animateInterpolation.apply(animateFromValue, internalValue, 1 - animateTime / animateDuration);
        return internalValue;
    }

    /** Sets the visual value equal to the actual value. This can be used to set the value without animating. */
    public void updateVisualValue () {
        animateTime = 0;
    }

    public float getPercent () {
        if (INTERNAL_MIN == INTERNAL_MAX) return 0;
        return (internalValue - INTERNAL_MIN) / (INTERNAL_MAX - INTERNAL_MIN);
    }

    public float getVisualPercent () {
        if (INTERNAL_MIN == INTERNAL_MAX) return 0;
        return visualInterpolation.apply((getVisualValue() - INTERNAL_MIN) / (INTERNAL_MAX - INTERNAL_MIN));
    }

    /** Returns progress bar visual position within the range (as it was last calculated in {@link #draw(Batch, float)}). */
    protected float getKnobPosition () {
        return this.position;
    }

    /** Sets the progress bar position, rounded to the nearest step size and clamped to the minimum and maximum values.
     * {@link #clamp(float)} can be overridden to allow values outside of the progress bar's min/max range.
     * @return false if the value was not changed because the progress bar already had the value or it was canceled by a
     *         listener. */
    private boolean setInternalValue(float internalValue, boolean createEvent) {
        internalValue = clamp(round(internalValue));
        float oldValue = this.internalValue;
        if (internalValue == oldValue) return false;
        float oldVisualValue = getVisualValue();
        this.internalValue = internalValue;

        if (programmaticChangeEvents && createEvent) {
            ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
            boolean cancelled = fire(changeEvent);
            Pools.free(changeEvent);
            if (cancelled) {
                this.value = oldValue;
            }
        }

        if (animateDuration > 0) {
            animateFromValue = oldVisualValue;
            animateTime = animateDuration;
        }
        return true;
    }

    /** Rouinds the value using the progress bar's step size. This can be overridden to customize or disable rounding. */
    protected float round (float value) {
        return Math.round(value / stepSize) * stepSize;
    }

    /** Clamps the value to the progress bar's min/max range. This can be overridden to allow a range different from the progress
     * bar knob's range. */
    protected float clamp (float value) {
        return MathUtils.clamp(value, INTERNAL_MIN, INTERNAL_MAX);
    }

    public void setNotches(int notches) {
        setStepSize(1f / notches * 2);
    }

    private void setStepSize (float stepSize) {
        if (stepSize <= 0) throw new IllegalArgumentException("steps must be > 0: " + stepSize);
        this.stepSize = stepSize;
    }

    private float getStepSize () {
        return this.stepSize;
    }

    public float getPrefWidth () {
        if (vertical) {
            Drawable knob = style.knob, bg = getBackgroundDrawable();
            return Math.max(knob == null ? 0 : knob.getMinWidth(), bg == null ? 0 : bg.getMinWidth());
        } else
            return 140;
    }

    public float getPrefHeight () {
        if (vertical)
            return 140;
        else {
            Drawable knob = style.knob, bg = getBackgroundDrawable();
            return Math.max(knob == null ? 0 : knob.getMinHeight(), bg == null ? 0 : bg.getMinHeight());
        }
    }

    /** If > 0, changes to the progress bar value via {@link #setInternalValue(float, boolean)} will happen over this duration in seconds. */
    public void setAnimateDuration (float duration) {
        this.animateDuration = duration;
    }

    /** Sets the interpolation to use for {@link #setAnimateDuration(float)}. */
    public void setAnimateInterpolation (Interpolation animateInterpolation) {
        if (animateInterpolation == null) throw new IllegalArgumentException("animateInterpolation cannot be null.");
        this.animateInterpolation = animateInterpolation;
    }

    /** Sets the interpolation to use for display. */
    public void setVisualInterpolation (Interpolation interpolation) {
        this.visualInterpolation = interpolation;
    }

    /** If true (the default), inner Drawable positions and sizes are rounded to integers. */
    public void setRound (boolean round) {
        this.round = round;
    }

    public void setDisabled (boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isAnimating () {
        return animateTime > 0;
    }

    public boolean isDisabled () {
        return disabled;
    }

    /** True if the progress bar is vertical, false if it is horizontal. **/
    public boolean isVertical () {
        return vertical;
    }

    /** If false, {@link #setInternalValue(float, boolean)} will not fire {@link ChangeEvent}. The event will only be fired when the user changes
     * the slider. */
    public void setProgrammaticChangeEvents (boolean programmaticChangeEvents) {
        this.programmaticChangeEvents = programmaticChangeEvents;
    }

    public float getIncrement() {
        return increment;
    }

    public void setIncrement(float increment) {
        this.increment = increment;
    }

    public float getInterval() {
        return interval;
    }

    public void setInterval(float interval) {
        this.interval = interval;
    }

    public float getValue() {
        return internalValue * increment + value;
    }

    public void setValue(float value) {
        float oldValue = this.value;
        if (value == oldValue) return;
        this.value = value;

        if (programmaticChangeEvents) {
            ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
            boolean cancelled = fire(changeEvent);
            Pools.free(changeEvent);
            if (cancelled) {
                this.value = oldValue;
            }
        }
    }
}
