package com.example.maisha_supermarket_management;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Grain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Grain extends Fragment {
    ImageView imageView;
    TextView textView;
    TextView loadedDescription;
    TextView loadedPrice;
    ImageButton btnDelete;
    Button backButton;
    String categories = "grain";
    StorageReference storageReference,StorageRef;
    DatabaseReference Dataref,DataRef;

    View v;
    boolean checkCart = false;
    boolean checkFavourites = false;

    ArrayList<String> myCartKeyList = new ArrayList<>();
    ArrayList<String> myCartItemsList = new ArrayList<>();
    ArrayList<String> myCartUrlList = new ArrayList<>();
    ArrayList<String> myFavouritesKeyList = new ArrayList<>();
    ArrayList<String> myFavouritesItemsList = new ArrayList<>();
    ArrayList<String> myFavouritesUrlList = new ArrayList<>();
    String[] c = new String[0];
    String[] f = new String[0];
    ArrayList<String> items = new ArrayList<>();

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Car> options;
    FirebaseRecyclerAdapter<Car,MyViewHolder> adapter;
    DatabaseReference databaseReference;
    DatabaseReference likeReference;

    int cartLoopCounter = 1;
    int favouritesLoopCounter = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Grain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tv.
     */
    // TODO: Rename and change types and number of parameters
    public static Grain newInstance(String param1, String param2) {
        Grain fragment = new Grain();
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
        v =  inflater.inflate(R.layout.fragment_tv, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("cart");
        likeReference = FirebaseDatabase.getInstance().getReference().child("favourites");

        /* Load items from cart child and store them in array*/
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String cartKey = snapshot.getKey();
                myCartKeyList.add(cartKey);
                c = myCartKeyList.toArray(new String[0]);
                int limit = c.length;
                Log.d("Limit ", String.valueOf(limit));
                Log.d("loopCounter ", String.valueOf(cartLoopCounter));

                databaseReference.child(c[cartLoopCounter-1]).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            String carName=dataSnapshot.child("CarName").getValue().toString();
                            String ImageUrl=dataSnapshot.child("ImageUrl").getValue().toString();
                            myCartItemsList.add(carName);
                            myCartUrlList.add(ImageUrl);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                cartLoopCounter++;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*Finish Load items from cart child*/

        /* Load items from favourites child and store them in array*/
        likeReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String cartKey = snapshot.getKey();
                myFavouritesKeyList.add(cartKey);
                f = myFavouritesKeyList.toArray(new String[0]);
                int limit = f.length;
                Log.d("Limit ", String.valueOf(limit));
                Log.d("loopCounter ", String.valueOf(favouritesLoopCounter));

                likeReference.child(f[favouritesLoopCounter-1]).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            String carName=dataSnapshot.child("CarName").getValue().toString();
                            String ImageUrl=dataSnapshot.child("ImageUrl").getValue().toString();
                            myFavouritesItemsList.add(carName);
                            myFavouritesUrlList.add(ImageUrl);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                favouritesLoopCounter++;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*Finish Load items from favourites child*/


        Dataref= FirebaseDatabase.getInstance().getReference().child("grain");
        recyclerView= v.findViewById(R.id.TvRecyclerView);
        recyclerView.setHasFixedSize(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);


        LoadData("");
        return v;
    }
    private void LoadData(String data) {
        Query query=Dataref.orderByChild("CarName").startAt(data).endAt(data+"\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<Car>().setQuery(query,Car.class).build();

        adapter = new FirebaseRecyclerAdapter<Car, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyViewHolder holder, final int position, @NonNull Car model) {

                String carName = model.getCarName();
                String loadPrice = model.getItemPrice();
                String loadDescription = model.getItemDescription();
                String ImageUrl = model.getImageUrl();
                String itemsCount = String.valueOf(model.getItemCount());
                String itemsRemain = itemsCount+"\nRemain";

                final String cartName = model.getCarName();
                final String cartUrl = model.getImageUrl();
                final String ItemsPrice = model.getItemPrice();
                String ItemPriceSet = "Tsh "+ItemsPrice;
                final String ItemsDescription = model.getItemDescription();
                items.add(cartName);

                holder.textView.setText(model.getCarName());
                holder.price.setText(ItemPriceSet);
                holder.count.setText(itemsRemain);
                holder.id.setText(String.valueOf(model.getItemId()));
                holder.imageButton.setId(View.generateViewId());
                holder.likeButton.setId(View.generateViewId());
                Picasso.get().load(model.getImageUrl()).into(holder.imageView);


                Log.d("name ",cartName);
                Log.d("url ",cartUrl);
                Log.d("ItemsPrice ",ItemsPrice);

                /*Checking if data loaded are present in cart*/
                for (String cartItemsUrl: myCartUrlList)
                {
                    if (cartUrl.equals(cartItemsUrl))
                    {
                        checkCart = true;
                        holder.imageButton.setImageResource(R.drawable.ic_baseline_shopping_redcart_24);
                        holder.imageButton.setBackground(getResources().getDrawable(R.drawable.cart_background_red));
                    }
                }
                /*Checking if data loaded are present in favourites*/
                for (String favouritesItemsUrl: myFavouritesUrlList)
                {
                    if (cartUrl.equals(favouritesItemsUrl))
                    {
                        checkFavourites = true;
                        holder.likeButton.setImageResource(R.drawable.ic_baseline_favorite_like_24);
                        holder.likeButton.setBackground(getResources().getDrawable(R.drawable.cart_background_red));
                    }
                }
                /*Handling cart button click events*/
                holder.imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int positionToRemoveValue = -1;
                        int cartLoopCounterOnClick = 0;
                        int howManyFound = 0;
                        if (checkCart)
                        {
                            for (String cartItemsUrl: myCartUrlList)
                            {
                                if (cartUrl.equals(cartItemsUrl))
                                {
                                    checkCart = true;
                                    howManyFound++;
                                    positionToRemoveValue = cartLoopCounterOnClick;
                                }
                                else{
                                    if (howManyFound <= 0)
                                    {
                                        checkCart = false;
                                    }
                                }
                                cartLoopCounterOnClick++;
                            }
//                            checkCart = true;
                        }
                        else{
                            checkCart = false;
                        }
                        customizeCartButton(howManyFound,positionToRemoveValue,cartName,cartUrl,ItemsPrice,holder,checkCart,position);
                    }
                });

                /*Handling favourites button click events*/
                holder.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int positionToRemoveValue = -1;
                        int favouriteLoopCounterOnClick = 0;
                        int howManyFound = 0;
                        if (checkFavourites)
                        {
                            for (String favouritesItemsUrl: myFavouritesUrlList)
                            {
                                if (cartUrl.equals(favouritesItemsUrl))
                                {
                                    checkFavourites = true;
                                    howManyFound++;
                                    positionToRemoveValue = favouriteLoopCounterOnClick;
                                }
                                else{
                                    if (howManyFound <= 0)
                                    {
                                        checkFavourites = false;
                                    }
                                }
                                favouriteLoopCounterOnClick++;
                            }
//                            checkFavourites = true;
                        }
                        else{
                            checkFavourites = false;
                        }
                        Log.d("checkFavourites", String.valueOf(checkFavourites));
                        customizeFavouriteButton(howManyFound,positionToRemoveValue,cartName,cartUrl,ItemsPrice,holder,checkFavourites,position);
                    }
                });

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String CarKey = getRef(position).getKey();
                        DataRef=FirebaseDatabase.getInstance().getReference().child(categories).child(CarKey);
                        StorageRef= FirebaseStorage.getInstance().getReference().child("CarImage").child(CarKey+".jpg");

                        Dialog dialog = new Dialog(v.getContext());
                        dialog.setContentView(R.layout.dialog_item);
                        dialog.setTitle(cartName);
//                        dialog.setMessage("Write your message here.");
                        dialog.setCancelable(true);

                        imageView=dialog.findViewById(R.id.image_single_view_dialog);
                        textView=dialog.findViewById(R.id.textView_single_view_dialog);
                        loadedPrice=dialog.findViewById(R.id.loadedPrice);
                        loadedDescription=dialog.findViewById(R.id.loadedDescription);
                        btnDelete=dialog.findViewById(R.id.btnDelete);
                        backButton=dialog.findViewById(R.id.backButton);

                        Picasso.get().load(ImageUrl).into(imageView);
                        textView.setText(carName);
                        loadedPrice.setText(loadPrice);
                        loadedDescription.setText(loadDescription);

                        btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int positionToRemoveValue = -1;
                                int cartLoopCounterOnClick = 0;
                                int howManyFound = 0;
                                if (checkCart)
                                {
                                    for (String cartItemsUrl: myCartUrlList)
                                    {
                                        if (cartUrl.equals(cartItemsUrl))
                                        {
                                            checkCart = true;
                                            howManyFound++;
                                            positionToRemoveValue = cartLoopCounterOnClick;
                                        }
                                        else{
                                            if (howManyFound <= 0)
                                            {
                                                checkCart = false;
                                            }
                                        }
                                        cartLoopCounterOnClick++;
                                    }
//                            checkCart = true;
                                }
                                else{
                                    checkCart = false;
                                }
                                customizeCartButton(howManyFound,positionToRemoveValue,cartName,cartUrl,ItemsPrice,holder,checkCart,position);
                            }
                        });
//                        btnDelete.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                DataRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        StorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                Toast.makeText(getContext(),"One item deleted",Toast.LENGTH_LONG).show();
////                                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//                                            }
//                                        });
//                                    }
//                                });
//                            }
//                        });

                        dialog.show();
                        backButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

//                        Intent intent=new Intent(getActivity(),ViewActivity.class);
//                        intent.putExtra("CarKey",getRef(position).getKey());
//                        intent.putExtra("category","juice");
//                        startActivity(intent);
                    }
                });


            }
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                Log.d("finish"," loading");
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view,parent,false);
                return new MyViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
    private void customizeCartButton(int howManyFound,int positionToRemoveValue,String cartName,String cartUrl,String ItemsPrice,final MyViewHolder holder,boolean cart,int position)
    {
//        if (howManyFound > 1)
        if (cart)
        {
            //Toast.makeText(MainActivity.this, "Data Successfully Uploaded!", Toast.LENGTH_SHORT).show();
            databaseReference.child(c[positionToRemoveValue]).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    checkCart = false;
                    holder.imageButton.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
                    holder.imageButton.setBackground(getResources().getDrawable(R.drawable.cart_background));
                }
            });
        }
        else {
            final  String key=databaseReference.push().getKey();
            HashMap hashMap=new HashMap();
            hashMap.put("CarName",cartName);
            hashMap.put("ImageUrl",cartUrl);
            hashMap.put("ItemPrice",ItemsPrice);
//            hashMap.put("ItemKey",c[position]);

            databaseReference.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    checkCart = true;
                    holder.imageButton.setImageResource(R.drawable.ic_baseline_shopping_redcart_24);
                    holder.imageButton.setBackground(getResources().getDrawable(R.drawable.cart_background_red));
                    //Toast.makeText(MainActivity.this, "Data Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void customizeFavouriteButton(int howManyFound, int positionToRemoveValue, String cartName, String cartUrl,String ItemsPrice, final MyViewHolder holder, boolean favourites,int position)
    {
//        if (howManyFound > 1)
        if (favourites)
        {
            //Toast.makeText(MainActivity.this, "Data Successfully Uploaded!", Toast.LENGTH_SHORT).show();
            likeReference.child(f[positionToRemoveValue]).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    checkFavourites = false;
                    holder.likeButton.setImageResource(R.drawable.ic_baseline_favorite_border_before_24);
                    holder.likeButton.setBackground(getResources().getDrawable(R.drawable.cart_background));
                    Log.d("checkFavourites2", String.valueOf(checkFavourites));
                }
            });
        }
        else {
            /*Handling favourites button click events*/
            final  String key=likeReference.push().getKey();
            HashMap hashMap=new HashMap();
            hashMap.put("CarName",cartName);
            hashMap.put("ImageUrl",cartUrl);
            hashMap.put("ItemPrice",ItemsPrice);
//            hashMap.put("ItemKey",f[position]);

            likeReference.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    checkFavourites = true;
                    holder.likeButton.setImageResource(R.drawable.ic_baseline_favorite_like_24);
                    holder.likeButton.setBackground(getResources().getDrawable(R.drawable.cart_background_red));
                    //Toast.makeText(MainActivity.this, "Data Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    void refresh()
    {
        Fragment frg = getActivity().getSupportFragmentManager().findFragmentByTag("MobilePhones");
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Log.d("Fragment ",frg.getTag());
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        v = null;
    }
}