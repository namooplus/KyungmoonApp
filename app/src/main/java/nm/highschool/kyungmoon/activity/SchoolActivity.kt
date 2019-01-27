package nm.highschool.kyungmoon.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import kotlinx.android.synthetic.main.activity_school.*
import nm.highschool.kyungmoon.R

class SchoolActivity: AppCompatActivity()
{
    //라이프사이클
    override fun onCreate(savedInstanceState: Bundle?)
    {
        overridePendingTransition(R.anim.activity_scale_plus_to_zero, R.anim.activity_scale_zero_to_minus)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school)

        initFlag()
        initImage()
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
    private fun initImage()
    {
        val imageLoader = ImageLoader.getInstance()
        imageLoader.init(ImageLoaderConfiguration.createDefault(this))
        imageLoader.displayImage("drawable://" + R.drawable.icon_school, school_icon_viewer)
        imageLoader.displayImage("drawable://" + R.drawable.background_school, school_background_viewer)
    }

    //클릭이벤트
    fun back(view: View)
    {
        finish()
    }
}