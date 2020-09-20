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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.task.app.R
import com.task.app.model.User
import com.task.app.room.UserViewModel
import com.task.app.ui.fragments.SignupFragmentDirections.Companion.actionSignupFragmentToMainFragment
import com.task.app.util.Tools
import com.task.app.util.base.BaseFragment
import com.task.app.util.shared.SharedPrefsUtil
import kotlinx.android.synthetic.main.fragment_signup.*
import java.io.ByteArrayOutputStream


class SignupFragment : BaseFragment() {

    private lateinit var mUserViewModel: UserViewModel
    private val RC_SELECT_IMAGE = 100
    private lateinit var user: User
    private var selectedImageUri: String = ""
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        btnNewUser.setOnClickListener {
            createNewUser(view)
        }
        btnChoosePic.setOnClickListener {
            if (checkPermissionREAD_EXTERNAL_STORAGE(requireContext())) {
                choosePic()
            }
        }


    }

    private fun createNewUser(view: View) {
        if (inputCheck()) {
            mUserViewModel.existsId(etId.text.trim().toString().toLong())
                .observe(viewLifecycleOwner, {
                    mUserViewModel.existsEmail(etEmail.text.trim().toString())
                        .observe(viewLifecycleOwner, { it1 ->
                            if (!it1) {
                                if (!it) {
                                    user = User(
                                        idNum = etId.text.trim().toString().toLong(),
                                        name = etName.text.trim().toString(),
                                        phoneNum = etPhone.text.trim().toString(),
                                        email = etEmail.text.trim().toString(),
                                        password = etPass.text.trim().toString(),
                                        image = selectedImageUri
                                    )
                                    view.findNavController()
                                        .navigate(actionSignupFragmentToMainFragment())

                                    mUserViewModel.registerUser(user)
                                    SharedPrefsUtil.storeUser(requireContext(), user)
                                } else {
                                    showSnackBar(
                                        requireView(),
                                        resources.getString(R.string.id_registered)
                                    )
                                }
                            } else {
                                showSnackBar(
                                    requireView(),
                                    resources.getString(R.string.email_registered)
                                )
                            }
                        })
                })
        }


    }

    private fun checkExistId(): Boolean {
        var exist = false
        mUserViewModel.existsId(etId.text.trim().toString().toLong())
            .observe(viewLifecycleOwner, {
                exist = it
            })
        return exist
    }

    private fun checkExistEmail(): Boolean {
        var exist = false
        mUserViewModel.existsEmail(etEmail.text.trim().toString())
            .observe(viewLifecycleOwner, {
                exist = it
            })
        return exist
    }

    private fun inputCheck(): Boolean {
        return when {
            etId.text.isEmpty() -> {
                etId.error = resources.getString(R.string.id_error)
                false
            }
            etName.text.isEmpty() -> {
                etName.error = resources.getString(R.string.name_error)
                false
            }
            etPhone.text.isEmpty() -> {
                etPhone.error = resources.getString(R.string.error_phone)
                false
            }
            etEmail.text.isEmpty() -> {
                etEmail.error = resources.getString(R.string.email_error)
                false
            }
            etPass.text.isEmpty() -> {
                etPass.error = resources.getString(R.string.error_password)
                false
            }
            etPass.text.length < 6 -> {
                etPass.error = resources.getString(R.string.error_pass_length)
                false
            }
            etConfirmPass.text.toString() != etPass.text.toString() -> {
                etConfirmPass.error = resources.getString(R.string.error_confirm)
                false
            }
            !Tools.isSaudiNumber(etPhone.text.toString()) -> {
                etPhone.error = resources.getString(R.string.phone_invalid)
                false
            }
            !Tools.validateEmail(etEmail.text.trim().toString()) -> {
                etEmail.error = resources.getString(R.string.email_invalid)
                false
            }
            selectedImageUri.isEmpty() ->{
                showSnackBar(requireView(), resources.getString(R.string.image_error))
                false
            }
            etId.text.length < 10 -> {
                etId.error = resources.getString(R.string.id_invalid)
                false
            }
            else -> {
                true
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
            //convert to byte array
            val outputstream = ByteArrayOutputStream()
            //compress selected image
            selectedImageBmp.compress(Bitmap.CompressFormat.WEBP, 90, outputstream)
            val selectedImageBytes = outputstream.toByteArray()
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
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
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
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (permissions.isNotEmpty() && grantResults.isNotEmpty()) {
                var flag = true
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false
                    }
                }
                if (flag) {
                    choosePic()
                } else {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                }
            }
        }
    }
}