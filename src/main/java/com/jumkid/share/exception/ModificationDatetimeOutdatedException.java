package com.jumkid.share.exception;

public class ModificationDatetimeOutdatedException extends Exception{
    private static final String ERROR = "Data is outdated.";

    public ModificationDatetimeOutdatedException() { super(ERROR); }
}
