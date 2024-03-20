package com.example.go4lunch.ui.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.models.firestore.Message;
import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.services.ChatRepository;
import com.example.go4lunch.data.services.UserRepository;
import com.example.go4lunch.ui.workmate.WorkmateViewState;
import com.example.go4lunch.utils.TimeUtils;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class ChatViewModel extends ViewModel {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MediatorLiveData<List<MessageViewState>> chatMessageLiveData = new MediatorLiveData<>();
    private final MutableLiveData<String> currentMessage = new MutableLiveData<>(null);

    public static MessageViewState mapMessageIntoMessageViewState(
            @NonNull Message message, @Nullable FirebaseUser currentUser, @NonNull User user
    ) {
        String date = TimeUtils.formatDateTimeForDisplay(message.getCreationDate(), Locale.getDefault());
        boolean isCurrentUser = currentUser != null && currentUser.getUid().equals(user.getUid());
        return new MessageViewState(message.getUid(), user, message.getMessage(), date, isCurrentUser);
    }

    public ChatViewModel(
            ChatRepository chatRepository,
            UserRepository userRepository
    ) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;

        LiveData<List<Message>> messagesLiveData = chatRepository.getMessages();
        LiveData<List<User>> usersLiveData = userRepository.getAllUsers();

        chatMessageLiveData.addSource(messagesLiveData, messages -> combine(usersLiveData.getValue(), messages));
        chatMessageLiveData.addSource(usersLiveData, users -> combine(users, messagesLiveData.getValue()));
    }

    private void combine(@Nullable List<User> users, @Nullable List<Message> messages) {

        if (users != null && messages != null) {

            List<MessageViewState> list = new ArrayList<>();
            FirebaseUser currentUser = userRepository.getCurrentUser();
            for (Message message : messages) {

                Optional<User> foundUser = users.stream()
                        .filter(user -> Objects.equals(user.getUid(), message.getUserId()))
                        .findFirst();

                foundUser.ifPresent(user -> list.add(mapMessageIntoMessageViewState(message, currentUser, user)));
            }

            chatMessageLiveData.setValue(list);
        } else {
            chatMessageLiveData.setValue(new ArrayList<>());
        }
    }

    public LiveData<List<MessageViewState>> getMessages() {
        return chatMessageLiveData;
    }

    public LiveData<String> getCurrentMessage() {
        return currentMessage;
    }

    public void onMessageChanged(String message) {
        currentMessage.setValue(message);
    }

    public void send() {
        if (currentMessage.getValue() != null && !Objects.equals(currentMessage.getValue(), "")) {
            this.chatRepository.sendMessage(currentMessage.getValue(), new Date().getTime());
            currentMessage.setValue(null);
        }
    }

}
