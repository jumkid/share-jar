package com.jumkid.share.exception;

public class ModificationDatetimeOutdatedException extends RuntimeException{
    private static final String ERROR = "Vehicle data is outdated.";

    public ModificationDatetimeOutdatedException() { super(ERROR); }
}
