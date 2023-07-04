package kz.mobydev.drevmass.model


import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


class Products : ArrayList<Products.ProductsItem>(){
    @Entity(tableName = "ProductsItem", primaryKeys = ["id"])
    data class ProductsItem(
        @SerializedName("depth")
        val depth: String, // 14.5
        @SerializedName("description")
        val description: String, // Массажер Древмасс - это эффективный тренажер, изготовленный из дерева бука, который представляет собой конструкцию из 5-ти роликов, закрепленных на деревянной раме и оснащенный удобной опорной рукояткой. Он является идеальным решением для тренировки различных групп мышц, включая спину, ноги. пресс, ягодицы и многое другое. Массажер Древмасс не только помогает укрепить мышцы спины и снять нагрузку с позвоночника, но и является универсальным тренажером для всего тела.
        @SerializedName("height")
        val height: String, // 22
        @SerializedName("icon")
        val icon: String?, // hot
        @SerializedName("id")
        val id: Int, // 1
        @SerializedName("image_src")
        val imageSrc: String, // images/1687911972.png
        @SerializedName("length")
        val length: String, // 73
        @SerializedName("name")
        val name: String, // Product 1
        @SerializedName("price")
        val price: Int, // 12900
        @SerializedName("status")
        val status: Int, // 1
        @SerializedName("title")
        val title: String, // 5-роликовый массажер
        @SerializedName("video_src")
        val videoSrc: String, // uR58PtUYSEc
        @SerializedName("weight")
        val weight: Int // 7000
    )
}