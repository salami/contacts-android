package contacts.entities

import kotlinx.android.parcel.Parcelize

@Parcelize
data class SipAddress internal constructor(

    override val id: Long?,

    override val rawContactId: Long?,

    override val contactId: Long?,

    override val isPrimary: Boolean,

    override val isSuperPrimary: Boolean,

    // Type and Label are also available. However, it is unnecessary as there is only one sip
    // address per contact.

    /**
     * The SIP address.
     */
    val sipAddress: String?

) : CommonDataEntity {

    override val mimeType: MimeType = MimeType.SipAddress

    override val isBlank: Boolean = propertiesAreAllNullOrBlank(sipAddress)

    fun toMutableSipAddress() = MutableSipAddress(
        id = id,
        rawContactId = rawContactId,
        contactId = contactId,

        isPrimary = isPrimary,
        isSuperPrimary = isSuperPrimary,

        sipAddress = sipAddress
    )
}

@Parcelize
data class MutableSipAddress internal constructor(

    override val id: Long?,

    override val rawContactId: Long?,

    override val contactId: Long?,

    override var isPrimary: Boolean,

    override var isSuperPrimary: Boolean,

    /**
     * See [SipAddress.sipAddress].
     */
    var sipAddress: String?

) : MutableCommonDataEntity {

    override val mimeType: MimeType = MimeType.SipAddress

    constructor() : this(null, null, null, false, false, null)

    override val isBlank: Boolean
        get() = propertiesAreAllNullOrBlank(sipAddress)
}