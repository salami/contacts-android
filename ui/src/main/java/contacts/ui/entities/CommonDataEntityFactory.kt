package contacts.ui.entities

import contacts.entities.*

/**
 * Creates instances of [CommonDataEntity].
 */
interface CommonDataEntityFactory<K : CommonDataEntity> {

    /**
     * Returns a new instance of [K].
     */
    fun create(): K
}

object AddressFactory : CommonDataEntityFactory<MutableAddress> {
    override fun create() = MutableAddress()
}

object EmailFactory : CommonDataEntityFactory<MutableEmail> {
    override fun create() = MutableEmail()
}

object EventFactory : CommonDataEntityFactory<MutableEvent> {
    override fun create() = MutableEvent()
}

object ImFactory : CommonDataEntityFactory<MutableIm> {
    override fun create() = MutableIm()
}

object PhoneFactory : CommonDataEntityFactory<MutablePhone> {
    override fun create() = MutablePhone()
}

object RelationFactory : CommonDataEntityFactory<MutableRelation> {
    override fun create() = MutableRelation()
}