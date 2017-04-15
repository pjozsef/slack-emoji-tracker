package com.github.pjozsef.slack.emoji.tracker.util

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.pjozsef.slack.emoji.tracker.model.UserInfo
import com.github.pjozsef.slack.emoji.tracker.model.UserUpdateMessage
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ConversionUtilsKtTest {
    @Before
    fun initialize(){
        Json.mapper.registerModule(KotlinModule())
        Json.prettyMapper.registerModule(KotlinModule())
    }
    @Test
    fun asJsonObject() {
        val input = UserUpdateMessage(
                "team",
                UserInfo("user", mutableMapOf(":frog:" to 3, ":thumbsup:" to 1)))

        val expected = JsonObject()
                .put("team", "team")
                .put("userInfo", JsonObject()
                        .put("user", "user")
                        .put("emojis", JsonObject()
                                .put(":frog:", 3)
                                .put(":thumbsup:", 1))
                        )
        val result = input.asJsonObject()

        assertEquals(expected, result)
    }

    @Test
    fun asObject(){
        val input = JsonObject()
                .put("team", "team")
                .put("userInfo", JsonObject()
                        .put("user", "user")
                        .put("emojis", JsonObject()
                                .put(":frog:", 3)
                                .put(":thumbsup:", 1))
                )
        val expected = UserUpdateMessage(
                "team",
                UserInfo("user", mutableMapOf(":frog:" to 3, ":thumbsup:" to 1)))
        val result = input.asObject<UserUpdateMessage>()

        assertEquals(expected, result)
    }
}