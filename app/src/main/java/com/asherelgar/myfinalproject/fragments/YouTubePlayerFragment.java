package com.asherelgar.myfinalproject.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asherelgar.myfinalproject.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class YouTubePlayerFragment extends Fragment {


    private static final String API_KEY = "AIzaSyC_Kf4w2BxMmavou1Phwi5h-d2LH2ioKrc";
    private static String VIDEO_ID = "EGy39OMyHzw";
    YouTubePlayer mPlayer;
    @BindView(R.id.youtube_player_view)
    FrameLayout youtubePlayerView;
    @BindView(R.id.play_video)
    ImageButton playVideo;
    @BindView(R.id.pause_video)
    ImageButton pauseVideo;
    @BindView(R.id.video_seekbar)
    SeekBar mSeekBar;
    @BindView(R.id.play_time)
    TextView mPlayTimeTextView;
    @BindView(R.id.video_control)
    LinearLayout videoControl;
    Unbinder unbinder;
    //    @BindView(R.id.fab)
//    FloatingActionButton fab;
    String linkPlayer;
    YouTubePlayer.PlayerStateChangeListener mPlayerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onAdStarted() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("joke");
            builder.show();
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
            mPlayer = null;
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
            SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
            String url = pref.getString("nextLink", "KKK");
            mPlayer.loadVideo(url);

        }

        @Override
        public void onVideoStarted() {
            displayCurrentTime();
        }
    };
    SeekBar.OnSeekBarChangeListener mVideoSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            long lengthPlayed = (mPlayer.getDurationMillis() * progress) / 100;
            mPlayer.seekToMillis((int) lengthPlayed);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    private Handler mHandler = null;
    ////
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            displayCurrentTime();
            mHandler.postDelayed(this, 100);
        }
    };
    YouTubePlayer.PlaybackEventListener mPlaybackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
            mHandler.removeCallbacks(runnable);
        }

        @Override
        public void onPlaying() {
            mHandler.postDelayed(runnable, 100);
            displayCurrentTime();
        }

        @Override
        public void onSeekTo(int arg0) {
            mHandler.postDelayed(runnable, 100);
        }

        @Override
        public void onStopped() {

            mHandler.removeCallbacks(runnable);
        }
    };

    public static YouTubePlayerFragment newInstance(String link, int position) {

        Bundle args = new Bundle();
        args.putString("link", link);
        args.putInt("position", position);
        YouTubePlayerFragment fragment = new YouTubePlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test_tube, container, false);


        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();


        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_player_view, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(API_KEY, new OnInitializedListener() {


            @Override
            public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
                mPlayer = player;
                if (!wasRestored) {
                    if (getArguments().getString("link").isEmpty()) {
                        Toast.makeText(getContext(), "error link", Toast.LENGTH_SHORT).show();
                    } else {
                        String link = getArguments().getString("link");
                        player.loadVideo(link);
                        linkPlayer = link;
                    }
                }

                player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);

                videoControl.setVisibility(View.VISIBLE);
                videoControl.setMinimumHeight(100);

                player.setPlayerStateChangeListener(mPlayerStateChangeListener);
                player.setPlaybackEventListener(mPlaybackEventListener);

            }

            @Override
            public void onInitializationFailure(Provider provider, YouTubeInitializationResult error) {
                // YouTube error
                String errorMessage = error.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("errorMessage:", errorMessage);
            }
        });

        unbinder = ButterKnife.bind(this, rootView);

        mSeekBar.setOnSeekBarChangeListener(mVideoSeekBarChangeListener);
//        mPlayTimeTextView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//                int seconds = (mPlayer.getDurationMillis() - mPlayer.getCurrentTimeMillis()) / 1000;
//                int minutes = seconds / 60;
//                int hours = minutes / 60;
//
//                int time =  (mPlayer.getDurationMillis() / 1000) - (mPlayer.getCurrentTimeMillis() / 1000);
//
//                Toast.makeText(getContext(), ""+ minutes, Toast.LENGTH_SHORT).show();
//
//                mSeekBar.setProgress(minutes);
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        mHandler = new Handler();
        return rootView;
    }

    private void displayCurrentTime() {
        if (null == mPlayer) return;
        String formattedTime = formatTime(mPlayer.getDurationMillis() - mPlayer.getCurrentTimeMillis());
        mPlayTimeTextView.setText(formattedTime);
    }

    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        return (hours == 0 ? " " : hours + ":") + String.format("%02d:%02d", minutes % 60, seconds % 60);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPlayer = null;
        unbinder.unbind();
    }

    @OnClick({R.id.play_video, R.id.pause_video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.play_video:
                if (null != mPlayer && !mPlayer.isPlaying())
                    mPlayer.play();
                break;
            case R.id.pause_video:
                if (null != mPlayer && mPlayer.isPlaying())
                    mPlayer.pause();
                break;
        }
    }

//    @OnClick(R.id.fab)
//    public void onViewClicked() {
//
//    }


}

