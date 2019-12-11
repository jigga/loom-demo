package pl.jigga.loom.example

import pl.jigga.loom.util.newLockingTask
import pl.jigga.loom.util.newPrintTask
import pl.jigga.loom.util.newVirtualThreadFactory
import pl.jigga.loom.util.shutdownAndAwaitTermination
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

fun main() {

    val singleThreadedScheduler = Executors.newSingleThreadExecutor()
    val lock = ReentrantLock()

//    singleThreadedScheduler.submit(newLockingTask(lock))
//    singleThreadedScheduler.submit(newLockingTask(lock))
//    singleThreadedScheduler.submit(newPrintTask("I'll run only when the single thread is available"))

    val virtualThreadFactory =
            newVirtualThreadFactory(singleThreadedScheduler)
    virtualThreadFactory.newThread(newLockingTask(lock)).start()
    virtualThreadFactory.newThread(newLockingTask(lock)).start()
    virtualThreadFactory.newThread(newPrintTask("Am able to run although the thread is blocked waiting for lock")).start()

    shutdownAndAwaitTermination(singleThreadedScheduler, 10, TimeUnit.SECONDS)

}