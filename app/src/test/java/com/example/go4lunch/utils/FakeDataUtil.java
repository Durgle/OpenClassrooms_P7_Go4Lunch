package com.example.go4lunch.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.location.Location;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.go4lunch.data.models.firestore.Message;
import com.example.go4lunch.data.models.firestore.Place;
import com.example.go4lunch.data.models.firestore.User;
import com.example.go4lunch.data.models.map.Bounds;
import com.example.go4lunch.data.models.map.Geometry;
import com.example.go4lunch.data.models.map.LatLngLiteral;
import com.example.go4lunch.data.models.map.MapPlace;
import com.example.go4lunch.data.models.map.PlaceAutocompletePrediction;
import com.example.go4lunch.data.models.map.PlaceOpeningHours;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FakeDataUtil {

    private static final User user1 = new User("user1", "User 1", "user1@gmail.com", "http://fakepicture1.jpg");
    private static final User user2 = new User("user2", "User 2", "user2@gmail.com", "http://fakepicture2.jpg");
    private static final Place place1 = new Place("place1", "Place 1", "46 rue test place");
    private static final Place place2 = new Place("place2", "Place 2", "18 rue test place 2");
    private static final Place place3 = new Place("place3", "Place 3", "73 rue test place 3");
    private static final User userWithPlace1 = new User("user3", "User 3", "user3@gmail.com", "http://fakepicture3.jpg", place1);
    private static final User userWithPlace2 = new User("user4", "User 4", "user4@gmail.com", "http://fakepicture3.jpg", place2);
    private static final User userWithPlace3 = new User("user5", "User 5", "user5@gmail.com", "http://fakepicture3.jpg", place1);

    public static List<User> getFirestoreUsers() {
        return new ArrayList<>(Arrays.asList(user1, userWithPlace1, user2, userWithPlace2, userWithPlace3));
    }

    public static List<User> getFirestoreUsersForPlace() {
        return new ArrayList<>(Arrays.asList(userWithPlace1, userWithPlace3));
    }

    public static List<User> getFirestoreUsersForPlace(int amount, @NonNull Place place) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            users.add(new User("user" + i, "User " + i, "user" + i + "@test.fr", null, place));
        }
        return users;
    }

    public static List<User> getFirestoreUsersWithPlace() {
        return new ArrayList<>(Arrays.asList(userWithPlace1, userWithPlace2, userWithPlace3));
    }

    public static User getFirestoreUser() {
        return user1;
    }

    public static User getFirestoreUserWithPlace(Place place) {
        return new User("user2", "User 2", "user2@gmail.com", "http://fakepicture2.jpg", place);
    }

    public static String getPhotoUrl() {
        return user1.getUrlPicture();
    }

    public static String getDisplayName() {
        return user1.getDisplayName();
    }

    public static String getUid() {
        return user1.getUid();
    }

    public static String getEmail() {
        return user1.getEmail();
    }

    public static Place getPlace() {
        return place1;
    }

    public static FirebaseUser mockFirebaseUser(FirebaseAuth firebaseAuthMock) {
        FirebaseUser firebaseUserMock = mock();
        when(firebaseAuthMock.getCurrentUser()).thenReturn(firebaseUserMock);
        when(firebaseUserMock.getUid()).thenReturn(FakeDataUtil.getUid());
        when(firebaseUserMock.getDisplayName()).thenReturn(FakeDataUtil.getDisplayName());
        when(firebaseUserMock.getEmail()).thenReturn(FakeDataUtil.getEmail());
        Uri uriMock = mock();
        when(uriMock.toString()).thenReturn(FakeDataUtil.getPhotoUrl());
        when(firebaseUserMock.getPhotoUrl()).thenReturn(uriMock);

        return firebaseUserMock;
    }

    public static List<Message> getChatMessage() {

        long creationDate = new Date().getTime();

        return new ArrayList<>(Arrays.asList(
                new Message("message1", "Message 1", creationDate, "user1"),
                new Message("message2", "Message 2", creationDate, "user2"),
                new Message("message3", "Message 3", creationDate, "user1"),
                new Message("message4", "Message 4", creationDate, "user3")
        ));
    }

    public static List<MapPlace> getNearbyPlaces() {

        return new ArrayList<>(Arrays.asList(
                new MapPlace(null, "14 rue test", null, null,
                        null, null, "Place 1", new PlaceOpeningHours(false, null,
                        null, null, null), "place1", 2F, null, null, null),
                new MapPlace(null, "18 rue test", null, null,
                        null, null, "Place 2", new PlaceOpeningHours(true, null,
                        null, null, null), "place2", 5F, null, null, null),
                new MapPlace(null, "72 rue machin", null, null,
                        null, null, "Place 3", new PlaceOpeningHours(false, null,
                        null, null, null), "place3", 1F, null, null, null),
                new MapPlace(null, "43 rue truc", null, null,
                        null, null, "Place 4", new PlaceOpeningHours(true, null,
                        null, null, null), "place4", 3F, null, null, null)
        ));
    }

    public static List<PlaceAutocompletePrediction> getPlacePredictions() {

        return new ArrayList<>(Arrays.asList(
                new PlaceAutocompletePrediction("Description place 1", "place1"),
                new PlaceAutocompletePrediction("Description place 2", "place2")
        ));
    }

    public static MapPlace getMapPlace() {

        return new MapPlace(null, "72 rue machin", null, getGeometry(),
                null, null, "Place 1", new PlaceOpeningHours(false, null,
                null, null, null), "place1", 4F, null, null, null);
    }

    public static Geometry getGeometry() {

        return new Geometry(new LatLngLiteral(1.0, 2.0), new Bounds(new LatLngLiteral(1.0, 2.0), new LatLngLiteral(1.0, 2.0)));
    }

    public static Location getLocation(double latitude, double longitude) {
        Location currentLocation = mock();
        Mockito.when(currentLocation.getLatitude()).thenReturn(latitude);
        Mockito.when(currentLocation.getLongitude()).thenReturn(longitude);
        return currentLocation;
    }
}
