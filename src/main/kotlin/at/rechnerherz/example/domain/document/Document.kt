package at.rechnerherz.example.domain.document

import at.rechnerherz.example.config.validation.OptionalPlainText
import at.rechnerherz.example.domain.account.Account
import at.rechnerherz.example.domain.base.SimpleBaseEntity
import at.rechnerherz.example.domain.permission.RelatedAccountRule
import at.rechnerherz.example.domain.permission.WithPermissionRule
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

/**
 * A document.
 *
 * Images have a maximum size of [DocumentProperties.maxImageSize] and thumbnails as defined in [DocumentProperties.extraImageSizes].
 */
@Entity
@WithPermissionRule(RelatedAccountRule::class)
class Document(
    uuid: UUID,
    type: DocumentType,
    name: String
) : SimpleBaseEntity() {

    override val keyProperties
        get() = listOf(Document::uuid)

    override val abbreviation
        get() = "D"

    /*------------------------------------*\
     * Fields
    \*------------------------------------*/

    /**
     * The UUID name of the document.
     */
    @NaturalId(mutable = false)
    @Column(nullable = false, unique = true, updatable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    val uuid: UUID = uuid

    /**
     * The type of the document.
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var type: DocumentType = type

    /**
     * The name of the document (the original filename for uploaded files).
     */
    @Column(nullable = false)
    @OptionalPlainText
    var name: String = name

    /**
     * The original width of the image, or `null` if not an image.
     */
    var width: Int? = null

    /**
     * The original height of the image, or `null` if not an image.
     */
    var height: Int? = null

    /*-------------------------------------*\
     * Relations
    \*-------------------------------------*/

    /**
     * The account that owns this document.
     */
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    @JsonIgnore
    var owner: Account? = null

    /*------------------------------------*\
     * Logic
    \*------------------------------------*/

    /**
     * The file extension of the document.
     */
    val extension: String
        get() = type.extension()

    /**
     * The file name of the document.
     */
    val fileName: String
        get() = "$uuid.$extension"

    /**
     * The display name of the document.
     */
    val displayName: String
        @JsonIgnore
        get() = "$name.$extension"

}
