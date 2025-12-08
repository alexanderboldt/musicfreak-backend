package org.musiccollection.util

import org.musiccollection.resource.Answer
import jakarta.ws.rs.WebApplicationException

class BadRequestException : WebApplicationException(Answer.badRequest())
