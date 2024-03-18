package com.example.go4lunch.ui.chat;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ChatListItemBinding;

public class ChatViewHolder extends RecyclerView.ViewHolder {

    private final ChatListItemBinding binding;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ChatListItemBinding.bind(itemView);
    }

    public void bind(@NonNull MessageViewState message) {
        String urlPicture = message.getUser().getUrlPicture();
        Glide.with(itemView.getContext())
                .load(urlPicture != null ? urlPicture : R.drawable.no_photos)
                .into(binding.chatUser);
        binding.chatMessage.setText(message.getMessage());
        binding.chatMessageDate.setText(message.getDate());
    }
}
