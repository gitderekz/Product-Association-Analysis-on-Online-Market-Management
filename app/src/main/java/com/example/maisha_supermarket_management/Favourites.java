package com.example.maisha_supermarket_management;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Favourites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favourites extends Fragment {
    boolean checkCart = false;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Car> options;
    FirebaseRecyclerAdapter<Car,MyViewHolder> adapter;
    DatabaseReference Dataref;
    private DatabaseReference databaseReference;
    private String[] c = new String[0];
    private ArrayList<String> myCartKeyList = new ArrayList<>();
    private ArrayList<String> dateList = new ArrayList<>();
    private ArrayList<String> itemsList = new ArrayList<>();
    private ArrayList<String> amountList = new ArrayList<>();
    private int cartLoopCounter = 1;
    private int total_amount = 0;
    private int howManyFound = 0;
    private ArrayList<String> myCartItemsList = new ArrayList<>();
    private ArrayList<String> myCartUrlList = new ArrayList<>();
    private String[] u = new String[0];

    ImageButton cart;
    Button generate_report;
    private TextView totalAmount;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Favourites() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Favourites.
     */
    // TODO: Rename and change types and number of parameters
    public static Favourites newInstance(String param1, String param2) {
        Favourites fragment = new Favourites();
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
        View v =  inflater.inflate(R.layout.fragment_favourites, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("cart");
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
                            u = myCartUrlList.toArray(new String[0]);
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

        Dataref= FirebaseDatabase.getInstance().getReference().child("purchases");
        recyclerView= v.findViewById(R.id.favouritesRecyclerView);
        recyclerView.setHasFixedSize(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);

        cart = v.findViewById(R.id.cartButton);
        generate_report = v.findViewById(R.id.generate_report);
        totalAmount = v.findViewById(R.id.totalAmount);


        LoadData("");
        return v;
    }
    private void LoadData(String data) {
        Query query=Dataref.orderByChild("amount").startAt(data).endAt(data+"\uf8ff");

        options=new FirebaseRecyclerOptions.Builder<Car>().setQuery(query,Car.class).build();
        adapter=new FirebaseRecyclerAdapter<Car, MyViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final MyViewHolder holder, final int position, @NonNull Car model) {

                final String transaction_time = model.getTime();
                final ArrayList<String> transaction_items = model.getItems();
                final String transaction_amount = model.getAmount();

                holder.transaction_time.setText(transaction_time);
                holder.transaction_items.setText(String.valueOf(transaction_items).replace("[","").replace("]","").replace(",",""));
                holder.transaction_amount.setText(transaction_amount);

                dateList.add(transaction_time);
                itemsList.add(String.valueOf(transaction_items));
                amountList.add(transaction_amount);

                total_amount = total_amount + Integer.parseInt(transaction_amount);
                totalAmount.setText("TSH "+String.valueOf(total_amount)+"/=");


                /*Checking if data loaded are present in cart*/
//                holder.v.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent=new Intent(getActivity(),ViewActivity.class);
//                        intent.putExtra("CarKey",getRef(position).getKey());
//                        intent.putExtra("category","earphones");
//                        startActivity(intent);
//                    }
//                });

            }
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.singe_view_favorites,parent,false);
                return new MyViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);


        generate_report.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
                Date now = new Date();
                String fileName = formatter.format(now) + ".txt";//like 2016_01_12.txt

                try
                {
                    File root = new File(Environment.getExternalStorageDirectory()+File.separator+"MaishaSupermarket", "Report");
                    //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
                    if (!root.exists()) {
                        Log.d("PATH: ",root.getAbsolutePath().toString());
                        if(root.mkdirs()){
                            // do something
                            File gpxfile = new File(root, fileName);

                            FileWriter writer = new FileWriter(gpxfile,true);
                            for (int x = 0; x < dateList.size(); x++){
                                writer.append(dateList.get(x)+ " " + itemsList.get(x)+ " " + amountList.get(x) +"\n");
                            }
                            writer.flush();
                            writer.close();
                            Toast.makeText(getContext(), "Data has been written to Dataset", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "Failed 02", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Failed 01", Toast.LENGTH_SHORT).show();
                    }
//                    if (!root.exists())
//                    {
//                        root.mkdirs();
//                    }
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed to write to Dataset", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}