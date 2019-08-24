package com.hesham.sawar.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.hesham.sawar.data.model.FacultyPojo;
import com.hesham.sawar.data.model.UserPojo;

import java.util.ArrayList;

public class UserResponse {
    public final boolean status;

    @Nullable
    @SerializedName("data")
    public final int cc_id;

    @Nullable
    private final String message;

    private UserResponse(@NonNull boolean status, @Nullable int data, @Nullable String message) {
        this.status = status;
        this.cc_id = data;
        this.message = message;
    }
    public boolean getSuccess() {
        return status;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}
