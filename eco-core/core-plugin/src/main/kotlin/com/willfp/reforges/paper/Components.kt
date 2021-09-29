package com.willfp.reforges.paper

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

private val serializer = LegacyComponentSerializer.builder()
    .hexColors()
    .useUnusualXRepeatedCharacterHexFormat()
    .character('§')
    .build()


fun String.toComponent(): Component {
    return serializer.deserialize(this)
}

fun Component.toBukkit(): String {
    return serializer.serialize(this)
}
