package com.github.pjozsef.slack.emoji.tracker.util

import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject

fun Any.asJsonObject(): JsonObject
        = JsonObject.mapFrom(this)

inline fun <reified T> JsonObject.asObject(): T
        = Json.mapper.convertValue(this, T::class.java)