package com.posytron.avacycmp.utils

import org.json.JSONArray
import org.json.JSONObject

internal object JsonUtils {
    fun mapToJson(map: MutableMap<String?, Any>?): JSONObject {
        return JSONObject(map as Map<*, *>)
    }

    fun jsonToMap(json: JSONObject?): MutableMap<String?, Any?> {
        var retMap: MutableMap<String?, Any?> = HashMap()
        if (json != null) {
            retMap = toMap(json)
        }
        return retMap
    }

    private fun toMap(`object`: JSONObject): MutableMap<String?, Any?> {
        val map: MutableMap<String?, Any?> = HashMap()
        val keysItr: Iterator<String> = `object`.keys().iterator()
        while (keysItr.hasNext()) {
            val key = keysItr.next()
            var value = `object`[key]
            if (value is JSONArray) {
                value = toList(value)
            } else if (value is JSONObject) {
                value = toMap(value)
            }
            map[key] = value
        }
        return map
    }

    private fun toList(array: JSONArray): List<Any> {
        val list: MutableList<Any> = ArrayList()
        for (i in 0 until array.length()) {
            var value = array[i]
            if (value is JSONArray) {
                value = toList(value)
            } else if (value is JSONObject) {
                value = toMap(value)
            }
            list.add(value)
        }
        return list
    }
}