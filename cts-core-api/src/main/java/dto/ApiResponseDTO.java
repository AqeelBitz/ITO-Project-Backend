package dto;

public class ApiResponseDTO<T> {

    private int status;
    private String message;
    private boolean success;
    private T data;

    public ApiResponseDTO(int status, String message, boolean success, T data) {
        this.status = status;
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
