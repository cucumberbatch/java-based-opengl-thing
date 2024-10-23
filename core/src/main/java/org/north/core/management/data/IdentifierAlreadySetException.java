package org.north.core.management.data;

public class IdentifierAlreadySetException extends RuntimeException {
    public IdentifierAlreadySetException() {
        super("A unique identifier already set-up for that object!");
    }
}
