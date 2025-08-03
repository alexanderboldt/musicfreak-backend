package com.alex.musicfreak.exception

import com.alex.musicfreak.util.Answer
import jakarta.ws.rs.WebApplicationException

class BadRequestException : WebApplicationException(Answer.badRequest())
