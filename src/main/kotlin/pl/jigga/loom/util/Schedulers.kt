package pl.jigga.loom.util

import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

fun shutdownAndAwaitTermination(scheduler: ExecutorService, timeout: Long, unit: TimeUnit) {
    unit.sleep(timeout)
    scheduler.shutdownNow()
//    scheduler.awaitTermination(timeout, unit)
}