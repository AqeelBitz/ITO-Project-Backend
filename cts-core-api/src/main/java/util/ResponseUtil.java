// package util/ResponseUtil.java
package util;

import dto.ApiResponseDTO;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class ResponseUtil {

    public static <T> Response buildSuccessResponse(T data) {
        return buildSuccessResponse(data, "Operation successful.");
    }

    public static <T> Response buildSuccessResponse(T data, String message) {
         ApiResponseDTO<T> successResponse = new ApiResponseDTO<>(
                 Response.Status.OK.getStatusCode(),
                 message,
                 true,
                 data);
         return Response.ok(successResponse).build();
    }

    public static <T> ApiResponseDTO<T> buildErrorDTO(int status, String message) {
        return new ApiResponseDTO<>(status, message, false, null);
    }

     public static Response buildErrorResponse(Response.Status status, String message) {
        ApiResponseDTO<Object> errorResponse = buildErrorDTO(status.getStatusCode(), message);
        return Response.status(status).entity(errorResponse).type(MediaType.APPLICATION_JSON).build();
     }

     public static Response buildNotFoundResponse(String resourceName) {
        return buildErrorResponse(Response.Status.NOT_FOUND, resourceName + " not found.");
     }
}