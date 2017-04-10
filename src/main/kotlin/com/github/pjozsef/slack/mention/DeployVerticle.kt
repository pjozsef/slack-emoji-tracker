package com.github.pjozsef.slack.mention

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.pjozsef.slack.mention.web.WebVerticle
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.Json

class DeployVerticle: AbstractVerticle() {
    override fun start() {
        Json.mapper.registerModule(KotlinModule())
        Json.prettyMapper.registerModule(KotlinModule())
        vertx.deployVerticle(WebVerticle())
    }
}