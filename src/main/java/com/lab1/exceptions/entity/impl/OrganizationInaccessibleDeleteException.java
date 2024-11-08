package com.lab1.exceptions.entity.impl;

import com.lab1.exceptions.entity.EntityInaccessibleDeleteException;

public class OrganizationInaccessibleDeleteException extends EntityInaccessibleDeleteException {
    public OrganizationInaccessibleDeleteException(String message) {
        super(message);
    }

    public OrganizationInaccessibleDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
