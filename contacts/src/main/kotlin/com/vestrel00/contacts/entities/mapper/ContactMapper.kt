package com.vestrel00.contacts.entities.mapper

import com.vestrel00.contacts.entities.Contact
import com.vestrel00.contacts.entities.cursor.ContactCursor

internal class ContactMapper(
    private val contactCursor: ContactCursor,
    private val optionsMapper: OptionsMapper
) {

    val contact: Contact
        get() = Contact(
            id = contactCursor.id,

            rawContacts = emptyList(),

            displayName = contactCursor.displayName,

            lastUpdatedTimestamp = contactCursor.lastUpdatedTimestamp,

            options = optionsMapper.options
        )
}