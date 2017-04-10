package com.github.pjozsef.slack.mention.util

import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject

fun Any.asJsonObject(): JsonObject
        = Json.mapper.convertValue(this, JsonObject::class.java)

inline fun <reified T> Any.asObject(): T
        = Json.mapper.convertValue(this, T::class.java)