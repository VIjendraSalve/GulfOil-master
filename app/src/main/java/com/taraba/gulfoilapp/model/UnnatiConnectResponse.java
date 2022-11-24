package com.taraba.gulfoilapp.model;

import java.util.List;

public class UnnatiConnectResponse {
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
        private List<SchemeLetter> scheme_letter;
        private List<WhatsNew> whats_new;
        private List<Brochure> brochure;

        public List<SchemeLetter> getScheme_letter() {
            return scheme_letter;
        }

        public void setScheme_letter(List<SchemeLetter> scheme_letter) {
            this.scheme_letter = scheme_letter;
        }

        public List<WhatsNew> getWhats_new() {
            return whats_new;
        }

        public void setWhats_new(List<WhatsNew> whats_new) {
            this.whats_new = whats_new;
        }

        public List<Brochure> getBrochure() {
            return brochure;
        }

        public void setBrochure(List<Brochure> brochure) {
            this.brochure = brochure;
        }
    }

    public class SchemeLetter {
        private String scheme_letter_id_pk;
        private String scheme_name;
        private String outer_image;
        private String inner_image;

        public String getScheme_letter_id_pk() {
            return scheme_letter_id_pk;
        }

        public void setScheme_letter_id_pk(String scheme_letter_id_pk) {
            this.scheme_letter_id_pk = scheme_letter_id_pk;
        }

        public String getScheme_name() {
            return scheme_name;
        }

        public void setScheme_name(String scheme_name) {
            this.scheme_name = scheme_name;
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

    public class WhatsNew {
        private String whats_new_id_pk;
        private String name;
        private String outer_image;
        private String inner_image;

        public String getWhats_new_id_pk() {
            return whats_new_id_pk;
        }

        public void setWhats_new_id_pk(String whats_new_id_pk) {
            this.whats_new_id_pk = whats_new_id_pk;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

    public class Brochure {
        private String brochure_id_pk;
        private String outer_image;
        private String inner_image;
        private String brochure_pdf;

        public String getBrochure_id_pk() {
            return brochure_id_pk;
        }

        public void setBrochure_id_pk(String brochure_id_pk) {
            this.brochure_id_pk = brochure_id_pk;
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

        public String getBrochure_pdf() {
            return brochure_pdf;
        }

        public void setBrochure_pdf(String brochure_pdf) {
            this.brochure_pdf = brochure_pdf;
        }
    }


}
