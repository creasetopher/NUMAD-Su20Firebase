package com.example.emojo.ui.login;

import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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
    List<ChatListObject> allChats = new ArrayList();
    List<ChatListObject> topChats = new ArrayList<>();
    Map<String, Map<String, ChatListObject>> chatsGroupedBySender = new HashMap<>();


    FirebaseUser currentUser;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Map h = new HashMap<String, String>();
//        h.put("sender", "me");
//        h.put("recipient", "chris.sims510@gmail.com");
//        h.put("message", "!!!");
//        h.put("time", "7:00pm");
//        topChats.add(new ChatListObject(h));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_logged_in);


        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        listAdapter = new ArrayAdapter(
                LoggedInActivity.this,
                android.R.layout.simple_list_item_1,
                topChats);

        listView = findViewById(R.id.listViewForUsers);

        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatListObject element = (ChatListObject)listView.getItemAtPosition(position);
                String sender = element.getSender();

                Map<String, ChatListObject> messageMap = chatsGroupedBySender.get(sender);

                ArrayList<CharSequence> allMessagesFromSender = new ArrayList<>();

                for (String timestamp : messageMap.keySet()) {
                    allMessagesFromSender.add(messageMap.get(timestamp).getMessage());
                }


                Bundle argBundle = new Bundle();
                argBundle.putString("sender", sender);
                argBundle.putCharSequenceArrayList("messages", allMessagesFromSender);

                Fragment chatThreadFragment = new FragmentChatThread();

                chatThreadFragment.setArguments(argBundle);

                Intent intent = new Intent(LoggedInActivity.this, chatThreadFragment.getClass());

                startActivity(intent);


            }

        });

        logButton = findViewById(R.id.chat_button);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAdapter.notifyDataSetChanged();
//                getChats(currentUser.getEmail());
                for(ChatListObject o : topChats) {
                    Log.v("chats", o.toString());
                }
            }

        });

        getChats(currentUser.getEmail());



    }


    private static class ChatListObject {
        String sender;
        String message;
        String time;

        // a single message from a sender
        public ChatListObject(Map<String, Object> chat) {
            this.sender = (String)chat.get("sender");
            this.message = (String)chat.get("message");
            this.time = (String)chat.get("time");
        }

        @Override
        public String toString() {
            return this.sender + "     " + this.time;
        }

        public String getMessage() {
            return this.message;
        }

        public String getSender() {
            return this.sender;
        }

        public String getTime() {
            return this.time;
        }

        public static Map<String, Map<String, ChatListObject> > groupBySender(List<ChatListObject> chats) {

            Map<String,  Map<String, ChatListObject>> groupedChats = new HashMap<>();

            for (ChatListObject chat : chats) {
                String sender = chat.getSender();

                if (!groupedChats.containsKey(sender)) {
                    groupedChats.put(sender, new HashMap<String, ChatListObject>());
                }
                groupedChats.get(sender).put(chat.getTime(), chat);

            }

            return groupedChats;

        }

        public static List<ChatListObject> getTopChats(

                Map<String, Map<String, ChatListObject>> groupedChatsBySender ) {

            List<ChatListObject> topChats = new ArrayList<>();

            for(String sender : groupedChatsBySender.keySet()) {

                Map<String, ChatListObject> chatsFromSender = groupedChatsBySender.get(sender);

                String lastSentTime =  chatsFromSender.keySet().iterator().next();


                if(lastSentTime != null) {
                    topChats.add(
                            chatsFromSender.get(lastSentTime)
                    );
                }

            }


            return topChats;
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

                                                     allChats.add(chat);

                                                 }

                                                 chatsGroupedBySender =
                                                         ChatListObject.groupBySender(allChats);

                                                 topChats.clear();

                                                 topChats.addAll(ChatListObject
                                                         .getTopChats(chatsGroupedBySender)
                                                 );

                                                 listAdapter.notifyDataSetChanged();
                                             }

                                             else {
                                                 new AlertDialog.Builder(LoggedInActivity.this).setMessage("Error Fetching Chats! :(").show();
                                             }


                                         }
                                     }


       );

    }
}