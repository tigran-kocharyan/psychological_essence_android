package ru.hse.pe.presentation.content.item

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.hse.pe.R
import ru.hse.pe.databinding.HolderFactBinding
import ru.hse.pe.domain.model.FactEntity
import ru.hse.pe.utils.callback.ContentClickListener

class FactItem(
    private val fact: FactEntity,
    private val clickListener: ContentClickListener
) : BindableItem<HolderFactBinding>() {

    override fun getLayout() = R.layout.holder_fact

    override fun initializeViewBinding(view: View): HolderFactBinding {
        return HolderFactBinding.bind(view)
    }

    override fun bind(binding: HolderFactBinding, position: Int) {
        binding.root.setOnClickListener { clickListener.onContentClick(fact, position) }
        if (fact.title == null) {
            binding.title.text = "*Заголовок*"
        } else {
            binding.title.text = fact.title
        }
    }
}
