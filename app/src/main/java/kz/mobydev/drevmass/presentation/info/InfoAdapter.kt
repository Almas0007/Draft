package kz.mobydev.drevmass.presentation.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.mobydev.drevmass.databinding.ViewAdapterInfoBinding
import kz.mobydev.drevmass.databinding.ViewAdapterWelcomeBinding
import kz.mobydev.drevmass.model.InfoPageModel
import kz.mobydev.drevmass.model.WelcomePageInfoModel

class InfoAdapter() :
    RecyclerView.Adapter<InfoAdapter.InfoPageViewHolder>() {

    private var infoPageList = mutableListOf<InfoPageModel>()

    inner class InfoPageViewHolder(private val binding: ViewAdapterInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(info: InfoPageModel) {
            binding.tvTextTitleInfo.text = info.title
            binding.tvTextDescriptionInfo.text = info.description
        }
    }

    fun submitList(listWelcome: List<InfoPageModel>) {
        infoPageList.addAll(listWelcome)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoPageViewHolder {
        return InfoPageViewHolder(
            ViewAdapterInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return infoPageList.size
    }

    override fun onBindViewHolder(holder: InfoPageViewHolder, position: Int) {
        holder.bindItem(infoPageList[position])
    }

}
