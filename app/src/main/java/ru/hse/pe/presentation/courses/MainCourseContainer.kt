package ru.hse.pe.presentation.courses

import com.xwray.groupie.*
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder


class MainCourseContainer(
    private val theme: String,
    private val name: String,
    private val imageId: Int,
    ) : Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getLayout() = R.layout.course


}