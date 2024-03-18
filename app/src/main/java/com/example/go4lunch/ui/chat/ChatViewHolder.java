package com.example.go4lunch.ui.chat;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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

        ConstraintLayout.LayoutParams userParams = (ConstraintLayout.LayoutParams) binding.chatUser.getLayoutParams();
        ConstraintLayout.LayoutParams chatMessageParams = (ConstraintLayout.LayoutParams) binding.chatMessageCard.getLayoutParams();
        ConstraintLayout.LayoutParams chatDateParams = (ConstraintLayout.LayoutParams) binding.chatMessageDate.getLayoutParams();
        if (message.isCurrentUser()) {
            userParams.startToStart = ConstraintLayout.LayoutParams.UNSET;
            userParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;

            chatMessageParams.startToEnd = ConstraintLayout.LayoutParams.UNSET;
            chatMessageParams.endToStart = R.id.chat_user;

            chatDateParams.startToStart = ConstraintLayout.LayoutParams.UNSET;
            chatDateParams.endToEnd = R.id.chat_message_card;

            binding.chatMessage.setBackgroundColor(itemView.getResources().getColor(R.color.green_10_darken));
        } else {
            userParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            userParams.endToEnd = ConstraintLayout.LayoutParams.UNSET;

            chatMessageParams.startToEnd = R.id.chat_user;
            chatMessageParams.endToStart = ConstraintLayout.LayoutParams.UNSET;

            chatDateParams.startToStart = R.id.chat_message_card;
            chatDateParams.endToEnd = ConstraintLayout.LayoutParams.UNSET;

            binding.chatMessage.setBackgroundColor(itemView.getResources().getColor(R.color.red_30_lightened));
        }
        binding.chatUser.setLayoutParams(userParams);
        binding.chatMessageCard.setLayoutParams(chatMessageParams);
        binding.chatMessageDate.setLayoutParams(chatDateParams);
    }
}
