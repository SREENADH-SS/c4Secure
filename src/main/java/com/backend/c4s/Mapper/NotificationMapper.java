package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.Notification.NotificationResponse;
import com.backend.c4s.Entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toNotificationResponse(Notification notification){
        if (notification==null){
            return null;
        }
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUser() != null ? notification.getUser().getId(): null)
                .title(notification.getTitle())
                .message(notification.getMessage())
                .isRead(notification.getIsReade())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
