package com.asherelgar.myfinalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class PageLoadActivity extends Activity {
    Thread splashTread;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_load);
        StartAnimations();
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        ConstraintLayout l = findViewById(R.id.conLoad);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate2);
        anim.reset();
        TextView tvLogoText = findViewById(R.id.tvLogoText);
        tvLogoText.clearAnimation();
        tvLogoText.startAnimation(anim);
        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim1.reset();
//        TextView tvTextStart = (TextView) findViewById(R.id.tvTextStart);
//        tvTextStart.clearAnimation();
//        tvTextStart.startAnimation(anim);
        ImageView ivText = findViewById(R.id.ivText);
        ivText.clearAnimation();
        ivText.startAnimation(anim1);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(PageLoadActivity.this,
                            MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    PageLoadActivity.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    PageLoadActivity.this.finish();
                }

            }
        };
        splashTread.start();

    }

}


