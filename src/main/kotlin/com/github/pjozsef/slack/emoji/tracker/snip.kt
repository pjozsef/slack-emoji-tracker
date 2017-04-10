package com.github.pjozsef.slack.emoji.tracker

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.pjozsef.slack.mention.model.UserInfo
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject

fun main(args: Array<String>) {
    Json.mapper.registerModule(KotlinModule())
    val s = "@pjozsef :asdf: :jkle: :frog:oijawef!"
    val emojisR = Regex(":\\w+:")
    val userR = Regex("^@\\w")

    val user = userR.find(s)?.value
    user?.let {
        val r = emojisR.findAll(s).toList().flatMap{it.groupValues}
        println(r)

        val enc = Json.encode(UserInfo("asf", mutableMapOf("a" to 3, "b" to 1)))
        println(enc)
        val jsonobject = JsonObject(enc)
        val de2 = Json.mapper.convertValue(jsonobject, UserInfo::class.java)
        println(de2)
        val de = Json.mapper.readValue(enc, UserInfo::class.java)
        println(de)


        println(Json.mapper.convertValue(de, Map::class.java))
    }
}