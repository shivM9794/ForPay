package in.forpay.model.busbookingModel;

import java.util.List;

public class SeatLayoutModel {


    /**
     * data : {"busId":"12650","seatLayout":[{"isAvailable":"true","fare":"840.00","name":"S12","colums":"10","row":"1","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S11","colums":"10","row":"2","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S24","colums":"10","row":"0","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S23","colums":"10","row":"1","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"L","colums":"10","row":"3","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"F","colums":"10","row":"4","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S10","colums":"8","row":"1","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S9","colums":"8","row":"2","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S22","colums":"8","row":"0","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S21","colums":"8","row":"1","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"K","colums":"8","row":"3","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"E","colums":"8","row":"4","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S8","colums":"6","row":"1","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S7","colums":"6","row":"2","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S20","colums":"6","row":"0","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S19","colums":"6","row":"1","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"J","colums":"6","row":"3","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"D","colums":"6","row":"4","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S6","colums":"4","row":"1","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S5","colums":"4","row":"2","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S18","colums":"4","row":"0","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S17","colums":"4","row":"1","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"I","colums":"4","row":"3","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"C","colums":"4","row":"4","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"S4","colums":"2","row":"1","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"S3","colums":"2","row":"2","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S16","colums":"2","row":"0","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S15","colums":"2","row":"1","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"H","colums":"2","row":"3","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"B","colums":"2","row":"4","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"S2","colums":"0","row":"1","width":"1","zIndex":"1","length":"2","ladiesSeat":"true","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"S1","colums":"0","row":"2","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S14","colums":"0","row":"0","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S13","colums":"0","row":"1","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"G","colums":"0","row":"3","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"A","colums":"0","row":"4","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"}]}
     * resCode :
     * resText : null
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
         * busId : 12650
         * seatLayout : [{"isAvailable":"true","fare":"840.00","name":"S12","colums":"10","row":"1","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S11","colums":"10","row":"2","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S24","colums":"10","row":"0","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S23","colums":"10","row":"1","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"L","colums":"10","row":"3","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"F","colums":"10","row":"4","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S10","colums":"8","row":"1","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S9","colums":"8","row":"2","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S22","colums":"8","row":"0","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S21","colums":"8","row":"1","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"K","colums":"8","row":"3","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"E","colums":"8","row":"4","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S8","colums":"6","row":"1","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S7","colums":"6","row":"2","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S20","colums":"6","row":"0","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S19","colums":"6","row":"1","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"J","colums":"6","row":"3","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"D","colums":"6","row":"4","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S6","colums":"4","row":"1","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S5","colums":"4","row":"2","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S18","colums":"4","row":"0","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S17","colums":"4","row":"1","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"I","colums":"4","row":"3","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"C","colums":"4","row":"4","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"S4","colums":"2","row":"1","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"S3","colums":"2","row":"2","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S16","colums":"2","row":"0","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S15","colums":"2","row":"1","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"H","colums":"2","row":"3","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"B","colums":"2","row":"4","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"S2","colums":"0","row":"1","width":"1","zIndex":"1","length":"2","ladiesSeat":"true","maleSeat":"false"},{"isAvailable":"false","fare":"840.00","name":"S1","colums":"0","row":"2","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S14","colums":"0","row":"0","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"S13","colums":"0","row":"1","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"G","colums":"0","row":"3","width":"1","zIndex":"0","length":"2","ladiesSeat":"false","maleSeat":"false"},{"isAvailable":"true","fare":"840.00","name":"A","colums":"0","row":"4","width":"1","zIndex":"1","length":"2","ladiesSeat":"false","maleSeat":"false"}]
         */

        private String busId;
        private List<SeatLayoutBean> seatLayout;

        public String getBusId() {
            return busId;
        }

        public void setBusId(String busId) {
            this.busId = busId;
        }

        public List<SeatLayoutBean> getSeatLayout() {
            return seatLayout;
        }

        public void setSeatLayout(List<SeatLayoutBean> seatLayout) {
            this.seatLayout = seatLayout;
        }

        public static class SeatLayoutBean {
            /**
             * isAvailable : true
             * fare : 840.00
             * name : S12
             * colums : 10
             * row : 1
             * width : 1
             * zIndex : 1
             * length : 2
             * ladiesSeat : false
             * maleSeat : false
             * commission : 95
             */

            private String isAvailable;
            private String fare;
            private String name;
            private String colums;
            private String row;
            private String width;
            private String zIndex;
            private String length;
            private String ladiesSeat;
            private String maleSeat;
            private String commission;

            public String getCommission() {
                return commission;
            }

            public void setCommission(String commission) {
                this.commission = commission;
            }

            public String getIsAvailable() {
                return isAvailable;
            }

            public void setIsAvailable(String isAvailable) {
                this.isAvailable = isAvailable;
            }

            public String getFare() {
                return fare;
            }

            public void setFare(String fare) {
                this.fare = fare;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getColums() {
                return colums;
            }

            public void setColums(String colums) {
                this.colums = colums;
            }

            public String getRow() {
                return row;
            }

            public void setRow(String row) {
                this.row = row;
            }

            public String getWidth() {
                return width;
            }

            public void setWidth(String width) {
                this.width = width;
            }

            public String getZIndex() {
                return zIndex;
            }

            public void setZIndex(String zIndex) {
                this.zIndex = zIndex;
            }

            public String getLength() {
                return length;
            }

            public void setLength(String length) {
                this.length = length;
            }

            public String getLadiesSeat() {
                return ladiesSeat;
            }

            public void setLadiesSeat(String ladiesSeat) {
                this.ladiesSeat = ladiesSeat;
            }

            public String getMaleSeat() {
                return maleSeat;
            }

            public void setMaleSeat(String maleSeat) {
                this.maleSeat = maleSeat;
            }
        }
    }
}
