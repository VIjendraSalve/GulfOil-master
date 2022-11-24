package com.taraba.gulfoilapp.model;

import java.util.List;

public class KnowledgeCornerResponse {
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
        private List<Newsletter> newsletter;
        private List<Webinar> webinar;

        public List<Newsletter> getNewsletter() {
            return newsletter;
        }

        public void setNewsletter(List<Newsletter> newsletter) {
            this.newsletter = newsletter;
        }

        public List<Webinar> getWebinar() {
            return webinar;
        }

        public void setWebinar(List<Webinar> webinar) {
            this.webinar = webinar;
        }

        public class Newsletter {
            private String newsletter_id_pk;
            private String newsletter_name;
            private String outer_image;
            private String inner_image;

            public String getNewsletter_id_pk() {
                return newsletter_id_pk;
            }

            public void setNewsletter_id_pk(String newsletter_id_pk) {
                this.newsletter_id_pk = newsletter_id_pk;
            }

            public String getNewsletter_name() {
                return newsletter_name;
            }

            public void setNewsletter_name(String newsletter_name) {
                this.newsletter_name = newsletter_name;
            }

            public String getOuter_image() {
                return outer_image;
            }

            public void setOuter_image(String outer_image) {
                this.outer_image = outer_image;
            }

            public String getInner_image() {
                return inner_image;
            }

            public void setInner_image(String inner_image) {
                this.inner_image = inner_image;
            }
        }

        public class Webinar {
            private String webinar_id_pk;
            private String webinar_name;
            private String webinar_banner;
            private String description;
            private String webinar_link;

            public String getWebinar_id_pk() {
                return webinar_id_pk;
            }

            public void setWebinar_id_pk(String webinar_id_pk) {
                this.webinar_id_pk = webinar_id_pk;
            }

            public String getWebinar_name() {
                return webinar_name;
            }

            public void setWebinar_name(String webinar_name) {
                this.webinar_name = webinar_name;
            }

            public String getWebinar_banner() {
                return webinar_banner;
            }

            public void setWebinar_banner(String webinar_banner) {
                this.webinar_banner = webinar_banner;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getWebinar_link() {
                return webinar_link;
            }

            public void setWebinar_link(String webinar_link) {
                this.webinar_link = webinar_link;
            }
        }
    }
}
