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

import com.example.emojo.R;
import com.example.emojo.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class LoggedInFragment extends Fragment {
    ArrayAdapter<String> listAdapter;
    ListView listView;
    List<User> links = new ArrayList<>();
    FirebaseUser currentUser;


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

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.v("!!!", currentUser.toString());

        listAdapter = new ArrayAdapter(
                this.getContext(),
                android.R.layout.simple_list_item_1,
                links);

        listView = (ListView)view.findViewById(R.id.listViewForUsers);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User element = (User)listView.getItemAtPosition(position);

            }
        });


    }

}
