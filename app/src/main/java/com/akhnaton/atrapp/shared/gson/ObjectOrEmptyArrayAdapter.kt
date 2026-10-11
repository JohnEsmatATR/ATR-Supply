package com.akhnaton.atrapp.shared.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ObjectOrEmptyArrayAdapter<T> : JsonDeserializer<T?> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): T? {

        if (json.isJsonNull) {
            return null
        }

        if (json.isJsonArray) {
            return null
        }

        if (json.isJsonObject) {
            return context.deserialize(json, typeOfT)
        }

        return null
    }
}