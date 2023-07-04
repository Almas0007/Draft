package kz.mobydev.drevmass.presentation.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.mobydev.drevmass.api.MEDIA_URL
import kz.mobydev.drevmass.databinding.AdapterProductsBinding
import kz.mobydev.drevmass.model.Products
import kz.mobydev.drevmass.utils.RUB_CHAR
import kz.mobydev.drevmass.utils.click.RecyclerViewItemClickCallback

class ProductAdapter(private var productViewModel: ProductViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback =
        object : DiffUtil.ItemCallback<Products.ProductsItem>() {
            override fun areItemsTheSame(
                oldItem: Products.ProductsItem,
                newItem: Products.ProductsItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Products.ProductsItem,
                newItem: Products.ProductsItem
            ): Boolean {
                return oldItem == newItem
            }

        }


    private var listenerItem: RecyclerViewItemClickCallback? = null

    fun setOnItemClickListener(listener: RecyclerViewItemClickCallback) {
        this.listenerItem = listener
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: Products) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding =
            AdapterProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    inner class ViewHolder(var binding:AdapterProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun initContent(item: Products.ProductsItem) {
            binding.tvTitle.text = item.title
            binding.tvPrice.text = "${item.price}$RUB_CHAR"
            Glide.with(itemView.context)
                .load(MEDIA_URL+item.imageSrc) // Замените URL на фактический путь к изображению
                .into(binding.imgProduct)
            binding.tvTitle.text = item.title
            when(item.icon){
                "fire" -> binding.btnImgIcon.background = ContextCompat.getDrawable(itemView.context, kz.mobydev.drevmass.R.drawable.fire)
                "star"-> binding.btnImgIcon.background = ContextCompat.getDrawable(itemView.context, kz.mobydev.drevmass.R.drawable.star)
                else -> binding.btnImgIcon.visibility = View.INVISIBLE
            }
            if (item.icon != null) {
                Glide.with(itemView.context)
                    .load(item.icon)
                    .into(binding.btnImgIcon)
            } else {
                binding.btnImgIcon.visibility = View.INVISIBLE
            }
            binding.productItemClick.setOnClickListener {
                listenerItem?.onRecyclerViewItemClick(item.id)
            }



        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        viewHolder.initContent(differ.currentList[position])
    }




}