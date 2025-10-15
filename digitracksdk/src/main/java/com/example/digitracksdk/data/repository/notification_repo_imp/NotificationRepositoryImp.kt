package com.example.digitracksdk.data.repository.notification_repo_imp

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.notification.NotificationDetailsRequestModel
import com.example.digitracksdk.domain.model.notification.NotificationDetailsResponseModel
import com.example.digitracksdk.domain.model.notification.NotificationListResponseModel
import com.example.digitracksdk.domain.repository.notification_repository.NotificationRepository

class NotificationRepositoryImp(private val apiService: ApiService) : NotificationRepository {
    override suspend fun callNotificationListApi(request: CommonRequestModel): NotificationListResponseModel {
        return apiService.callNotificationListApi(request)
    }

    override suspend fun callNotificationDetailsApi(request: NotificationDetailsRequestModel): NotificationDetailsResponseModel {
        return apiService.callNotificationDetailsApi(request)
    }
}