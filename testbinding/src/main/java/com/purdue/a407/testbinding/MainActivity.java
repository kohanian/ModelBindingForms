package com.purdue.a407.testbinding;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import com.kyleohanian.databinding.modelbindingforms.Listeners.OnBindDialogCancelListener;
import com.kyleohanian.databinding.modelbindingforms.Listeners.OnBindDialogCreateListener;
import com.kyleohanian.databinding.modelbindingforms.UIObjects.ModelForm;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button create;

    ArrayList<User> users = new ArrayList<>();


    UserAdapter userAdapter;
    Context context;

    public static final int CREATE_USER_ACTIVITY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        create = findViewById(R.id.create);
        context = this;
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final ModelForm binding = new ModelForm(context, User.class);
                builder.setView(binding.setUpCreate());
                final AlertDialog dialog = builder.create();
                dialog.show();
                binding.setOnBindDialogCreateListener(new OnBindDialogCreateListener() {
                    @Override
                    public void onCreate(Object obj, View view) {
                        User user = (User) obj;
                        users.add(user);
                        userAdapter.addItems(users);
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
        userAdapter = new UserAdapter(this, new ArrayList<User>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userAdapter);


    }
}

