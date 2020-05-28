package com.cleveroad.bootstrap.kotlin_auth.facebook

enum class FacebookPermission(private val value: String) {
    PUBLIC_PROFILE("public_profile"),
    USER_FRIENDS("user_friends"),
    USER_PHOTOS("user_photos"),
    USER_VIDEOS("user_videos"),
    ABOUT_ME("user_about_me"),
    LOCATION("user_location"),
    USER_BIRTHDAY("user_birthday"),
    EMAIL("email");

    operator fun invoke() = value
}
