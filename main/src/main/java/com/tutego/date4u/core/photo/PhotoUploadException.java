package com.tutego.date4u.core.photo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value  = HttpStatus.BAD_REQUEST,
        reason = "Error while Uploading" )
public class PhotoUploadException extends RuntimeException{
}
