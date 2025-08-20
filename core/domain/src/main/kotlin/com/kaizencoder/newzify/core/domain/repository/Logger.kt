package com.kaizencoder.newzify.core.domain.repository

interface Logger {

    fun d(message: String)
    fun e(throwable: Throwable, message: String = "")

}