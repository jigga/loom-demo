package pl.jigga.loom

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/loom")
internal class WorkshopController(private val client: BinClient) {

    @GetMapping(value = ["/demo"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun demo(): String {
        return client.getWithDelay()
    }

    @GetMapping(value = ["/gc"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun gc(): String {
        System.gc()
        return """{"status": "OK"}"""
    }

}