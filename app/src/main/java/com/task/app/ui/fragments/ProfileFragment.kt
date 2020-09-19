package com.task.app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.task.app.R
import com.task.app.model.User
import com.task.app.ui.fragments.ProfileFragmentDirections.Companion.actionProfileFragmentToRecordsFragment
import com.task.app.util.base.BaseFragment
import com.task.app.util.shared.SharedPrefsUtil
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : BaseFragment() {
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = SharedPrefsUtil.fetchUser(context)
        navigationClicks(view)
        setupData(user)
        requireActivity().setTitle(R.string.my_profile)

    }

    private fun setupData(user: User?) {
        tvName.text = user?.name
        tvId.text = user?.idNum.toString()
        tvPhone.text = user?.phoneNum
        tvEmail.text = user?.email
    }

    private fun navigationClicks(view: View) {
        btnRecords.setOnClickListener {
            view.findNavController().navigate(actionProfileFragmentToRecordsFragment())
        }
    }
}