package com.example.domain.common

inline fun <T> Iterable<T>.takeIf(condition: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        if (condition(item)) {
            list.add(item)
        }
    }
    return list
}