package io.imulab.astrea.service.flow.code.persistence

import io.imulab.astrea.sdk.commons.doNotCall
import io.imulab.astrea.sdk.oauth.request.OAuthRequest
import io.imulab.astrea.sdk.oauth.token.storage.AccessTokenRepository

/**
 * NoOp implementation of [AccessTokenRepository]. Since we are implementing JWT bearer token type. There is no need
 * to save state for introspection.
 */
object NoOpAccessTokenRepository : AccessTokenRepository {
    override suspend fun createAccessTokenSession(token: String, request: OAuthRequest) {}  // no-op
    override suspend fun getAccessTokenSession(token: String): OAuthRequest = doNotCall()
    override suspend fun deleteAccessTokenSession(token: String) = doNotCall()
    override suspend fun deleteAccessTokenAssociatedWithRequest(requestId: String) = doNotCall()
}