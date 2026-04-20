package com.project.orderinventorymanagement;

import com.project.orderinventorymanagement.customerservice.exception.CustomerDeleteException;
import com.project.orderinventorymanagement.customerservice.exception.CustomerNotFoundException;
import com.project.orderinventorymanagement.customerservice.exception.CustomerUpdateException;
import com.project.orderinventorymanagement.customerservice.exception.InvalidCustomerDataException;
import com.project.orderinventorymanagement.orderservice.exception.OrderNotFoundException;
import com.project.orderinventorymanagement.productservice.exception.ProductNotFoundException;
import com.project.orderinventorymanagement.shippingservice.exception.ShipmentNotFoundException;
import com.project.orderinventorymanagement.storeservice.exception.InsufficientStockException;
import com.project.orderinventorymanagement.storeservice.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleCustomerNotFound() {
        ResponseEntity<ErrorResponse> response = handler.handle(new CustomerNotFoundException("MSG"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("MSG");
    }

    @Test
    void handleOrderNotFound() {
        ResponseEntity<ErrorResponse> response = handler.handleOrderNotFound(new OrderNotFoundException("MSG"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void handleProductNotFound() {
        ResponseEntity<ErrorResponse> response = handler.handleProduct(new ProductNotFoundException("MSG"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void handleInsufficientStock() {
        ResponseEntity<ErrorResponse> response = handler.handle(new InsufficientStockException("MSG"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void handleDataIntegrity() {
        ResponseEntity<ErrorResponse> response = handler.handleDataIntegrity(new DataIntegrityViolationException("MSG"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleIllegalArgument() {
        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgument(new IllegalArgumentException("MSG"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleAuthenticationException() {
        ResponseEntity<ErrorResponse> response = handler.handleAuthenticationException(new BadCredentialsException("MSG"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void handleGeneralException() {
        ResponseEntity<ErrorResponse> response = handler.handleGeneral(new Exception("MSG"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void handleCustomerUpdateException() {
        ResponseEntity<ErrorResponse> response = handler.handleUpdateError(new CustomerUpdateException("MSG"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleCustomerDeleteException() {
        ResponseEntity<ErrorResponse> response = handler.handleDeleteError(new CustomerDeleteException("MSG"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void handleInvalidCustomerData() {
        ResponseEntity<ErrorResponse> response = handler.handleInvalidData(new InvalidCustomerDataException("MSG"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleShipmentNotFound() {
        ResponseEntity<ErrorResponse> response = handler.handleShipment(new ShipmentNotFoundException("MSG"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void handleResourceNotFound() {
        ResponseEntity<ErrorResponse> response = handler.handleResourceNotFound(new ResourceNotFoundException("MSG"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void handleTypeMismatch() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getValue()).thenReturn("VAL");
        when(ex.getName()).thenReturn("PARAM");
        when(ex.getRequiredType()).thenAnswer(i -> String.class);
        ResponseEntity<ErrorResponse> response = handler.handleTypeMismatch(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void handleNoResourceFound() {
        NoResourceFoundException ex = mock(NoResourceFoundException.class);
        when(ex.getResourcePath()).thenReturn("PATH");
        ResponseEntity<ErrorResponse> response = handler.handleNoResourceFound(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void handleWrongMethod() {
        HttpRequestMethodNotSupportedException ex = mock(HttpRequestMethodNotSupportedException.class);
        when(ex.getMethod()).thenReturn("POST");
        ResponseEntity<ErrorResponse> response = handler.handleWrongMethod(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    }
}
