package com.github.pjozsef.slack.emoji.tracker.data

import com.github.pjozsef.slack.emoji.tracker.model.json
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.FindOptions
import io.vertx.ext.mongo.UpdateOptions

data class UpdateCommand(val query: JsonObject,
                    val update: JsonObject,
                    val findOptions: FindOptions,
                    val updateOptions: UpdateOptions) {
    companion object {
        fun of(user: String, emojis: Map<String, Int>): UpdateCommand {
            val query = json { "user" to user }
            val update = json {
                "\$inc" to json {
                    emojis.forEach { (key, value) ->
                        "emojis.$key" to value
                    }
                }
            }
            val findOptions = FindOptions()
            val updateOptions = UpdateOptions().setUpsert(true).setReturningNewDocument(true)

            return UpdateCommand(query, update, findOptions, updateOptions)
        }
    }
}