package com.endava.ticketsmobile.data.services.util;

import com.endava.ticketsmobile.data.model.ErrorResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class TicketsErrorHandler {
    public static String getErrorMessageFromResponse(Response<?> response) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        try (ResponseBody responseBody = response.errorBody()) {
            if (responseBody != null) {
                try {
                    ErrorResponse errorResponse = gson.fromJson(responseBody.string(), ErrorResponse.class);

                    return errorResponse.getErrorMessage();
                } catch (IOException e) {
                    return null;
                }
            }
        }

        return null;
    }
}
