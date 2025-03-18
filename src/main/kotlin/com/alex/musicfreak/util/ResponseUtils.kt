package com.alex.musicfreak.util

import com.alex.musicfreak.repository.api.ApiModelError
import jakarta.ws.rs.core.Response

fun ok(entity: Any) = Response.ok(entity).build()

fun created(entity: Any) = Response.status(Response.Status.CREATED).entity(entity).build()

fun noContent() = Response.noContent().build()

fun badRequest(message: String) = Response.status(Response.Status.BAD_REQUEST).entity(ApiModelError(Response.Status.BAD_REQUEST.statusCode, message)).build()