package com.example.database.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class NavigationItemWithSliderButtons(

    @Embedded
    val navigationItem: NavigationItem? = null,

    @Relation(
        parentColumn = "navigationItemId",
        entityColumn = "navigationItemId",
    )
    val actionButtons: List<SliderButton>

)
