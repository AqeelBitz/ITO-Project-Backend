// package exception/GenericExceptionMapper.java
package exception;

import dto.ApiResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger logger = LoggerFactory.getLogger(GenericExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        int statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String message = "An internal server error occurred.";

        // You can customize based on exception type if needed
        if (exception instanceof WebApplicationException) {
            // Use status code from JAX-RS specific exceptions if available
            statusCode = ((WebApplicationException) exception).getResponse().getStatus();
            // You might want a more specific message for known web exceptions
            // Be cautious about leaking internal details from exception.getMessage()
        }
        // Log the full error for internal diagnostics
        logger.error("Unhandled exception caught by mapper: {}", exception.getMessage(), exception);

        ApiResponseDTO<Object> errorResponse = new ApiResponseDTO<>(
                statusCode,
                message,
                false,
                null);

        return Response.status(statusCode)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}