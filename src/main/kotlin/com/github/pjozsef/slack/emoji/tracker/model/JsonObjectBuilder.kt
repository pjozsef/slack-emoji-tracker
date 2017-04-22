package com.github.pjozsef.slack.emoji.tracker.model

import io.vertx.core.json.JsonObject

class JsonObjectBuilder {
    val json = JsonObject()

    infix fun <B> String.to(other: B) {
        json.put(this, other)
    }
}

fun json(init: JsonObjectBuilder.() -> Unit) = JsonObjectBuilder().apply(init).json
