package com.example.digitracksdk.domain.usecase.notification_usecase

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.notification.NotificationListResponseModel
import com.example.digitracksdk.domain.repository.notification_repository.NotificationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class NotificationListUseCase constructor(private val notificationRepository: NotificationRepository) :
    UseCase<NotificationListResponseModel, Any?>() {
    override suspend fun run(params: Any?): NotificationListResponseModel {
        return notificationRepository.callNotificationListApi(params as CommonRequestModel)
    }
}