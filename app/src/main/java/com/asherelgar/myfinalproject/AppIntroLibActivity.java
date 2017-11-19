package com.asherelgar.myfinalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AppIntroLibActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        addSlide(AppIntroFragment.newInstance(0));
//        addSlide(AppIntroFragment.newInstance(1));
//        addSlide(AppIntroFragment.newInstance(2));
//        addSlide(AppIntroFragment.newInstance(3));
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String name = currentUser.getDisplayName();
        addSlide(com.github.paolorotolo.appintro.AppIntroFragment.newInstance
                ("ברוך הבא, " + name,
                        "אנחנו שמחים לצרף אותך למשפחה שלנו...",
                        R.drawable.umbrella_icon_end,
                        getResources().getColor(R.color.bootstrap_brand_warning)
                ));
        addSlide(com.github.paolorotolo.appintro.AppIntroFragment.newInstance
                ("להיות בעניינים,",
                        "בכל רגע נתון...",
                        R.drawable.rss_reader,
                        getResources().getColor(R.color.bootstrap_brand_danger)
                ));

        addSlide(com.github.paolorotolo.appintro.AppIntroFragment.newInstance
                ("לדעת את מזג האוויר",
                        "לפני דני רופ...",
                        R.drawable.weather,
                        getResources().getColor(R.color.colorAccent)
                ));

        addSlide(com.github.paolorotolo.appintro.AppIntroFragment.newInstance
                ("מי שמזמזם חי יותר",
                        "תאזינו לשירים שאתם אוהבים...",
                        R.drawable.listen_music,
                        getResources().getColor(R.color.bootstrap_brand_danger)
                ));
        addSlide(com.github.paolorotolo.appintro.AppIntroFragment.newInstance
                ("תשמרו על עצמכם...",
                        "לדעת כמה אתם שורפים...",
                        R.drawable.footprints,
                        getResources().getColor(R.color.bootstrap_brand_success)
                ));
        addSlide(com.github.paolorotolo.appintro.AppIntroFragment.newInstance
                ("מעכשיו החיים לא יהיו אותו הדבר",
                        "בואו נתחיל...",
                        R.drawable.ok_icon,
                        getResources().getColor(R.color.bootstrap_brand_info)
                ));
        showSkipButton(false);

//        setFadeAnimation();
//        setZoomAnimation();
        setFlowAnimation();
//        setSlideOverAnimation();
//        setDepthAnimation();

//        askForPermissions(new String[]{Manifest.permission.CAMERA}, 2);


    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        SharedPreferences pref = getSharedPreferences("SkipLib", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("SkipLib", true);
        editor.commit();
        finish();
    }
}

