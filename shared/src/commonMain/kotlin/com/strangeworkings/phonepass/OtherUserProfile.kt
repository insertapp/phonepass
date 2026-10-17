package com.strangeworkings.phonepass

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import kotlinx.serialization.encodeToString
import kotlin.collections.iterator

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString())
    }
}

@Serializable
class OtherUserProfile: Profiles {
    var timesSeen: Int
    @Serializable(with = LocalDateTimeSerializer::class)
    var lastPassTime: LocalDateTime

    constructor(name: String, pronouns: String, bio: String, timesSeen: Int, lastPassTime: LocalDateTime) : super(name,pronouns,bio) {
        this.timesSeen = timesSeen
        this.lastPassTime = lastPassTime
    }

}