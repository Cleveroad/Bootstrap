package com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth.network

import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth.network.clients.LinkedInClient

object NetworkModule {

    val client by lazy { LinkedInClient() }
}