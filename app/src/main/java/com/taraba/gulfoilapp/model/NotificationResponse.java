package com.taraba.gulfoilapp.model;

import java.util.List;

public class NotificationResponse {
    private String status;
    private String error;
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private List<Notification> notification;

        public List<Notification> getNotification() {
            return notification;
        }

        public void setNotification(List<Notification> notification) {
            this.notification = notification;
        }

        public class Notification {
            private String notification_name;
            private String notification_title;
            private String notification_type;
            private String body;
            private String notification_image;
            private String tag;

            public String getNotification_name() {
                return notification_name;
            }

            public void setNotification_name(String notification_name) {
                this.notification_name = notification_name;
            }

            public String getNotification_title() {
                return notification_title;
            }

            public void setNotification_title(String notification_title) {
                this.notification_title = notification_title;
            }

            public String getNotification_type() {
                return notification_type;
            }

            public void setNotification_type(String notification_type) {
                this.notification_type = notification_type;
            }

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public String getNotification_image() {
                return notification_image;
            }

            public void setNotification_image(String notification_image) {
                this.notification_image = notification_image;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }
        }
    }
}
