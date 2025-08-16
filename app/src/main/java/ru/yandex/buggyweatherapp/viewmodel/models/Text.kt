package ru.yandex.buggyweatherapp.viewmodel.models

import androidx.annotation.StringRes

sealed interface Text {

    data class Value(val value: String) : Text

    data class Resource(@StringRes val resId: Int) : Text
}