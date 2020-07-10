package com.example.emojo.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emojo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewChatActivity extends AppCompatActivity {
List<UserListObject> allUsers = new ArrayList<>();
String[] rawStickers = new String[] {"😄", "😁", "😂", "😎", "🤩", "🤯", "😡"};
List<String> stickers = new ArrayList();
ArrayAdapter<String> userArrayAdapter;

FirebaseUser currentUser;
String sender;
private Spinner userDropdown;

private final FirebaseFirestore db = FirebaseFirestore.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_chat);
        for(String s : rawStickers) {
            stickers.add(s);
        }

        sender = getIntent().getStringExtra("sender");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userDropdown = findViewById(R.id.username_dropdown);



        userArrayAdapter = new ArrayAdapter(NewChatActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                allUsers);

        userDropdown.setAdapter(userArrayAdapter);

        getAllUsers();

    }


    List<Object> getAllUsers() {
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
                                    Log.v("!QFQF", doc.getData().toString());
                                }
                                userArrayAdapter.notifyDataSetChanged();
                                Log.v("!!!!!?", allUsers.toString());
                                allUsers.stream().map(i -> Log.v("!!", i.toString()));

                            }

                        }
                    }
                });
        return null;

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
            return this.username;
        }

    }


}