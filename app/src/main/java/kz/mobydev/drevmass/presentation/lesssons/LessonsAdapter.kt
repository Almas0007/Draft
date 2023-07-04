package kz.mobydev.drevmass.presentation.lesssons

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import javax.inject.Inject
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.api.MEDIA_URL
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.AdapterLessonsBinding
import kz.mobydev.drevmass.databinding.AdapterProductsBinding
import kz.mobydev.drevmass.model.Lesson
import kz.mobydev.drevmass.utils.click.RecyclerViewFavoriteClickCallback
import kz.mobydev.drevmass.utils.click.RecyclerViewItemClickCallback
import kz.mobydev.drevmass.utils.click.RecyclerViewPlayClickCallback

class LessonsAdapter ():
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var token :String = ""
    private val diffCallback =
        object : DiffUtil.ItemCallback<Lesson>() {
            override fun areItemsTheSame(
                oldItem: Lesson,
                newItem: Lesson
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Lesson,
                newItem: Lesson
            ): Boolean {
                return oldItem == newItem
            }

        }


    private var listenerItem: RecyclerViewItemClickCallback? = null
    private var listenerFavorite: RecyclerViewFavoriteClickCallback? = null
    private var listenerPlay: RecyclerViewPlayClickCallback? = null

    fun setOnItemClickListener(listener: RecyclerViewItemClickCallback) {
        this.listenerItem = listener
    }
    fun setOnPlayClickListener(listener: RecyclerViewPlayClickCallback) {
        this.listenerPlay = listener
    }
    fun setOnFavoriteClickListener(listener: RecyclerViewFavoriteClickCallback) {
        this.listenerFavorite = listener
    }
    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Lesson>) {
        differ.submitList(list)
    }

    fun submitToken(token: String) {
       this.token = token
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding =
            AdapterLessonsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    inner class ViewHolder(var binding: AdapterLessonsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun initContent(item: Lesson) {
            binding.btnLesson.setOnClickListener {
                listenerItem?.onRecyclerViewItemClick(item.id)
            }
            val focusBackground = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_favorite_selected)
            val unFocusBackground = ContextCompat.getDrawable(binding.root.context, R.drawable.button_favorite)

            if (item.favorite == 1) {
                binding.btnFavorite.background = focusBackground
            } else {
                binding.btnFavorite.background = unFocusBackground
            }
            binding.btnFavorite.setOnClickListener {
                val buttonFavorite = binding.btnFavorite

                if (buttonFavorite.background.constantState == focusBackground?.constantState) {
                    buttonFavorite.background = unFocusBackground
                    listenerFavorite?.onRecyclerViewFavoriteClick(item.id,false)
                } else {
                    buttonFavorite.background = focusBackground
                    listenerFavorite?.onRecyclerViewFavoriteClick(item.id,true)
                }
            }
            binding.btnPlayVideo.setOnClickListener { view->
                listenerPlay?.onRecyclerViewPlayClick(item.videoSrc)
            }

            binding.tvTittle.text = item.name
            binding.tvDescriptionLessons.text = item.description
            Glide.with(itemView.context)
                .load(MEDIA_URL +item.imageSrc) // Замените URL на фактический путь к изображению
                .into(binding.imgLessons)

            val minutes = item.duration / 60
            val seconds = item.duration % 60

            val formattedTime = String.format("%d:%02d", minutes, seconds)
            binding.tvTimeVideo.text = formattedTime


        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        viewHolder.initContent(differ.currentList[position])
    }




}