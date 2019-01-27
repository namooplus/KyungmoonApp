package nm.highschool.kyungmoon.activity

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_calendar.*
import nm.highschool.kyungmoon.R
import nm.highschool.kyungmoon.util.CheckUtil

class CalendarActivity: AppCompatActivity()
{
    //라이프사이클
    override fun onCreate(savedInstanceState: Bundle?)
    {
        overridePendingTransition(R.anim.activity_scale_plus_to_zero, R.anim.activity_scale_zero_to_minus)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        initFlag()
        initWeb()
        loadWeb()
    }
    override fun finish()
    {
        super.finish()
        overridePendingTransition(R.anim.activity_scale_minus_to_zero, R.anim.activity_scale_zero_to_plus)
    }

    //설정
    private fun initFlag()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        else
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    private fun initWeb()
    {
        calendar_webview.settings.javaScriptEnabled = true
        calendar_webview.settings.setSupportZoom(false)
        calendar_webview.webViewClient = object: WebViewClient()
        {
            override fun onPageStarted(view: WebView, url:String, favicon: Bitmap?)
            {
                super.onPageStarted(view, url, favicon)

                calendar_loading_view.visibility = View.VISIBLE
            }
            override fun onPageFinished(view: WebView, url:String)
            {
                super.onPageFinished(view, url)

                calendar_webview.loadUrl("javascript:var con = document.getElementById('Header'); con.parentNode.removeChild(con); " +
                            "javascript:var con2 = document.getElementById('section_3'); con2.parentNode.removeChild(con2);" +
                            "javascript:var con3 = document.getElementById('cntTitle'); con3.parentNode.removeChild(con3);" +
                            "javascript:var con4 = document.getElementById('Footer'); con4.parentNode.removeChild(con4);" +
                            "javascript:var con5 = document.getElementById('SkipNavigation'); con5.parentNode.removeChild(con5);")

                calendar_webview.setBackgroundColor(Color.TRANSPARENT)
                calendar_webview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)
            }
        }
        calendar_webview.webChromeClient = object: WebChromeClient()
        {
            override fun onProgressChanged(view: WebView, newProgress:Int)
            {
                super.onProgressChanged(view, newProgress)

                if (newProgress >= 100)
                    calendar_loading_view.visibility = View.GONE
            }
        }
    }
    private fun loadWeb()
    {
        if (CheckUtil.isNetworkAvailable)
            calendar_webview.loadUrl("http://www.kyungmoon.hs.kr/72566/subMenu.do")

        else
            calendar_warning_view.visibility = View.VISIBLE
    }

    //클릭이벤트
    fun back(view: View)
    {
        finish()
    }
}