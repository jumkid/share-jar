package com.jumkid.share.exception;

public class ModificationDatetimeOutdatedException extends RuntimeException{
    private static final String ERROR = "Data is outdated.";

    public ModificationDatetimeOutdatedException() { super(ERROR); }
}
