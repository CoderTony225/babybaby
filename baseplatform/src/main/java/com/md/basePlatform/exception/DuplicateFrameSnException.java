package com.md.basePlatform.exception;

/**
 * 机身编号重复。
 */
public class DuplicateFrameSnException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateFrameSnException(String message) {
        super(message);
    }
}
