package at.rechnerherz.example.domain.account.contact

import at.rechnerherz.example.config.validation.OptionalPlainText
import at.rechnerherz.example.config.validation.ValidAlpha3CountryCode
import at.rechnerherz.example.util.joinNotBlankToString
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Address(
    country: String,
    city: String,
    postCode: String,
    street: String
) {

    constructor() : this("", "", "", "")

    @Column(nullable = false, length = 3)
    @ValidAlpha3CountryCode
    var country: String = country

    @Column(nullable = false)
    @OptionalPlainText
    var city: String = city

    @Column(nullable = false)
    @OptionalPlainText
    var postCode: String = postCode

    @Column(nullable = false)
    @OptionalPlainText
    var street: String = street

    val postCodeCity: String
        @JsonIgnore
        get() = sequenceOf(postCode, city).joinNotBlankToString(separator = " ")

    val fullAddress: String
        @JsonIgnore
        get() = sequenceOf(street, postCodeCity, country).joinNotBlankToString()
}
