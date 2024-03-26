package com.example.go4lunch.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.data.models.firestore.Setting;
import com.example.go4lunch.data.services.SettingRepository;
import com.example.go4lunch.utils.FakeDataUtil;
import com.example.go4lunch.utils.LiveDataTestUtil;
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
public class SettingRepositoryUnitTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private final FirebaseFirestore firebaseFirestoreMock = mock();
    private final FirebaseAuth firebaseAuthMock = mock();
    private final CollectionReference settingsCollectionMock = mock();
    private final DocumentReference documentReferenceMock = mock();
    private FirebaseUser firebaseUserMock;

    private SettingRepository settingRepository;

    private final String SETTINGS_COLLECTION_NAME = "settings";

    @Before
    public void setup() {
        settingRepository = new SettingRepository(firebaseFirestoreMock, firebaseAuthMock);
        when(firebaseFirestoreMock.collection(SETTINGS_COLLECTION_NAME)).thenReturn(settingsCollectionMock);
        firebaseUserMock = FakeDataUtil.mockFirebaseUser(firebaseAuthMock);
    }

    @Test
    public void saveSetting_success() {
        String uid = firebaseUserMock.getUid();
        Task<Void> voidTaskMock = mock();
        boolean notificationStatus = true;
        Setting setting = new Setting(uid, notificationStatus);

        when(settingsCollectionMock.document(uid)).thenReturn(documentReferenceMock);
        when(documentReferenceMock.set(any())).thenReturn(voidTaskMock);

        settingRepository.saveSetting(notificationStatus);

        verify(documentReferenceMock).set(setting);
        verifyNoMoreInteractions(documentReferenceMock);
    }

    @Test
    public void getSettings_existSuccess() throws InterruptedException {
        String uid = firebaseUserMock.getUid();
        DocumentSnapshot documentSnapshotMock = mock();
        Setting settings = new Setting("userId", true);
        when(settingsCollectionMock.document(uid)).thenReturn(documentReferenceMock);
        when(documentSnapshotMock.exists()).thenReturn(true);
        when(documentSnapshotMock.toObject(Setting.class)).thenReturn(settings);

        LiveData<Setting> settingLiveData = settingRepository.getSettings();

        ArgumentCaptor<EventListener<DocumentSnapshot>> argumentCaptor = ArgumentCaptor.captor();
        verify(documentReferenceMock).addSnapshotListener(argumentCaptor.capture());

        EventListener<DocumentSnapshot> listener = argumentCaptor.getValue();
        listener.onEvent(documentSnapshotMock, null);

        Setting actualSettings = LiveDataTestUtil.getOrAwaitValue(settingLiveData);

        verify(firebaseFirestoreMock).collection(SETTINGS_COLLECTION_NAME);
        verifyNoMoreInteractions(firebaseFirestoreMock);

        assertEquals(settings, actualSettings);

    }

    @Test
    public void getSettings_notExistSuccess() throws InterruptedException {
        String uid = firebaseUserMock.getUid();
        DocumentSnapshot documentSnapshotMock = mock();
        when(settingsCollectionMock.document(uid)).thenReturn(documentReferenceMock);
        when(documentSnapshotMock.exists()).thenReturn(false);

        LiveData<Setting> settingLiveData = settingRepository.getSettings();

        ArgumentCaptor<EventListener<DocumentSnapshot>> argumentCaptor = ArgumentCaptor.captor();
        verify(documentReferenceMock).addSnapshotListener(argumentCaptor.capture());

        EventListener<DocumentSnapshot> listener = argumentCaptor.getValue();
        listener.onEvent(documentSnapshotMock, null);

        Setting actualSettings = LiveDataTestUtil.getOrAwaitValue(settingLiveData);

        verify(firebaseFirestoreMock).collection(SETTINGS_COLLECTION_NAME);
        verifyNoMoreInteractions(firebaseFirestoreMock);

        assertNull(actualSettings);
    }

}
