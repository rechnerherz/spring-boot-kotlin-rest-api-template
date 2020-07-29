package at.rechnerherz.example.domain.document

import at.rechnerherz.example.domain.permission.meta.HasRoleAdmin
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

/**
 * Provides a multipart file upload endpoint.
 */
@RestController
@RequestMapping(path = ["/api/documents"])
class DocumentController(
    private val documentService: DocumentService
) {

    @HasRoleAdmin
    @PostMapping(path = ["/upload"])
    @Throws(IOException::class)
    fun upload(
        @RequestParam files: List<MultipartFile>
    ): ResponseEntity<Set<Document>> {

        if (files.isEmpty())
            return ResponseEntity(HttpStatus.BAD_REQUEST)

        val documents = files.mapTo(mutableSetOf()) { file ->

            if (!DocumentType.isAccepted(file.contentType))
                return ResponseEntity(HttpStatus.BAD_REQUEST)

            documentService.saveMultipartFile(file)
        }

        return ResponseEntity(documents, HttpStatus.OK)
    }
}
