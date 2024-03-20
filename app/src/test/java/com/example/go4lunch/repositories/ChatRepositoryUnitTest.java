package com.example.go4lunch.repositories;

import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.go4lunch.data.models.firestore.Message;
import com.example.go4lunch.data.services.ChatRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;

@RunWith(JUnit4.class)
public class ChatRepositoryUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private ChatRepository chatRepository;

    @Before
    public void setup() {
        firebaseFirestore = mock(FirebaseFirestore.class);
        firebaseAuth = mock(FirebaseAuth.class);
        chatRepository = new ChatRepository(firebaseFirestore, firebaseAuth);
    }

    @Test
    public void sendMessage_success() {
        String messageContent = "Test message";
        long creationTimestamp = new Date().getTime();
        String userId = "testUserID";
        Message message = new Message(userId, messageContent, creationTimestamp, userId);
    }

    @Test
    public void getMessages_success() {

    }

}
