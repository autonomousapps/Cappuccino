package com.metova.capuccino;

import android.support.annotation.NonNull;

public class CapuccinoException extends RuntimeException {

    public CapuccinoException() {

    }

    public CapuccinoException(@NonNull String message) {
        super(message);
    }

    public CapuccinoException(@NonNull Throwable cause) {
        super(cause);
    }

    public CapuccinoException(@NonNull String message, @NonNull Throwable cause) {
        super(message, cause);
    }
}