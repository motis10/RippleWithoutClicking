package com.example.rippleanimationwithoutclicking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private boolean isRippleShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.tv);

        if (textView != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isRippleShowing) {
                        textView.setText("START RIPPLE");
                        RippleAnimationUtils.stopAnimateClickEvent(textView);

                    } else {
                        textView.setText("STOP RIPPLE");
                        RippleAnimationUtils.animateClickEvent(textView, RippleAnimationUtils.ANIMATE_CLICK_EVENT_FINGER_UP_TIME,
                                RippleAnimationUtils.ANIMATE_CLICK_EVENT_FINGER_DOWN_TIME, RippleAnimationUtils.ANIMATE_CLICK_EVENT_RIPPLE_TIME);
                    }

                    isRippleShowing = !isRippleShowing;
                }
            });
        }
    }
}
