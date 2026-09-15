package com.example.database.data.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = NavigationItem::class,
            parentColumns = ["navigationItemId"],
            childColumns = ["navigationItemId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
@Keep
data class Sliders(
    @PrimaryKey (autoGenerate = true)
    val sliderId: Long,
    val navigationItemId: Long,
    val sliderLabel: String,
    val action: String,
    val isAdmin:Boolean,
)