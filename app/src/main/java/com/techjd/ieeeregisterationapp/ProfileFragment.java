package com.techjd.ieeeregisterationapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private TextView username;
    private FirebaseAuth mAuth;
    private String currentusername;
    private  DatabaseReference databaseReference;
    private TextView Amt ;
    private TextView Total ;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        
        username = view.findViewById(R.id.username);
        Amt = view.findViewById(R.id.textView9);
        Total = view.findViewById(R.id.textView11);
        mAuth = FirebaseAuth.getInstance();
        currentusername = mAuth.getCurrentUser().getUid();
        getUserName();
        getAmt();
        getTotal();
    }



    private void getUserName() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(currentusername);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() == null){
                    return;
                }
                 username user = dataSnapshot.getValue(username.class);


                username.setText(user.getFullname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getTotal() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RegisteredPsersons");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Total.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAmt() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RegisteredPsersons");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long totalamout = dataSnapshot.getChildrenCount();
                int total = (int) totalamout;

                int multiply = 350;

                int result = multiply * total ;

                String finalamt = String.valueOf(result);
                Amt.setText(""+finalamt);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
