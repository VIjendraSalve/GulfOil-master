package com.taraba.gulfoilapp.model;

import java.util.List;

public class DashboardDataResponse {
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
        public ParticipantDashboard participant_dashboard;
        private String participant_login_id;
        private String profile_image;
        private String full_name;
        private boolean is_login;

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public boolean isIs_login() {
            return is_login;
        }

        public void setIs_login(boolean is_login) {
            this.is_login = is_login;
        }

        public String getParticipant_login_id() {
            return participant_login_id;
        }

        public void setParticipant_login_id(String participant_login_id) {
            this.participant_login_id = participant_login_id;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

        public ParticipantDashboard getParticipant_dashboard() {
            return participant_dashboard;
        }

        public void setParticipant_dashboard(ParticipantDashboard participant_dashboard) {
            this.participant_dashboard = participant_dashboard;
        }

        public class ParticipantDashboard {
            private String points;
            private int magic_bonanza;
            private String magic_bonanza_title;
            private Milestone milestone;
            private String rank;
            private List<TrendingReward> trending_rewards;

            public String getPoints() {
                return points;
            }

            public void setPoints(String points) {
                this.points = points;
            }

            public int getMagic_bonanza() {
                return magic_bonanza;
            }

            public void setMagic_bonanza(int magic_bonanza) {
                this.magic_bonanza = magic_bonanza;
            }

            public String getMagic_bonanza_title() {
                return magic_bonanza_title;
            }

            public void setMagic_bonanza_title(String magic_bonanza_title) {
                this.magic_bonanza_title = magic_bonanza_title;
            }

            public Milestone getMilestone() {
                return milestone;
            }

            public void setMilestone(Milestone milestone) {
                this.milestone = milestone;
            }

            public String getRank() {
                return rank;
            }

            public void setRank(String rank) {
                this.rank = rank;
            }

            public List<TrendingReward> getTrending_rewards() {
                return trending_rewards;
            }

            public void setTrending_rewards(List<TrendingReward> trending_rewards) {
                this.trending_rewards = trending_rewards;
            }

            public class Milestone {
                private int achievement;
                private boolean popup_display;
                private int target_volume;

                public int getAchievement() {
                    return achievement;
                }

                public void setAchievement(int achievement) {
                    this.achievement = achievement;
                }

                public boolean isPopup_display() {
                    return popup_display;
                }

                public void setPopup_display(boolean popup_display) {
                    this.popup_display = popup_display;
                }

                public int getTarget_volume() {
                    return target_volume;
                }

                public void setTarget_volume(int target_volume) {
                    this.target_volume = target_volume;
                }
            }

            public class TrendingReward {
                private String product_id_pk;
                private String name;
                private String product_code;
                private String small_description;
                private String small_image_link;
                private String sort_order;
                private String points;
                private String product_visible;
                private String active_status;
                private String product_type;
                private String record_date;
                private String update_date;

                public String getProduct_id_pk() {
                    return product_id_pk;
                }

                public void setProduct_id_pk(String product_id_pk) {
                    this.product_id_pk = product_id_pk;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getProduct_code() {
                    return product_code;
                }

                public void setProduct_code(String product_code) {
                    this.product_code = product_code;
                }

                public String getSmall_description() {
                    return small_description;
                }

                public void setSmall_description(String small_description) {
                    this.small_description = small_description;
                }

                public String getSmall_image_link() {
                    return small_image_link;
                }

                public void setSmall_image_link(String small_image_link) {
                    this.small_image_link = small_image_link;
                }

                public String getSort_order() {
                    return sort_order;
                }

                public void setSort_order(String sort_order) {
                    this.sort_order = sort_order;
                }

                public String getPoints() {
                    return points;
                }

                public void setPoints(String points) {
                    this.points = points;
                }

                public String getProduct_visible() {
                    return product_visible;
                }

                public void setProduct_visible(String product_visible) {
                    this.product_visible = product_visible;
                }

                public String getActive_status() {
                    return active_status;
                }

                public void setActive_status(String active_status) {
                    this.active_status = active_status;
                }

                public String getProduct_type() {
                    return product_type;
                }

                public void setProduct_type(String product_type) {
                    this.product_type = product_type;
                }

                public String getRecord_date() {
                    return record_date;
                }

                public void setRecord_date(String record_date) {
                    this.record_date = record_date;
                }

                public String getUpdate_date() {
                    return update_date;
                }

                public void setUpdate_date(String update_date) {
                    this.update_date = update_date;
                }
            }
        }
    }
}
