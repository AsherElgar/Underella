package com.asherelgar.myfinalproject.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asherelgar.myfinalproject.R;
import com.asherelgar.myfinalproject.models.YouTubePlaylistData;
import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistFragment extends Fragment {

    String link;
    @BindView(R.id.rvPlaylist)
    RecyclerView rvPlaylist;
    Unbinder unbinder;
    List<YouTubePlaylistData> playlistTube;
    int runPosition;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        unbinder = ButterKnife.bind(this, view);


        YouTubePlaylistData play = (YouTubePlaylistData) getArguments().getSerializable("play");

        playlistTube.add(play);

        Toast.makeText(getContext(), playlistTube.toString(), Toast.LENGTH_SHORT).show();

        rvPlaylist.setAdapter(new YoutubePlaylist(playlistTube, getActivity(), LayoutInflater.from(getContext()), runPosition));
        rvPlaylist.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

//    public static YouTubeListFragment newInstance(String link) {
//
//        Bundle args = new Bundle();
//        args.putString("playlist", link);
//        PlaylistFragment fragment = new PlaylistFragment();
//        fragment.setArguments(args);
//        return YouTubeListFragment.newInstance(link);
//    }

    public interface OnLinkClickedPlaylist {
        void onLinkClickedPlaylist(String link, int position);
    }

    public class YoutubePlaylist extends RecyclerView.Adapter<YoutubePlaylist.YoutubeViewHolder> {
        int runPosition;
        LayoutInflater inflater;
        private List<YouTubePlaylistData> data;
        private FragmentActivity activity;


        public YoutubePlaylist(List<YouTubePlaylistData> data, FragmentActivity activity, LayoutInflater inflater, int runPosition) {
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
            YouTubePlaylistData tubeData = data.get(position);
            holder.btnLike.setLiked(true);
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

        class YoutubeViewHolder extends RecyclerView.ViewHolder implements OnLikeListener {
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
                        YouTubePlaylistData youTubeData = data.get(adapterPosition + 1);
                        String url = youTubeData.getUrl();


                        SharedPreferences pref = getActivity().getSharedPreferences("playlist", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("Link", link);
                        editor.putString("nextLink", url);
                        editor.commit();

                        if (getActivity() instanceof OnLinkClickedPlaylist) {
                            OnLinkClickedPlaylist activity = (OnLinkClickedPlaylist) getActivity();
                            activity.onLinkClickedPlaylist(link, runPosition);
                        } else
                            Toast.makeText(getActivity(), "did Not implemented OnLinkClickedMain", Toast.LENGTH_SHORT).show();

                    }
                });
            }


            @Override
            public void liked(LikeButton likeButton) {

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                int position = getAdapterPosition();
                btnLike.setLiked(false);
                YouTubePlaylistData playlist = data.get(position);
                playlistTube.remove(playlist);
            }
        }
    }

}
