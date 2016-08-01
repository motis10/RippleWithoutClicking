package com.example.rippleanimationwithoutclicking;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import java.util.HashMap;

public class RippleAnimationUtils {
    public static final int ANIMATE_CLICK_EVENT_RIPPLE_TIME = 2000;
    public static final int ANIMATE_CLICK_EVENT_FINGER_DOWN_TIME = 150;
    public static final int ANIMATE_CLICK_EVENT_FINGER_UP_TIME = 600;

    private static HashMap<Drawable, MultiTimesRunnable> runningDecayingAnimations = new HashMap<>();
    private static final Handler handler = new Handler();

    public static void animateClickEvent(final View view, final int rippleTimeInMs, final int nonRippleTimeInMs, final int animationCycleCount) {
        if (view != null) {
            Drawable drawable = view.getBackground();
            if (drawable != null) {
                MultiTimesRunnable multiTimesRunnable = new MultiTimesRunnable(view.getBackground(), rippleTimeInMs + nonRippleTimeInMs, animationCycleCount, new Runnable() {
                    @Override
                    public void run() {
                        forceRippleAnimation(view, nonRippleTimeInMs);
                    }
                });
                runningDecayingAnimations.put(drawable, multiTimesRunnable);
                handler.post(multiTimesRunnable);
            }
        }
    }

    public static void stopAnimateClickEvent(View view) {
        if (view != null) {
            Drawable background = view.getBackground();
            MultiTimesRunnable multiTimesRunnable = runningDecayingAnimations.get(background);
            if (multiTimesRunnable != null) {
                handler.removeCallbacks(multiTimesRunnable);
                runningDecayingAnimations.remove(background);

                if (background != null) {
                    background.setState(new int[]{});
                    background.setVisible(true, true);
                    view.invalidate();
                }
            }
        }
    }

    private static void forceRippleAnimation(View view, int duration) {
        final Drawable background = view.getBackground();
        if (background != null) {
            background.setState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled});
            background.setVisible(true, true);
            view.invalidate();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    background.setState(new int[]{});
                }
            }, duration);
        }
    }

    private static class MultiTimesRunnable implements Runnable {
        private final Drawable drawable;
        private int repeatingCounter;
        private final int repeatingDelay;
        private final Runnable task;

        private MultiTimesRunnable(final Drawable drawable, int repeatingDelay, int runsAmount, Runnable task) {
            this.drawable = drawable;
            this.repeatingCounter = runsAmount;
            this.repeatingDelay = repeatingDelay;
            this.task = task;
        }

        @Override
        public void run() {
            if (task != null) {
                task.run();
            }

            repeatingCounter--;
            if (repeatingCounter == 0) {
                runningDecayingAnimations.remove(drawable);
            } else {
                handler.postDelayed(this, repeatingDelay);
            }
        }
    }
}
