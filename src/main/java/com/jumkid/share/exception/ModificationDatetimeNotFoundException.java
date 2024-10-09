package com.jumkid.share.exception;

public class ModificationDatetimeNotFoundException extends Exception{
    private static final String ERROR = "Modification datetime is missing.";

    public ModificationDatetimeNotFoundException() { super(ERROR); }
}
