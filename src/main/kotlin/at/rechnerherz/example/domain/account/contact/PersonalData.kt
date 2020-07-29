package at.rechnerherz.example.domain.account.contact

import at.rechnerherz.example.config.validation.OptionalPlainText
import at.rechnerherz.example.util.joinNotBlankToString
import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
class PersonalData(
    gender: Gender?,
    firstName: String,
    lastName: String,
    prefix: String,
    suffix: String,
    birthDate: LocalDate?
) {

    constructor() : this(null, "", "", "", "", null)

    @Enumerated(EnumType.STRING)
    var gender: Gender? = gender

    @Column(nullable = false)
    @OptionalPlainText
    var firstName: String = firstName

    @Column(nullable = false)
    @OptionalPlainText
    var lastName: String = lastName

    @Column(nullable = false)
    @OptionalPlainText
    var prefix: String = prefix

    @Column(nullable = false)
    @OptionalPlainText
    var suffix: String = suffix

    var birthDate: LocalDate? = birthDate

    val fullName: String
        @JsonIgnore
        get() = sequenceOf(sequenceOf(
            prefix,
            firstName,
            lastName
        ).joinNotBlankToString(separator = " "),
            suffix
        ).joinNotBlankToString(separator = ", ")

}
