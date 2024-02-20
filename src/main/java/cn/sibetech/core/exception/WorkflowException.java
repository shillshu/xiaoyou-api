package cn.sibetech.core.exception;

public class WorkflowException extends RuntimeException {
    public WorkflowException() {
        super();
    }

    public WorkflowException(String message) {
        super(message);
    }
}
