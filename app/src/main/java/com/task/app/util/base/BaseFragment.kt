package com.task.app.util.base

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.task.app.ui.activity.MainActivity

import java.util.*

abstract class BaseFragment : Fragment() {
    //private var waitingDialog: SpotsDialog? = null

    fun hideKeyboard() {
        val imm =
            this.requireContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }


    fun removeBackStackItem() {
        (activity as MainActivity).removeBackStackItem()

    }

    fun addFragmentWithResultData(BackFragment: Fragment, targetFragment: Fragment, code: Int) {
        (activity as MainActivity).addFragmentWithResultData(BackFragment, targetFragment, code)
    }

    fun addFragmentWithResultDataBundle(
        backFragment: Fragment,
        targetFragment: Fragment,
        code: Int,
        fragmentManager: FragmentManager,
        bundle: Bundle
    ) {
        (activity as MainActivity).addFragmentWithResultDataBundle(
            backFragment=backFragment,
            targetFragment=targetFragment,
            code=code,
            fragmentManager = fragmentManager,
            bundle=bundle
        )
    }

    fun addFragment(targetFragment: Fragment, fragmentManager: FragmentManager) {
        (activity as MainActivity).addFragment(
            targetFragment = targetFragment,
            fragmentManager = fragmentManager
        )
    }

    fun addFragmentBundle(targetFragment: Fragment, fragmentManager: FragmentManager, b: Bundle) {
        (activity as MainActivity).addFragmentBundle(
            targetFragment = targetFragment, fragmentManager = fragmentManager, b = b
        )
    }

    fun hideAllFragments() {
        (activity as MainActivity).hideAllFragments()
    }

    fun scaleAnim(view: View) {
        (context as MainActivity).scaleAnim(view)
    }

    fun showLongToast(strMsg: String = "", intMsg: Int = 0) {
        if (strMsg.isNotEmpty()) {
            Toast.makeText(context, strMsg, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, intMsg, Toast.LENGTH_LONG).show()
        }
    }


    fun showSnackBar(view: View, strMsg: String = "", intMsg: Int = 0) {
        val imm = this.requireContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        if (strMsg.isNotEmpty()) {
            Snackbar.make(view, strMsg, Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(view, intMsg, Snackbar.LENGTH_LONG).show()
        }

    }

    fun printer(message: String) {
        Log.d("test3", message)
    }

    fun getDuration(filePath: String): String {
        val uri: Uri = Uri.parse(filePath)
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(context?.applicationContext, uri)
        val durationStr =
            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val millSecond = durationStr.toInt() + 1000
        val seconds = (millSecond % (1000 * 60 * 60) % (1000 * 60) / 1000)
        val minutes = (seconds % 3600) / 60
        return String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds)
    }

    override fun onStart() {
        super.onStart()
        setOnBackPressed(null)
    }

    //Method must be declared as open, for overriding in child class
    open fun setOnBackPressed(onBackAlternative: (() -> Unit)?) {
        (activity as BaseActivity).onBackPressAlternative = onBackAlternative
    }

}