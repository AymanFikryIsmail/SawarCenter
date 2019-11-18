package com.hesham.sawar.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class CheckEmployeeResponse {
    public final boolean status;

    @Nullable
    @SerializedName("data")
    public final boolean data;

    @Nullable
    private final String message;

    private CheckEmployeeResponse(@NonNull boolean status, @Nullable boolean data, @Nullable String message) {
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
