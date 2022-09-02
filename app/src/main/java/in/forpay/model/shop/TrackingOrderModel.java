package in.forpay.model.shop;

public class TrackingOrderModel {


    /**
     * data : {"startLat":"21.953120","startLong":"70.799181","fromLatitude":"21.953119","fromLongitude":"70.799179","toLatitude":"22.004459","toLongitude":"70.792618"}
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
        /**
         * startLat : 21.953120
         * startLong : 70.799181
         * fromLatitude : 21.953119
         * fromLongitude : 70.799179
         * toLatitude : 22.004459
         * toLongitude : 70.792618
         */

        private String startLat;
        private String startLong;
        private String fromLatitude;
        private String fromLongitude;
        private String toLatitude;
        private String toLongitude;

        public String getStartLat() {
            return startLat;
        }

        public void setStartLat(String startLat) {
            this.startLat = startLat;
        }

        public String getStartLong() {
            return startLong;
        }

        public void setStartLong(String startLong) {
            this.startLong = startLong;
        }

        public String getFromLatitude() {
            return fromLatitude;
        }

        public void setFromLatitude(String fromLatitude) {
            this.fromLatitude = fromLatitude;
        }

        public String getFromLongitude() {
            return fromLongitude;
        }

        public void setFromLongitude(String fromLongitude) {
            this.fromLongitude = fromLongitude;
        }

        public String getToLatitude() {
            return toLatitude;
        }

        public void setToLatitude(String toLatitude) {
            this.toLatitude = toLatitude;
        }

        public String getToLongitude() {
            return toLongitude;
        }

        public void setToLongitude(String toLongitude) {
            this.toLongitude = toLongitude;
        }
    }
}
