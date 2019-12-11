package pl.jigga.loom.util

import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path
import java.time.Duration
import java.util.concurrent.BlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.stream.Collectors
import kotlin.concurrent.withLock

class SleepTask(private val unit: TimeUnit, private val timeout: Long): Runnable {
    override fun run() {
        println("${Thread.currentThread().name} - going to sleep for $timeout $unit")
        unit.sleep(timeout)
        println("${Thread.currentThread().name} - woke up after sleeping for $timeout $unit")
    }
}

fun newSleepTask(unit: TimeUnit, timeout: Long): SleepTask {
    return SleepTask(unit, timeout)
}

class Producer<T>(private val queue: BlockingQueue<T>, private val supplier: () -> T) : Runnable {
    override fun run() {
        while (true) {
            queue.put(supplier.invoke())
            TimeUnit.SECONDS.sleep(1)
        }
    }
}

fun <T> newProducerTask(queue: BlockingQueue<T>, supplier: () -> T): Producer<T> {
    return Producer(queue, supplier)
}

class Consumer<T>(private val queue: BlockingQueue<T>, private val consumer: (T) -> Unit) : Runnable {
    override fun run() {
        while (true) {
            consumer.invoke(queue.take())
            TimeUnit.SECONDS.sleep(1)
        }
    }
}

fun <T> newConsumerTask(queue: BlockingQueue<T>, consumer: (T) -> Unit): Consumer<T> {
    return Consumer(queue, consumer)
}

fun newPrintTask(message: String): Runnable {
    return Runnable {
        println(Thread.currentThread().name + " - " + message)
    }
}

class ReadFileTask(private val path: Path): Runnable {
    internal var content: String? = null

    override fun run() {
        if (!Files.exists(path)) {
            throw IllegalArgumentException("File $path does not exist")
        }
        println("${Thread.currentThread().name} - Starting reading file: $path")
        content = Files
//                .lines(path)
//                .peek { TimeUnit.SECONDS.sleep(1) }
//                .collect(Collectors.joining(System.lineSeparator()))
//                .readString(path)
                .readAllLines(path)
                .stream()
                .collect(Collectors.joining(System.lineSeparator()))
        println("${Thread.currentThread().name} - DONE reading file $path")
    }
}

fun newReadFileTask(path: Path): ReadFileTask {
    return ReadFileTask(path)
}

class HttpGetTask(private val uri: URI): Runnable {
    companion object {
        private val httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build()
    }
    private var response: String? = null

    override fun run() {
        val request = HttpRequest.newBuilder(uri)
                .GET()
                .timeout(Duration.ofSeconds(30))
                .header("Accept", "application/json")
                .build()
        println("${Thread.currentThread().name} - Calling $uri...")
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body()
        println("${Thread.currentThread().name} - Received response:\n$response")
    }
}

fun newHttpGetTask(uri: URI): HttpGetTask {
    return HttpGetTask(uri)
}

class GetURLContentTask(private val url: URL): Runnable {
    override fun run() {
        println("${Thread.currentThread().name} - Getting content of $url...")
        val response = url.openStream().use {
            String(it.readAllBytes())
        }
        println("${Thread.currentThread().name} - Content of $url is:\n$response")
    }
}

fun newGetURLContentTask(url: URL): GetURLContentTask {
    return GetURLContentTask(url)
}

class LockingTask(private val lock: Lock): Runnable {

    override fun run() {
        println("${Thread.currentThread().name} - Trying to acquire a lock: $lock")
        lock.withLock {
            println("${Thread.currentThread().name} - Acquired $lock, doing privileged work")
            TimeUnit.SECONDS.sleep(3)
        }
        println("${Thread.currentThread().name} - Released $lock")
    }

}

fun newLockingTask(lock: Lock): LockingTask {
    return LockingTask(lock)
}

class NativeLockingTask(private val lock: Any): Runnable {
    override fun run() {
        println("${Thread.currentThread().name} - Trying to acquire native lock: $lock")
        synchronized(lock) {
            println("${Thread.currentThread().name} - Acquired native $lock, doing privileged work")
            TimeUnit.SECONDS.sleep(3)
        }
        println("${Thread.currentThread().name} - Released native $lock")
    }
}

fun newNativeLockingTask(lock: Any): NativeLockingTask {
    return NativeLockingTask(lock)
}