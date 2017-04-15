package com.github.pjozsef.slack.emoji.tracker

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.pjozsef.slack.emoji.tracker.data.DataVerticle
import com.github.pjozsef.slack.emoji.tracker.web.WebVerticle
import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.json.Json

class DeployVerticle : AbstractVerticle() {
    override fun start() {
        Json.mapper.registerModule(KotlinModule())
        Json.prettyMapper.registerModule(KotlinModule())
        vertx.deployVerticle(
                WebVerticle(),
                DeploymentOptions().setConfig(config().getJsonObject("web")))
        vertx.deployVerticle(
                DataVerticle(),
                DeploymentOptions().setConfig(config().getJsonObject("data")))
    }
}