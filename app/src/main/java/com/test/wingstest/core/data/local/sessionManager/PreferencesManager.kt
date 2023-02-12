package com.test.wingstest.core.data.local.sessionManager

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.wingstest.core.data.model.UserInformation
import com.test.wingstest.core.utils.SHARED_PREFERENCE_NAME
import com.test.wingstest.core.utils.USER_KEY
import com.test.wingstest.core.utils.checkStringNullOrNot
import javax.inject.Inject

class PreferencesManager @Inject constructor(private val context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun saveUserKey(data: UserInformation) {
        val jsonText = Gson().toJson(data)
        editor.putString(USER_KEY, jsonText).apply()
    }

    fun getUserKey(): UserInformation? {
        return try {
            val jsonText = sharedPref.getString(USER_KEY, "")
            return if (checkStringNullOrNot(jsonText)) {
                return Gson().fromJson(jsonText, object : TypeToken<UserInformation>() {}.type)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    fun logout() = editor.clear()
}