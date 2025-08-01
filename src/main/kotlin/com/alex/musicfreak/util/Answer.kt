package com.alex.musicfreak.util

import com.alex.musicfreak.domain.model.Error
import jakarta.ws.rs.core.Response

/**
 * Contains convenient functions to handle responses with different status-codes.
 */
object Answer {

    /**
     * Sends a response with status-code 200.
     *
     * @param entity The entity to show as a result.
     *
     * @return Returns the response.
     */
    fun ok(entity: Any): Response = Response.ok(entity).build()

    /**
     * Sends a response with status-code 201.
     *
     * @param entity The entity to show as a result.
     *
     * @return Returns the response.
     */
    fun created(entity: Any): Response = Response
        .status(Response.Status.CREATED)
        .entity(entity)
        .build()

    /**
     * Sends a response with status-code 204.
     *
     * @return Returns the response.
     */
    fun noContent(): Response = Response.noContent().build()

    /**
     * Sends a response with status-code 400.
     *
     * @return Returns the response.
     */
    fun badRequest(): Response = Response
        .status(Response.Status.BAD_REQUEST)
        .entity(Error(Response.Status.BAD_REQUEST.statusCode, "Invalid input"))
        .build()
}
