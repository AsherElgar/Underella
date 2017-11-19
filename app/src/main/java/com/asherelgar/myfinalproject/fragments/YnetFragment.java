package com.asherelgar.myfinalproject.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asherelgar.myfinalproject.R;
import com.asherelgar.myfinalproject.models.YnetDataSource;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * A fragment representing a list of Alcohol Items.
 * <p/>
 */
public class YnetFragment extends Fragment implements YnetDataSource.OnYnetArrivedListener {

    RecyclerView v;

    public static YnetFragment newInstance(int col) {

        Bundle args = new Bundle();
        args.putInt("col", col);
        YnetFragment fragment = new YnetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = (RecyclerView) inflater.inflate(R.layout.fragment_ynet, container, false);

        YnetDataSource.getYnet(this);

        return v;
    }


    @Override
    public void onYnetArrived(final List<YnetDataSource.Ynet> data, final Exception e) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e == null) {
                    v.setAdapter(new YnetAdapter(data, getActivity()));
                    v.setLayoutManager(new LinearLayoutManager(getActivity()));
                } else
                    Toast.makeText(getActivity(), "error connecting to server", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class YnetAdapter extends RecyclerView.Adapter<YnetAdapter.YnetViewHolder> {
        LayoutInflater inflater;
        private List<YnetDataSource.Ynet> data;
        private FragmentActivity activity;

        public YnetAdapter(List<YnetDataSource.Ynet> ynet, FragmentActivity context) {
            this.data = ynet;
            this.activity = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public YnetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.ynet_list_item, parent, false);

            return new YnetViewHolder(v);
        }

        @Override
        public void onBindViewHolder(YnetViewHolder holder, int position) {
            YnetDataSource.Ynet ynet = data.get(position);
            holder.tvTitle.setText(ynet.getTitle());
            holder.tvContent.setText(ynet.getContent());
            if (ynet.getThumbnail().isEmpty()) {
                holder.ivImage.setImageResource(R.drawable.ic_menu_camera);
            } else {
                Glide.with(activity).load(ynet.getThumbnail()).into(holder.ivImage);
            }
//            Picasso.with(activity).load(ynet.getThumbnail()).into(holder.ivImage);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class YnetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView ivImage;
            TextView tvTitle, tvContent;

            public YnetViewHolder(View itemView) {
                super(itemView);
                ivImage = itemView.findViewById(R.id.ivAlcoImg);
                tvTitle = itemView.findViewById(R.id.tvTitleAlco);
                tvContent = itemView.findViewById(R.id.tvContent);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                YnetDataSource.Ynet ynet = data.get(position);
//                Toast.makeText(activity, ynet.getTitle(), Toast.LENGTH_SHORT).show();

                activity.getSupportFragmentManager().beginTransaction().addToBackStack("Ynet").replace(R.id.allFrame, YnetWebViewFragment.newInstance(ynet.getUrl())).commit();

                //Toast.makeText(getContext(), ""+ ynet.getUrl(), Toast.LENGTH_SHORT).show();

            }
        }
    }
}
