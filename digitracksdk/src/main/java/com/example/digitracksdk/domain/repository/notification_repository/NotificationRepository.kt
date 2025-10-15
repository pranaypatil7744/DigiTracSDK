package com.example.digitracksdk.domain.repository.notification_repository

import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.notification.NotificationDetailsRequestModel
import com.example.digitracksdk.domain.model.notification.NotificationDetailsResponseModel
import com.example.digitracksdk.domain.model.notification.NotificationListResponseModel

interface NotificationRepository {

    suspend fun callNotificationListApi(request: CommonRequestModel): NotificationListResponseModel

    suspend fun callNotificationDetailsApi(request: NotificationDetailsRequestModel): NotificationDetailsResponseModel

}