package com.taraba.gulfoilapp.model;

import java.util.List;

public class LeaderBoardResponse {
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
        private String rank_title;
        private String rank;
        private String rank_message;
        private boolean score_flag;
        private ScoreDetail score_detail;
        private String message_title;
        private String message;

        public String getRank_title() {
            return rank_title;
        }

        public void setRank_title(String rank_title) {
            this.rank_title = rank_title;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getRank_message() {
            return rank_message;
        }

        public void setRank_message(String rank_message) {
            this.rank_message = rank_message;
        }

        public boolean isScore_flag() {
            return score_flag;
        }

        public void setScore_flag(boolean score_flag) {
            this.score_flag = score_flag;
        }

        public ScoreDetail getScore_detail() {
            return score_detail;
        }

        public void setScore_detail(ScoreDetail score_detail) {
            this.score_detail = score_detail;
        }

        public String getMessage_title() {
            return message_title;
        }

        public void setMessage_title(String message_title) {
            this.message_title = message_title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public class ScoreDetail {
            private String score_title;
            private List<ScoreData> score_data;

            public String getScore_title() {
                return score_title;
            }

            public void setScore_title(String score_title) {
                this.score_title = score_title;
            }

            public List<ScoreData> getScore_data() {
                return score_data;
            }

            public void setScore_data(List<ScoreData> score_data) {
                this.score_data = score_data;
            }

            public class ScoreData {
                private String title;
                private List<Detail> detail;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public List<Detail> getDetail() {
                    return detail;
                }

                public void setDetail(List<Detail> detail) {
                    this.detail = detail;
                }

                public class Detail {
                    private String key;
                    private String value;

                    public String getKey() {
                        return key;
                    }

                    public void setKey(String key) {
                        this.key = key;
                    }

                    public String getValue() {
                        return value;
                    }

                    public void setValue(String value) {
                        this.value = value;
                    }
                }
            }
        }
    }
}
