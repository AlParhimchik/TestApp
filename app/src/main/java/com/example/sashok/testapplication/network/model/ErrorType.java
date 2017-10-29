package com.example.sashok.testapplication.network.model;

import com.example.sashok.testapplication.R;

/**
 * Created by sashok on 26.10.17.
 */

public enum ErrorType {

    OK(0, true, 0),

    /**
     * Special error type for empty response
     */
    EMPTY_RESPONSE(Integer.MAX_VALUE - 1, true, R.string.network_error),

    /**
     * No internet connection
     */
    NO_INTERNET(Integer.MAX_VALUE - 2, true, R.string.network_no_internet_connection),

    /**
     * Endpoint not found
     */
    NO_ENDPOINT(Integer.MAX_VALUE - 3, true, R.string.network_no_endpoint),

    /**
     * Network error
     */
    NETWORK_ERROR(Integer.MAX_VALUE - 4, false, 0),

    /**
     * Unrecognized error. Treat as system
     */
    UNKNOWN(Integer.MAX_VALUE, true, R.string.network_error);

    public static ErrorType getType(int value) {
        ErrorType[] values = values();
        for (ErrorType errorType : values) {
            if (errorType.getCode() == value) {
                return errorType;
            }
        }
        return UNKNOWN;
    }

    private final int code;
    private final boolean system;
    private final int defaultErrorMessage;

    ErrorType(int code, boolean system, int message) {
        this.code = code;
        this.system = system;
        if (message == 0) {
            message = R.string.network_error;
        }
        this.defaultErrorMessage = message;
    }

    /**
     * Is error code has app-wide effect
     */
    public boolean isSystem() {
        return system;
    }

    /**
     * Default message to show in case of error
     */
    public int getDefaultErrorMessage() {
        return defaultErrorMessage;
    }

    /**
     * Status code
     */
    public int getCode() {
        return code;
    }
}
