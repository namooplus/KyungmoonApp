package nm.highschool.kyungmoon.util

import android.content.Context
import nm.highschool.kyungmoon.Kyungmoon.Companion.context

object DataUtil
{
    const val DEFAULT = "default"
    const val MEAL = "meal"

    fun put(name: String, location: String, data: Any)
    {
        val sharedPreference = context.getSharedPreferences(location, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()

        when (data)
        {
            is String -> editor.putString(name, data)
            is Int -> editor.putInt(name, data)
            is Boolean -> editor.putBoolean(name, data)
        }

        editor.apply()
    }
    fun getString(name: String, location: String, defaultValue: String = ""): String = context.getSharedPreferences(location, Context.MODE_PRIVATE).getString(name, defaultValue) ?: defaultValue
    fun getInt(name: String, location: String, defaultValue: Int = 0): Int = context.getSharedPreferences(location, Context.MODE_PRIVATE).getInt(name, defaultValue)
    fun getBoolean(name: String, location: String, defaultValue: Boolean = false): Boolean = context.getSharedPreferences(location, Context.MODE_PRIVATE).getBoolean(name, defaultValue)

    fun remove(location: String)
    {
        val sharedPreference = context.getSharedPreferences(location, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()

        editor.clear()
        editor.apply()
    }
}