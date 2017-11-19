package com.asherelgar.myfinalproject.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.asherelgar.myfinalproject.R;
import com.asherelgar.myfinalproject.models.ShoppingList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.asherelgar.myfinalproject.R.id.buttonFrame;


/**
 * A simple {@link Fragment} subclass.
 */
public class HostPlaylistFragment extends Fragment implements View.OnClickListener {


    Unbinder unbinder;
    String youLink;


    YouTubePlayerFragment youTubePlayerFragment;


    @BindView(R.id.vpButtomPager)
    ViewPager vpButtomPager;
    @BindView(R.id.videoFramePlaylist)
    FrameLayout videoFramePlaylist;
    @BindView(R.id.ivChatPlaylist)
    ImageView ivChatPlaylist;
    @BindView(R.id.ivListPlaylist)
    ImageView ivListPlaylist;
    @BindView(R.id.vpButtomPagerPlaylist)
    ViewPager vpButtomPagerPlaylist;
    @BindView(R.id.buttonFramePlaylist)
    FrameLayout buttonFramePlaylist;

    public static HostPlaylistFragment newInstance(String link) {

        Bundle args = new Bundle();
        args.putString("linkPlay", link);
        HostPlaylistFragment fragment = new HostPlaylistFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_playlist_host, container, false);

        ivChatPlaylist = v.findViewById(R.id.ivChatPlaylist);


//        youLink = getArguments().getString("link");

        videoFramePlaylist = v.findViewById(R.id.videoFramePlaylist);
//
//        getChildFragmentManager().beginTransaction().
//                replace(R.id.buttonFramePlaylist, new PlaylistFragment(), "pl")
//                .commit();
//
//                Bundle bundle = getArguments();
//                link = bundle.getString("link");
//                if (bundle != null) {
////                    if (bundle.getBoolean("clicked")) {
//                        onRvclick();
////                    }
//                }

        ivChatPlaylist.setOnClickListener(this);

        unbinder = ButterKnife.bind(this, v);


        return v;
    }


    public void onRvclick() {

        //we know the link...

        Bundle bundle = getArguments();

//        boolean clicked = bundle.getBoolean("clicked");

        videoFramePlaylist.setVisibility(View.VISIBLE);

//        getChildFragmentManager().beginTransaction().
//                replace(R.id.videoFrame,  YouTubePlayerFragment.newInstance(youLink, ), "rv")
//                .commit();

    }


    public void onChatClicked() {

        //we know the link...


        String link = "";
//        getChildFragmentManager().beginTransaction().
//                replace(R.id.videoFrame,  ChatFrag.newInstance(link), "rv")
//                .commit();

    }


//}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onClick(View v) {

        buttonFramePlaylist.setBackgroundColor(Color.parseColor("#9E737647"));
        videoFramePlaylist.setBackgroundColor(Color.parseColor("#9E737647"));

        SharedPreferences pref = getActivity().getSharedPreferences("playlist", Context.MODE_PRIVATE);
        String link = pref.getString("Link", youLink);

        ShoppingList list = new ShoppingList(link);
        getChildFragmentManager().beginTransaction().replace(buttonFrame, ChatFragment.newInstance(list)).commit();

        ivChatPlaylist.setVisibility(View.GONE);
        ivListPlaylist.setVisibility(View.VISIBLE);
        ivListPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivChatPlaylist.setVisibility(View.VISIBLE);
                getChildFragmentManager().beginTransaction().
                        replace(buttonFrame, new PlaylistFragment(), "pl")
                        .commit();
                ivListPlaylist.setVisibility(View.GONE);

            }
        });

    }


}
