package ru.vsultanyarov.nearby_backend.handler;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.vsultanyarov.nearby_backend.exception.LogException;
import ru.vsultanyarov.nearby_backend.mods.dto.Error;

import static org.springframework.util.StringUtils.hasText;

@RestControllerAdvice(basePackages = "ru.vsultanyarov.nearby_backend")
public class ApiExceptionHandler {

    @ExceptionHandler(LogException.class)
    public final ResponseEntity<Error> handleBusinessException(LogException ex) {
        BusinessError businessError = AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), BusinessError.class);
        Assert.notNull(businessError, "Can't find BusinessError annotation");
        return getResponseFromBusinessError(businessError);
    }

    private ResponseEntity<Error> getResponseFromBusinessError(BusinessError businessError) {
        if (hasText(businessError.errorCode()) || businessError.isEmptyErrorCode()) {
            Error err = new Error();
            err.setApplicationErrorCode(businessError.errorCode());
            err.setMessage(businessError.message());
            return new ResponseEntity<>(err, businessError.status());
        }
        return new ResponseEntity<>(businessError.status());
    }
}
