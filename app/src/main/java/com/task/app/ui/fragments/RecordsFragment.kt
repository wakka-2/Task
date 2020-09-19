package com.task.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class RecordsFragment : BaseFragment() {
    private lateinit var items: ArrayList<Course>
    private lateinit var mUserViewModel: UserViewModel
    private var user: User?= null

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


        navigationClicks(view)
        initRecycler()
    }
    private fun navigationClicks(view: View) {
        fabAdd.setOnClickListener {
            view.findNavController().navigate(actionRecordsFragmentToAddCourseFragment())
        }
    }
    private fun initRecycler() {

        user?.idNum?.let { it1  ->
            mUserViewModel.getAllCourses(userId = it1).observe(viewLifecycleOwner, {
                if(it != null){
                    items = it as ArrayList<Course>

                    // Setup Recyclerview's Layout
                    rvCourses.setHasFixedSize(true)

                    // Add Item Touch Listener
                    val adapter = CourseAdapter(items, object : ClickListener {
                        override fun onClick(position: Int) {
                            showSnackBar(requireView(),"download ...")
                        }

                    })
                    rvCourses.adapter = adapter
                }
            })
        }

    }
}