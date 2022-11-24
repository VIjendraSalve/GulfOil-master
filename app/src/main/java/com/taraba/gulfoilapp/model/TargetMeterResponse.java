package com.taraba.gulfoilapp.model;

public class TargetMeterResponse {
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
        public String login_id_fk;
        public String retailer_code;
        public String uin;
        public String header;
        public String name;
        public int base_growth_target;
        public boolean star_growth_meter;
        public int star_growth_target;
        public String achievement;
        public BaseGrowthValue base_growth_value;
        public StarGrowthValue star_growth_value;

        public String getLogin_id_fk() {
            return login_id_fk;
        }

        public void setLogin_id_fk(String login_id_fk) {
            this.login_id_fk = login_id_fk;
        }

        public String getRetailer_code() {
            return retailer_code;
        }

        public void setRetailer_code(String retailer_code) {
            this.retailer_code = retailer_code;
        }

        public String getUin() {
            return uin;
        }

        public void setUin(String uin) {
            this.uin = uin;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getBase_growth_target() {
            return base_growth_target;
        }

        public void setBase_growth_target(int base_growth_target) {
            this.base_growth_target = base_growth_target;
        }

        public boolean isStar_growth_meter() {
            return star_growth_meter;
        }

        public void setStar_growth_meter(boolean star_growth_meter) {
            this.star_growth_meter = star_growth_meter;
        }

        public int getStar_growth_target() {
            return star_growth_target;
        }

        public void setStar_growth_target(int star_growth_target) {
            this.star_growth_target = star_growth_target;
        }

        public String getAchievement() {
            return achievement;
        }

        public void setAchievement(String achievement) {
            this.achievement = achievement;
        }

        public BaseGrowthValue getBase_growth_value() {
            return base_growth_value;
        }

        public void setBase_growth_value(BaseGrowthValue base_growth_value) {
            this.base_growth_value = base_growth_value;
        }

        public StarGrowthValue getStar_growth_value() {
            return star_growth_value;
        }

        public void setStar_growth_value(StarGrowthValue star_growth_value) {
            this.star_growth_value = star_growth_value;
        }

        public class BaseGrowthValue {
            private String target;
            private String achieved;
            private String balance;

            public String getTarget() {
                return target;
            }

            public void setTarget(String target) {
                this.target = target;
            }

            public String getAchieved() {
                return achieved;
            }

            public void setAchieved(String achieved) {
                this.achieved = achieved;
            }

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }
        }

        public class StarGrowthValue {
            private String target;
            private String achieved;
            private String balance;

            public String getTarget() {
                return target;
            }

            public void setTarget(String target) {
                this.target = target;
            }

            public String getAchieved() {
                return achieved;
            }

            public void setAchieved(String achieved) {
                this.achieved = achieved;
            }

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }
        }
    }
}
