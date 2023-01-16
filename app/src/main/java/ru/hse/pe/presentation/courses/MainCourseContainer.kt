package ru.hse.pe.presentation.courses

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import ru.hse.pe.R


class MainCourseContainer(
    private val theme: String,
    private val name: String,
    private val imageId: Int,
    ) : Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getLayout() = R.layout.course_small_item


}