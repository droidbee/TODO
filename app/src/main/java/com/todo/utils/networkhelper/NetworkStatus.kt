package com.todo.utils.networkhelper

sealed class NetworkStatus{
    object Available: NetworkStatus()
    object Unavailable: NetworkStatus()
}
