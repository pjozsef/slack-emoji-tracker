package com.github.pjozsef.slack.emoji.tracker.model

import io.vertx.core.json.JsonObject

class JsonObjectBuilder {
    val json = JsonObject()

    infix fun <B> String.to(other: B) {
        json.put(this, other)
    }
}

fun json(init: JsonObjectBuilder.() -> Unit) = JsonObjectBuilder().apply(init).json

fun main(args: Array<String>) {
    val noBuilder = JsonObject()
            .put("key1", "val1")
            .put("key2", JsonObject()
                    .put("key3", "val3")
                    .put("key4", 4)
                    .put("key5", listOf(1, 2, 3, 4, 5)))

    val json = json {
        "key1" to "val1"
        "key2" to json {
            "key3" to "val3"
            "key4" to 4
            "key5" to listOf(1, 2, 3, 4, 5)
        }
    }
    println("Without builder: \n" + noBuilder.encodePrettily())
    println("With builder: \n" + json.encodePrettily())
}