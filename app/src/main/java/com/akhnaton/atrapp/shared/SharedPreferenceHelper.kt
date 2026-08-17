package com.akhnaton.atrapp.shared

import android.content.Context
import android.content.SharedPreferences
import com.akhnaton.atrapp.data.model.auth.LoginModel
import com.google.gson.Gson
import java.util.Locale

object SharedPreferenceHelper {

    private const val mySharedPreferenceName = "ROVI"
    private var mAppContext: Context? = null
    private const val mySharedPreference_token = "token"
    private const val mySharedPreference_firebaseToken = "firebaseToken"
    private const val mySharedPreference_userObj = "user"
    private const val mySharedPreference_logged = "isLogged"
    private const val mySharedPreference_language = "language"
    private const val mySharedPreference_version = "version"

    fun init(appContext: Context?) {
        mAppContext = appContext
    }

    private val sharedPreferences: SharedPreferences
        get() = mAppContext!!.getSharedPreferences(
            mySharedPreferenceName,
            Context.MODE_PRIVATE
        )
    var userToken: String?
        get() = sharedPreferences.getString(mySharedPreference_token, "")
        set(token) {
            val editor = sharedPreferences.edit()
            editor.putString(mySharedPreference_token, token).apply()
        }
    var firebaseToken: String?
        get() = sharedPreferences.getString(mySharedPreference_firebaseToken, "")
        set(token) {
            val editor = sharedPreferences.edit()
            editor.putString(mySharedPreference_firebaseToken, token).apply()
        }
    var language: String?
        get() = sharedPreferences.getString(mySharedPreference_language, "ar")
        set(language) {
            val editor = sharedPreferences.edit()
            editor.putString(mySharedPreference_language, language).apply()
        }
    var version: String?
        get() = sharedPreferences.getString(mySharedPreference_version, "0.0")
        set(language) {
            val editor = sharedPreferences.edit()
            editor.putString(mySharedPreference_version, language).apply()
        }
    var isLogged: Boolean?
        get() = sharedPreferences.getBoolean(mySharedPreference_logged, false)
        set(loged) {
            val editor = sharedPreferences.edit()
            editor.putBoolean(mySharedPreference_logged, loged!!).apply()
        }


    var userObj: LoginModel?
        get() {
            val gson = Gson()
            val user = sharedPreferences.getString(mySharedPreference_userObj, "")
            return gson.fromJson(user, LoginModel::class.java)
        }
        set(value) {
            val gson = Gson()
            val json = gson.toJson(value)
            val editor = sharedPreferences.edit()
            editor.putString(mySharedPreference_userObj, json).apply()

        }

    fun setLocale(context: Context, langCode: String): Context {
        language = langCode
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        return context
    }

}