package at.rechnerherz.example.domain.document

import at.rechnerherz.example.domain.base.BaseRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.transaction.annotation.Transactional

@RepositoryRestResource(path = "documents", collectionResourceRel = "documents")
@Transactional(readOnly = true)
interface DocumentRepository : BaseRepository<Document>
