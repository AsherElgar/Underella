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
import android.widget.LinearLayout;

import com.asherelgar.myfinalproject.R;
import com.asherelgar.myfinalproject.models.ShoppingList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class HostFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.videoFrame)
    FrameLayout videoFrame;
    @BindView(R.id.buttonFrame)
    FrameLayout buttonFrame;
    Unbinder unbinder;
    String youLink;
    LinearLayout linearLayout;
    ImageView ivChat;


    YouTubePlayerFragment youTubePlayerFragment;
    YouTubeListFragment youTubeListFragment;
    @BindView(R.id.vpButtomPager)
    ViewPager vpButtomPager;
    @BindView(R.id.ivList)
    ImageView ivList;

    public static HostFragment newInstance(String link) {

        Bundle args = new Bundle();
        args.putString("link", link);
        HostFragment fragment = new HostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_host, container, false);

        ivChat = v.findViewById(R.id.ivChat);


//        youLink = getArguments().getString("link");

        videoFrame = v.findViewById(R.id.videoFrame);


        getChildFragmentManager().beginTransaction().
                replace(R.id.buttonFrame, new YouTubeListFragment(), "rv")
                .commit();

//                Bundle bundle = getArguments();
//                link = bundle.getString("link");
//                if (bundle != null) {
////                    if (bundle.getBoolean("clicked")) {
//                        onRvclick();
////                    }
//                }


        ivChat.setOnClickListener(this);

        unbinder = ButterKnife.bind(this, v);


        return v;
    }


//    public void onRvclick() {
//
//        //we know the link...
//
//        Bundle bundle = getArguments();
//
////        boolean clicked = bundle.getBoolean("clicked");
//
//        videoFrame.setVisibility(View.VISIBLE);
//
////        getChildFragmentManager().beginTransaction().
////                replace(R.id.videoFrame,  YouTubePlayerFragment.newInstance(youLink, ), "rv")
////                .commit();
//
//    }
//
//
//    public void onChatClicked() {
//
//        //we know the link...
//
//
//        String link = "";
////        getChildFragmentManager().beginTransaction().
////                replace(R.id.videoFrame,  ChatFrag.newInstance(link), "rv")
////                .commit();
//
//    }
//
//
////}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onClick(View v) {

        buttonFrame.setBackgroundColor(Color.parseColor("#9E737647"));
        videoFrame.setBackgroundColor(Color.parseColor("#9E737647"));

        SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String link = pref.getString("Link", youLink);

        ShoppingList list = new ShoppingList(link);
        getChildFragmentManager().beginTransaction().replace(R.id.buttonFrame, ChatFragment.newInstance(list)).commit();

        ivChat.setVisibility(View.GONE);
        ivList.setVisibility(View.VISIBLE);
        ivList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivChat.setVisibility(View.VISIBLE);
                getChildFragmentManager().beginTransaction().
                        replace(R.id.buttonFrame, new YouTubeListFragment(), "rv")
                        .commit();
                ivList.setVisibility(View.GONE);

            }
        });
//        Toast.makeText(getContext(), link, Toast.LENGTH_SHORT).show();


//        vpButtomPager.setAdapter(new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                switch (position){
//                    case 0:
//                        ShoppingList list = new ShoppingList(" k");
//                        getChildFragmentManager().beginTransaction().replace(R.id.buttonFrame, ChatFragment.newInstance(list)).commit();
//                        break;
//                    case 1:
//                        getChildFragmentManager().beginTransaction().
//                                replace(R.id.buttonFrame, new YouTubeListFragment(), "rv")
//                                .commit();
//                        break;
//
//                }
//                return null;
//            }
//
//            @Override
//            public int getCount() {
//                return 2;
//            }
//        });
    }


}
