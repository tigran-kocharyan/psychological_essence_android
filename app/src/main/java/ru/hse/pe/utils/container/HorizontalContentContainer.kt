package ru.hse.pe.utils.container

import android.view.View
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.ContainerHorizontalListBinding

/**
 * Класс для универсального горизонтального списка в groupie
 */
class HorizontalContentContainer(
    private val title: String,
    private val items: List<BindableItem<*>>
) : BindableItem<ContainerHorizontalListBinding>() {

    override fun getLayout() = R.layout.container_horizontal_list

    override fun bind(binding: ContainerHorizontalListBinding, position: Int) {
        binding.title.text = title
        binding.itemsContainer.adapter = GroupAdapter<GroupieViewHolder>().apply { addAll(items) }
    }

    override fun initializeViewBinding(view: View) = ContainerHorizontalListBinding.bind(view)
}