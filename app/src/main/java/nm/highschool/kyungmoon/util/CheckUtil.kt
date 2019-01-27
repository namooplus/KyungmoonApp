package nm.highschool.kyungmoon.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import nm.highschool.kyungmoon.Kyungmoon.Companion.context

object CheckUtil
{
    val isNetworkAvailable: Boolean
        get()
        {
            val activeNetwork: NetworkInfo? = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo

            return activeNetwork?.isConnected ?: false
        }
}