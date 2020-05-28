package com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.network

import com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.network.clients.InstagramClient

object NetworkModule {

    val client by lazy { InstagramClient() }
}