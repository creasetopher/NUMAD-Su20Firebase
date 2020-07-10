package com.example.emojo.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.emojo.R;
import com.example.emojo.data.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class LoggedInFragment extends Fragment {
    FloatingActionButton newChatButton;



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logged_in, container, false);
    }

    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newChatButton = (FloatingActionButton)view.findViewById(R.id.floatingButton);
        newChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("FROMFRAG", "works!");
//                Bundle argBundle = new Bundle();
//                argBundle.putString("sender", passedUsername);
//
//                Fragment loggedInFragment = new LoggedInFragment();
//                getSupportFragmentManager().beginTransaction().add(loggedInFragment, "loggedInFragment");
//                Fragment fragmentNewChat = new FragmentNewChat();
//
//                fragmentNewChat.setArguments(argBundle);

//                Intent intent = new Intent(LoggedInActivity.this, fragmentNewChat.getClass());
//
//                startActivity(intent);
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.container, new Fragment(),"MyFragment").commit();
//                Fragment frag = ((Fragment) getSupportFragmentManager().findFragmentByTag("MyFragment"));

//                NavHostFragment.findNavController(loggedInFragment).navigate(R.id.action_fragment_logged_in_to_fragment_new_chat);

//                NavController navController = Navigation.findNavController(findViewById(android.R.id.content));
//                navController.navigate(R.id.action_fragment_logged_in_to_fragment_new_chat);
            }
            });

    }
}
