package org.andulir.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AndulirSystemException extends RuntimeException {
    private String msg;
}
