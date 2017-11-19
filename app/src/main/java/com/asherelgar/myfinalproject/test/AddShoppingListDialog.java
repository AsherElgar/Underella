//package com.asherelgar.myfinalproject.fragments;
//
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//
//import com.asherelgar.myfinalproject.R;
//import com.asherelgar.myfinalproject.models.ShoppingList;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.Date;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class AddShoppingListDialog extends DialogFragment {
//
//
//    @BindView(R.id.logo)
//    ImageView logo;
//    @BindView(R.id.etListTitle)
//    EditText etListTitle;
//    @BindView(R.id.btnDone)
//    Button btnDone;
//    Unbinder unbinder;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_add_shopping_list, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        return view;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
//
//    @OnClick(R.id.btnDone)
//    public void onDoneClicked() {
//        String title = etListTitle.getText().toString();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        Uri photoUrl = user.getPhotoUrl();
//
//        String img = "https://firebasestorage.googleapis.com/v0/b/shopee-c946a.appspot.com/o/default_avatar.png?alt=media&token=1bb47198-9680-439a-a35e-f927c7c627ac";
//
//        if (photoUrl != null){
//            img = photoUrl.toString();
//        }
//
//        DatabaseReference rowRef = FirebaseDatabase.getInstance().getReference("ShoppingList").child(user.getUid()).push();
//
//        String key = rowRef.getKey();
//
//        ShoppingList list = new ShoppingList(title, user.getUid(), new Date().toString(),img, key);
//
//        rowRef.setValue(list);
//
//        dismiss();
//    }
//}
