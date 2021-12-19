package com.example.ecommerce.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.BoutiqueViewHolder;
import com.example.ecommerce.models.Boutiques;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference BoutiqueRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_home);

        BoutiqueRef = FirebaseDatabase.getInstance().getReference().child("Companies");


        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Boutiques> options =
                new FirebaseRecyclerOptions.Builder<Boutiques>()
                        .setQuery(BoutiqueRef, Boutiques.class)
                        .build();


        FirebaseRecyclerAdapter<Boutiques, BoutiqueViewHolder> adapter =
                new FirebaseRecyclerAdapter<Boutiques, BoutiqueViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull BoutiqueViewHolder holder, int position, @NonNull Boutiques model)
                    {
                        Picasso.get().load(model.getBackground()).into(holder.boutique_image);
                        Picasso.get().load(model.getLogo()).into(holder.boutique_logo);
                    }

                    @NonNull
                    @Override
                    public BoutiqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_boutiques_layout, parent, false);
                        BoutiqueViewHolder holder = new BoutiqueViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
