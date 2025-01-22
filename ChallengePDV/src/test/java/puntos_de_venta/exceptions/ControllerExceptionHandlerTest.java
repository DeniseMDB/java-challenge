package puntos_de_venta.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerExceptionHandlerTest {

    @InjectMocks
    private ControllerExceptionHandler exceptionHandler;

    @Test
    void shouldHandleGlobalException() {
        // Arrange
        Exception exception = new Exception("Test exception");
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("Test request description");

        // Act
        ErrorMessage response = exceptionHandler.globalExceptionHandler(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
        assertEquals("Test exception", response.getMessage());
        assertEquals("Test request description", response.getDescription());
    }
}
