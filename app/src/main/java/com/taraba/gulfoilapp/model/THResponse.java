package com.taraba.gulfoilapp.model;

import java.util.List;

public class THResponse {
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

    public class Data {
        public String period;
        public String uin;
        public int earned_points;
        public int redeemed_points;
        public int total_balance;
        public String transaction_history_label;
        public TransactionHistory transaction_history;
    }

    public class TransactionHistory {
        public List<April> april;
        public List<May> may;
        public List<June> june;
        public List<July> july;
        public List<August> august;
        public List<September> september;
        public List<October> october;
        public List<November> november;
        public List<December> december;
        public List<January> january;
        public List<February> february;
        public List<March> march;

        public class April {
            public String key;
            public String value;
        }

        public class May {
            public String key;
            public String value;
        }

        public class June {
            public String key;
            public String value;
        }

        public class July {
            public String key;
            public String value;
        }

        public class August {
            public String key;
            public String value;
        }

        public class September {
            public String key;
            public String value;
        }

        public class October {
            public String key;
            public String value;
        }

        public class November {
            public String key;
            public String value;
        }

        public class December {
            public String key;
            public String value;
        }

        public class January {
            public String key;
            public String value;
        }

        public class February {
            public String key;
            public String value;
        }

        public class March {
            public String key;
            public String value;
        }
    }
}
