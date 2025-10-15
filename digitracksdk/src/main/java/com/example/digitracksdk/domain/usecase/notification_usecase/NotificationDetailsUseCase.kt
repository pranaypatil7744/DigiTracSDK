package com.example.digitracksdk.domain.usecase.notification_usecase

import com.example.digitracksdk.domain.model.notification.NotificationDetailsRequestModel
import com.example.digitracksdk.domain.model.notification.NotificationDetailsResponseModel
import com.example.digitracksdk.domain.repository.notification_repository.NotificationRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class NotificationDetailsUseCase constructor(private val notificationRepository: NotificationRepository) :
    UseCase<NotificationDetailsResponseModel, Any?>() {
    override suspend fun run(params: Any?): NotificationDetailsResponseModel {
        return notificationRepository.callNotificationDetailsApi(params as NotificationDetailsRequestModel)
    }
}