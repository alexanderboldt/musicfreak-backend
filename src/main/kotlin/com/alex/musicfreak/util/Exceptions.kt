package com.alex.musicfreak.util

import com.alex.musicfreak.resource.Answer
import jakarta.ws.rs.WebApplicationException

class BadRequestException : WebApplicationException(Answer.badRequest())
