package com.hesham.sawar.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hesham.sawar.data.model.OrderDetailsPojo;
import com.hesham.sawar.data.model.OrderPojo;

import java.util.ArrayList;

public class DetailsResponse {

    public final boolean status;

    @Nullable
    public final ArrayList<OrderDetailsPojo> data;

    @Nullable
    private final String message;

    private DetailsResponse(@NonNull boolean status, @Nullable ArrayList<OrderDetailsPojo> data, @Nullable String message) {
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