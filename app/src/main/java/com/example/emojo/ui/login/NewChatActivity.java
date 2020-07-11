package com.example.emojo.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emojo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewChatActivity extends AppCompatActivity {
List<UserListObject> allUsers = new ArrayList<>();
List<String> stickers = new ArrayList();
ArrayAdapter<String> userArrayAdapter;
ArrayAdapter<String> messageArrayAdapter;
String selectedSticker;
String selectedUser;
Button sendChatButton;



FirebaseUser currentUser;
String sender;
private Spinner userDropdown;

private Spinner messageDropdown;

ProgressBar loadingProgressBar;


private final FirebaseFirestore db = FirebaseFirestore.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_chat);
        String[] rawStickers = new String[] {"😄", "😁", "😂", "😎", "🤩", "🤯", "😡"};
        Collections.addAll(stickers, rawStickers);

        sender = getIntent().getStringExtra("sender");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        userDropdown = findViewById(R.id.username_dropdown);
        userDropdown.setGravity(Gravity.CENTER);

        messageDropdown = findViewById(R.id.message_dropdown);




        userArrayAdapter = new ArrayAdapter(NewChatActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                allUsers);

        messageArrayAdapter = new ArrayAdapter(NewChatActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                stickers);

        userDropdown.setAdapter(userArrayAdapter);

        userDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  UserListObject userObj = (UserListObject)userDropdown.getItemAtPosition(position);
                  selectedUser = userObj.toString();
                  TextView selectedStickerView = findViewById(R.id.selected_user_view);
                  selectedStickerView.setText(String.format("Sending to %s", selectedUser));
              }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                UserListObject userObj = (UserListObject)userDropdown.getItemAtPosition(0);
//                selectedUser = userObj.toString();
//                TextView selectedStickerView = findViewById(R.id.selected_user_view);
//                selectedStickerView.setText(String.format("Sending to %s", selectedUser));
            }
        });


        messageDropdown.setAdapter(messageArrayAdapter);

        messageDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSticker = (String)messageDropdown.getItemAtPosition(position);
                TextView selectedStickerView =  findViewById(R.id.selected_sticker_view);
                selectedStickerView.setText(selectedSticker);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                selectedSticker = (String)messageDropdown.getItemAtPosition(0);
//                TextView selectedStickerView =  findViewById(R.id.selected_sticker_view);
//                selectedStickerView.setText(selectedSticker);
            }
        });

        loadingProgressBar = findViewById(R.id.progress_circular);

        sendChatButton = findViewById(R.id.send_chat_button);
        sendChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        getAllUsers();

    }


    private List<Object> getAllUsers() {
        Task<QuerySnapshot> q = db.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot docs = task.getResult();
                            if (!docs.isEmpty()){
                                allUsers.clear();
                                for(QueryDocumentSnapshot doc : docs) {
                                    allUsers.add(new UserListObject(doc.getData()));
                                }
                                userArrayAdapter.notifyDataSetChanged();

                            }

                        }
                    }
                });
        return null;

    }

    private void sendMessage() {
        if(selectedUser != null  && selectedSticker != null) {
            loadingProgressBar.setVisibility(View.VISIBLE);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime currentTime = LocalDateTime.now();
            Map<String, Object> messageMap = new HashMap<>();

            messageMap.put("sender", currentUser.getEmail());
            messageMap.put("recipient", selectedUser);
            messageMap.put("message", selectedSticker);
            messageMap.put("time", timeFormatter.format(currentTime));


            db.collection("chats").document().set(messageMap).addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            loadingProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Message Sent!", Toast.LENGTH_SHORT).show();

                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Error sending! Please try again.", Toast.LENGTH_SHORT).show();

                }
            });
        }

        else {
            Toast.makeText(getApplicationContext(), "Please select a user and sticker from the dropdowns", Toast.LENGTH_SHORT).show();
        }

    }



    public static class UserListObject {
        String username;
        String email;

        public UserListObject(Map<String, Object> user) {
            this.username = (String)user.get("username");
            this.email = (String)user.get("email");
        }

        @Override
        public String toString() {
            return this.username != null ? this.username : String.format("%s is unable to receive messages", this.email);
        }

    }




}