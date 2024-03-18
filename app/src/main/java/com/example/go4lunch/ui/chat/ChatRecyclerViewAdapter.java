package com.example.go4lunch.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.go4lunch.R;

public class ChatRecyclerViewAdapter extends ListAdapter<MessageViewState,ChatViewHolder> {

    protected ChatRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static final DiffUtil.ItemCallback<MessageViewState> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MessageViewState>() {
                @Override
                public boolean areItemsTheSame(@NonNull MessageViewState oldMessage, @NonNull MessageViewState newMessage) {
                    return oldMessage.getId().equals(newMessage.getId());
                }
                @Override
                public boolean areContentsTheSame(@NonNull MessageViewState oldMessage, @NonNull MessageViewState newMessage) {
                    return oldMessage.equals(newMessage);
                }
            };
}
