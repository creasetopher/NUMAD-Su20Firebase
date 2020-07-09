package com.example.emojo.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.emojo.R;
import com.example.emojo.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoggedInActivity extends AppCompatActivity {
    ArrayAdapter<String> listAdapter;
    ListView listView;
    Button logButton;
    List<ChatListObject> chats = new ArrayList();
    List<String> r = new ArrayList();

    FirebaseUser currentUser;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_logged_in);


//        chats.add("chrisljones1@gmail.com");
//        r.add("chrisljones1@gmail.com");
//        r.add("?");


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        Log.v("!!!", currentUser.getEmail());


        listAdapter = new ArrayAdapter(
                LoggedInActivity.this,
                android.R.layout.simple_list_item_1,
                chats);

        listView = findViewById(R.id.listViewForUsers);

        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User element = (User) listView.getItemAtPosition(position);

            }

        });

        logButton = findViewById(R.id.chat_button);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAdapter.notifyDataSetChanged();
//                getChats(currentUser.getEmail());
                for(ChatListObject o : chats) {
                    Log.v("chats", o.toString());
                }
            }
        });

        getChats(currentUser.getEmail());

    }


    private class ChatListObject {
        String sender;
        String message;

        public ChatListObject(Map<String, Object> chat) {
            this.sender = (String)chat.get("sender");
            this.message = (String)chat.get("message");
        }

        @Override
        public String toString() {
            return this.sender;
        }

        public String getMessage() {
            return this.message;
        }

    }



    private void getChats(String userEmail) {
       Query q = db.collection("chats").whereEqualTo("recipient", userEmail);
       q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                         @Override
                                         public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                             if (task.isSuccessful()) {
                                                 for (QueryDocumentSnapshot document : task.getResult()) {

                                                     ChatListObject chat = new ChatListObject(document.getData());
                                                     chats.add(chat);
//                                                     Log.d("query", document.getId() + " => " + document.getData());
                                                 }
                                             } else {
                                                 new AlertDialog.Builder(LoggedInActivity.this).setMessage("Error Fetching Chats! :(").show();
                                             }
                                             listAdapter.notifyDataSetChanged();
                                         }
                                     }


       );

    }
}