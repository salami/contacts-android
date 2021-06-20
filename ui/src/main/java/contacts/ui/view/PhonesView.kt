package contacts.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import contacts.entities.MutablePhone
import contacts.entities.Phone
import contacts.entities.removeAll

/**
 * A (vertical) [LinearLayout] that displays a list of [MutablePhone]s and handles the modifications
 * to the given [phones]. Each of the phone is displayed in a [PhoneView].
 *
 * Setting the [phones] will automatically update the views. Any modifications in the views will
 * also be made to the [phones].
 *
 * ## Note
 *
 * This is a very rudimentary view that is not styled or made to look good. It may not follow any
 * good practices and may even implement bad practices. Consumers of the library may choose to use
 * this as is or simply as a reference on how to implement this part of native Contacts app.
 *
 * This does not support state retention (e.g. device rotation). The OSS community may contribute to
 * this by implementing it.
 *
 * The community may contribute by styling and adding more features and customizations with these
 * views if desired.
 *
 * ## Developer Notes
 *
 * I usually am a proponent of passive views and don't add any logic to views. However, I will make
 * an exception for this basic view that I don't really encourage consumers to use.
 */
class PhonesView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    /**
     * The list of phones that is shown in this view. Setting this will automatically update the
     * views. Any modifications in the views will also be made to the this.
     */
    var phones: MutableList<MutablePhone> = mutableListOf()
        set(value) {
            field = value

            setPhonesViews()
        }

    /**
     * A PhoneView with a new empty phone. Used to add a new phone to the list of [phones].
     */
    private lateinit var emptyPhoneView: PhoneView

    private fun setPhonesViews() {
        removeAllViews()

        phones.forEach { phone ->
            addPhoneView(phone)
        }

        addEmptyPhoneView()
    }

    private fun addPhoneView(phone: MutablePhone): PhoneView {
        val phoneView = PhoneView(context).apply {
            this.phone = phone
            onPhoneDeleteButtonClicked = ::onPhoneDeleteButtonClicked
            onPhoneNumberCleared = ::onPhoneNumberCleared
            onPhoneNumberBegin = ::onPhoneNumberBegin
        }

        addView(phoneView)

        return phoneView
    }

    private fun addEmptyPhoneView() {
        // In the native Contacts app, the new empty phone that is added has a phone type of
        // either mobile, home, work, or other in that other; which ever has not yet been added.
        // If all those phone types already exist, it defaults to other.
        val existingPhoneTypes = phones.map { it.type }
        val phoneType = DEFAULT_PHONE_TYPES.minus(existingPhoneTypes).firstOrNull()
            ?: DEFAULT_PHONE_TYPES.last()

        emptyPhoneView = addPhoneView(MutablePhone().apply { type = phoneType })
        phones.add(emptyPhoneView.phone)
    }

    private fun onPhoneNumberCleared(phoneView: PhoneView) {
        removePhoneView(emptyPhoneView)
        emptyPhoneView = phoneView
    }

    private fun onPhoneNumberBegin() {
        addEmptyPhoneView()
    }

    private fun onPhoneDeleteButtonClicked(phoneView: PhoneView) {
        removePhoneView(phoneView)
    }

    private fun removePhoneView(phoneView: PhoneView) {
        // There may be duplicate phones. Therefore, we need to remove the exact phone instance.
        // Thus, we remove the phone by reference equality instead of by content/structure equality.
        phones.removeAll(phoneView.phone, byReference = true)
        removeView(phoneView)
    }
}

private val DEFAULT_PHONE_TYPES = sequenceOf(
    Phone.Type.MOBILE, Phone.Type.HOME, Phone.Type.WORK, Phone.Type.OTHER
)