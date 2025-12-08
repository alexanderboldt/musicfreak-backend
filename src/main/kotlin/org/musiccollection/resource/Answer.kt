package org.musiccollection.resource

import org.musiccollection.domain.Error
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.Response

/**
 * Contains convenient functions to handle responses with different status-codes.
 */
object Answer {

    /**
     * Sends a response with status-code 200.
     *
     * @param entity the entity to show as a result.
     * @return the response as a [jakarta.ws.rs.core.Response].
     */
    fun ok(entity: Any): Response = Response.ok(entity).build()

    /**
     * Sends a file-response with status-code 200.
     *
     * @param entity the entity to show as a result.
     * @param filename the filename as a [String].
     * @return the response as a [Response].
     */
    fun file(entity: Any, filename: String): Response = Response
        .ok(entity)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
        .build()

    /**
     * Sends a response with status-code 201.
     *
     * @param entity the entity to show as a result.
     * @return the response as a [Response].
     */
    fun created(entity: Any): Response = Response
        .status(Response.Status.CREATED)
        .entity(entity)
        .build()

    /**
     * Sends a response with status-code 204.
     *
     * @return the response as a [Response].
     */
    fun noContent(): Response = Response.noContent().build()

    /**
     * Sends a response with status-code 400.
     *
     * @return the response as a [Response].
     */
    fun badRequest(): Response = Response
        .status(Response.Status.BAD_REQUEST)
        .entity(Error(Response.Status.BAD_REQUEST.statusCode, "Invalid input"))
        .build()
}
