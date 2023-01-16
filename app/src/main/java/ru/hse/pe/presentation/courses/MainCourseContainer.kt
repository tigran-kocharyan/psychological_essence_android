package ru.hse.pe.presentation.courses

import ru.hse.pe.R
import java.util.Collections.addAll

class MainCourseContainer(
    private val name: String? = "",
    private val description: String? = "",
    private val onClick: (url: String) -> Unit,
    private val items: List<Course>
) : Course() {

    override fun getLayout() = R.layout.item_card

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.title_text_view.text = title
        viewHolder.description_text_view.text = description
        viewHolder.items_container.adapter =
            GroupAdapter<GroupieViewHolder>().apply { addAll(items) }
    }
}