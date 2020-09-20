package com.task.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.task.app.R
import com.task.app.model.Course
import com.task.app.util.interfaces.ClickListener
import kotlinx.android.synthetic.main.course_item.view.*
import java.util.*


/*
Created by Aiman Qaid on 18,سبتمبر,2020
Contact me at wakka-2@hotmail.com
*/
class CourseAdapter(
    private var items: ArrayList<Course>,
    private val clickListener: ClickListener
) :
    RecyclerView.Adapter<CourseAdapter.NavigationItemViewHolder>() {

    private lateinit var context: Context

    class NavigationItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationItemViewHolder {
        context = parent.context
        val navItem = LayoutInflater.from(parent.context).inflate(
            R.layout.course_item,
            parent,
            false
        )
        return NavigationItemViewHolder(navItem)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: NavigationItemViewHolder, position: Int) {
        holder.itemView.tvTitle.text = items[position].title
        holder.itemView.tvCity.text = items[position].city
        holder.itemView.tvDate.text = items[position].date
        holder.itemView.tvDuration.text = items[position].duration
        holder.itemView.tvRating.text = items[position].rating

        holder.itemView.btnDownload.setOnClickListener {
            clickListener.onClick(position,items[position].certificateImg)
        }


    }

}