package org.example.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class FieldExceptionDetails extends ExceptionDetails {
    protected final String[] field;
}
