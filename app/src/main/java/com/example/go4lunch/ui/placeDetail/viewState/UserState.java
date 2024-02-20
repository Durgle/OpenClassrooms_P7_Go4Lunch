package com.example.go4lunch.ui.placeDetail.viewState;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class UserState {

    private final String id;
    private final Boolean like;
    private final Boolean choose;

    @Override
    public int hashCode() {
        return Objects.hash(id, like, choose);
    }

    public UserState(
            @NonNull String id,
            @Nullable Boolean like,
            @Nullable Boolean choose
    ) {
        this.id = id;
        this.like = like;
        this.choose = choose;
    }

    public String getId() {
        return id;
    }

    public Boolean isLike() {
        return like;
    }

    public Boolean isChoose() {
        return choose;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserState userState = (UserState) o;
        return Objects.equals(id, userState.id) && Objects.equals(like, userState.like) && Objects.equals(choose, userState.choose);
    }

    @NonNull
    @Override
    public String toString() {
        return "UserState{" +
                "id='" + id + '\'' +
                ", like=" + like +
                ", choose=" + choose +
                '}';
    }
}
