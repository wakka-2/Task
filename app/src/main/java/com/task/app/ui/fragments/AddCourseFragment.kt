package com.task.app.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.task.app.R
import com.task.app.model.Course
import com.task.app.model.User
import com.task.app.room.UserViewModel
import com.task.app.util.base.BaseFragment
import com.task.app.util.shared.SharedPrefsUtil
import com.zeugmasolutions.localehelper.LocaleHelper
import kotlinx.android.synthetic.main.fragment_add_course.*
import net.alhazmy13.hijridatepicker.date.gregorian.GregorianDatePickerDialog
import net.alhazmy13.hijridatepicker.date.hijri.HijriDatePickerDialog
import java.io.ByteArrayOutputStream
import java.util.*

class AddCourseFragment : BaseFragment(), HijriDatePickerDialog.OnDateSetListener,
    GregorianDatePickerDialog.OnDateSetListener {

    private lateinit var city:String
    private lateinit var mUserViewModel: UserViewModel
    private var user: User?= null
    private var date: String? = null
    private val RC_SELECT_IMAGE = 102
    private var selectedImageUri: String = ""
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 124


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_course, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setTitle(R.string.add_course)
        user = SharedPrefsUtil.fetchUser(context)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setupCitySpinner()
        showDate()
        btnSave.setOnClickListener {
            if(inputCheck()){
                addCourse(view)
            }
        }
        btnAttach.setOnClickListener {
            if (checkPermissionREAD_EXTERNAL_STORAGE(requireContext())) {
                choosePic()
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
                rating = etRating.text.trim().toString(),
                certificateImg = selectedImageUri
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

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
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
                showSnackBar(requireView(), resources.getString(R.string.city_error))
                false
            }
            selectedImageUri.isEmpty() ->{
                showSnackBar(requireView(), resources.getString(R.string.certificate_error))
                false
            }
            else -> {
                true
            }
        }
    }
    private fun showDate() {
        etDate.setOnClickListener {
            if (LocaleHelper.getLocale(requireContext()).displayLanguage == "English") {
                val now = Calendar.getInstance()
                val dpd: GregorianDatePickerDialog = GregorianDatePickerDialog.newInstance(
                    this,
                    now[Calendar.YEAR],
                    now[Calendar.MONTH],
                    now[Calendar.DAY_OF_MONTH]
                )
                dpd.show(requireFragmentManager(), "GregorianDatePickerDialog")
                dpd.vibrate(false)
                dpd.autoDismiss(true)
            }else{
                val now = UmmalquraCalendar()
                val dpd = HijriDatePickerDialog.newInstance(
                    this,
                    now[UmmalquraCalendar.YEAR],
                    now[UmmalquraCalendar.MONTH],
                    now[UmmalquraCalendar.DAY_OF_MONTH]
                )
                dpd.show(requireFragmentManager(), "HijriDatePickerDialog")
                dpd.vibrate(false)
                dpd.autoDismiss(true)
            }
        }
    }
    private fun choosePic() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).also {
            it.addCategory(Intent.CATEGORY_OPENABLE)
            it.type = "image/*"
            it.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Choose Picture"
            ), RC_SELECT_IMAGE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            selectedImageUri = data.data.toString()
            val selectedImageBmp = MediaStore.Images.Media.getBitmap(
                context?.contentResolver,
                data.data
            )
            requireContext().contentResolver.takePersistableUriPermission(
                data.data!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }
    }

    private fun checkPermissionREAD_EXTERNAL_STORAGE(context: Context?): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    showDialogPermission(
                        "External storage", context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        context, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    )
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }

    private fun showDialogPermission(
        msg: String, context: Context?,
        permission: String
    ) {
        val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle(resources.getString(R.string.permission_necessary))
        alertBuilder.setMessage("$msg permission is necessary")
        alertBuilder.setPositiveButton(
            android.R.string.yes
        ) { _, _ ->
            ActivityCompat.requestPermissions(
                context as Activity, arrayOf(permission),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        }
        val alert: AlertDialog = alertBuilder.create()
        alert.show()
    }

    override fun onDateSet(
        view: HijriDatePickerDialog?,
        year: Int,
        month: Int,
        day: Int
    ) {
        var month = month
        month += 1
        if (day < 10 && month < 10) {
            val s = "0$day"
            val s2 = "0$month"
            date = "$year/$s2/$s"
        } else {
            date = if (month < 10) {
                val s2 = "0$month"
                "$year/$s2/$day"
            } else {
                if (day < 10) {
                    val s = "0$day"
                    "$year/$month/$s"
                } else {
                    "$year/$month/$day"
                }
            }
        }
        etDate.setText(date)
    }

    override fun onDateSet(
        view: GregorianDatePickerDialog?,
        year: Int,
        month: Int,
        day: Int
    ) {
        var month = month
        month += 1
        if (day < 10 && month < 10) {
            val s = "0$day"
            val s2 = "0$month"
            date = "$year/$s2/$s"
        } else {
            date = if (month < 10) {
                val s2 = "0$month"
                "$year/$s2/$day"
            } else {
                if (day < 10) {
                    val s = "0$day"
                    "$year/$month/$s"
                } else {
                    "$year/$month/$day"
                }
            }
        }
        etDate.setText(date)
    }
}