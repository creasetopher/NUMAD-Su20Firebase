package com.example.emojo.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.List;

public class ChatThreadActivity extends AppCompatActivity {

ListView chatThreadList;
ArrayAdapter<String> listAdapter;
List<String> allChats = new ArrayList();
FirebaseUser currentUser;
private final FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String sender = getIntent().getStringExtra("sender");
        ArrayList<String> messages = getIntent().getStringArrayListExtra("messages");
        ArrayList<String> timestamps = getIntent().getStringArrayListExtra("timestamps");

        setContentView(R.layout.fragment_chat_thread);

        for (int i = 0; i < messages.size(); i++) {
            String m = String.format("%s          |         %s", messages.get(i), timestamps.get(i));
            allChats.add(m);
        }

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        chatThreadList = findViewById(R.id.chat_thread_list);


        listAdapter = new ArrayAdapter(
                ChatThreadActivity.this,
                android.R.layout.simple_list_item_1,
                allChats);

        chatThreadList.setAdapter(listAdapter);

//        allChats.addAll(messages);



//        getChats(currentUser.getEmail());

        TextView chatCount = findViewById(R.id.chat_count_text);
        chatCount.setText(String.format("Stickers received: %d", messages.size()));

    }



    private void getChats(String userEmail) {

        Query q = db.collection("chats")
                .whereEqualTo(
                        "recipient",
                        userEmail.substring(0, userEmail.indexOf('@')));

        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
              @Override
              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                  if (task.isSuccessful()) {
                      for (QueryDocumentSnapshot document : task.getResult()) {
                          LoggedInActivity.ChatListObject chat = new LoggedInActivity.ChatListObject(document.getData());
//                          allChats.add(chat);
                      }

                      listAdapter.notifyDataSetChanged();
                  }

                  else {
                      new AlertDialog.Builder(ChatThreadActivity.this).setMessage("Error Fetching Chats! :(").show();
                  }


              }
          }


        );

    }




}
