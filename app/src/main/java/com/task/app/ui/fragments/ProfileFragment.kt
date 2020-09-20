package com.task.app.ui.fragments

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.task.app.R
import com.task.app.model.User
import com.task.app.ui.fragments.ProfileFragmentDirections.Companion.actionProfileFragmentToRecordsFragment
import com.task.app.util.base.BaseFragment
import com.task.app.util.shared.SharedPrefsUtil
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
        if (user?.image?.isNotEmpty()!!) {
            val bitmap = when {
                Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver,
                    Uri.parse(user.image)
                )
                else -> {
                    val source = ImageDecoder.createSource(
                        requireContext().contentResolver,
                        Uri.parse(user.image)
                    )
                    ImageDecoder.decodeBitmap(source)
                }
            }
            ivProfile.setImageBitmap(bitmap)
        }
    }

    private fun navigationClicks(view: View) {
        btnRecords.setOnClickListener {
            view.findNavController().navigate(actionProfileFragmentToRecordsFragment())
        }
    }
}