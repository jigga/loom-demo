package pl.jigga.loom.util

import java.util.concurrent.Executor
import java.util.concurrent.ThreadFactory

enum class ThreadLocalSupport {
    INHERIT,
    DISALLOW
}

fun newVirtualThreadFactory(): ThreadFactory {
    return Thread.builder()
            .virtual()
            .name("virtual-thread-", 1)
            .factory()
}

fun newVirtualThreadFactory(scheduler: Executor): ThreadFactory {
    return Thread.builder()
            .virtual(scheduler)
            .name("virtual-thread-", 1)
            .factory()
}

fun newVirtualThreadFactory(scheduler: Executor, daemon: Boolean): ThreadFactory {
    return Thread.builder()
            .virtual(scheduler)
            .name("virtual-thread-", 1)
            .daemon(daemon)
            .factory()
}

fun newVirtualThreadFactory(scheduler: Executor, threadLocalSupport: ThreadLocalSupport): ThreadFactory {
    val builder = Thread.builder()
            .virtual(scheduler)
            .name("virtual-thread-", 1)
    when (threadLocalSupport) {
        ThreadLocalSupport.INHERIT -> builder.inheritThreadLocals()
        ThreadLocalSupport.DISALLOW -> builder.disallowThreadLocals()
    }
    return builder.factory()
}