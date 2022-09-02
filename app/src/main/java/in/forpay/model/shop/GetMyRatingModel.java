package in.forpay.model.shop;

import java.util.List;

public class GetMyRatingModel {


    /**
     * data : {"myRating":[{"rateBy":"Abcd Kumar","rating":"5","ratingText":"seller review"}],"shopRating":[{"rateBy":"Abcd Kumar","rating":"5","ratingText":"seller review"}]}
     * resCode : 200
     * resText :
     */

    private DataBean data;
    private String resCode;
    private String resText;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public static class DataBean {
        private List<MyRatingBean> myRating;
        private List<ShopRatingBean> shopRating;

        public List<MyRatingBean> getMyRating() {
            return myRating;
        }

        public void setMyRating(List<MyRatingBean> myRating) {
            this.myRating = myRating;
        }

        public List<ShopRatingBean> getShopRating() {
            return shopRating;
        }

        public void setShopRating(List<ShopRatingBean> shopRating) {
            this.shopRating = shopRating;
        }

        public static class MyRatingBean {
            /**
             * rateBy : Abcd Kumar
             * rating : 5
             * ratingText : seller review
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

        public static class ShopRatingBean {
            /**
             * rateBy : Abcd Kumar
             * rating : 5
             * ratingText : seller review
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
}
