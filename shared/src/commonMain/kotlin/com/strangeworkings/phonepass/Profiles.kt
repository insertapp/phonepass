package com.strangeworkings.phonepass

class Profiles {
    var name: String
    var pronouns: String
    var bio: String

    constructor(name: String, pronouns: String, bio: String) {
        this.name = name
        this.pronouns = pronouns
        this.bio = bio
    }

    override fun toString(): String {
        return name + " " + pronouns + " " + bio
    }
}
