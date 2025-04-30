package org.acme.exceptions;

import jakarta.json.bind.annotation.JsonbTransient; // Import JsonbTransient
import java.util.Map;

public class ConsignDatailsException extends Exception {
    private final Map<String, String> errorDetails;

    public ConsignDatailsException(String message, Throwable cause) {
        super(message, cause);
        this.errorDetails = Map.of("error", message, "details", cause != null ? cause.getMessage() : "N/A");
    }

    public Map<String, String> getErrorDetails() {
        return errorDetails;
    }

    @JsonbTransient // Exclude the cause from JSON serialization
    @Override
    public Throwable getCause() {
        return super.getCause();
    }
}