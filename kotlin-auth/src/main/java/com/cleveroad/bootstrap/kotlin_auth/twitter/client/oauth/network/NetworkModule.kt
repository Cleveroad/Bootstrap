package com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.network

import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.network.clients.TwitterClient

object NetworkModule {

    val client by lazy { TwitterClient() }
}