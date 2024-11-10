package com.lab1.exceptions.entity.impl;

import com.lab1.exceptions.entity.EntityInaccessibleDeleteException;

public class StaffInaccessibleDeleteException extends EntityInaccessibleDeleteException {
    public StaffInaccessibleDeleteException(String message) {
        super(message);
    }

    public StaffInaccessibleDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
