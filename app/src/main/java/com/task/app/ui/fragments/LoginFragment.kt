package com.task.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.task.app.R
import com.task.app.room.UserViewModel
import com.task.app.ui.fragments.LoginFragmentDirections.Companion.actionLoginFragmentToMainFragment
import com.task.app.ui.fragments.LoginFragmentDirections.Companion.actionLoginFragmentToSignupFragment
import com.task.app.util.base.BaseFragment
import com.task.app.util.shared.SharedPrefsUtil
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(SharedPrefsUtil.getBoolean(requireContext(),"isSignedIn",false)) {
            findNavController().navigate(actionLoginFragmentToMainFragment())
        }
            // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        navigationClicks(view)

    }

    private fun navigationClicks(view: View) {
        tvCreateNewAccount.setOnClickListener {
            view.findNavController().navigate(actionLoginFragmentToSignupFragment())
        }
        btnLogin.setOnClickListener {
            if(checkInput())
            login(view)
        }
    }

    private fun login(view: View) {
        mUserViewModel.login(etEmail.text.trim().toString(), etPassword.text.trim().toString())
            ?.observe(viewLifecycleOwner, { user ->
                if (user != null) {
                    if (checkBox.isChecked) {
                        SharedPrefsUtil.saveBoolean(requireContext(), "isSignedIn", true)
                    }
                    SharedPrefsUtil.storeUser(requireContext(), user)
                    view.findNavController().navigate(actionLoginFragmentToMainFragment())
                } else {
                    showSnackBar(view, resources.getString(R.string.error_login))
                }
            })

    }

    private fun checkInput(): Boolean {
        return when{
            etEmail.text.isEmpty() ->{
                etEmail.error = resources.getString(R.string.email_error)
                false
            }
            etPassword.text.isEmpty() ->{
                etEmail.error = resources.getString(R.string.error_password)
                false
            }
            else -> {
                true
            }
        }
    }
}