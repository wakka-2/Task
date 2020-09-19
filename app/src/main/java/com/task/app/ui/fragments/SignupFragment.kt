package com.task.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.task.app.R
import com.task.app.model.User
import com.task.app.room.UserViewModel
import com.task.app.ui.fragments.SignupFragmentDirections.Companion.actionSignupFragmentToMainFragment
import com.task.app.util.base.BaseFragment
import com.task.app.util.shared.SharedPrefsUtil
import kotlinx.android.synthetic.main.fragment_signup.*


class SignupFragment : BaseFragment() {

    private lateinit var mUserViewModel: UserViewModel

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

    }

    private fun createNewUser(view: View) {
        if (inputCheck()) {
            val user = User(
                idNum = etId.text.trim().toString().toLong(),
                name = etName.text.trim().toString(),
                phoneNum = etPhone.text.trim().toString(),
                email = etEmail.text.trim().toString(),
                etPass.text.trim().toString()
            )
            view.findNavController().navigate(actionSignupFragmentToMainFragment())

            mUserViewModel.registerUser(user)
            SharedPrefsUtil.storeUser(requireContext(), user)
        }
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
                etPass.error = resources.getString(R.string.error_confirm)
                false
            }
            else -> {
                true
            }
        }
    }
}