
package com.quartercode.nodemapper.ui;

@SuppressWarnings ("serial")
public class GeneralException extends Exception {

    public GeneralException(final String message) {

        super(message);
    }

    @Override
    public String toString() {

        return getLocalizedMessage();
    }

}
