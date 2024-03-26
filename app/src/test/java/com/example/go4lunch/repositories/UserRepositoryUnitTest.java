package com.example.go4lunch.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.net.Uri;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.models.firestore.Place;
import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.services.UserRepository;
import com.example.go4lunch.utils.FakeDataUtil;
import com.example.go4lunch.utils.LiveDataTestUtil;
import com.example.go4lunch.utils.SingleLiveEvent;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(JUnit4.class)
public class UserRepositoryUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private final FirebaseFirestore firebaseFirestoreMock = mock();
    private final FirebaseAuth firebaseAuthMock = mock();
    private final AuthUI authUiMock = mock();
    private final CollectionReference usersCollectionMock = mock();
    private final QuerySnapshot querySnapshotMock = mock();
    private final Task<Void> voidTaskMock = mock();
    private final DocumentReference documentReferenceMock = mock();
    private final DocumentSnapshot documentSnapshotMock = mock();
    private FirebaseUser firebaseUserMock;


    private UserRepository userRepository;

    private final String USERS_COLLECTION_NAME = "users";
    private final String USERS_ID_FIELD = "uid";
    private final String USERS_EMAIL_FIELD = "email";
    private final String USERS_DISPLAY_NAME_FIELD = "displayName";
    private final String USERS_URL_PICTURE_FIELD = "urlPicture";
    private final String USERS_PLACE_FIELD = "place";
    private final String USERS_PLACE_ID_FIELD = "place.uid";

    @Before
    public void setup() {
        userRepository = new UserRepository(firebaseFirestoreMock, authUiMock, firebaseAuthMock);
        when(firebaseFirestoreMock.collection(USERS_COLLECTION_NAME)).thenReturn(usersCollectionMock);
        firebaseUserMock = FakeDataUtil.mockFirebaseUser(firebaseAuthMock);
    }

    @Test
    public void signOut_success() throws InterruptedException {
        Context contextMock = mock();
        Void voidMock = mock();

        when(authUiMock.signOut(contextMock)).thenReturn(voidTaskMock);
        when(voidTaskMock.addOnSuccessListener(any())).thenReturn(voidTaskMock);
        when(voidTaskMock.addOnFailureListener(any())).thenReturn(voidTaskMock);

        SingleLiveEvent<Boolean> singleLiveEvent = userRepository.signOut(contextMock);

        ArgumentCaptor<OnSuccessListener<Void>> argumentCaptor = ArgumentCaptor.captor();
        verify(voidTaskMock).addOnSuccessListener(argumentCaptor.capture());

        OnSuccessListener<Void> listener = argumentCaptor.getValue();
        listener.onSuccess(voidMock);

        Boolean result = LiveDataTestUtil.getOrAwaitValue(singleLiveEvent);

        assertTrue(result);

    }

    @Test
    public void createOrUpdateUser_creationSuccess() {
        Task<DocumentSnapshot> taskDocumentSnapshotMock = mock();

        String uid = firebaseUserMock.getUid();
        String email = firebaseUserMock.getEmail();
        String displayName = firebaseUserMock.getDisplayName();
        Uri photoUrl = firebaseUserMock.getPhotoUrl();

        when(usersCollectionMock.document(uid)).thenReturn(documentReferenceMock);
        when(documentReferenceMock.get()).thenReturn(taskDocumentSnapshotMock);
        when(taskDocumentSnapshotMock.addOnSuccessListener(any())).thenReturn(taskDocumentSnapshotMock);
        when(taskDocumentSnapshotMock.addOnFailureListener(any())).thenReturn(taskDocumentSnapshotMock);
        when(documentSnapshotMock.exists()).thenReturn(false);

        userRepository.createOrUpdateUser();

        ArgumentCaptor<OnSuccessListener<DocumentSnapshot>> argumentCaptor = ArgumentCaptor.captor();
        verify(taskDocumentSnapshotMock).addOnSuccessListener(argumentCaptor.capture());
        OnSuccessListener<DocumentSnapshot> listener = argumentCaptor.getValue();
        listener.onSuccess(documentSnapshotMock);

        User user = new User(uid, displayName, email, photoUrl.toString());

        verify(documentReferenceMock).set(user);
        verify(documentReferenceMock, never()).update(any());

    }

    @Test
    public void createOrUpdateUser_updateSuccess() {
        Task<DocumentSnapshot> taskDocumentSnapshotMock = mock();

        String uid = firebaseUserMock.getUid();
        String email = firebaseUserMock.getEmail();
        String displayName = firebaseUserMock.getDisplayName();
        Uri photoUrl = firebaseUserMock.getPhotoUrl();

        when(usersCollectionMock.document(uid)).thenReturn(documentReferenceMock);
        when(documentReferenceMock.get()).thenReturn(taskDocumentSnapshotMock);
        when(taskDocumentSnapshotMock.addOnSuccessListener(any())).thenReturn(taskDocumentSnapshotMock);
        when(taskDocumentSnapshotMock.addOnFailureListener(any())).thenReturn(taskDocumentSnapshotMock);
        when(documentSnapshotMock.exists()).thenReturn(true);

        userRepository.createOrUpdateUser();

        ArgumentCaptor<OnSuccessListener<DocumentSnapshot>> argumentCaptor = ArgumentCaptor.captor();
        verify(taskDocumentSnapshotMock).addOnSuccessListener(argumentCaptor.capture());
        OnSuccessListener<DocumentSnapshot> listener = argumentCaptor.getValue();
        listener.onSuccess(documentSnapshotMock);

        Map<String, Object> data = new HashMap<>();
        data.put(USERS_EMAIL_FIELD, email);
        data.put(USERS_DISPLAY_NAME_FIELD, displayName);
        data.put(USERS_URL_PICTURE_FIELD, photoUrl.toString());

        verify(documentReferenceMock).update(data);
        verify(documentReferenceMock, never()).set(any());
    }

    @Test
    public void updateChosenRestaurant_success() {
        String uid = firebaseUserMock.getUid();

        when(usersCollectionMock.document(uid)).thenReturn(documentReferenceMock);
        when(documentReferenceMock.update(anyString(), any())).thenReturn(voidTaskMock);
        Place fakePlace = FakeDataUtil.getPlace();
        userRepository.updateChosenRestaurant(fakePlace);

        verify(documentReferenceMock).update(USERS_PLACE_FIELD, fakePlace);
    }

    @Test
    public void getUserData_success() throws InterruptedException {
        String uid = firebaseUserMock.getUid();
        User user = FakeDataUtil.getFirestoreUser();

        when(usersCollectionMock.document(uid)).thenReturn(documentReferenceMock);
        when(documentSnapshotMock.toObject(User.class)).thenReturn(user);
        when(documentSnapshotMock.exists()).thenReturn(true);

        LiveData<User> liveDataUsers = userRepository.getUserData();

        ArgumentCaptor<EventListener<DocumentSnapshot>> argumentCaptor = ArgumentCaptor.captor();
        verify(documentReferenceMock).addSnapshotListener(argumentCaptor.capture());

        EventListener<DocumentSnapshot> eventListener = argumentCaptor.getValue();
        eventListener.onEvent(documentSnapshotMock, null);

        User userData = LiveDataTestUtil.getOrAwaitValue(liveDataUsers);

        assertEquals(user, userData);
    }

    @Test
    public void getUserData_notExist() throws InterruptedException {
        String uid = firebaseUserMock.getUid();
        User user = FakeDataUtil.getFirestoreUser();

        when(usersCollectionMock.document(uid)).thenReturn(documentReferenceMock);
        when(documentSnapshotMock.toObject(User.class)).thenReturn(user);
        when(documentSnapshotMock.exists()).thenReturn(false);

        LiveData<User> liveDataUsers = userRepository.getUserData();

        ArgumentCaptor<EventListener<DocumentSnapshot>> argumentCaptor = ArgumentCaptor.captor();
        verify(documentReferenceMock).addSnapshotListener(argumentCaptor.capture());

        EventListener<DocumentSnapshot> eventListener = argumentCaptor.getValue();
        eventListener.onEvent(documentSnapshotMock, null);

        User userData = LiveDataTestUtil.getOrAwaitValue(liveDataUsers);

        assertNull(userData);
    }

    @Test
    public void getAllWorkmates_success() throws InterruptedException {

        Query queryMock = mock();
        String uid = firebaseUserMock.getUid();

        List<User> users = FakeDataUtil.getFirestoreUsers();
        when(querySnapshotMock.toObjects(User.class)).thenReturn(users);
        when(usersCollectionMock.whereNotEqualTo(USERS_ID_FIELD, uid)).thenReturn(queryMock);

        LiveData<List<User>> liveDataUsers = userRepository.getAllWorkmates();

        ArgumentCaptor<EventListener<QuerySnapshot>> argumentCaptor = ArgumentCaptor.captor();
        verify(queryMock).addSnapshotListener(argumentCaptor.capture());

        EventListener<QuerySnapshot> eventListener = argumentCaptor.getValue();
        eventListener.onEvent(querySnapshotMock, null);

        List<User> workmates = LiveDataTestUtil.getOrAwaitValue(liveDataUsers);

        verify(firebaseFirestoreMock).collection(USERS_COLLECTION_NAME);
        verify(usersCollectionMock).whereNotEqualTo(USERS_ID_FIELD, uid);
        verifyNoMoreInteractions(firebaseFirestoreMock);

        List<User> expectedUserResult = users.stream()
                .sorted(Comparator.comparing((User user) -> user.getPlace() != null ? 0 : 1)
                        .thenComparing(User::getDisplayName))
                .collect(Collectors.toList());

        assertEquals(expectedUserResult, workmates);
    }

    @Test
    public void getAllUsers_success() throws InterruptedException {

        List<User> users = FakeDataUtil.getFirestoreUsers();
        when(querySnapshotMock.toObjects(User.class)).thenReturn(users);

        LiveData<List<User>> liveDataUsers = userRepository.getAllUsers();

        ArgumentCaptor<EventListener<QuerySnapshot>> argumentCaptor = ArgumentCaptor.captor();
        verify(usersCollectionMock).addSnapshotListener(argumentCaptor.capture());

        EventListener<QuerySnapshot> eventListener = argumentCaptor.getValue();
        eventListener.onEvent(querySnapshotMock, null);

        List<User> actualUsers = LiveDataTestUtil.getOrAwaitValue(liveDataUsers);

        verify(firebaseFirestoreMock).collection(USERS_COLLECTION_NAME);
        verifyNoMoreInteractions(firebaseFirestoreMock);

        assertEquals(users, actualUsers);
    }

    @Test
    public void getWorkmatesForPlace_success() throws InterruptedException {

        String placeID = "place1";
        String uid = firebaseUserMock.getUid();
        List<User> users = FakeDataUtil.getFirestoreUsersForPlace();

        Query queryMock = mock();
        when(querySnapshotMock.toObjects(User.class)).thenReturn(users);
        when(usersCollectionMock.whereEqualTo(USERS_PLACE_ID_FIELD, placeID)).thenReturn(queryMock);
        when(queryMock.whereNotEqualTo(USERS_ID_FIELD, uid)).thenReturn(queryMock);

        LiveData<List<User>> liveDataUsers = userRepository.getWorkmatesForPlace(placeID);

        ArgumentCaptor<EventListener<QuerySnapshot>> argumentCaptor = ArgumentCaptor.captor();
        verify(queryMock).addSnapshotListener(argumentCaptor.capture());

        EventListener<QuerySnapshot> eventListener = argumentCaptor.getValue();
        eventListener.onEvent(querySnapshotMock, null);

        List<User> workmates = LiveDataTestUtil.getOrAwaitValue(liveDataUsers);

        verify(usersCollectionMock).whereEqualTo(USERS_PLACE_ID_FIELD, placeID);
        verify(queryMock).whereNotEqualTo(USERS_ID_FIELD, uid);
        assertEquals(users, workmates);
    }

}
