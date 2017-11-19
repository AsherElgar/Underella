//package com.asherelgar.myfinalproject.fragments;
//
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.asherelgar.myfinalproject.R;
//import com.asherelgar.myfinalproject.models.ShoppingList;
//import com.asherelgar.myfinalproject.models.User2;
//import com.bumptech.glide.Glide;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.Unbinder;
//import de.hdodenhof.circleimageview.CircleImageView;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class ShareFragment extends DialogFragment {
//
//
//    @BindView(R.id.rvUser)
//    RecyclerView rvUser;
//    Unbinder unbinder;
//
//    ShoppingList list;
//
//    public static ShareFragment newInstance(ShoppingList shoppingList) {
//
//        Bundle args = new Bundle();
//        args.putParcelable("ShoppingList", shoppingList);
//        ShareFragment fragment = new ShareFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_share, container, false);
//        unbinder = ButterKnife.bind(this, view);
//
//        Query ref = FirebaseDatabase.getInstance().getReference("Users");
//
//
//        rvUser.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        list = getArguments().getParcelable("ShoppingList");
//        ShareAdapter adapter = new ShareAdapter(getContext(), ref, list);
//        rvUser.setAdapter(adapter);
//
//        Toast.makeText(getContext(), list.toString(), Toast.LENGTH_SHORT).show();
//        return view;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
//
//    public static class ShareAdapter extends FirebaseRecyclerAdapter<User2, ShareAdapter.SharViewHolder> {
//
//        private Context context;
//        private ShoppingList list;
//        public ShareAdapter(Context context, Query ref, ShoppingList list) {
//            super(User2.class, R.layout.share_item, SharViewHolder.class, ref);
//            this.context = context;
//            this.list = list;
//        }
//
//        @Override
//        protected void populateViewHolder(SharViewHolder viewHolder, User2 model, int position) {
//            viewHolder.tvUserName.setText(model.getName());
//            Glide.with(context).load(model.getPhotoUrl()).into(viewHolder.ivProfile);
//
//            viewHolder.u  = model;
//            viewHolder.list = list;
//
//        }
//
//        public static class SharViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//            TextView tvUserName;
//            CircleImageView ivProfile;
//            User2 u;
//            public ShoppingList list;
//
//            public SharViewHolder(View itemView) {
//                super(itemView);
//                tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
//                ivProfile = (CircleImageView) itemView.findViewById(R.id.ivProfile);
//
//                itemView.setOnClickListener(this);
//            }
//
//            @Override
//            public void onClick(View v) {
//                FirebaseDatabase.getInstance().
//                        getReference("ShoppingList").child(u.getUserID()).child(list.getListID()).setValue(list);
//            }
//
//            void onClick(){
//                FirebaseDatabase.getInstance().getReference("ShoppingList").child(u.getUserID()).child(list.getListID()).removeValue();
//
//                FirebaseDatabase.getInstance().getReference("ListItems").child(u.getUserID()).child(list.getListID()).removeValue();
//            }
//        }
//    }
//}
