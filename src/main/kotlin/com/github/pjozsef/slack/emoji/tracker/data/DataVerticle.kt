package com.github.pjozsef.slack.emoji.tracker.data

import com.github.pjozsef.slack.emoji.tracker.model.EventBusAddress
import com.github.pjozsef.slack.emoji.tracker.model.UserUpdateMessage
import com.github.pjozsef.slack.emoji.tracker.util.asObject
import com.github.pjozsef.slack.emoji.tracker.util.handleAsyncResult
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.mongo.MongoClient

class DataVerticle : AbstractVerticle() {
    val log = LoggerFactory.getLogger(this::class.java)

    override fun start() {
        val mongoConfig = JsonObject()
                .put("db_name", config().getString("mongo.db"))
                .put("connection_string", config().getString("mongo.url"))
        val mongo = MongoClient.createShared(vertx, mongoConfig)

        vertx.eventBus().consumer<JsonObject>(EventBusAddress.USER_UPDATE) { message ->
            val content = message.body().asObject<UserUpdateMessage>()
            val (user, emojis) = content.userInfo
            val (query, update, findOptions, updateOptions) = UpdateCommand.of(user, emojis)
            mongo.findOneAndUpdateWithOptions(content.team, query, update, findOptions, updateOptions) { result ->
                handleAsyncResult(result, log) { reply ->
                    message.reply(reply)
                }
            }
        }
    }
}