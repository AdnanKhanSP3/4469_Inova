package com.example.database.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
@Keep
data class NavigationItem(
    @PrimaryKey(autoGenerate = true)
    val navigationItemId: Long,
    val label: String,
    val route: String,
    val createdByAdmin: Boolean,
    val position: Int
): Parcelable

