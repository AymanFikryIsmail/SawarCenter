package com.hesham.sawar.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hesham.sawar.data.model.CategoryPojo;
import com.hesham.sawar.data.model.FilterPojo;

import java.util.ArrayList;

public class FilterResponse {

    public final boolean status;

    @Nullable
    public final ArrayList<FilterPojo> data;

    @Nullable
    private final String message;

    private FilterResponse(@NonNull boolean status, @Nullable ArrayList<FilterPojo> data, @Nullable String message) {
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