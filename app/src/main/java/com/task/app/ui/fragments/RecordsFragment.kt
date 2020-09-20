package com.task.app.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.task.app.R
import com.task.app.adapter.CourseAdapter
import com.task.app.model.Course
import com.task.app.model.User
import com.task.app.room.UserViewModel
import com.task.app.ui.fragments.RecordsFragmentDirections.Companion.actionRecordsFragmentToAddCourseFragment
import com.task.app.util.base.BaseFragment
import com.task.app.util.interfaces.ClickListener
import com.task.app.util.shared.SharedPrefsUtil
import kotlinx.android.synthetic.main.fragment_records.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class RecordsFragment : BaseFragment() {
    private lateinit var items: ArrayList<Course>
    private lateinit var mUserViewModel: UserViewModel
    private var user: User? = null
    private val PERMISSION_REQUEST_CODE = 200

    private var bitmap:Bitmap ?= null
    private var position:Int ?= null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_records, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        user = SharedPrefsUtil.fetchUser(context)
        requireActivity().setTitle(R.string.course_record)


        navigationClicks(view)
        initRecycler()
    }

    private fun navigationClicks(view: View) {
        fabAdd.setOnClickListener {
            view.findNavController().navigate(actionRecordsFragmentToAddCourseFragment())
        }
    }

    private fun initRecycler() {

        user?.idNum?.let { it1 ->
            mUserViewModel.getAllCourses(userId = it1).observe(viewLifecycleOwner, {
                if (it != null) {
                    items = it as ArrayList<Course>

                    // Setup Recyclerview's Layout
                    rvCourses.setHasFixedSize(true)

                    // Add Item Touch Listener
                    val adapter = CourseAdapter(items, object : ClickListener {
                        override fun onClick(position: Int, uri: String) {
                            val bitmap = when {
                                Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                                    requireActivity().contentResolver,
                                    Uri.parse(uri)
                                )
                                else -> {
                                    val source = ImageDecoder.createSource(
                                        requireContext().contentResolver,
                                        Uri.parse(uri)
                                    )
                                    ImageDecoder.decodeBitmap(source)
                                }
                            }
                            this@RecordsFragment.bitmap = bitmap
                            this@RecordsFragment.position = position
                            if(checkPermissionWRITE_EXTERNAL_STORAGE(requireContext())) {
                                val path = saveImage(bitmap, items[position].id.toString())
                                showSnackBar(requireView(), resources.getString(R.string.downloaded,path))
                            }
                        }

                    })
                    rvCourses.adapter = adapter
                }
            })
        }

    }

    @Throws(IOException::class)
    private fun saveImage(bitmap: Bitmap, name: String): String {
        var path = ""
        val fos: OutputStream?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver: ContentResolver = requireContext().contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$name.jpg")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = Objects.requireNonNull(imageUri)?.let { resolver.openOutputStream(it) }
        } else {
            val imagesDir: String =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString()
            val image = File(imagesDir, "Task_cert$name.jpg")
            fos = FileOutputStream(image)
            path = "$imagesDir    Task_cert$name.jpg"
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        (fos)?.close()
        return path
    }

    private fun checkPermissionWRITE_EXTERNAL_STORAGE(context: Context?): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    showDialogPermission(
                        "External storage", context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                } else {
                    requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_CODE
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
        val alertBuilder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle(resources.getString(R.string.permission_necessary))
        alertBuilder.setMessage("$msg permission is necessary")
        alertBuilder.setPositiveButton(
            android.R.string.yes
        ) { _, _ ->
            requestPermissions(
                arrayOf(permission),
                PERMISSION_REQUEST_CODE
            )
        }
        val alert: android.app.AlertDialog = alertBuilder.create()
        alert.show()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.isNotEmpty() && grantResults.isNotEmpty()) {
                var flag = true
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false
                    }
                }
                if (flag) {
                    if(bitmap != null && position != null) {
                        saveImage(bitmap!!, items[position!!].id.toString())
                    }
                } else {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                }
            }
        }
    }
}