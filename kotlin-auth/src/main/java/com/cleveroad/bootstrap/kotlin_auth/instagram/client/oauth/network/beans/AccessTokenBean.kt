package com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.network.beans

import com.fasterxml.jackson.annotation.JsonProperty

data class AccessTokenBean(@JsonProperty("access_token")
                           val accessToken: String,
                           @JsonProperty("user_id")
                           val userId: Long)