package com.github.pjozsef.slack.emoji.tracker.pairschedule

import com.github.pjozsef.slack.emoji.tracker.model.json
import com.github.pjozsef.slack.emoji.tracker.util.handleAsyncResult
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.client.WebClient
import java.util.*

class PairScheduleVerticle : AbstractVerticle() {

    override fun start() {
        val log = LoggerFactory.getLogger(this::class.java)
        val client = WebClient.create(vertx)
        val eb = vertx.eventBus()
        val teamString = config().getString("members")

        subscribeToCron(eb, teamString, client, log)
        scheduleMorning(eb, log)
        scheduleAfternoon(eb, log)
    }

    private fun subscribeToCron(eb: EventBus, teamString: String, client: WebClient, log: Logger) {
        eb.consumer<Any?>("pairschedule.action") {
            val team = teamString.split(",").toMutableList()
            Collections.shuffle(team)

            val (first, second) = team

            val payload = json {
                "text" to "Current pair is: $first - $second"
            }

            client.post(443, "hooks.slack.com", config().getString("token"))
                    .ssl(true)
                    .sendJsonObject(payload) { result ->
                        handleAsyncResult(result, log)
                    }
        }
    }

    private fun scheduleMorning(eb: EventBus, log: Logger) {
        schedule("0 30 9 ? * MON-FRI *", eb, log)
    }

    private fun scheduleAfternoon(eb: EventBus, log: Logger) {
        schedule("0 0 13 ? * MON-FRI *", eb, log)
    }

    private fun schedule(cron: String, eb: EventBus, log: Logger) {
        val message = json {
            "cron_expression" to cron
            "address" to "pairschedule.action"
            "repeat" to true
            "action" to "send"
        }
        eb.send<Any>("pairschedule.schedule", message) {
            handleAsyncResult(it, log) {
                log.info(it.body())
            }
        }
    }
}