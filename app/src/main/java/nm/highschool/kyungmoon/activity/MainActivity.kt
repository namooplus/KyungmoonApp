package nm.highschool.kyungmoon.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import de.psdev.licensesdialog.LicensesDialog
import nm.highschool.kyungmoon.R

class MainActivity: AppCompatActivity()
{
    //라이프사이클
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFlag()
    }

    //설정
    private fun initFlag()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        else
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    //클릭 이벤트
    fun meal(view: View)
    {
        startActivity(Intent(this, MealActivity::class.java))
    }
    fun calendar(view: View)
    {
        startActivity(Intent(this, CalendarActivity::class.java))
    }
    fun school(view: View)
    {
        startActivity(Intent(this, SchoolActivity::class.java))
    }
    fun about(view: View)
    {
        val popup = PopupMenu(this, view)
        popup.menu.add("경문고 홈페이지")
        popup.menu.add("개발자 정보")
        popup.menu.add("오픈소스 라이센스")
        popup.setOnMenuItemClickListener { item ->
            when (item.title.toString())
            {
                "경문고 홈페이지" ->
                {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("http://www.kyungmoon.hs.kr/index.do")
                    startActivity(intent)
                }
                "개발자 정보" ->
                {
                    val dialog = AlertDialog.Builder(this@MainActivity)

                    with(dialog)
                    {
                        setTitle("개발자 정보")
                        setView(layoutInflater.inflate(R.layout.dialog_about_layout, null))
                        setPositiveButton("확인", null)
                        show()
                    }
                }
                "오픈소스 라이센스" ->
                {
                    LicensesDialog.Builder(this@MainActivity)
                        .setNotices(R.raw.license)
                        .build()
                        .show()
                }
            }
            true
        }
        popup.show()
    }
}
