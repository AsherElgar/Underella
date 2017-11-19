package com.asherelgar.myfinalproject.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.asherelgar.myfinalproject.R;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {


    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etMessage)
    EditText etMessage;

    Unbinder unbinder;

    DatabaseReference ref;
    @BindView(R.id.btnSignIn)
    ActionProcessButton btnSignIn;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("ContactMessage").child(user.getUid());


        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.btnSignIn)
    public void onViewClicked() {
        final Editable name = etName.getText();
        Editable email = etEmail.getText();
        Editable message = etMessage.getText();

        ref.setValue("Name: " + name + " Email: " + email + " Message: " + message);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AlertDialog.Builder d = new AlertDialog.Builder(getContext());
                d.setTitle("Dear, " + name);
                d.setMessage("We appreciate you contacting us,\nOne of our colleagues will get back to you shortly.\n" +
                        "\n" +
                        "Have a great day!");
                d.setCancelable(true);
                d.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        getFragmentManager().beginTransaction().replace(R.id.allFrame, new YnetFragment(), "Y").commit();
                    }
                });

                AlertDialog alert = d.create();
                alert.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
