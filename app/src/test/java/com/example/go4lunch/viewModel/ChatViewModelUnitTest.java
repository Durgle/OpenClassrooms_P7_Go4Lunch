package com.example.go4lunch.viewModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.models.firestore.Message;
import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.services.ChatRepository;
import com.example.go4lunch.data.services.UserRepository;
import com.example.go4lunch.ui.chat.ChatViewModel;
import com.example.go4lunch.ui.chat.MessageViewState;
import com.example.go4lunch.utils.LiveDataTestUtil;
import com.example.go4lunch.utils.TimeUtils;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


@RunWith(JUnit4.class)
public class ChatViewModelUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private ChatRepository chatRepository;
    private UserRepository userRepository;
    private FirebaseUser firebaseUser;

    private static final Message message1 = new Message("1", "Bonjour, ça va ?", 1710895508017L, "user1");
    private static final Message message2 = new Message("2", "Bien et toi ?", 1710895526154L, "user2");
    private static final User user1 = new User("user1", "User 1", "user1@gmail.com", null);
    private static final User user2 = new User("user2", "User 2", "user2@gmail.com", null);

    @NonNull
    private List<User> getFakeUsers() {
        return new ArrayList<>(Arrays.asList(user1, user2));
    }

    @NonNull
    private List<Message> getFakeMessage() {
        return new ArrayList<>(Arrays.asList(message1, message2));
    }

    @Before
    public void setup() {
        chatRepository = Mockito.mock(ChatRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        firebaseUser = Mockito.mock(FirebaseUser.class);

        LiveData<List<Message>> messageLiveData = new MutableLiveData<>(getFakeMessage());
        LiveData<List<User>> userLiveData = new MutableLiveData<>(getFakeUsers());

        Mockito.when(chatRepository.getMessages())
                .thenReturn(messageLiveData);
        Mockito.when(userRepository.getAllUsers())
                .thenReturn(userLiveData);
        Mockito.when(userRepository.getCurrentUser())
                .thenReturn(firebaseUser);
    }

    @Test
    public void onMessageChanged_success() throws InterruptedException {
        ChatViewModel viewModel = new ChatViewModel(chatRepository, userRepository);
        String message = "Mon message test";

        String result1 = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentMessage());
        viewModel.onMessageChanged(message);
        String result2 = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentMessage());

        assertNull(result1);
        assertEquals(result2, message);
    }

    @Test
    public void mapMessageIntoMessageViewState_success() throws InterruptedException {
        Mockito.when(firebaseUser.getUid()).thenReturn("user1");
        MessageViewState result = ChatViewModel.mapMessageIntoMessageViewState(message1, firebaseUser, user1);

        assertEquals(result.getMessage(), message1.getMessage());
        assertEquals(result.getId(), message1.getUid());
        assertEquals(result.getUser(), user1);
        assertEquals(result.getDate(), TimeUtils.formatDateTimeForDisplay(1710895508017L, Locale.getDefault()));
        assertTrue(result.isCurrentUser());
    }

    @Test
    public void getMessages_success() throws InterruptedException {
        ChatViewModel viewModel = new ChatViewModel(chatRepository, userRepository);

        Mockito.when(firebaseUser.getUid()).thenReturn("user1");

        List<MessageViewState> result = LiveDataTestUtil.getOrAwaitValue(viewModel.getMessages());
        List<MessageViewState> expected = new ArrayList<>(
                Arrays.asList(
                        ChatViewModel.mapMessageIntoMessageViewState(message1, firebaseUser, user1),
                        ChatViewModel.mapMessageIntoMessageViewState(message2, firebaseUser, user2)
                )
        );

        assertEquals(result, expected);
    }

    @Test
    public void send_success() {
        ChatViewModel viewModel = new ChatViewModel(chatRepository, userRepository);

        String message = "Message de chat";
        viewModel.onMessageChanged(message);
        viewModel.send();

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.captor();
        Mockito.verify(chatRepository).sendMessage(argumentCaptor.capture(), anyLong());
        String messagePassed = argumentCaptor.getValue();
        assertEquals(message, messagePassed);
    }

    @Test
    public void sendWithoutMessage_success() {
        ChatViewModel viewModel = new ChatViewModel(chatRepository, userRepository);

        String message = "";
        viewModel.onMessageChanged(message);
        viewModel.send();

        Mockito.verify(chatRepository, Mockito.never()).sendMessage(anyString(), anyLong());
    }

}
