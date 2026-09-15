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
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
@Keep
data class ActionButtons(

    @PrimaryKey(autoGenerate = true)
    val actionButtonId: Long,
    //foregin key
    val navigationItemId : Long,
    val buttonlabel: String,
    val action: String,
    val isAdmin:Boolean,
    val isFavorite: Boolean,
    val position: Int
)