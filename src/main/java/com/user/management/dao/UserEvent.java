package com.user.management.dao;

import java.time.LocalDateTime;

import jakarta.persistence.Column;

public class UserEvent {

    private Long userId;
    private String username;
    private String eventType;
    private LocalDateTime timestamp;
    private String details;

    public UserEvent() {
    }

    public UserEvent(Long userId, String username, String eventType, LocalDateTime timestamp, String details) {
        this.userId = userId;
        this.username = username;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.details = details;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "UserEvent{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", eventType='" + eventType + '\'' +
                ", timestamp=" + timestamp +
                ", details='" + details + '\'' +
                '}';
    }
}
