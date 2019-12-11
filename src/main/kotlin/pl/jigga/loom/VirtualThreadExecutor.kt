package pl.jigga.loom

import pl.jigga.loom.util.ThreadLocalSupport
import pl.jigga.loom.util.newVirtualThreadFactory
import java.util.concurrent.Executor

class VirtualThreadExecutor(delegate: Executor) : Executor {

    private val virtualThreadFactory = newVirtualThreadFactory(delegate, ThreadLocalSupport.INHERIT)

    override fun execute(command: Runnable) {
        virtualThreadFactory.newThread {
            RequestId.get()
            command.run()
        }.start()
    }

}