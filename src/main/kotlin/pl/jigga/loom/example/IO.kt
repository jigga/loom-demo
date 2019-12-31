package pl.jigga.loom.example

import pl.jigga.loom.util.newGetURLContentTask
import pl.jigga.loom.util.newVirtualThreadFactory
import pl.jigga.loom.util.shutdownAndAwaitTermination
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @see sun.nio.ch.Poller.register
 */
fun main() {

    val scheduler = Executors.newSingleThreadExecutor()

//    scheduler.submit(newGetURLContentTask(URL("http://localhost/delay/3")))
//    scheduler.submit(newGetURLContentTask(URL("http://localhost/delay/3")))
//    scheduler.submit(newGetURLContentTask(URL("http://localhost/delay/3")))

    val virtualThreadFactory = newVirtualThreadFactory(scheduler, false)
    virtualThreadFactory.newThread(newGetURLContentTask(URL("http://localhost/delay/3"))).start()
    virtualThreadFactory.newThread(newGetURLContentTask(URL("http://localhost/delay/3"))).start()
    virtualThreadFactory.newThread(newGetURLContentTask(URL("http://localhost/delay/3"))).start()

    shutdownAndAwaitTermination(scheduler, 5, TimeUnit.SECONDS)

}
