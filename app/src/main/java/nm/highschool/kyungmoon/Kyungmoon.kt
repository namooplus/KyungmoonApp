package nm.highschool.kyungmoon

import android.app.Application
import android.content.Context

class Kyungmoon : Application()
{
    init
    {
        instance = this
    }
    companion object
    {
        private var instance: Kyungmoon? = null

        val context : Context
            get() = instance!!.applicationContext
    }
}