package test.aop.testaop.exception;

import lombok.Getter;

@Getter
public class VoidException extends Exception {

    private final boolean internal;

    public VoidException(String exception, boolean internal) {
        super(exception);
        this.internal = internal;
    }
}
