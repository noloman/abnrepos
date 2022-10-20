package com.nulltwenty.abnrepos.domain

import com.nulltwenty.abnrepos.domain.model.ResultOf
import java.io.IOException

/**
 * Wrap a suspending API [call] in try/catch. In case an exception is thrown, a [Result.Failure] is
 * created based on the exception message.
 */
suspend fun <T : Any> safeApiCall(
    call: suspend () -> ResultOf<T>
): ResultOf<T> {
    return try {
        call()
    } catch (e: Exception) {
        // An exception was thrown when calling the API so we're converting this to an IOException
        ResultOf.Error(IOException(e.message, e))
    }
}