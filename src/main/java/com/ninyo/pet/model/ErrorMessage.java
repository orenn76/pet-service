package com.ninyo.pet.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@JsonPropertyOrder({"timestamp", "status", "error", "exception", "message", "path"})
@Value
@Builder
public class ErrorMessage implements Serializable {

    private static final long serialVersionUID = -8042087345155849600L;

    String timestamp;
    int status;
    String error;
    String exception;
    String message;
    String path;
}