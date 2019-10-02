package com.hesham.sawar.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.hesham.sawar.data.model.UserPojo;

import java.util.List;

public class CenterResponse {
    public final boolean status;

    @Nullable
    @SerializedName("data")
    public final List<UserPojo> data;

    @Nullable
    private final String message;

    private CenterResponse(@NonNull boolean status, @Nullable List<UserPojo> data, @Nullable String message) {
        this.status = status;
        this.data = data;
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
