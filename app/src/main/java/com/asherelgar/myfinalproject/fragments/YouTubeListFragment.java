package com.asherelgar.myfinalproject.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asherelgar.myfinalproject.R;
import com.asherelgar.myfinalproject.models.YouTubeData;
import com.asherelgar.myfinalproject.models.YouTubePlaylistData;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubePlayer;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class YouTubeListFragment extends Fragment implements YouTubeData.OnYouTubeArrived {

//    OnLinkClickedMain mCallback;


    @BindView(R.id.rvNewYouTube)
    RecyclerView rvNewYouTube;
    Unbinder unbinder;
    YouTubePlayer player;
    String link;

    String channelLink = "PL3oW2tjiIxvQ1BZS58qtot3-p-lD32oWT";
    @BindView(R.id.addChannel)
    FloatingActionButton addChannel;
    @BindView(R.id.etChannel)
    EditText etChannel;

    boolean countET = false;
    int runPosition;
//
//    @Override
//    public void onGlobalLayout() {
//        Log.d("MMMG", "onGlobalLayout: "+ getView().getHeight());
//    }

    public static YouTubeListFragment newInstance(String link) {


        Bundle args = new Bundle();
        args.putString("link", link);
        YouTubeListFragment fragment = new YouTubeListFragment();
        fragment.setArguments(args);
        return YouTubeListFragment.newInstance(link);
    }

    @OnClick(R.id.addChannel)
    public void onViewClicked() {
        if (!countET) {
            etChannel.setVisibility(View.VISIBLE);
            countET = true;
        } else if (countET) {
            String l = etChannel.getText().toString();
            Log.d("JJJ", "onViewClicked: " + l);
            if (l.isEmpty()) {
                l = "PL3oW2tjiIxvQ1BZS58qtot3-p-lD32oWT";   //PL3oW2tjiIxvQ1BZS58qtot3-p-lD32oWT
                YouTubeData.getYoutube(this, l);
            } else {
                String b = l.substring(49);
                Log.d("KKK123", "onViewClicked: " + b);
                channelLink = b;

                YouTubeData.getYoutube(this, channelLink);
            }
            etChannel.setVisibility(View.GONE);
            countET = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_new_youtube, container, false);

        final ViewTreeObserver height = view.getViewTreeObserver();
        //height.addOnGlobalLayoutListener(this);

        height.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int height = view.getMeasuredHeight();
                Log.d("MMMG", "onGlobalLayout: " + height);
                if (height < 1000) {
                    addChannel.setVisibility(View.GONE);
                }
            }
        });

        unbinder = ButterKnife.bind(this, view);

        YouTubeData.getYoutube(this, channelLink);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onYoutubeArrived(final List<YouTubeData> data, final Exception e) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e == null) {
                    rvNewYouTube.setAdapter(new YoutubeNew(data, getActivity(), LayoutInflater.from(getContext()), runPosition));
                    rvNewYouTube.setLayoutManager(new LinearLayoutManager(getActivity()));
                } else
                    Toast.makeText(getActivity(), "error connecting to server", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public interface OnLinkClicked {
        void onLinkClicked(String link, int position);
    }

    public class YoutubeNew extends RecyclerView.Adapter<YoutubeNew.YoutubeViewHolder> {
        int runPosition;
        LayoutInflater inflater;
        private List<YouTubeData> data;
        private FragmentActivity activity;


        public YoutubeNew(List<YouTubeData> data, FragmentActivity activity, LayoutInflater inflater, int runPosition) {
            this.data = data;
            this.activity = activity;
            this.inflater = inflater;
            this.runPosition = runPosition;

        }


        @Override
        public YoutubeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.youtube_item, parent, false);

            return new YoutubeViewHolder(v);
        }

        @Override
        public void onBindViewHolder(YoutubeViewHolder holder, int position) {
            YouTubeData tubeData = data.get(position);
            holder.btnLike.setLiked(false);
            holder.tvDurationSong.setText(tubeData.getUrl());
            holder.tvNameSong.setText(tubeData.getSongName());
            if (tubeData.getImg().isEmpty()) {
                holder.img.setImageResource(R.drawable.ic_menu_camera);
            } else {
                Glide.with(activity).load(tubeData.getImg()).into(holder.img);
            }

            this.runPosition = position;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class YoutubeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnLikeListener {
            int runPosition;
            LikeButton btnLike;
            TextView tvNameSong, tvDurationSong;
            ImageView img;

            public YoutubeViewHolder(final View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.youtube);
                tvDurationSong = itemView.findViewById(R.id.tvDurationSong);
                tvNameSong = itemView.findViewById(R.id.tvNameSong);
                btnLike = itemView.findViewById(R.id.btnLike);

                btnLike.setOnLikeListener(this);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        link = tvDurationSong.getText().toString();
                        Log.d("YYY", link);
                        int adapterPosition = getAdapterPosition();
                        YouTubeData youTubeData = data.get(adapterPosition + 1);
                        String url = youTubeData.getUrl();

                        etChannel.setVisibility(View.GONE);
                        addChannel.setVisibility(View.GONE);

                        SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("Link", link);
                        editor.putString("nextLink", url);
                        editor.commit();

                        if (getActivity() instanceof OnLinkClicked) {
                            OnLinkClicked activity = (OnLinkClicked) getActivity();
                            activity.onLinkClicked(link, runPosition);
                        } else
                            Toast.makeText(getActivity(), "did Not implemented OnLinkClickedMain", Toast.LENGTH_SHORT).show();

                    }
                });
            }


            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                YouTubeData data1 = data.get(position);

            }


            @Override
            public void liked(LikeButton likeButton) {
                List<YouTubePlaylistData> playlistTube = new ArrayList<YouTubePlaylistData>();

                int position = getAdapterPosition();
                btnLike.setLiked(true);
                YouTubeData playlist = data.get(position);
                String favorite = playlist.getFavorite();
                String url = playlist.getUrl();
                String songName = playlist.getSongName();
                String img = playlist.getImg();
                playlistTube.add(new YouTubePlaylistData(img, songName, url));
                Toast.makeText(getContext(), playlistTube.toString(), Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putSerializable("play", (Serializable) playlistTube);
                PlaylistFragment f = new PlaylistFragment();
                f.setArguments(bundle);

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                int position = getAdapterPosition();
                btnLike.setLiked(false);
                YouTubeData playlist = data.get(position);
//                playlistTube.remove(playlist);
            }
        }
    }

}
