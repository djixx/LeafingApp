package com.example.leafingapp.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leafingapp.Chat;
import com.example.leafingapp.R;
import com.example.leafingapp.model.UserModel;
import com.example.leafingapp.utils.FirebaseUtil;
import com.example.leafingapp.utils.Util;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecycler extends FirestoreRecyclerAdapter<UserModel, SearchUserRecycler.UserModelViewHolder> {


    Context context;

    public SearchUserRecycler(@NonNull FirestoreRecyclerOptions<UserModel> options,Context context) {
        super(options);
        this.context = context;
    }



    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {
        holder.usernameText.setText(model.getUsername());
        holder.phoneText.setText(model.getPhone());
        if(model.getUserId().equals(FirebaseUtil.currentUserId())){
            holder.usernameText.setText(model.getUsername()+" (Me)");
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Chat.class);
            Util.passUserModelAsIntent(intent,model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }
    /*metoda se poziva svaki put kada se treba postaviti sadržaj jednog ViewHolder-a s podacima iz određenog položaja u datasetu */
    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row,parent,false);
        return new UserModelViewHolder(view);

    }
/*. Kada se poziva onCreateViewHolder(), metoda kreira novu instancu UserModelViewHolder klase.
    Ova instanca ViewHolder-a će sadržavati referencu na sve View elemente unutar reda
    (npr. TextView za korisničko ime, TextView za broj telefona, ImageView za profilnu sliku),
    što omogućava efikasno upravljanje prikazom podataka za svaki redak u RecyclerView-u*/

    class UserModelViewHolder extends RecyclerView.ViewHolder{
        TextView usernameText;
        TextView phoneText;
        ImageView profilePic;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            phoneText = itemView.findViewById(R.id.phone_text);
            profilePic = itemView.findViewById(R.id.profile_picture);
        }
    }
}
