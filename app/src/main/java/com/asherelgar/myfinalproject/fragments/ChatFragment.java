package com.asherelgar.myfinalproject.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.asherelgar.myfinalproject.R;
import com.asherelgar.myfinalproject.models.ShoppingList;
import com.asherelgar.myfinalproject.models.ShoppingListItem;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.joda.time.LocalTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {


    @BindView(R.id.rvShoppingListProducts)
    RecyclerView rvShoppingListProducts;
    Unbinder unbinder;
    ShoppingList list;
    @BindView(R.id.etProduct)
    EditText etProduct;
    //    @BindView(R.id.tvSongNameChat)
//    TextView tvSongNameChat;
//    @BindView(R.id.frameList)
//    FrameLayout frameList;
    @BindView(R.id.favAdd)
    ImageView favAdd;
    int itemCount;

    public static ChatFragment newInstance(ShoppingList list) {

        Bundle args = new Bundle();
        args.putParcelable("List", list);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_list_products, container, false);
        unbinder = ButterKnife.bind(this, view);

        list = getArguments().getParcelable("List");

        rvShoppingListProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ShoppingList").child(list.getListID());
        ProductAdapter adapter = new ProductAdapter(ref);
        rvShoppingListProducts.setAdapter(adapter);

//        rvShoppingListProducts.scrollToPosition();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.favAdd)
    public void onViewClicked() {
        if (etProduct.getText().length() < 1) {
            etProduct.setError("Can't be empty");
            return;
        }
        //1) ref to the table with a new row:
        DatabaseReference newItemRef = FirebaseDatabase.getInstance().getReference("ShoppingList").child(list.getListID()).push();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ShoppingListItem item = new ShoppingListItem(
                etProduct.getText().toString(),
                currentUser.getUid(), /*current user id*/
                newItemRef.getKey(),/*key of the new row that we pushed*/
                false); /*not deleted yet*/

        newItemRef.setValue(item);

        etProduct.setText(null);
    }


    public static class ProductAdapter extends FirebaseRecyclerAdapter<ShoppingListItem, ProductAdapter.ProductViewHolder> {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        public ProductAdapter(Query query) {
            super(ShoppingListItem.class, R.layout.shopping_list_product_item, ProductViewHolder.class, query);
        }

        @Override
        protected void populateViewHolder(ProductViewHolder viewHolder, ShoppingListItem model, int position) {

            viewHolder.tvProduct.setText(model.getName());
            Glide.with(viewHolder.tvProduct.getContext()).load(user.getPhotoUrl()).into(viewHolder.ivOwner);
            String s = LocalTime.now().toString("HH:mm");
            viewHolder.tvTime.setText(s);

        }


        public static class ProductViewHolder extends RecyclerView.ViewHolder {
            TextView tvProduct, tvTime;
            ShoppingListItem model;
            CircleImageView ivOwner;

            public ProductViewHolder(View itemView) {
                super(itemView);
                ivOwner = itemView.findViewById(R.id.ivOwner);
                tvProduct = itemView.findViewById(R.id.tvProduct);
                tvTime = itemView.findViewById(R.id.tvTime);

            }
        }
    }
}
