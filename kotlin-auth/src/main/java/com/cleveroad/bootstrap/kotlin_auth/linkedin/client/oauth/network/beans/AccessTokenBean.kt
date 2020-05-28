package com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth.network.beans

import com.fasterxml.jackson.annotation.JsonProperty

data class AccessTokenBean(@JsonProperty("access_token")
                           val accessToken: String,
                           @JsonProperty("expires_in")
                           val expiresIn: Long)