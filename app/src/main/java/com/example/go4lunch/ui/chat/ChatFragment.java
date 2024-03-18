package com.example.go4lunch.ui.chat;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.databinding.FragmentChatBinding;
import com.example.go4lunch.injection.ViewModelFactory;

public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    private ChatViewModel viewModel;
    private ChatRecyclerViewAdapter adapter;

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapter = new ChatRecyclerViewAdapter();
        this.viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ChatViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentChatBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getMessages().observe(getViewLifecycleOwner(), list -> adapter.submitList(list));
        binding.chatMessageList.setAdapter(adapter);
        viewModel.getCurrentMessages().observe(getViewLifecycleOwner(), currentMessage -> {
            if (!binding.chatMessageInput.getText().toString().equals(currentMessage)) {
                binding.chatMessageInput.setText(currentMessage);
            }
        });
        binding.chatMessageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.onMessageChanged(editable.toString());
            }
        });
        binding.chatSendMessage.setOnClickListener(v -> {
            viewModel.send();
            binding.chatMessageInput.clearFocus();

            //Close keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });
    }

}