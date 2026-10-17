package com.strangeworkings.phonepass

import kotlinx.serialization.Serializable

@Serializable
class UserProfile: Profiles {

    constructor(name: String, pronouns: String, bio: String) : super(name,pronouns,bio) {


    }
}