package com.purdue.a407.testbinding;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import com.kyleohanian.databinding.modelbindingforms.Listeners.OnBindDialogCancelListener;
import com.kyleohanian.databinding.modelbindingforms.Listeners.OnBindDialogUpdateListener;
import com.kyleohanian.databinding.modelbindingforms.UIObjects.ModelForm;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<User> items;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView firstName;
        TextView lastName;
        TextView age;

        public MyViewHolder(View view) {
            super(view);
            firstName = view.findViewById(R.id.firstName);
            lastName = view.findViewById(R.id.lastName);
            age = view.findViewById(R.id.age);
        }
    }


    public UserAdapter(Context context, ArrayList<User> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_user, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final User item = items.get(position);
        holder.firstName.setText("First Name: " + item.getFirstName());
        holder.lastName.setText("Last Name: " +item.getLastName());
        holder.age.setText("Age: "+String.valueOf(item.getAge()));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(List<User> users) {
        items.clear();
        items.addAll(users);
        notifyDataSetChanged();
    }
}

