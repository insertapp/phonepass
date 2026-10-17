package com.strangeworkings.phonepass

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform