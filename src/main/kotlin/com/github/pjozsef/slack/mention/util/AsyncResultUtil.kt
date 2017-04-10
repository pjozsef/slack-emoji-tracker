package com.github.pjozsef.slack.mention.util

import io.vertx.core.AsyncResult
import io.vertx.core.logging.Logger

fun <T> handleAsyncResult(asyncResult: AsyncResult<T>, log: Logger, success: ((T)->Unit)? = null){
    if (asyncResult.failed()){
        log.error(asyncResult.cause())
    } else {
        success?.invoke(asyncResult.result())
    }
}