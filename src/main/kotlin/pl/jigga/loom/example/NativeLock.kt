package pl.jigga.loom.example

import pl.jigga.loom.util.newNativeLockingTask
import pl.jigga.loom.util.newPrintTask
import pl.jigga.loom.util.newVirtualThreadFactory
import pl.jigga.loom.util.shutdownAndAwaitTermination
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

// -Djdk.tracePinnedThreads=full
fun main() {
    val singleThreadedScheduler = Executors.newSingleThreadExecutor()
    val lock = Any()

//    singleThreadedScheduler.submit(newNativeLockingTask(lock))
//    singleThreadedScheduler.submit(newNativeLockingTask(lock))
//    singleThreadedScheduler.submit(newPrintTask("I'll run only when the single thread is available"))

    val virtualThreadFactory =
            newVirtualThreadFactory(singleThreadedScheduler, false)
    virtualThreadFactory.newThread(newNativeLockingTask(lock)).start()
    virtualThreadFactory.newThread(newNativeLockingTask(lock)).start()
    virtualThreadFactory.newThread(newPrintTask("Am able to run although the thread is blocked waiting for lock")).start()

    shutdownAndAwaitTermination(singleThreadedScheduler, 10, TimeUnit.SECONDS)
}