# Insert contacts into SIM card

This library provides the `SimContactsInsert` API that allows you to create/insert contacts into the
SIM card.

An instance of the `SimContactsInsert` API is obtained by,

```kotlin
val insert = Contacts(context).sim().insert()
```

Note that SIM card inserts will only work if there is a SIM card in the ready state. For more info,
read [SIM card state](./../sim/about-sim-contacts.md#sim-card-state).

## A basic insert

To create/insert a new contact into the SIM card,

```kotlin
val insertResult = Contacts(context)
    .sim()
    .insert()
    .simContact(NewSimContact(name = "Dude", number = "5555555555"))
    .commit()
```

If you need to insert multiple contacts,

```kotlin
val newContact1 = NewSimContact(name = "Dude1", number = "1234567890")
val newContact2 = NewSimContact(name = "Dude2", number = "0987654321")

val insertResult = Contacts(context)
    .sim()
    .insert()
    .simContacts(newContact1, newContact2)
    .commit()
```

## Blank contacts are not allowed

For more info, read about [SIM Contacts](./../sim/about-sim-contacts.md#blanks-are-not-allowed)

## Character limits

For more info, read about [SIM Contacts](./../sim/about-sim-contacts.md#character-limits)

## Executing the insert

To execute the insert,

```kotlin
.commit()
```

### Handling the insert result

The `commit` function returns a `Result`.

To check if all inserts succeeded,

```kotlin
val allInsertsSuccessful = insertResult.isSuccessful
```

To check if a particular insert succeeded,

```kotlin
val firstInsertSuccessful = insertResult.isSuccessful(newContact1)
```

To get all newly created SimContacts, you may use the extensions provided
in `SimContactsInsertResult`,

```kotlin
val simContacts = insertResult.simContacts(contactsApi)
```

To get a particular simContact,

```kotlin
val simContact = insertResult.simContact(contactsApi, newSimContact1)
```

> ⚠️ The `IccProvider` does not yet return the row ID of newly inserted contacts. Look at the "TODO"
> at line 259 of Android's [IccProvider](https://android.googlesource.com/platform/frameworks/opt/telephony/+/51302ef/src/java/com/android/internal/telephony/IccProvider.java#259).
> Therefore, this library's insert API can only support getting the new rows from the result with some
> limitations around duplicate entries (see documentation in `SimContactsInsertResult`).

### Handling insert failure

The insert may fail for a particular SIM contact for various reasons,

```kotlin
insertResult.failureReason(newSimContact1)?.let {
    when (it) {
        NAME_EXCEEDED_MAX_CHAR_LIMIT -> tellUserTheNameIsTooLong()
        NUMBER_EXCEEDED_MAX_CHAR_LIMIT -> tellUserTheNumberIsTooLong()
        NAME_AND_NUMBER_ARE_BLANK -> tellUserTheNameAndNumberCannotBothBeBlank()
        UNKNOWN -> showGenericErrorMessage()
    }
}
```

## Cancelling the insert

To cancel an insert amid execution,

```kotlin
.commit { returnTrueIfInsertShouldBeCancelled() }
```

The `commit` function optionally takes in a function that, if it returns true, will cancel insert
processing as soon as possible. The function is called numerous times during insert processing to
check if processing should stop or continue. This gives you the option to cancel the insert.

For example, to automatically cancel the insert inside a Kotlin coroutine when the coroutine is
cancelled,

```kotlin
launch {
    withContext(coroutineContext) {
        val insertResult = insert.commit { !isActive }
    }
}
```

## Performing the insert and result processing asynchronously

Inserts are executed when the `commit` function is invoked. The work is done in the same thread as
the call-site. This may result in a choppy UI.

To perform the work in a different thread, use the Kotlin coroutine extensions provided in
the `async` module. For more info,
read [Execute work outside of the UI thread using coroutines](./../async/async-execution-coroutines.md).

You may, of course, use other multi-threading libraries or just do it yourself =)

> ℹ️ Extensions for Kotlin Flow and RxJava are also in the v1 roadmap.

## Performing the insert with permission

Inserts require the `android.permission.WRITE_CONTACTS` permission. If not granted, the insert will
do nothing and return a failed result.

To perform the insert with permission, use the extensions provided in the `permissions` module.
For more info, read [Permissions handling using coroutines](./../permissions/permissions-handling-coroutines.md).

You may, of course, use other permission handling libraries or just do it yourself =)