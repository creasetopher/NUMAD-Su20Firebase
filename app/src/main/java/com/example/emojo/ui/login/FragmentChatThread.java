package com.example.emojo.ui.login;

//NOT USED



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.emojo.R;

import java.util.List;

public class FragmentChatThread extends Fragment {
    String sender;
    List<CharSequence> messages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        sender = args.getString("sender");
        messages = args.getCharSequenceArrayList("messages");
        return inflater.inflate(R.layout.fragment_chat_thread, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
