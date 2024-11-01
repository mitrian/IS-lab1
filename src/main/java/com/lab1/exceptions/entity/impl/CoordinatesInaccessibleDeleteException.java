package com.lab1.exceptions.entity.impl;

import com.lab1.exceptions.entity.EntityInaccessibleDeleteException;

public class CoordinatesInaccessibleDeleteException extends EntityInaccessibleDeleteException {
    public CoordinatesInaccessibleDeleteException(String message) {
        super(message);
    }

    public CoordinatesInaccessibleDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
