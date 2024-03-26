package com.example.go4lunch.repositories;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.models.firestore.Message;
import com.example.go4lunch.data.services.ChatRepository;
import com.example.go4lunch.utils.FakeDataUtil;
import com.example.go4lunch.utils.LiveDataTestUtil;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;

import java.util.Date;
import java.util.List;

@RunWith(JUnit4.class)
public class ChatRepositoryUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private final FirebaseFirestore firebaseFirestoreMock = mock();
    private final FirebaseAuth firebaseAuthMock = mock();
    private final CollectionReference chatCollectionMock = mock();
    private final DocumentReference documentReferenceMock = mock();
    private FirebaseUser firebaseUserMock;

    private ChatRepository chatRepository;

    private final String CHAT_COLLECTION_NAME = "chat";

    @Before
    public void setup() {
        chatRepository = new ChatRepository(firebaseFirestoreMock, firebaseAuthMock);
        when(firebaseFirestoreMock.collection(CHAT_COLLECTION_NAME)).thenReturn(chatCollectionMock);
        firebaseUserMock = FakeDataUtil.mockFirebaseUser(firebaseAuthMock);
    }

    @Test
    public void sendMessage_success() {
        String messageContent = "Test message";
        long creationTimestamp = new Date().getTime();
        String userId = firebaseUserMock.getUid();
        String messageUid = userId + "_" + creationTimestamp;
        Message messageExpected = new Message(messageUid, messageContent, creationTimestamp, userId);
        DocumentReference documentReferenceMock = mock();
        Task<Void> voidTaskMock = mock();

        when(chatCollectionMock.document(messageUid)).thenReturn(documentReferenceMock);
        when(documentReferenceMock.set(any())).thenReturn(voidTaskMock);

        chatRepository.sendMessage(messageContent, creationTimestamp);

        verify(documentReferenceMock).set(messageExpected);
    }

    @Test
    public void getMessages_success() throws InterruptedException {

        QuerySnapshot querySnapshot = mock();
        List<Message> messages = FakeDataUtil.getChatMessage();

        when(querySnapshot.toObjects(Message.class)).thenReturn(messages);

        LiveData<List<Message>> messagesLiveData = chatRepository.getMessages();

        ArgumentCaptor<EventListener<QuerySnapshot>> argumentCaptor = ArgumentCaptor.captor();
        verify(chatCollectionMock).addSnapshotListener(argumentCaptor.capture());

        EventListener<QuerySnapshot> eventListener = argumentCaptor.getValue();

        eventListener.onEvent(querySnapshot, null);

        List<Message> currentMessages = LiveDataTestUtil.getOrAwaitValue(messagesLiveData);

        assertEquals(messages, currentMessages);
        verifyNoMoreInteractions(chatCollectionMock);
    }

}
