package xyz.kyleohanian.modelformsbinding;

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
import xyz.kyleohanian.modelformsbinding.Listeners.OnBindDialogCancelListener;
import xyz.kyleohanian.modelformsbinding.Listeners.OnBindDialogUpdateListener;
import xyz.kyleohanian.modelformsbinding.UIObjects.ModelForm;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<User> items;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView firstName;
        TextView lastName;
        TextView age;
        TextView rating;

        public MyViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.cv);
            firstName = view.findViewById(R.id.firstName);
            lastName = view.findViewById(R.id.lastName);
            age = view.findViewById(R.id.age);
            rating = view.findViewById(R.id.rating);
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
        holder.rating.setText("Rating: "+String.valueOf(item.getRating()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final ModelForm binding = new ModelForm(context, User.class);
                builder.setView(binding.setUpUpdate(item));
                final AlertDialog dialog = builder.create();
                dialog.show();
                binding.setOnBindDialogUpdateListener(new OnBindDialogUpdateListener() {
                    @Override
                    public void onUpdate(Object obj, View view) {
                        User user = (User) obj;
                        items.set(position, user);
                        notifyItemChanged(position);
                        dialog.dismiss();
                    }
                });
                binding.setOnBindDialogCancelListener(new OnBindDialogCancelListener() {
                    @Override
                    public void onCancel(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

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

