package com.taraba.gulfoilapp.model;

import com.google.gson.annotations.SerializedName;

public class MilestoneJourneyResponse {
    private String status;
    private String error;
    private String message;
    public Data data;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Data {
        private String uid;
        private String total_target;
        private String achievement_percenatage;
        private String achievement_label;
        @SerializedName("25")
        private _25 _25;
        @SerializedName("50")
        private _50 _50;
        @SerializedName("75")
        private _75 _75;
        @SerializedName("100")
        private _100 _100;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getTotal_target() {
            return total_target;
        }

        public void setTotal_target(String total_target) {
            this.total_target = total_target;
        }

        public String getAchievement_percenatage() {
            return achievement_percenatage;
        }

        public void setAchievement_percenatage(String achievement_percenatage) {
            this.achievement_percenatage = achievement_percenatage;
        }

        public String getAchievement_label() {
            return achievement_label;
        }

        public void setAchievement_label(String achievement_label) {
            this.achievement_label = achievement_label;
        }

        public Data._25 get_25() {
            return _25;
        }

        public void set_25(Data._25 _25) {
            this._25 = _25;
        }

        public Data._50 get_50() {
            return _50;
        }

        public void set_50(Data._50 _50) {
            this._50 = _50;
        }

        public Data._75 get_75() {
            return _75;
        }

        public void set_75(Data._75 _75) {
            this._75 = _75;
        }

        public Data._100 get_100() {
            return _100;
        }

        public void set_100(Data._100 _100) {
            this._100 = _100;
        }

        public class _25 {
            private String title;
            private boolean rewards;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public boolean isRewards() {
                return rewards;
            }

            public void setRewards(boolean rewards) {
                this.rewards = rewards;
            }
        }

        public class _50 {
            private String title;
            private boolean rewards;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public boolean isRewards() {
                return rewards;
            }

            public void setRewards(boolean rewards) {
                this.rewards = rewards;
            }
        }

        public class _75 {
            private String title;
            private boolean rewards;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public boolean isRewards() {
                return rewards;
            }

            public void setRewards(boolean rewards) {
                this.rewards = rewards;
            }
        }

        public class _100 {
            private String title;
            private boolean rewards;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public boolean isRewards() {
                return rewards;
            }

            public void setRewards(boolean rewards) {
                this.rewards = rewards;
            }
        }
    }
}
