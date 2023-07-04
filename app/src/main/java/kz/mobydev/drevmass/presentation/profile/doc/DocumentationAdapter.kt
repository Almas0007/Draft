package kz.mobydev.drevmass.presentation.profile.doc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.api.MEDIA_URL
import kz.mobydev.drevmass.databinding.AdapterLessonsBinding
import kz.mobydev.drevmass.databinding.AdapterRulesBinding
import kz.mobydev.drevmass.databinding.ViewAdapterInfoBinding
import kz.mobydev.drevmass.model.InfoPageModel
import kz.mobydev.drevmass.model.Lesson
import kz.mobydev.drevmass.presentation.product.ProductAdapter
import kz.mobydev.drevmass.utils.click.RecyclerViewFavoriteClickCallback
import kz.mobydev.drevmass.utils.click.RecyclerViewItemClickCallback
import kz.mobydev.drevmass.utils.click.RecyclerViewPlayClickCallback

class DocumentationAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var ruleList = mutableListOf<String>()
    fun submitList(list: List<String>) {
        list.forEach { ruleList.add(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding =
            AdapterRulesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    inner class ViewHolder(var binding: AdapterRulesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun initContent(item: String) {
            binding.rule.text = item

        }
    }

    override fun getItemCount(): Int = ruleList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        viewHolder.initContent(ruleList[position])
    }
}



