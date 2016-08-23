package com.ga.roosevelt.firebase_lab;

import android.app.Dialog;
import android.os.*;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    String userId;
    String userColor;

    EditText edtMessage;
    Button btnSend;
    ListView lstView;

    DatabaseReference dbRef, messagesRef;
    FirebaseListAdapter<Message> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();

        dbRef = FirebaseDatabase.getInstance().getReference();
        messagesRef = dbRef.child("messages");

        mAdapter = new FirebaseListAdapter<Message>(this,
                Message.class,
                android.R.layout.simple_expandable_list_item_2,
                messagesRef) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView txtUser = (TextView) v.findViewById(android.R.id.text1);
                TextView txtMessage = (TextView) v.findViewById(android.R.id.text2);

                txtUser.setText(model.getUsername());
                txtMessage.setText(model.getMessage());

                String color = model.getColor();
                if(color != null){
                    switch(color){
                        case "green":
                            txtUser.setTextColor(ContextCompat
                                    .getColor(MainActivity.this, R.color.colorGreen));
                            break;
                        case "yellow":
                            txtUser.setTextColor(ContextCompat
                                    .getColor(MainActivity.this, R.color.colorYellow));
                            break;
                        case "red":
                            txtUser.setTextColor(ContextCompat
                                    .getColor(MainActivity.this, R.color.colorRed));
                            break;
                    }
                }
            }
        };
        lstView.setAdapter(mAdapter);

        changeToolbarTitle();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch(menuId){
            case R.id.changeProfile:
                editProfile();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeToolbarTitle(){
        getSupportActionBar().setTitle(userId);
    }

    private void initializeViews(){
        userId = getRandomUserName();
        userColor = null;

        edtMessage = (EditText) findViewById(R.id.edtMessage);
        btnSend = (Button) findViewById(R.id.btnSend);
        lstView = (ListView) findViewById(R.id.lstView);

        btnSend.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSend:
                String message = edtMessage.getText().toString().trim();
                Message messageObject = new Message(userId, message, userColor);
                messagesRef.push().setValue(messageObject);
                mAdapter.notifyDataSetChanged();
                edtMessage.setText("");
                break;
        }
    }

    private String getRandomUserName(){
        Random rand = new SecureRandom();
        int postfix = (rand.nextInt() % 10000) + 1;
        return "User" + String.format(Locale.ENGLISH, "%04d", postfix);
    }

    private void editProfile(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_profile);
        dialog.setTitle("Edit Profile");

        // set the custom dialog components - text, image and button
        final EditText edtUsername = (EditText) dialog.findViewById(R.id.edtNewUsername);

        Button dialogButton = (Button) dialog.findViewById(R.id.btnSave);

        final RadioGroup rdoColors = (RadioGroup) dialog.findViewById(R.id.colorPick);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO save userId;
                String newName = edtUsername.getText().toString().trim();
                if(!newName.equals(""))
                    userId = newName;
                changeToolbarTitle();
                switch(rdoColors.getCheckedRadioButtonId()){
                    case R.id.rdoGreen:
                        userColor = "green";
                        break;
                    case R.id.rdoYellow:
                        userColor = "yellow";
                        break;
                    case R.id.rdoRed:
                        userColor = "red";
                        break;
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
