package com.kazeneko.RequestQueue;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CustomResponseError {
    private final LocalDateTime dateTime;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
}
