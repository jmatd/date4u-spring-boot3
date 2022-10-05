package com.tutego.date4u.core.photo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value  = HttpStatus.NOT_FOUND,
                 reason = "No such photo" )
class PhotoNotFoundException extends RuntimeException {
}