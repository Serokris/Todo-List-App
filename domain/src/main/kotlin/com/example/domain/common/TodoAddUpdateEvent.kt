package com.example.domain.common

sealed class TodoAddUpdateEvent(val message: String? = null) {
    class Success(message: String) : TodoAddUpdateEvent(message)
    class Error(message: String) : TodoAddUpdateEvent(message)
    object Empty : TodoAddUpdateEvent()
}