package com.alex.musicfreak.util

import com.alex.musicfreak.repository.api.ApiModelError
import jakarta.ws.rs.core.Response

fun badRequest(message: String): Response {
    return Response.status(Response.Status.BAD_REQUEST).entity(ApiModelError(Response.Status.BAD_REQUEST.statusCode, message)).build()
}