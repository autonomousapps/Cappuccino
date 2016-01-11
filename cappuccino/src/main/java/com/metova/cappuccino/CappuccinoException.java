package com.metova.cappuccino;

import android.support.annotation.NonNull;

public class CappuccinoException extends RuntimeException {

    public CappuccinoException() {

    }

    public CappuccinoException(@NonNull String message) {
        super(message);
    }

    public CappuccinoException(@NonNull Throwable cause) {
        super(cause);
    }

    public CappuccinoException(@NonNull String message, @NonNull Throwable cause) {
        super(message, cause);
    }
}