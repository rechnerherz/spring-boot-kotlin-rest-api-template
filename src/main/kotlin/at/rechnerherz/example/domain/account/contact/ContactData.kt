package at.rechnerherz.example.domain.account.contact

import at.rechnerherz.example.config.validation.OptionalPlainText
import at.rechnerherz.example.config.validation.OptionalURL
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.validation.Valid

@Embeddable
class ContactData(
    email: String,
    phone: String,
    fax: String,
    homepage: String,
    address: Address
) {

    constructor() : this("", "", "", "", Address())

    @Column(nullable = false)
    @OptionalPlainText
    var email: String = email

    @Column(nullable = false)
    @OptionalPlainText
    var phone: String = phone

    @Column(nullable = false)
    @OptionalPlainText
    var fax: String = fax

    @Column(nullable = false)
    @OptionalURL
    var homepage: String = homepage

    @Valid
    var address: Address = address

}
