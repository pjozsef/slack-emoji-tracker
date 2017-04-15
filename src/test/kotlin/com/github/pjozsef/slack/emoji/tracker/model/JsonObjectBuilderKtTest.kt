package com.github.pjozsef.slack.emoji.tracker.model

import io.vertx.core.json.JsonObject
import org.junit.Assert.*
import org.junit.Test

class JsonObjectBuilderKtTest {
    @Test
    fun emptyJson() {
        assertEquals(JsonObject(), json {  })
    }

    @Test
    fun nestedJsonTest() {
        val expected = JsonObject()
                .put("key1", "val1")
                .put("key2", JsonObject()
                        .put("key3", "val3")
                        .put("key4", 4)
                        .put("key5", listOf(1,2,3,4,5)))

        val result = json {
            "key1" to "val1"
            "key2" to json {
                "key3" to "val3"
                "key4" to 4
                "key5" to listOf(1,2,3,4,5)
            }
        }

        assertEquals(expected, result)
    }
}