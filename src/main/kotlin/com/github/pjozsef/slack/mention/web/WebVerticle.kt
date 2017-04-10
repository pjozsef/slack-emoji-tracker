package com.github.pjozsef.slack.mention.web

import io.vertx.core.AbstractVerticle
import io.vertx.core.logging.LoggerFactory

class WebVerticle : AbstractVerticle() {

    val log = LoggerFactory.getLogger(this::class.java)

    override fun start() {
        vertx.createHttpServer()
                .requestHandler { WebRouter.create(vertx).accept(it) }
                .listen(config().getInteger("http.port", 9000))
    }
}