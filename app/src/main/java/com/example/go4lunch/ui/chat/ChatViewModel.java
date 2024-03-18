package com.example.go4lunch.ui.chat;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.models.firestore.Message;
import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.services.ChatRepository;
import com.example.go4lunch.data.services.UserRepository;

import java.text.SimpleDateFormat;
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
    private final MutableLiveData<String> currentMessage = new MutableLiveData<>();

    public ChatViewModel(
            ChatRepository chatRepository,
            UserRepository userRepository
    ) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;

        LiveData<List<Message>> messagesLiveData = chatRepository.getMessages();
        LiveData<List<User>> usersLiveData = userRepository.getAllUsers();

        chatMessageLiveData.addSource(messagesLiveData, messages -> combine(usersLiveData.getValue(), messages));
        chatMessageLiveData.addSource(usersLiveData, users -> combine(users,messagesLiveData.getValue()));
    }

    private void combine(@Nullable List<User> users,@Nullable List<Message> messages) {

        if (users != null && messages != null) {

            List<MessageViewState> list = new ArrayList<>();
            for (Message message : messages) {

                Optional<User> foundUser = users.stream()
                        .filter(user -> Objects.equals(user.getUid(), message.getUserId()))
                        .findFirst();

                if(foundUser.isPresent()){
                    list.add(new MessageViewState(message.getUid(), foundUser.get(),message.getMessage(),String.valueOf( message.getCreationDate())));
                }
            }

            chatMessageLiveData.setValue(list);
        } else {
            chatMessageLiveData.setValue(new ArrayList<>());
        }
    }

    public LiveData<List<MessageViewState>> getMessages() {
        return chatMessageLiveData;
    }

    public LiveData<String> getCurrentMessages() {
        return currentMessage;
    }

    public void onMessageChanged(String message) {
        currentMessage.setValue(message);
    }

    public void send() {
        if (currentMessage.getValue() != null && !Objects.equals(currentMessage.getValue(), "")) {
            this.chatRepository.sendMessage(currentMessage.getValue());
            currentMessage.setValue(null);
        }
    }

}
