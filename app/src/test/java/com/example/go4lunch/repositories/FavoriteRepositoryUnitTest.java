package com.example.go4lunch.repositories;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.models.firestore.Favorite;
import com.example.go4lunch.data.services.FavoriteRepository;
import com.example.go4lunch.utils.FakeDataUtil;
import com.example.go4lunch.utils.LiveDataTestUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;

@RunWith(JUnit4.class)
public class FavoriteRepositoryUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private final FirebaseFirestore firebaseFirestoreMock = mock();
    private final FirebaseAuth firebaseAuthMock = mock();
    private final CollectionReference favoritesCollectionMock = mock();
    private final DocumentReference documentReferenceMock = mock();
    private FirebaseUser firebaseUserMock;

    private FavoriteRepository favoriteRepository;

    private final String FAVORITES_COLLECTION_NAME = "favorites";

    @Before
    public void setup() {
        favoriteRepository = new FavoriteRepository(firebaseFirestoreMock, firebaseAuthMock);
        when(firebaseFirestoreMock.collection(FAVORITES_COLLECTION_NAME)).thenReturn(favoritesCollectionMock);
        firebaseUserMock = FakeDataUtil.mockFirebaseUser(firebaseAuthMock);
    }

    @Test
    public void isLike_existSuccess() throws InterruptedException {
        String userUid = firebaseUserMock.getUid();
        String placeUid = FakeDataUtil.getPlace().getUid();
        String favoriteUid = userUid + "_" + placeUid;
        DocumentSnapshot documentSnapshot = mock();

        when(favoritesCollectionMock.document(favoriteUid)).thenReturn(documentReferenceMock);
        when(documentSnapshot.exists()).thenReturn(true);

        LiveData<Boolean> isLikeLiveData = favoriteRepository.isLike(placeUid);

        ArgumentCaptor<EventListener<DocumentSnapshot>> argumentCaptor = ArgumentCaptor.captor();
        verify(documentReferenceMock).addSnapshotListener(argumentCaptor.capture());

        EventListener<DocumentSnapshot> eventListener = argumentCaptor.getValue();
        eventListener.onEvent(documentSnapshot, null);

        Boolean isLike = LiveDataTestUtil.getOrAwaitValue(isLikeLiveData);

        verify(firebaseFirestoreMock).collection(FAVORITES_COLLECTION_NAME);
        verifyNoMoreInteractions(firebaseFirestoreMock);
        assertTrue(isLike);
    }

    @Test
    public void isLike_notExistSuccess() throws InterruptedException {
        String userUid = firebaseUserMock.getUid();
        String placeUid = FakeDataUtil.getPlace().getUid();
        String favoriteUid = userUid + "_" + placeUid;
        DocumentSnapshot documentSnapshotMock = mock();

        when(favoritesCollectionMock.document(favoriteUid)).thenReturn(documentReferenceMock);
        when(documentSnapshotMock.exists()).thenReturn(false);

        LiveData<Boolean> isLikeLiveData = favoriteRepository.isLike(placeUid);

        ArgumentCaptor<EventListener<DocumentSnapshot>> argumentCaptor = ArgumentCaptor.captor();
        verify(documentReferenceMock).addSnapshotListener(argumentCaptor.capture());

        EventListener<DocumentSnapshot> eventListener = argumentCaptor.getValue();
        eventListener.onEvent(documentSnapshotMock, null);

        Boolean isLike = LiveDataTestUtil.getOrAwaitValue(isLikeLiveData);

        verify(firebaseFirestoreMock).collection(FAVORITES_COLLECTION_NAME);
        verifyNoMoreInteractions(firebaseFirestoreMock);

        assertFalse(isLike);
    }

    @Test
    public void togglePlaceLike_existSuccess() {
        String userUid = firebaseUserMock.getUid();
        String placeUid = FakeDataUtil.getPlace().getUid();
        String favoriteUid = userUid + "_" + placeUid;
        Task<DocumentSnapshot> documentSnapshotTaskMock = mock();
        DocumentSnapshot documentSnapshotMock = mock();

        when(favoritesCollectionMock.document(favoriteUid)).thenReturn(documentReferenceMock);
        when(documentReferenceMock.get()).thenReturn(documentSnapshotTaskMock);
        when(documentSnapshotMock.exists()).thenReturn(true);
        when(documentSnapshotTaskMock.addOnSuccessListener(any())).thenReturn(documentSnapshotTaskMock);
        when(documentSnapshotTaskMock.addOnFailureListener(any())).thenReturn(documentSnapshotTaskMock);

        favoriteRepository.togglePlaceLike(placeUid);

        ArgumentCaptor<OnSuccessListener<DocumentSnapshot>> argumentCaptor = ArgumentCaptor.captor();
        verify(documentSnapshotTaskMock).addOnSuccessListener(argumentCaptor.capture());

        OnSuccessListener<DocumentSnapshot> eventListener = argumentCaptor.getValue();
        eventListener.onSuccess(documentSnapshotMock);

        verify(documentReferenceMock).delete();

    }

    @Test
    public void togglePlaceLike_notExistSuccess() {
        String userUid = firebaseUserMock.getUid();
        String placeUid = FakeDataUtil.getPlace().getUid();
        String favoriteUid = userUid + "_" + placeUid;
        Favorite favorite = new Favorite(favoriteUid, placeUid, userUid);
        Task<DocumentSnapshot> documentSnapshotTaskMock = mock();
        DocumentSnapshot documentSnapshotMock = mock();

        when(favoritesCollectionMock.document(favoriteUid)).thenReturn(documentReferenceMock);
        when(documentReferenceMock.get()).thenReturn(documentSnapshotTaskMock);
        when(documentSnapshotMock.exists()).thenReturn(false);
        when(documentSnapshotTaskMock.addOnSuccessListener(any())).thenReturn(documentSnapshotTaskMock);
        when(documentSnapshotTaskMock.addOnFailureListener(any())).thenReturn(documentSnapshotTaskMock);

        favoriteRepository.togglePlaceLike(placeUid);

        ArgumentCaptor<OnSuccessListener<DocumentSnapshot>> argumentCaptor = ArgumentCaptor.captor();
        verify(documentSnapshotTaskMock).addOnSuccessListener(argumentCaptor.capture());

        OnSuccessListener<DocumentSnapshot> eventListener = argumentCaptor.getValue();
        eventListener.onSuccess(documentSnapshotMock);

        verify(documentReferenceMock).set(favorite);
    }

}
