package io.adekvatn0.eloservice.dto

data class Result<T>(
    val status: Status,
    val entity: T? = null,
    val details: String? = null
) {

    companion object {
        fun <T> success(entity: T?): Result<T> {
            return Result(Status.SUCCESS, entity = entity)
        }

        fun <T> error(details: String): Result<T> {
            return Result(Status.ERROR, details = details)
        }
    }

    enum class Status {
        SUCCESS,
        ERROR
    }
}