package ru.vsultanyarov.nearby_backend.exception;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public abstract class LogException extends RuntimeException {
    protected void logMessage(@NonNull String logMessage, Object... arguments) {
        log.error(logMessage, arguments);
    }
}
