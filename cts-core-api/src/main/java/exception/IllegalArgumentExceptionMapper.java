// package exception/IllegalArgumentExceptionMapper.java
package exception;

import dto.ApiResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    private static final Logger logger = LoggerFactory.getLogger(IllegalArgumentExceptionMapper.class);

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        String message = exception.getMessage() != null ? exception.getMessage()
                : "Invalid request parameter provided.";
        logger.warn("Bad Request mapping: {}", message);

        ApiResponseDTO<Object> errorResponse = new ApiResponseDTO<>(
                Response.Status.BAD_REQUEST.getStatusCode(),
                message,
                false,
                null);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}