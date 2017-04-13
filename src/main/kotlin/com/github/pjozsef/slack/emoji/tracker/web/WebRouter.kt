package com.github.pjozsef.slack.emoji.tracker.web

import com.github.pjozsef.slack.emoji.tracker.web.handler.HealthCheckHandler
import com.github.pjozsef.slack.emoji.tracker.web.handler.SlackEventHandler
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.LoggerHandler

class WebRouter {
    val log = LoggerFactory.getLogger(this::class.java)

    companion object {
        fun create(vertx: Vertx, config: JsonObject): Router = WebRouter().create(vertx, config)
    }

    fun create(vertx: Vertx, config: JsonObject) = Router.router(vertx).apply {
        route("/*").handler(LoggerHandler.create())
        post("/*").handler(BodyHandler.create())
        get("/healthcheck").handler(HealthCheckHandler())
        post("/slack/:team").handler(SlackEventHandler(vertx, config.getString("slack.webhookurl")))
    }
}