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
import com.asherelgar.myfinalproject.models.AlcoholDataSource;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * A fragment representing a list of Alcohol Items.
 * <p/>
 */
public class AlcoholFragment extends Fragment implements AlcoholDataSource.OnAlcoholArrivedListener {

    RecyclerView rv;

    public static AlcoholFragment newInstance(int col) {

        Bundle args = new Bundle();
        args.putInt("col", col);
        AlcoholFragment fragment = new AlcoholFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alcohol, container, false);

        rv = v.findViewById(R.id.rvAlcohol);

        AlcoholDataSource.getAlcohol(this);

        return v;
    }


    @Override
    public void onAlcoholArrived(final List<AlcoholDataSource.Alcohol> data, final Exception e) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e == null) {
                    rv.setAdapter(new AlcoholAdapter(data, getActivity()));
                    rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                } else
                    Toast.makeText(getActivity(), "error connecting to server", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class AlcoholAdapter extends RecyclerView.Adapter<AlcoholAdapter.AlcoholViewHolder> {
        LayoutInflater inflater;
        private List<AlcoholDataSource.Alcohol> data;
        private FragmentActivity activity;

        public AlcoholAdapter(List<AlcoholDataSource.Alcohol> alcohols, FragmentActivity context) {
            this.data = alcohols;
            this.activity = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public AlcoholViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.alcohol_list_item, parent, false);

            return new AlcoholViewHolder(v);
        }

        @Override
        public void onBindViewHolder(AlcoholViewHolder holder, int position) {
            AlcoholDataSource.Alcohol alcohol = data.get(position);
            holder.tvTitle.setText(alcohol.getTitle());
            holder.tvContent.setText(alcohol.getContent());
            if (alcohol.getThumbnail().isEmpty()) {
                holder.ivImage.setImageResource(R.drawable.ic_menu_camera);
            } else {
                Glide.with(activity).load(alcohol.getThumbnail()).into(holder.ivImage);
            }
//            Picasso.with(activity).load(ynet.getThumbnail()).into(holder.ivImage);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class AlcoholViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView ivImage;
            TextView tvTitle, tvContent, tvFullContent;

            public AlcoholViewHolder(View itemView) {
                super(itemView);
                ivImage = itemView.findViewById(R.id.ivAlcoImg);
                tvTitle = itemView.findViewById(R.id.tvTitleAlco);
                tvContent = itemView.findViewById(R.id.tvContent);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                AlcoholDataSource.Alcohol alcohol = data.get(position);
//                Toast.makeText(activity, ynet.getTitle(), Toast.LENGTH_SHORT).show();

                activity.getSupportFragmentManager().beginTransaction().
                        addToBackStack("food").
                        replace(R.id.allFrame, FoodWebViewFragment.newInstance(alcohol.getUrl())).commit();
            }

//                tvContent.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        tvFullContent.setVisibility(View.VISIBLE);
//                    }
//                });
//                ivImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        tvFullContent.setVisibility(View.GONE);
//                    }
//                });

        }

    }
}
