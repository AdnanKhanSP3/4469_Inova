package com.example.database.data.repository


import com.example.database.data.dao.SliderDao
import com.example.database.data.model.Sliders
import com.example.database.domain.repository.SliderRepository
import javax.inject.Inject

class SliderRepositoryImpl
    @Inject constructor(
        private val sliderDao: SliderDao
    ): SliderRepository {
    override suspend fun insertSlider(slider: Sliders) {
         sliderDao.insertSlider(slider)
    }

    override suspend fun deleteSlider(slider: Sliders) {
        sliderDao.deleteSlider(slider)
    }

    override suspend fun getAllSliders(navigationItemId: Long): Sliders {
        return  sliderDao.getAllSliders(navigationItemId)
    }
}