package at.steell.mystuff.application.configuration;

import at.steell.mystuff.domain.exception.NotAvailable;
import at.steell.mystuff.domain.exception.NotReadable;
import at.steell.mystuff.domain.interactor.AssetInteractor.AssetNotFound;
import at.steell.mystuff.domain.interactor.AssetInteractor.NoOwner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class RestExceptionAdvice {

    @ExceptionHandler(AssetNotFound.class)
    public <T extends AssetNotFound> ResponseEntity<String> handle(final T ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NotAvailable.class)
    public <T extends NotAvailable> ResponseEntity<String> handle(final T ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(NotReadable.class)
    public <T extends NotReadable> ResponseEntity<String> handle(final T ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(NoOwner.class)
    public <T extends NoOwner> ResponseEntity<String> handle(final T ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
