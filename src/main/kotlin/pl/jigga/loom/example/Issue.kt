package pl.jigga.loom.example

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() {
    val task = Runnable {
        println("${Thread.currentThread().name} - Going to sleep for 10 seconds")
        TimeUnit.SECONDS.sleep(10)
        println("${Thread.currentThread().name} - Woke up after 10 seconds")
    }

    val scheduler = Executors.newSingleThreadExecutor()
    val virtualThreadFactory = Thread.builder()
            .virtual()
            .scheduler(scheduler)
            .daemon(false)
            .name("virtual-thread-", 1)
            .factory()

    virtualThreadFactory.newThread(task).start()
    virtualThreadFactory.newThread(task).start()
    virtualThreadFactory.newThread(task).start()

    TimeUnit.SECONDS.sleep(1)
    scheduler.shutdown()
    scheduler.awaitTermination(10, TimeUnit.SECONDS)
}

//    val threadScheduler = Executors.newCachedThreadPool()
//
//    threadScheduler.submit(task)
//    threadScheduler.submit(task)
//    threadScheduler.submit(task)
//
//    TimeUnit.SECONDS.sleep(1)
//    threadScheduler.shutdown()
//    threadScheduler.awaitTermination(15, TimeUnit.SECONDS)
