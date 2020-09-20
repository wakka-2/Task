package com.task.app.ui.fragments

import android.animation.Animator
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.task.app.R
import com.task.app.ui.activity.MainActivity
import com.task.app.ui.fragments.MainFragmentDirections.Companion.actionMainFragmentToLoginFragment
import com.task.app.util.Locales
import com.task.app.util.base.BaseFragment
import com.task.app.util.shared.SharedPrefsUtil
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class MainFragment : BaseFragment() {
    private val listener = NavController.OnDestinationChangedListener { _, _, _ ->
        hideAllFragments()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        lifecycleScope.launch {
            delay(500)
            removeViewAnim()
        }

        navView.menu!!.findItem(R.id.changeLang).setOnMenuItemClickListener { menuItem: MenuItem? ->
            //write your implementation here
            changeLanguage()
            //to close the navigation drawer
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
//            Toast.makeText(applicationContext, "single item click listener implemented", Toast.LENGTH_SHORT).show()
            false
        }
    }
    private fun changeLanguage() {
        if (Locale.getDefault().displayLanguage == "English") {
            (activity as MainActivity).updateLocale(Locales.arabic)
        } else {
            (activity as MainActivity).updateLocale(Locales.english)
        }
    }
    private fun initViews() {
        (context as MainActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        val navController = this.requireActivity().findNavController(R.id.mainFragmentHostFragment)
        NavigationUI.setupWithNavController(toolbar, navController, drawerLayout)
        val menu = drawerLayout.navView.menu
        val item = menu.findItem(R.id.logoutFragment)
        logout(item)
        item.icon.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                Color.RED,
                BlendModeCompat.SRC_ATOP
            )
        R.id.logoutFragment.setMenuTextColor(menu, R.string.logout)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(listener)

        toolbar.setNavigationOnClickListener {
            when {
//                navController.currentDestination?.id == R.id.addMedicineFragment -> {
//                    customBackListener?.onBack(0)
//                }
                navController.currentDestination?.id != R.id.profileFragment -> {
                    navController.navigateUp()
                }
                else -> {
                    navController.navigateUp(drawerLayout)
                }
            }
        }
    }
    private fun logout(item: MenuItem?) {
        item?.setOnMenuItemClickListener {
            SharedPrefsUtil.clearUser(requireContext())
            SharedPrefsUtil.clearPreference(requireContext(),"isSignedIn")
            findNavController().navigate(actionMainFragmentToLoginFragment())
            true
        }
    }
    private fun Int.setMenuTextColor(menu: Menu, menuTextResource: Int) {
        val item: MenuItem = menu.findItem(this)
        val s = SpannableString(getString(menuTextResource))
        s.setSpan(ForegroundColorSpan(Color.RED), 0, s.length, 0)
        item.title = s
    }
    private fun removeViewAnim() {

        val x: Int = centerPoint.right
        val y: Int = centerPoint.bottom
        val startRadius: Float = mainView.width.coerceAtLeast(mainView.height).toFloat()

        val animator = ViewAnimationUtils.createCircularReveal(animate_view, x, y, startRadius, 0f)
        animator.duration = 700
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                animate_view.visibility = View.GONE

            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })

    }
}