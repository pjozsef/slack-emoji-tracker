package com.github.pjozsef.slack.emoji.tracker.model

data class UserInfo(val user: String, val emojis: MutableMap<String, Int> = mutableMapOf()) {
    companion object {
        private val userRegex = Regex("^(<@\\w+>).*!$")
        private val emojiRegex = Regex(":[_\\w]+:")

        fun of(text: String): UserInfo? {
            userRegex.find(text)?.groups?.get(1)?.value?.let { username ->
                val emojis = emojiRegex
                        .findAll(text)
                        .toList()
                        .flatMap { it.groupValues }
                        .groupBy { it }
                        .mapValues { it.value.size }
                        .toMutableMap()
                return UserInfo(username, emojis)
            } ?: return null
        }
    }
}