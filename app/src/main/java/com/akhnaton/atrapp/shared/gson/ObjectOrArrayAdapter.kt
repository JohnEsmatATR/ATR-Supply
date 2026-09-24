package com.akhnaton.atrapp.shared.gson

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ObjectOrArrayAdapter<T>(
    private val gson: Gson
) : JsonDeserializer<T?> {

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
            return gson.fromJson(json, typeOfT)
        }

        return null
    }
}