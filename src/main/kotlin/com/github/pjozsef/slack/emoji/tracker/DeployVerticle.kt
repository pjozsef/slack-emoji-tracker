package com.github.pjozsef.slack.emoji.tracker

import com.diabolicallabs.vertx.cron.CronEventSchedulerVertical
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.pjozsef.slack.emoji.tracker.data.DataVerticle
import com.github.pjozsef.slack.emoji.tracker.pairschedule.PairScheduleVerticle
import com.github.pjozsef.slack.emoji.tracker.util.handleAsyncResult
import com.github.pjozsef.slack.emoji.tracker.web.WebVerticle
import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.json.Json
import io.vertx.core.logging.LoggerFactory

class DeployVerticle : AbstractVerticle() {

    val log = LoggerFactory.getLogger(this::class.java)

    override fun start() {
        Json.mapper.registerModule(KotlinModule())
        Json.prettyMapper.registerModule(KotlinModule())

        vertx.deployVerticle(
                WebVerticle(),
                DeploymentOptions().setConfig(config().getJsonObject("web")))

        vertx.deployVerticle(
                DataVerticle(),
                DeploymentOptions().setConfig(config().getJsonObject("data")))

        vertx.deployVerticle(
                CronEventSchedulerVertical(),
                DeploymentOptions().setConfig(config().getJsonObject("pairschedule"))) {
            handleAsyncResult(it, log) {
                vertx.deployVerticle(
                        PairScheduleVerticle(),
                        DeploymentOptions().setConfig(config().getJsonObject("pairschedule")))
            }
        }
    }
}