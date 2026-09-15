package com.example.database.data.repository


import com.example.database.data.dao.ActionButtonsDao
import com.example.database.data.model.ActionButtons
import com.example.database.data.model.SliderButton
import com.example.database.domain.repository.ActionButtonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ActionButtonRepositoryImpl
    @Inject constructor(
        private val actionButtonDao: ActionButtonsDao
    ): ActionButtonRepository {
    override suspend fun insertActionButton(actionButton: ActionButtons) {

        actionButtonDao.insertActionButtons(actionButton)
    }

    override suspend fun deleteActionButton(actionButton: ActionButtons) {
        actionButtonDao.deleteActionButtons(actionButton)
    }

    override fun getAllActionButtons(navigationItemId: Long): Flow<List<ActionButtons>> {
       return actionButtonDao.getAllActionButtons(navigationItemId)
    }

    override fun updateFavourite(actionButton: ActionButtons) {
        actionButtonDao.updateFavouriteStatus(actionButton)
    }

    override suspend fun updateAllActionButtonsPosition(actionButtons: List<ActionButtons>) {
        return actionButtonDao.updateAllActionButtonsPosition(actionButtons)
    }

    override fun updateActionButton(actionButton: ActionButtons) {
        actionButtonDao.updateFavouriteStatus(actionButton)
    }

    override fun getAllFavouriteSliderButton(): Flow<List<ActionButtons>> {
        return  actionButtonDao.getAllFavouriteActionButton()
    }

    override fun updateFavouriteStatus(buttonId: Long, isFav: Boolean) {
        actionButtonDao.updateFavouriteStatus(buttonId, isFav)
    }
}