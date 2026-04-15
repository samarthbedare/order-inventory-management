package com.project.orderinventorymanagement.storeservice.exception;

public class InsufficientStockException extends RuntimeException {
    private static final long serialVersionUID = 2L;

    public InsufficientStockException(String message) {
        super(message);
    }

    public static class ResourceNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public ResourceNotFoundException(String message) {

            super(message);
        }
    }
}
