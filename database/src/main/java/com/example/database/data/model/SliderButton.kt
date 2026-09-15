package com.example.database.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = NavigationItem::class,
            parentColumns = ["navigationItemId"],
            childColumns = ["navigationItemId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class SliderButton(
    @PrimaryKey(autoGenerate = true)
    val sliderButtonId: Long,
    //foregin key
    val navigationItemId : Long,
    val buttonlabel: String,
    val action: String,
    val isAdmin:Boolean,
    val isFavorite: Boolean,
    val position: Int
)
