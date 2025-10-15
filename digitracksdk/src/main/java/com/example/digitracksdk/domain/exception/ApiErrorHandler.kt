package com.example.digitracksdk.domain.exception

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.CommonApiErrorModel
import com.example.digitracksdk.domain.model.UNKNOWN_ERROR_MESSAGE
import com.example.digitracksdk.utils.AppUtils
import retrofit2.HttpException
import java.io.IOException
import java.lang.reflect.Type
import java.net.SocketTimeoutException

/**
 * Trace exceptions(api call or parse data or connection errors) &
 * depending on what exception returns [ApiError]
 *
 * */
fun traceErrorException(throwable: Throwable?): ApiError {

    return when (throwable) {

        is HttpException -> {
            val responseError = throwable.response()?.errorBody()?.string()
            Log.e("error " , "message -> $responseError")
            val collectionType: Type = object : TypeToken<CommonApiErrorModel>() {}.type
            var errorBody: CommonApiErrorModel? = CommonApiErrorModel()
            try {
                errorBody = Gson().fromJson<CommonApiErrorModel>(responseError, collectionType)
            } catch (e: Exception) {
                AppUtils.INSTANCE?.logMe("Error Log", e.localizedMessage)
            }

            when (throwable.code()) {
                400 -> ApiError(
                    errorBody?.message?:throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.BAD_REQUEST
                )

                401 -> ApiError(
                    errorBody?.message?:throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.UNAUTHORIZED
                )

                403 -> ApiError(
                    errorBody?.message?:throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.FORBIDDEN
                )

                404 -> ApiError(
                    errorBody?.message?:throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.NOT_FOUND
                )

                405 -> ApiError(
                    errorBody?.message?:throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.METHOD_NOT_ALLOWED
                )

                409 -> ApiError(
                    errorBody?.message?:throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.CONFLICT
                )

                500 -> ApiError(
                    errorBody?.message?:throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.INTERNAL_SERVER_ERROR
                )

                else -> ApiError(
                    UNKNOWN_ERROR_MESSAGE,
                    0,
                    ApiError.ErrorStatus.UNKNOWN_ERROR
                )
            }
        }

        is SocketTimeoutException -> {
            ApiError(throwable.message, ApiError.ErrorStatus.TIMEOUT)
        }

        is IOException -> {
            ApiError(throwable.message, ApiError.ErrorStatus.NO_CONNECTION)
        }

        else -> ApiError(UNKNOWN_ERROR_MESSAGE, 0, ApiError.ErrorStatus.UNKNOWN_ERROR)
    }
}