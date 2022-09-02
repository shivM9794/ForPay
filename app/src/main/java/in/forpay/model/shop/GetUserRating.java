package in.forpay.model.shop;

import java.util.List;

public class GetUserRating {


    /**
     * data : [{"rateBy":"Abcd Kumar","rating":"4","ratingText":"Test"}]
     * yourRaing :
     * yourRatingText :
     * resCode : 200
     * resText :
     */

    private String yourRaing;
    private String yourRatingText;
    private String resCode;
    private String resText;
    private List<DataBean> data;

    public String getYourRaing() {
        return yourRaing;
    }

    public void setYourRaing(String yourRaing) {
        this.yourRaing = yourRaing;
    }

    public String getYourRatingText() {
        return yourRatingText;
    }

    public void setYourRatingText(String yourRatingText) {
        this.yourRatingText = yourRatingText;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResText() {
        return resText;
    }

    public void setResText(String resText) {
        this.resText = resText;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * rateBy : Abcd Kumar
         * rating : 4
         * ratingText : Test
         */

        private String rateBy;
        private String rating;
        private String ratingText;

        public String getRateBy() {
            return rateBy;
        }

        public void setRateBy(String rateBy) {
            this.rateBy = rateBy;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getRatingText() {
            return ratingText;
        }

        public void setRatingText(String ratingText) {
            this.ratingText = ratingText;
        }
    }
}
