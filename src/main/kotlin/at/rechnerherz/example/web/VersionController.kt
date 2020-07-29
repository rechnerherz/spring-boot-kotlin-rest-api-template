package at.rechnerherz.example.web

import at.rechnerherz.example.config.PUBLIC_URL
import org.springframework.boot.info.BuildProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class VersionController(
    private val buildProperties: BuildProperties
) {

    @GetMapping(path = ["$PUBLIC_URL/version"])
    fun getVersion(): ResponseEntity<Map<String, String>> =
        ResponseEntity(mapOf("version" to buildProperties.version), HttpStatus.OK)

}
