package com.timmytruong.dogbreeds.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class DogBreed(
    @ColumnInfo(name = "breed_id")
    @SerializedName(value = "id")
    val breedId: String?,

    @ColumnInfo(name = "dog_name")
    @SerializedName(value = "name")
    val dogBreed: String?,

    @ColumnInfo(name = "life_span")
    @SerializedName(value = "life_span")
    val lifespan: String?,

    @ColumnInfo(name = "breed_group")
    @SerializedName(value = "breed_group")
    val breedGroup: String?,

    @ColumnInfo(name = "bred_for")
    @SerializedName(value = "bred_for")
    val bredFor: String?,

    @SerializedName(value = "temperament")
    val temperament: String?,

    @ColumnInfo(name = "image_url")
    @SerializedName(value = "url")
    val imageUrl: String?
)
{
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}

data class DogPalette(var colour: Int)

data class SmsInfo(
        var to: String,
        var text: String,
        var imageUrl: String?
)