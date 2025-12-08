package org.musiccollection.service

import io.quarkus.security.identity.SecurityIdentity
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserService(private val securityIdentity: SecurityIdentity) {

    val userId: String
        get() = securityIdentity.principal.name
}
