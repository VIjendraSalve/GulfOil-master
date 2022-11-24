package com.taraba.gulfoilapp.model;

public class AppVersionResponse {
    private String status;
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private String version_name;
        private String version_code;
        private String link;
        private boolean is_major_update;

        public String getVersion_name() {
            return version_name;
        }

        public void setVersion_name(String version_name) {
            this.version_name = version_name;
        }

        public String getVersion_code() {
            return version_code;
        }

        public void setVersion_code(String version_code) {
            this.version_code = version_code;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public boolean isIs_major_update() {
            return is_major_update;
        }

        public void setIs_major_update(boolean is_major_update) {
            this.is_major_update = is_major_update;
        }
    }
}
