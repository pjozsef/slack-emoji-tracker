package com.github.pjozsef.slack.emoji.tracker.web.handler

import com.github.pjozsef.slack.emoji.tracker.model.EventBusAddress
import com.github.pjozsef.slack.emoji.tracker.model.UserInfo
import com.github.pjozsef.slack.emoji.tracker.model.UserUpdateMessage
import com.github.pjozsef.slack.emoji.tracker.util.asJsonObject
import com.github.pjozsef.slack.emoji.tracker.util.asObject
import com.github.pjozsef.slack.emoji.tracker.util.handleAsyncResult
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient

class SlackEventHandler(val vertx: Vertx, val config: JsonObject) : Handler<RoutingContext> {
    val eb = vertx.eventBus()
    val log = LoggerFactory.getLogger(this::class.java)
    val client = WebClient.create(vertx)

    override fun handle(ctx: RoutingContext) {
        if (ctx.bodyAsJson.containsKey("challenge")) {
            handleVerification(ctx)
        } else {
            handleEvent(ctx)
        }
    }

    private fun handleVerification(ctx: RoutingContext) {
        val json = JsonObject()
                .put("challenge", ctx.bodyAsJson.getString("challenge"))
        ctx.response().end(json.encode())
    }

    private fun handleEvent(ctx: RoutingContext) {
        val team = ctx.pathParam("team")
        config.getString(team)?.let { webhook ->
            val body = ctx.bodyAsJson
            log.info(body.encodePrettily())
            ctx.response().end()

            val event = body.getJsonObject("event")
            if (!event.containsKey("bot_id")) {
                val text = event.getString("text")
                UserInfo.of(text)?.let { userInfo ->
                    if(userInfo.emojis.isNotEmpty()){
                        val jsonMessage = UserUpdateMessage(team, userInfo).asJsonObject()
                        eb.send<JsonObject>(EventBusAddress.USER_UPDATE, jsonMessage) { result ->
                            handleAsyncResult(result, log) { message ->
                                val updatedUserInfo = message.body().asObject<UserInfo>()
                                val replyText = makeReplyText(updatedUserInfo)
                                val payload = JsonObject().put("text", replyText)
                                client.post(443, "hooks.slack.com", webhook)
                                        .ssl(true)
                                        .sendJsonObject(payload) { result ->
                                            handleAsyncResult(result, log)
                                        }
                            }
                        }
                    }
                }
            }
        } ?: let {
            log.warn("Unconfigured team: $team")
        }
    }

    private fun makeReplyText(updatedUserInfo: UserInfo): String {
        val emojis = updatedUserInfo.emojis
                .map { it.key to it.value }
                .sortedByDescending { it.second }
                .map { "${it.first}: ${it.second}" }
                .joinToString(", ")
        return "${updatedUserInfo.user} has $emojis"

    }
}