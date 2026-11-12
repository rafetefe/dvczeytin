package io.github.rafetefe

import java.util.UUID

/**
 * Data model representing a row in the right-side form table.
 */
data class FormRow(
    val id: String = UUID.randomUUID().toString(),
    val cins: String,
    val boyut: String,
    val miktar: String,
    val toplam: String,
)
