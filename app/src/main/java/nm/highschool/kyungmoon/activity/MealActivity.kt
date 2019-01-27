package nm.highschool.kyungmoon.activity

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_meal.*
import nm.highschool.kyungmoon.R
import nm.highschool.kyungmoon.school.School
import nm.highschool.kyungmoon.school.SchoolException
import nm.highschool.kyungmoon.util.DataUtil
import java.util.*

class MealActivity: AppCompatActivity(), DatePickerDialog.OnDateSetListener
{
    var selectedYear: Int = 0
    var selectedMonth: Int = 0
    var selectedDay: Int = 0

    //라이프사이클
    override fun onCreate(savedInstanceState: Bundle?)
    {
        overridePendingTransition(R.anim.activity_scale_plus_to_zero, R.anim.activity_scale_zero_to_minus)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)

        initFlag()
        initDate()
        initMeal()
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
    private fun initDate()
    {
        val calendar = Calendar.getInstance()

        selectedYear = calendar.get(Calendar.YEAR)
        selectedMonth = calendar.get(Calendar.MONTH) + 1
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH)
    }
    private fun initMeal()
    {
        val meal = DataUtil.getString("$selectedYear.$selectedMonth.$selectedDay", DataUtil.MEAL, "null")

        if (meal == "null" || !meal.contains("#")) //다운로드 필요
        {
            val dialog = AlertDialog.Builder(this)

            with(dialog)
            {
                setTitle("급식정보 다운로드")
                setMessage("${selectedMonth}월의 급식 정보를 다운로드합니다.\n\n다운로드 이후 ${selectedMonth}월의 급식 정보는 데이터 없이 볼 수 있습니다.")
                setCancelable(false)
                setPositiveButton("다운로드") { _, _ -> LoadMealDataTask().execute() }
                setNegativeButton("취소") { _, _ -> meal_meal_viewer.text = "먼저 급식을 다운로드 받아주세요." }
                show()
            }
        }
        else //이미 다운로드됨
        {
            val lunch = if (!meal.split("#")[0].trim().isEmpty()) meal.split("#")[0] else "점심이 없습니다"
            val dinner = if (!meal.split("#")[1].trim().isEmpty()) meal.split("#")[1] else "저녁이 없습니다"

            meal_date_viewer.text = "${selectedMonth}월 ${selectedDay}일의 급식"
            meal_meal_viewer.text = "<오늘의 점심>\n$lunch\n\n<오늘의 저녁>\n$dinner".removeAllegyInfo()
        }
    }

    //클릭이벤트
    fun changeDate(view: View)
    {
        val dialog = DatePickerDialog.newInstance(this, selectedYear, selectedMonth - 1, selectedDay)
        dialog.show(supportFragmentManager, "날짜 변경")
    }
    fun reset(view: View)
    {
        val dialog = AlertDialog.Builder(this)

        with(dialog)
        {
            setTitle("급식 정보 초기화")
            setMessage("이전에 다운로드 받은 급식 정보를 지우고 업데이트된 새로운 급식 정보를 다운로드합니다.")
            setPositiveButton("확인") {_, _ ->
                DataUtil.remove(DataUtil.MEAL)
                initMeal()
            }
            setNegativeButton("취소", null)
            show()
        }
    }
    fun allergy(view: View)
    {
        val dialog = AlertDialog.Builder(this)

        with(dialog)
        {
            setTitle("알레르기 정보")
            setMessage("1. 난류\n2. 우유\n3. 메밀\n4. 땅콩\n5. 대두\n6. 밀\n7. 고등어\n8. 게\n9. 새우\n10. 돼지고기\n11. 복숭아\n12. 토마토\n13. 아황산염\n14. 호두\n15. 닭고기\n16. 소고기\n17. 오징어\n18. 조개류(굴, 전복, 홍합 포함)")
            setPositiveButton("확인", null)
            show()
        }
    }
    fun copy(view: View)
    {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("lunch", meal_meal_viewer.text.toString().removeAllegyInfo())
        clipboardManager.primaryClip = clipData

        Toast.makeText(this, "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
    }

    //쓰레드
    private inner class LoadMealDataTask : AsyncTask<Void, Void, Void>()
    {
        override fun onPreExecute()
        {
            meal_meal_viewer.text = "급식 정보를 불러오는 중입니다"
        }
        override fun doInBackground(vararg params: Void?): Void?
        {
            val meals = School(School.Type.HIGH, School.Region.SEOUL, "B100000378") //E100001786

            try
            {
                val monthlyMeal = meals.getMonthlyMenu(selectedYear, selectedMonth)
                var index = 0

                monthlyMeal.forEach {
                    DataUtil.put("$selectedYear.$selectedMonth.${index + 1}", DataUtil.MEAL, "${it.lunch}#${it.dinner}")
                    index++
                }
            }
            catch (e: SchoolException)
            {
                e.printStackTrace()

                runOnUiThread { Toast.makeText(this@MealActivity, "다운로드에 실패하였습니다.\n${e.message}", Toast.LENGTH_SHORT).show() }
            }

            return null
        }
        override fun onPostExecute(result: Void?)
        {
            initMeal()
        }
    }

    //메소드
    private fun String.removeAllegyInfo(): String
    {
        return this.replace("18.", "")
            .replace("17.", "")
            .replace("16.", "")
            .replace("15.", "")
            .replace("14.", "")
            .replace("13.", "")
            .replace("12.", "")
            .replace("11.", "")
            .replace("10.", "")
            .replace("9.", "")
            .replace("8.", "")
            .replace("7.", "")
            .replace("6.", "")
            .replace("5.", "")
            .replace("4.", "")
            .replace("3.", "")
            .replace("2.", "")
            .replace("1.", "")
    }

    //리스너
    override fun onDateSet(view: DatePickerDialog?, year: Int, month: Int, day: Int)
    {
        selectedYear = year
        selectedMonth = month + 1
        selectedDay = day

        initMeal()
    }
}