package pl.jigga.loom

import java.util.*

object RequestId {
    private val requestId = ThreadLocal.withInitial { UUID.randomUUID().toString() }
    fun get(): String {
        return requestId.get()
    }
}