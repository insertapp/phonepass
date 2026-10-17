package com.strangeworkings.phonepass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlin.collections.iterator





@Serializable
abstract sealed class Profiles {
    var name: String
    var pronouns: String
    var bio: String



    //var collectedProfiles: HashMap<Profiles, Int>

    constructor(name: String, pronouns: String, bio: String) {
        this.name = name

        this.pronouns = pronouns
        this.bio = bio

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
