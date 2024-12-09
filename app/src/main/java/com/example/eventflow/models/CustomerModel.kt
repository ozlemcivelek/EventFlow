package com.example.eventflow.models

data class CustomerModel(
    var customerRef: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    var isSelected: Boolean = false,
) {
    override fun equals(other: Any?): Boolean {
        return other is CustomerModel && name == other.name && email == other.email && phone == other.phone
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + phone.hashCode()
        return result
    }
}
