package com.asherelgar.myfinalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.asherelgar.myfinalproject.fragments.AlcoholFragment;
import com.asherelgar.myfinalproject.fragments.ContactFragment;
import com.asherelgar.myfinalproject.fragments.HostFragment;
import com.asherelgar.myfinalproject.fragments.StepCounterFragment;
import com.asherelgar.myfinalproject.fragments.WeatherFragment;
import com.asherelgar.myfinalproject.fragments.YnetFragment;
import com.asherelgar.myfinalproject.fragments.YouTubeListFragment;
import com.asherelgar.myfinalproject.fragments.YouTubePlayerFragment;
import com.asherelgar.myfinalproject.models.User2;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.Scopes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.LocalTime;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//import com.asherelgar.myfinalproject.fragments.ShoppingListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, YouTubeListFragment.OnLinkClicked, View.OnClickListener {


    private static final int RC_SIGNIN = 1;
    String linkU;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.frame)
    ConstraintLayout frame;
    //    @BindView(R.id.mViewPager)
//    ViewPager mViewPager;
    ImageView imageProfile;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    //    @BindView(R.id.frame_lay)
//    FrameLayout frameLay;
//    @BindView(R.id.frameRecycler)
//    FrameLayout frameRecycler;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    TextView tvNameDrawer, tvTime;
    String linkClick;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            mUser = firebaseAuth.getCurrentUser();
            if (mUser == null) {
                //GOTO Login

                List<AuthUI.IdpConfig> mProviders = Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).setPermissions(Arrays.asList(Scopes.EMAIL, Scopes.PROFILE)).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).setPermissions(Arrays.asList(Scopes.EMAIL, Scopes.PROFILE)).setPermissions(Arrays.asList("user_friends")).build()

                );
                @SuppressWarnings("deprecation") Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                        .setLogo(R.drawable.umbrella_2)
                        .setTheme(R.style.MyTheme)
                        .setProviders(mProviders)
                        .setIsSmartLockEnabled(false)
                        .build();

                startActivityForResult(intent, RC_SIGNIN);
            } else {
                User2 u = new User2(mUser);
                mDatabase.getReference("Users").child(mUser.getUid()).setValue(u);
                tvNameDrawer.setText(mUser.getDisplayName());
                //tvNameDrawer.setText("Taylor Shore");
                tvTime.setText(LocalTime.now().toString("HH:mm"));

                //Log.d("HACKERU$$$", mUser.getPhotoUrl().toString());

//                Picasso.with(MainActivity.this).load(mUser.getPhotoUrl()).into(imageProfile);
                Glide.with(MainActivity.this).load(mUser.getPhotoUrl()).into(imageProfile);

                //imageProfile.setImageResource(R.drawable.profile_image);
                getSupportFragmentManager().beginTransaction().replace(R.id.allFrame, new YnetFragment()).commit();


                SharedPreferences pref = getSharedPreferences("SkipLib", Context.MODE_PRIVATE);
                boolean skip = pref.getBoolean("SkipLib", false);
                if (!skip) {
                    Intent intent = new Intent(MainActivity.this, AppIntroLibActivity.class);
                    startActivity(intent);
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mAuth.removeAuthStateListener(mAuthListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setSupportActionBar(toolbar);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        tvNameDrawer = headerView.findViewById(R.id.tvNameProfile);
        imageProfile = headerView.findViewById(R.id.imageProfile);
        tvTime = headerView.findViewById(R.id.tvTime);


        sha1();

//        FragmentManager fm = getSupportFragmentManager();
//
//        youTubeListFragment = new YouTubeListFragment();
//        youTubePlayerFragment = new YouTubePlayerFragment();


    }

    void sha1() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.asherelgar.shopee", PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA1");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //
//            @Override
//    public void onStart() {
//            super.onStart();
//
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_sign_out:
                AuthUI.getInstance().signOut(MainActivity.this);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_news) {
            getSupportFragmentManager().beginTransaction().replace(R.id.allFrame, new YnetFragment(), "Y").commit();

//            frameRecycler.setVisibility(View.INVISIBLE);


            // Handle the camera action
        } else if (id == R.id.nav_music) {
            getSupportFragmentManager().beginTransaction().replace(R.id.allFrame, new HostFragment()).commit();

        } else if (id == R.id.nav_alcohol) {
            getSupportFragmentManager().beginTransaction().replace(R.id.allFrame, new AlcoholFragment(), "A").commit();

        } else if (id == R.id.nav_manage) {
            getSupportFragmentManager().beginTransaction().replace(R.id.allFrame, new ContactFragment()).commit();

        } else if (id == R.id.nav_share) {

            getSupportFragmentManager().beginTransaction().replace(R.id.allFrame, new WeatherFragment()).commit();

        } else if (id == R.id.nav_send) {

            SensorManager mSensorManager;

            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
//            for (int i = 0; i< deviceSensors.size(); i++) {
//                if (deviceSensors.get(i).getType() == Sensor.TYPE_ACCELEROMETER) {
//                    Toast.makeText(this, "No Sensor At Your Phone :(", Toast.LENGTH_LONG).show();
//                    getSupportFragmentManager().beginTransaction().replace(R.id.allFrame, new YnetFragment(), "Y").commit();
//                    break;
//                }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.allFrame, new StepCounterFragment()).commit();
//                }
//            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onLinkClicked(final String link, int position) {

        FrameLayout videoFrame = (FrameLayout) findViewById(R.id.videoFrame);
        videoFrame.setVisibility(View.VISIBLE);
        ImageView ivChat = (ImageView) findViewById(R.id.ivChat);

        ivChat.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.videoFrame, YouTubePlayerFragment.newInstance(link, position), "rv")
                .commit();

//
//        ivChat.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//
//            ShoppingList list = new ShoppingList(link);
//            getSupportFragmentManager().beginTransaction().replace(R.id.buttonFrame, ChatFragment.newInstance(list)).commit();
//
//            ImageView ivChat = (ImageView) findViewById(R.id.ivChat);
//            ivChat.setVisibility(View.GONE);
//
//        }
//    });

    }

    @Override
    public void onClick(View v) {

    }
}
