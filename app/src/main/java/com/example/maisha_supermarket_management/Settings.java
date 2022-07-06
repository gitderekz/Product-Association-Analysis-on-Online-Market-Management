package com.example.maisha_supermarket_management;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Settings extends Fragment implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;

    CardView light;
    CardView dark;
    CardView name;
    CardView contacts;
    CardView gift;
    CardView share;
    CardView rate;
    CardView logInOrOut;

    ImageView profile;
    TextView contactName;
    TextView phoneNumber;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static Settings newInstance(String param1, String param2) {
        Settings fragment = new Settings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
//        light = view.findViewById(R.id.card_view1);
//        dark = view.findViewById(R.id.card_view2);
        name = view.findViewById(R.id.card_view3);
        contacts = view.findViewById(R.id.card_view4);
        gift = view.findViewById(R.id.card_view5);
        share = view.findViewById(R.id.card_view6);
        rate = view.findViewById(R.id.card_view7);
        logInOrOut = view.findViewById(R.id.card_view8);

        profile = view.findViewById(R.id.imageView3);
        contactName = view.findViewById(R.id.textViewName3);
        phoneNumber = view.findViewById(R.id.textViewName4);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            Picasso.get().load(SharebleResources.imageUrl).transform(new CropCircleTransformation()).into(profile);
            contactName.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            phoneNumber.setText(SharebleResources.phoneNo);
        }

//        light.setOnClickListener(this);
//        dark.setOnClickListener(this);
        name.setOnClickListener(this);
        contacts.setOnClickListener(this);
        gift.setOnClickListener(this);
        share.setOnClickListener(this);
        rate.setOnClickListener(this);
        logInOrOut.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
//            case R.id.card_view1:
//                Log.d("light ","White");
//                break;
//            case R.id.card_view2:
//                Log.d("light ","dark");
//                break;
            case R.id.card_view3:
                Log.d("light ","name");
                break;
            case R.id.card_view4:
                Log.d("light ","contacts");
                break;
            case R.id.card_view5:
                Log.d("light ","gift");
                break;
            case R.id.card_view6:
                Log.d("light ","share");
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT,"Mihamala app https://play.google.com/store/apps/details?id=com.mihamala.mihamala");
                startActivity(Intent.createChooser(intent,"share with"));
                break;
            case R.id.card_view7:
                Log.d("light ","rate");
                Intent rate = new Intent(Intent.ACTION_VIEW);
                rate.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.mihamala.mihamala"));
                startActivity(rate);
                break;
            case R.id.card_view8:
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(),Login.class));
        }
    }
}