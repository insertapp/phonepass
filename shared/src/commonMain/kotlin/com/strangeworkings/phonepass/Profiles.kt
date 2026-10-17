package com.strangeworkings.phonepass

class Profiles {
    var name: String
    var pronouns: String
    var bio: String

    var collectedProfiles: List<Pair<Profiles, Int>>

    constructor(name: String, pronouns: String, bio: String) {
        this.name = name
        this.pronouns = pronouns
        this.bio = bio
        collectedProfiles = emptyList()
    }

    override fun toString(): String {
        return name + " " + pronouns + " " + bio
    }

    fun setthisName(name: String){
        this.name = name
    }

    fun setthisPronouns(pronouns: String){
        this.pronouns = pronouns
    }

    fun setthisBio(bio: String){
        this.bio = bio
    }
}
