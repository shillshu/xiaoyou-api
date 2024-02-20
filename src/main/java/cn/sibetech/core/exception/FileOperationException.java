package cn.sibetech.core.exception;

public class FileOperationException extends RuntimeException{
	
	public FileOperationException(){
		super();
	}
	
	public FileOperationException(String message) {
		super(message);
	}
	
	public FileOperationException(String message, Throwable t) {
		super(message, t);
	}
}
