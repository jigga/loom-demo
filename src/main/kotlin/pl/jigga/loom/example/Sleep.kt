package pl.jigga.loom.example

import pl.jigga.loom.util.newPrintTask
import pl.jigga.loom.util.newSleepTask
import pl.jigga.loom.util.newVirtualThreadFactory
import pl.jigga.loom.util.shutdownAndAwaitTermination
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @see Thread.sleep
 * @see Fiber.parkNanos
 */
fun main() {

    val singleThreadedScheduler = Executors.newSingleThreadExecutor()

    // dinosaur threads
//    singleThreadedScheduler.submit(newSleepTask(TimeUnit.SECONDS, 3))
//    singleThreadedScheduler.submit(newSleepTask(TimeUnit.SECONDS, 3))

    // virtual threads
    val virtualThreadFactory =
            newVirtualThreadFactory(singleThreadedScheduler, false)
    virtualThreadFactory.newThread(newSleepTask(TimeUnit.SECONDS, 3)).start()
    virtualThreadFactory.newThread(newSleepTask(TimeUnit.SECONDS, 3)).start()
    virtualThreadFactory.newThread(newPrintTask("Hello, WJUG!")).start()

    shutdownAndAwaitTermination(singleThreadedScheduler, 10, TimeUnit.SECONDS)

}