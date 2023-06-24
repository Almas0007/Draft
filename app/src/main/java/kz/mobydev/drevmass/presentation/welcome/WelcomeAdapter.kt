package kz.mobydev.drevmass.presentation.welcome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.mobydev.drevmass.databinding.ViewAdapterWelcomeBinding
import kz.mobydev.drevmass.model.WelcomePageInfoModel

class WelcomeAdapter() :
    RecyclerView.Adapter<WelcomeAdapter.WelcomePageViewHolder>() {

    private var welcomePageInfoList = mutableListOf<WelcomePageInfoModel>()

    inner class WelcomePageViewHolder(private val binding: ViewAdapterWelcomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(info: WelcomePageInfoModel) {
            binding.tvTitleWelcomeSlider.text = info.title
            binding.tvDesriptionWelcomeSlider.text = info.description
            binding.tvBackWelcomeSlider.setImageResource(info.img)

        }
    }

    fun submitList(listWelcome: List<WelcomePageInfoModel>) {
        welcomePageInfoList.addAll(listWelcome)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomePageViewHolder {
        return WelcomePageViewHolder(
            ViewAdapterWelcomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return welcomePageInfoList.size
    }

    override fun onBindViewHolder(holder: WelcomePageViewHolder, position: Int) {
        holder.bindItem(welcomePageInfoList[position])
    }

}
