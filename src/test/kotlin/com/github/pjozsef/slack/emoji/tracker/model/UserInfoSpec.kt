package com.github.pjozsef.slack.emoji.tracker.model

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.*

object UserInfoSpec : Spek({
    given("a correct input string with no emoji duplicates") {
        val input = "<@93847FD34> qwerty :asdf: :aa::b: 3456 :a_a:!"

        on("calling the factory method") {
            val info = UserInfo.of(input)

            it("should have the correct username") {
                val expected = "<@93847FD34>"
                assertEquals(expected, info?.user)
            }

            it("should have the correct emoji map") {
                val expected = mapOf(":asdf:" to 1, ":aa:" to 1, ":b:" to 1, ":a_a:" to 1)
                assertEquals(expected, info?.emojis)
            }
        }
    }

    given("a correct input string with duplicated emojis"){
        val input = "<@93847FD34> qwerty :asdf: :asdf: :a_a:!"

        on("calling the factory method") {
            val info = UserInfo.of(input)

            it("should have the correct username") {
                val expected = "<@93847FD34>"
                assertEquals(expected, info?.user)
            }

            it("should have the correct emoji map") {
                val expected = mapOf(":asdf:" to 2, ":a_a:" to 1)
                assertEquals(expected, info?.emojis)
            }
        }
    }

    given("an input without exclamation marks at the end") {
        val input = "<@93847FD34> qwerty :asdf: :aa::b: 3456 :a_a:"

        on("calling the factory method") {
            val info = UserInfo.of(input)

            it("should return null") {
                assertNull(info)
            }
        }
    }

    given("an input without username") {
        val input = "incorrect :hjk:!"

        on("calling the factory method") {
            val info = UserInfo.of(input)

            it("should return null") {
                assertNull(info)
            }
        }
    }
})