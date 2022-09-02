package in.forpay.util;


import java.util.Comparator;

import in.forpay.model.busbookingModel.BusAvailableOnJourney;

public class BusSorting {

    public static class BoardingPoint implements Comparator<BusAvailableOnJourney.DataBean.BusStopsBean.BoardingPointsBean> {

        public int compare(BusAvailableOnJourney.DataBean.BusStopsBean.BoardingPointsBean busTimes, BusAvailableOnJourney.DataBean.BusStopsBean.BoardingPointsBean busTimes2) {
            return Integer.parseInt(busTimes.getBpName()) < Integer.parseInt(busTimes2.getBpName()) ? 1 : -1;
        }
    }

    public static class DepartureAscending implements Comparator<BusAvailableOnJourney.DataBean.BusStopsBean> {
        public int compare(BusAvailableOnJourney.DataBean.BusStopsBean busDetail, BusAvailableOnJourney.DataBean.BusStopsBean busDetail2) {
            return Long.compare(Constant.getTimeInMilli(busDetail.getDepartureTime(),"yyyy-mm-dd HH:mm:ss"),
                    (Constant.getTimeInMilli(busDetail2.getDepartureTime(),"yyyy-mm-dd HH:mm:ss")));
        }
    }

    public static class DepartureDescending implements Comparator<BusAvailableOnJourney.DataBean.BusStopsBean> {
        public int compare(BusAvailableOnJourney.DataBean.BusStopsBean busDetail, BusAvailableOnJourney.DataBean.BusStopsBean busDetail2) {

                return Long.compare(Constant.getTimeInMilli(busDetail2.getDepartureTime(),"yyyy-mm-dd HH:mm:ss"),
                        (Constant.getTimeInMilli(busDetail.getDepartureTime(),"yyyy-mm-dd HH:mm:ss")));
            }

        }


    public static class DurationAscending implements Comparator<BusAvailableOnJourney.DataBean.BusStopsBean> {
        public int compare(BusAvailableOnJourney.DataBean.BusStopsBean busDetail, BusAvailableOnJourney.DataBean.BusStopsBean busDetail2) {
            return Long.compare(Constant.getTimeInMilli(busDetail.getTravelDuration(),"HH:mm:ss"),(Constant.getTimeInMilli(busDetail2.getTravelDuration(),"HH:mm:ss")));
        }
    }

    public static class DurationDescending implements Comparator<BusAvailableOnJourney.DataBean.BusStopsBean> {
        public int compare(BusAvailableOnJourney.DataBean.BusStopsBean busDetail, BusAvailableOnJourney.DataBean.BusStopsBean busDetail2) {
            return Long.compare(Constant.getTimeInMilli(busDetail2.getTravelDuration(),"HH:mm:ss"),(Constant.getTimeInMilli(busDetail.getTravelDuration(),"HH:mm:ss")));
        }
    }

    public static class PriceAscending implements Comparator<BusAvailableOnJourney.DataBean.BusStopsBean> {
        public int compare(BusAvailableOnJourney.DataBean.BusStopsBean busDetail, BusAvailableOnJourney.DataBean.BusStopsBean busDetail2) {
            if (Double.parseDouble(busDetail.getFares().get(0)) <= Double.parseDouble(busDetail2.getFares().get(0))){
                return -1;
            }
            return 1;
        }
    }

    public static class PriceDescending implements Comparator<BusAvailableOnJourney.DataBean.BusStopsBean> {
        public int compare(BusAvailableOnJourney.DataBean.BusStopsBean busDetail, BusAvailableOnJourney.DataBean.BusStopsBean busDetail2) {
            if (Double.parseDouble(busDetail.getFares().get(0)) >= Double.parseDouble(busDetail2.getFares().get(0))){
                return -1;
            }
            return 1;
        }
    }





    public static class BusTimeBetween6PMTO6AM implements Comparator<BusAvailableOnJourney.DataBean.BusStopsBean>{
        public int compare(BusAvailableOnJourney.DataBean.BusStopsBean busDetail, BusAvailableOnJourney.DataBean.BusStopsBean busDetail2) {

            long startTime = Constant.getTimeInMilli("06 PM","hh a");
            long endTime = Constant.getTimeInMilli("06 AM", "hh a");

            long departureTime = Constant.getTimeInMilli(busDetail.getDepartureTime(),"hh a");
            long arrivalTime = Constant.getTimeInMilli(busDetail.getArrivalTime(), "hh a");

            long departureTime2 = Constant.getTimeInMilli(busDetail2.getDepartureTime(),"hh a");
            long arrivalTime2 = Constant.getTimeInMilli(busDetail2.getArrivalTime(), "hh a");

            if ((departureTime >= startTime && arrivalTime <= endTime)  &&  ((departureTime2 >= startTime && arrivalTime2 <= endTime)) ){
                return -1;
            }
            return 1;
        }
    }



}
