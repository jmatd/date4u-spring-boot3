package com.tutego.date4u.core.profile;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value  = HttpStatus.NOT_FOUND,
                 reason = "No such profile" )
class ProfileNotFoundException extends RuntimeException {
}