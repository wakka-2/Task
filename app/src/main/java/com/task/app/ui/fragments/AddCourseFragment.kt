package com.task.app.ui.fragments

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.task.app.R
import com.task.app.model.Course
import com.task.app.model.User
import com.task.app.room.UserViewModel
import com.task.app.util.base.BaseFragment
import com.task.app.util.shared.SharedPrefsUtil
import kotlinx.android.synthetic.main.fragment_add_course.*
import java.util.*

class AddCourseFragment : BaseFragment() {

    private lateinit var city:String
    private lateinit var mUserViewModel: UserViewModel
    private var user: User?= null
    private var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private var date: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_course, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = SharedPrefsUtil.fetchUser(context)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setupCitySpinner()
        showDate()
        btnSave.setOnClickListener {
            if(inputCheck()){
                addCourse(view)
            }
        }
    }

    private fun addCourse(view: View) {
        val course = user?.idNum?.let { it1 ->
            Course(
                id = 0,
                idNum = it1,
                title = etTitle.text.trim().toString(),
                city = city,
                date = etDate.text.trim().toString(),
                duration = etDuration.text.trim().toString(),
                rating = etRating.text.trim().toString()
            )
        }
        view.findNavController().popBackStack()

        course?.let { it1 -> mUserViewModel.addCourse(it1) }
    }

    private fun setupCitySpinner() {
        val stringArray = resources.getStringArray(R.array.cities_list)
        val adapter: SpinnerAdapter = ArrayAdapter(
            requireContext(), R.layout.spinner_item,
            stringArray
        )
        spCity.adapter = adapter

        spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                city = stringArray[position]
            }

        }

    }

    private fun inputCheck(): Boolean {
        return when {
            etTitle.text.isEmpty() -> {
                etTitle.error = resources.getString(R.string.title_error)
                false
            }
            etIssued.text.isEmpty() -> {
                etIssued.error = resources.getString(R.string.issed_by_error)
                false
            }
            etDate.text.isEmpty() -> {
                etDate.error = resources.getString(R.string.date_error)
                false
            }
            etDuration.text.isEmpty() -> {
                etDuration.error = resources.getString(R.string.duration_error)
                false
            }
            etRating.text.isEmpty() -> {
                etRating.error = resources.getString(R.string.rating_error)
                false
            }
            city.isEmpty() ->{
                showSnackBar(requireView(),resources.getString(R.string.city_error))
                false
            }
            else -> {
                true
            }
        }
    }
    private fun showDate() {
        etDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
        mDateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            var month = month
            month += 1
            if (day < 10 && month < 10) {
                val s = "0$day"
                val s2 = "0$month"
                date = "$year-$s2-$s"
            } else {
                date = if (month < 10) {
                    val s2 = "0$month"
                    "$year-$s2-$day"
                } else {
                    if (day < 10) {
                        val s = "0$day"
                        "$year-$month-$s"
                    } else {
                        "$year-$month-$day"
                    }
                }
            }
            etDate.setText(date)
        }
    }
}