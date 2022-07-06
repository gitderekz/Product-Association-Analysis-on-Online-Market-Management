package com.example.maisha_supermarket_management;

import android.app.Dialog;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnCart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnCart extends Fragment {
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Car> options;
    FirebaseRecyclerAdapter<Car,ViewOncart> adapter;
    private String[] c = new String[0];
    boolean implement = false;

    Button btnDiscounts;
    Button backButton;
    TextView rules;
    EditText priceField;

    DatabaseReference Dataref,discountDataref,databaseReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> myCartKeyList = new ArrayList<>();
    private int cartLoopCounter = 1;
    private ArrayList<String> myCartItemsList = new ArrayList<>();
    private ArrayList<String> myCartUrlList = new ArrayList<>();
    private ArrayList<String> itemsList = new ArrayList<>();
    private ArrayList<String> idsList = new ArrayList<>();
    private ArrayList<Integer> returnedItemIdList = new ArrayList<>();
    private ArrayList<Integer> transaction_itemIds_biglist = new ArrayList<>();
    private ArrayList<String> transaction_items_biglist = new ArrayList<>();
    private ArrayList<String> transaction_urls_biglist = new ArrayList<>();

    private ArrayList<String> final_urls = new ArrayList<>();
    private ArrayList<String> final_items = new ArrayList<>();
    private ArrayList<Integer> final_ids = new ArrayList<>();



    private String[] u = new String[0];
    private String items_all = "",price;
    HashMap<ArrayList<Integer>, ArrayList<String>> IdItemPair = new HashMap<ArrayList<Integer>, ArrayList<String>>();
    int counting;

    Button generate_associations,generate_data_set;
    TextView frequent_items,confidence;

    public OnCart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnCart.
     */
    // TODO: Rename and change types and number of parameters
    public static OnCart newInstance(String param1, String param2) {
        OnCart fragment = new OnCart();
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
        View v =  inflater.inflate(R.layout.fragment_on_cart, container, false);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("cart");
        recyclerView= v.findViewById(R.id.OnCartRecyclerView);
        recyclerView.setHasFixedSize(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);

        /* Load items from cart child and store them in array*/
        databaseReference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String cartKey = snapshot.getKey();
                myCartKeyList.add(cartKey);
                c = myCartKeyList.toArray(new String[0]);
//                int limit = c.length;
//                Log.d("Limit ", String.valueOf(limit));
//                Log.d("loopCounter ", String.valueOf(cartLoopCounter));
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
        discountDataref= FirebaseDatabase.getInstance().getReference().child("home");

        generate_associations = v.findViewById(R.id.generate_association);
        generate_data_set = v.findViewById(R.id.generate_data_set);
        frequent_items = v.findViewById(R.id.frequent_items);
        confidence = v.findViewById(R.id.confidence);

        LoadData("");
        return v;
    }
    private void LoadData(String data) {
        Query query=Dataref.orderByChild("time").startAt(data).endAt(data+"\uf8ff");

        options=new FirebaseRecyclerOptions.Builder<Car>().setQuery(query,Car.class).build();
        adapter=new FirebaseRecyclerAdapter<Car, ViewOncart>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewOncart holder, final int position, @NonNull final Car model) {
                final String transaction_time = model.getTime();
                final ArrayList<String> transaction_items = model.getItems();
                final ArrayList<String> transaction_urls = model.getImageUrls();
                final ArrayList<Integer> transaction_itemIds = model.getItemsId();
                 
                for (int each = 0;each < transaction_itemIds.size();each++){
                    transaction_itemIds_biglist.add(transaction_itemIds.get(each));
                    transaction_items_biglist.add(transaction_items.get(each));
                    transaction_urls_biglist.add(transaction_urls.get(each));
                }

//                Set<Integer> set = new HashSet<>(transaction_itemIds_biglist);
//                transaction_itemIds_biglist.clear();
//                transaction_itemIds_biglist.addAll(set);
//                Set<String> set02 = new HashSet<>(transaction_items_biglist);
//                transaction_items_biglist.clear();
//                transaction_items_biglist.addAll(set02);

                holder.transaction_time.setText(transaction_time);
                holder.transaction_items.setText(String.valueOf(transaction_items).replace("[","").replace("]","").replace(",",""));
                holder.transaction_itemIds.setText(String.valueOf(transaction_itemIds).replace("[","").replace("]","").replace(",",""));

                itemsList.add(String.valueOf(transaction_items).replace("[","").replace("]","").replace(",",""));
//                idsList.add(transaction_itemIds.get(position));
                idsList.add(String.valueOf(transaction_itemIds).replace("[","").replace("]","").replace(",",""));



                generate_associations.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {

                        if(implement == false) {
                            String[] args = new String[0];
                            try {
                                new App(args);
                                int x = 0;
                                String associations_all = "", confidence_all = "",items_all = "";
                                ArrayList<String> associated_items = new ArrayList<>();//NOT USED*******
                                Object value;
                                String key = "";
                                for (String i : SharebleResources.ITEMS) {
                                    associations_all = associations_all + i + "\n";
                                    confidence_all = confidence_all + SharebleResources.CONFIDENCE.get(x) + "%\n";

//                                    Log.d("Items..",SharebleResources.ITEMS.get(x));
                                    key = key+(i.replace("(","").replace(")","").replace("=","").replace(">",","))+",";

//                                    value = IdItemPair.get(key);
//                                    Log.d("Map..", String.valueOf(value));


                                    x++;
                                }
                                SharebleResources.ITEMS.clear();
                                SharebleResources.CONFIDENCE.clear();

                                frequent_items.setText(associations_all);
                                confidence.setText(confidence_all);
                                Log.d("RulesItemIds..", String.valueOf(key));
                                String tempChar = "";
                                for(int s = 0;s < key.length();s++){
                                    if (String.valueOf(key.charAt(s)).equals(",")){
                                        returnedItemIdList.add(Integer.parseInt(tempChar));
                                        tempChar = "";
                                        Log.d("current", String.valueOf(returnedItemIdList.get(counting)));
                                        counting++;
                                    }else {
                                        tempChar = tempChar.concat(String.valueOf(key.charAt(s)));
                                    }

                                }
                                counting = 0;
                                for(int a:transaction_itemIds_biglist){
                                    Log.d("current id", String.valueOf(a));
                                    try{
                                        //reducing lists
                                        Set<Integer> set = new HashSet<>(returnedItemIdList);
                                        returnedItemIdList.clear();
                                        returnedItemIdList.addAll(set);
                                        /**/
                                        for (int aa:returnedItemIdList){
                                            if (aa == a){
                                                Log.d("ITEM FOUND",transaction_items_biglist.get(counting));
                                                Log.d("LINK FOUND",transaction_urls_biglist.get(counting));
                                                //creating final lists
                                                final_urls.add(transaction_urls_biglist.get(counting));
                                                final_items.add(transaction_items_biglist.get(counting));
                                                final_ids.add(transaction_itemIds_biglist.get(counting));
                                            }else{
                                            }
                                        }
                                    }catch (Exception e){
                                        Log.d("FAILED","ITEM NOT FOUND!");
                                    }
                                    counting++;
                                }

                                items_all = associations_all;
//                                items_all = items_all.replace("(","").replace(")","").replace("=","").replace(">",",");
                                frequent_items.setText(items_all);
//                                Log.d("ALL_IDS",items_all);
//                                for(int i=0;i<SharebleResources.ITEMS.size();i++){
//                                    Log.d("Items..",SharebleResources.ITEMS.get(i));
//                                }

                                //DISPLAY DIALOGUE
                                Dialog dialog = new Dialog(v.getContext());
                                dialog.setContentView(R.layout.association_rules_dialogue);
                                dialog.setTitle("CREATE OFFERS");
//                               dialog.setMessage("Write your message here.");
                                dialog.setCancelable(true);

                                btnDiscounts = dialog.findViewById(R.id.btnDiscount);
                                backButton = dialog.findViewById(R.id.btnBack);
                                rules = dialog.findViewById(R.id.discountDescription);
                                priceField = dialog.findViewById(R.id.discountPrice);

                                rules.setText(items_all);
                                price = String.valueOf(priceField.getText());

                                btnDiscounts.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //reducing lists
                                        Set<String> set = new HashSet<>(final_urls);
                                        final_urls.clear();
                                        final_urls.addAll(set);
                                        /**/
                                        HashMap hashMap = new HashMap();
                                        try {
                                            for (int discount = 1;discount < final_urls.size();discount++){
                                                hashMap.put("ImageUrl"+discount,final_urls.get(discount));
                                            }
                                        }catch (Exception e){}
                                        try {
                                            hashMap.put("ImageUrl", final_urls.get(0));
                                        }catch (Exception e){}

                                        hashMap.put("CarName","~Discount~");
                                        hashMap.put("ItemPrice",price);
                                        hashMap.put("ItemDescription","Buy all items for this lower price");
                                        hashMap.put("time_stamp", Timestamp.now().toDate().toString());

                                        discountDataref.child(Timestamp.now().toDate().toString()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
//                                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                                returnedItemIdList.clear();
                                                itemsList.clear();
                                                idsList.clear();
                                                transaction_itemIds_biglist.clear();
                                                transaction_items_biglist.clear();
                                                transaction_urls_biglist.clear();
                                                final_ids.clear();
                                                final_items.clear();
                                                final_urls.clear();

                                                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(OnCart.this.getId(), new OnCart()).commit();
                                                dialog.dismiss();
                                                Toast.makeText(getContext(), "Discount Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                dialog.show();
                                backButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        returnedItemIdList.clear();
                                        itemsList.clear();
                                        idsList.clear();
                                        transaction_itemIds_biglist.clear();
                                        transaction_items_biglist.clear();
                                        transaction_urls_biglist.clear();
                                        final_ids.clear();
                                        final_items.clear();
                                        final_urls.clear();
                                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(OnCart.this.getId(), new OnCart()).commit();
                                        dialog.dismiss();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            implement = true;
//                            generate_associations.setText("IMPLEMENT");

                        }else{
                            //implement promotion,discount and suggestions

                            frequent_items.setText("ASSOCIATIONS");
                            confidence.setText("CONFIDENCE");

                            implement = false;
                            generate_associations.setText("GENERATE ASSOCIATIONS");

                            items_all = items_all.replace("(","").replace(")","").replace("=","").replace(">",",");
                            frequent_items.setText(items_all);
                            Log.d("ALL_ITEMS",items_all);
                        }
//                        returnedItemIdList.clear();
//                        itemsList.clear();
//                        idsList.clear();
//                        transaction_itemIds_biglist.clear();
//                        transaction_items_biglist.clear();
//                        transaction_urls_biglist.clear();
//                        final_ids.clear();
//                        final_items.clear();
//                        final_urls.clear();
                    }
                });
            }
            @NonNull
            @Override
            public ViewOncart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cart_item,parent,false);
                return new ViewOncart(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

        generate_data_set.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
                Date now = new Date();
                String fileName = formatter.format(now) + ".dat";//like 2016_01_12.txt
                String items_fileName = formatter.format(now) + "items.dat";//like 2016_01_12.txt

                try
                {
                    File root = new File(Environment.getExternalStorageDirectory()+File.separator+"MaishaSupermarket", "Dataset");
                    //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
                    if (!root.exists()) {
                        Log.d("PATH: ",root.getAbsolutePath().toString());
                        if(root.mkdirs()){
                            // do something
                            File gpxfile = new File(root, fileName);
                            File items_gpxfile = new File(root, items_fileName);

                            FileWriter writer = new FileWriter(gpxfile,true);
                            FileWriter items_writer = new FileWriter(items_gpxfile,true);
                            for (int x = 0; x < idsList.size(); x++){
                                writer.append(idsList.get(x) +"\n");
                                items_writer.append(itemsList.get(x) +"\n");
                            }
                            writer.flush();
                            items_writer.flush();
                            writer.close();
                            items_writer.close();
                            Toast.makeText(getContext(), "Data has been written to Dataset", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "Failed 02", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        // do something
                        File gpxfile = new File(root, fileName);
                        File items_gpxfile = new File(root, items_fileName);

                        FileWriter writer = new FileWriter(gpxfile,true);
                        FileWriter items_writer = new FileWriter(items_gpxfile,true);
                        for (int x = 0; x < idsList.size(); x++){
                            writer.append(idsList.get(x) +"\n");
                            items_writer.append(itemsList.get(x) +"\n");
                        }
                        writer.flush();
                        items_writer.flush();
                        writer.close();
                        items_writer.close();
                        Toast.makeText(getContext(), "Data has been written to Dataset", Toast.LENGTH_SHORT).show();
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
//
//        generate_associations.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onClick(View v) {
//                if(implement == false) {
//                    String[] args = new String[0];
//                    try {
//                        new App(args);
//                        int x = 0;
//                        String associations_all = "", confidence_all = "";
//                        for (String i : SharebleResources.ITEMS) {
//                            associations_all = associations_all + SharebleResources.ITEMS.get(x) + "\n";
//                            confidence_all = confidence_all + SharebleResources.CONFIDENCE.get(x) + "%\n";
//                            x++;
//                        }
//                        SharebleResources.ITEMS.clear();
//                        SharebleResources.CONFIDENCE.clear();
//                        frequent_items.setText(associations_all);
//                        confidence.setText(confidence_all);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    implement = true;
//                    generate_associations.setText("IMPLEMENT");
//                }else{
//                    //implement promotion,discount and suggestions
//
//                    frequent_items.setText("ASSOCIATIONS");
//                    confidence.setText("CONFIDENCE");
//
//                    implement = false;
//                    generate_associations.setText("GENERATE ASSOCIATIONS");
//
//                    holder.transaction_items.setText(String.valueOf(transaction_items).replace("[","").replace("]","").replace(",",""));
//
//                }
//            }
//        });
    }
}
