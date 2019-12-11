package pl.jigga.loom.example

import pl.jigga.loom.util.newConsumerTask
import pl.jigga.loom.util.newProducerTask
import pl.jigga.loom.util.newVirtualThreadFactory
import pl.jigga.loom.util.shutdownAndAwaitTermination
import java.util.concurrent.*
import kotlin.random.Random

fun main() {

    val scheduler: ExecutorService =
            Executors.newSingleThreadExecutor()
    val queue = SynchronousQueue<Int>()
    val producer = newProducerTask(queue) {
        val next = Random.nextInt()
        println("Produced: $next")
        next
    }
    val consumer = newConsumerTask(queue) { println("Consumed $it") }

//    scheduler.submit(producer)
//    scheduler.submit(consumer)

    val threadFactory: ThreadFactory = newVirtualThreadFactory(scheduler)
    threadFactory.newThread(producer).start()
    threadFactory.newThread(consumer).start()

    shutdownAndAwaitTermination(scheduler, 10, TimeUnit.SECONDS)

}
