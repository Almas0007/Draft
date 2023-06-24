package kz.mobydev.drevmass.model


import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Lesson", primaryKeys = ["id"])
data class Lesson(
    @SerializedName("created_at")
    val createdAt: String, // 2023-06-06T09:38:05.000000Z
    @SerializedName("description")
    val description: String, // Description
    @SerializedName("duration")
    val duration: Int, // 235
    @SerializedName("id")
    val id: Int, // 1
    @SerializedName("image_src")
    val imageSrc: String, // images/1686044285.svg
    @SerializedName("name")
    val name: String, // Test
    @SerializedName("title")
    val title: String, // Test
    @SerializedName("updated_at")
    val updatedAt: String, // 2023-06-06T09:39:16.000000Z
    @SerializedName("video_src")
    val videoSrc: String // https://www.youtube.com/embed/IUN664s7N-c
)