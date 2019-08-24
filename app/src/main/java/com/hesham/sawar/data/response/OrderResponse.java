package com.hesham.sawar.data.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hesham.sawar.data.model.OrderPojo;
import com.hesham.sawar.data.model.PaperPojo;

import java.util.ArrayList;

public class OrderResponse {

    public final boolean status;

    @Nullable
    public final ArrayList<OrderPojo> data;

    @Nullable
    private final String message;

    private OrderResponse(@NonNull boolean status, @Nullable ArrayList<OrderPojo> data, @Nullable String message) {
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