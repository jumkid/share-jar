package com.jumkid.share.exception;

public class ModificationDatetimeNotFoundException extends RuntimeException{
    private static final String ERROR = "Modification datetime is missing.";

    public ModificationDatetimeNotFoundException() { super(ERROR); }
}
