//package com.asherelgar.myfinalproject.fragments;
//
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.asherelgar.myfinalproject.R;
//import com.asherelgar.myfinalproject.models.ShoppingList;
//import com.bumptech.glide.Glide;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class ShoppingListFragment extends Fragment {
//
//
//    private FirebaseUser mUser;
//    private FirebaseDatabase mDatabase;
//
//    @BindView(R.id.rvShoppingList)
//    RecyclerView rvShoppingList;
//    Unbinder unbinder;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
//        unbinder = ButterKnife.bind(this, view);
//
//        mUser = FirebaseAuth.getInstance().getCurrentUser();
//        mDatabase = FirebaseDatabase.getInstance();
//
//        rvShoppingList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        DatabaseReference ref = mDatabase.getReference("ShoppingList").child(mUser.getUid());
//        rvShoppingList.setAdapter(new ShoppingAdapter(ref, getChildFragmentManager(), getActivity()));
//        return view;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
//
//    @OnClick(R.id.favAdd)
//    public void onAddClicked() {
//        AddShoppingListDialog dialog = new AddShoppingListDialog();
//        dialog.show(getFragmentManager(), "addListDialog");
//
////        mDatabase.getReference("UserList").child(mUser.getUid()).push().setValue("new title");
//    }
//
//    public static class ShoppingAdapter extends FirebaseRecyclerAdapter<ShoppingList, ShoppingAdapter.ShoppingListViewHolder>{
//
//        private FragmentManager fm;
//        private FragmentActivity a;
//        public ShoppingAdapter(Query ref, FragmentManager fm, FragmentActivity a) {
//            super(ShoppingList.class, R.layout.shopping_list_name, ShoppingListViewHolder.class, ref);
//            this.fm = fm;
//            this.a = a;
//        }
//
//        @Override
//        protected void populateViewHolder(final ShoppingListViewHolder viewHolder, ShoppingList list, int position) {
//
//            viewHolder.tvTitle.setText(list.getTitle() + " \n\n" + list.getUpdaeTime().substring(0,19));
//            Glide.with(viewHolder.tvTitle.getContext()).load(list.getProfileImage()).into(viewHolder.ivProfile);
//            viewHolder.shoppingList = list;
//            viewHolder.modelKey = getRef(position).getKey();
//            viewHolder.fm = fm;
//            viewHolder.a = a;
//        }
//
//        public static class ShoppingListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//            TextView tvTitle;
//            ImageView ivProfile, ivShare;
//            ShoppingList shoppingList;
//            String modelKey;
//            FragmentManager fm;
//            FragmentActivity a;
//
//            public ShoppingListViewHolder(View itemView) {
//                super(itemView);
//                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
//                ivProfile = (ImageView) itemView.findViewById(R.id.ivOwner);
//                ivShare = (ImageView) itemView.findViewById(R.id.ivShare);
//                ivShare.setOnClickListener(this);
//
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ChatFragment shoppingListProductsFragment = ChatFragment.newInstance(shoppingList);
//
//                        a.getSupportFragmentManager().beginTransaction().replace(R.id.allFrame, shoppingListProductsFragment).commit();
//                    }
//                });
//
//            }
//
//            @Override
//            public void onClick(View v) {
//                ShareFragment dialog = ShareFragment.newInstance(shoppingList);
//                dialog.show(fm, "userDialog");
//
//            }
//        }
//    }
//}
