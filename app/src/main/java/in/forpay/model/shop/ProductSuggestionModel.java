package in.forpay.model.shop;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ProductSuggestionModel implements Parcelable{


    /**
     * data : {"shopName":"Test ABC","imageId":"1409","imgUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM107.jpeg","contactNumber":"8999898998","deliveryRange":"10","latitude":"21.974209","longitude":"70.793411","productSuggestion":[{"id":"1","name":"Redmi Note 8 Pro (Halo White, 128 GB) 6 GB RAM","brand":"Redmi","warranty":"1 Year Warranty on Phone, 6 Months Warranty on Accessories","specs1":"[\"6 GB RAM | 128 GB ROM | Expandable Upto 512 GB\",\"16.59 cm (6.53 inch) Full HD+ Display\",\"64MP + 8MP + 2MP + 2MP | 20MP Front Camera\",\"4500 mAh Li-polymer Battery\",\"MediaTek Helio G90T Processor\"]","specs2":"[\"6 GB RAM | 128 GB Storage\",\"16.59 cm (6.53 inch) Full HD+ Display\",\"4500 mAh\",\"64MP + 8MP + 2MP + 2MP\",\"20MP Front Camera\"]","images":[{"imageId":"1324","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM10.jpeg"},{"imageId":"1325","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM11.jpeg"},{"imageId":"1326","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM12.jpeg"},{"imageId":"1327","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM13.jpeg"},{"imageId":"1328","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM14.jpeg"},{"imageId":"1329","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM15.jpeg"},{"imageId":"1330","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM16.jpeg"},{"imageId":"1331","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM17.jpeg"},{"imageId":"1332","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM18.jpeg"},{"imageId":"1333","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM19.jpeg"},{"imageId":"1334","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM110.jpeg"},{"imageId":"1335","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM111.jpeg"},{"imageId":"1336","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM112.jpeg"},{"imageId":"1337","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM113.jpeg"},{"imageId":"1338","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM114.jpeg"}]},{"id":"2","name":"Realme Narzo 10A (So Blue, 32 GB) 3 GB RAM","brand":"Realme","warranty":"Brand Warranty of 1 Year Available for Mobile and 6 Months for Accessories","specs1":"[\"3 GB RAM | 32 GB ROM | Expandable Upto 256 GB\",\"16.56 cm (6.52 inch) HD+ Display\",\"12MP + 2MP + 2MP | 5MP Front Camera\",\"5000 mAh Lithium-ion Battery\",\"MediaTek Helio G70 (12 nm) Processor\"]","specs2":"[\"3 GB RAM | 32 GB Storage\",\"16.56 cm (6.52 inch) HD+ Display\",\"5000 mAh\",\"12MP + 2MP + 2MP\",\"5MP Front Camera\"]","images":[{"imageId":"1339","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM20.jpeg"},{"imageId":"1340","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM21.jpeg"},{"imageId":"1341","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM22.jpeg"},{"imageId":"1342","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM23.jpeg"},{"imageId":"1343","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM24.jpeg"},{"imageId":"1344","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM25.jpeg"},{"imageId":"1345","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM26.jpeg"},{"imageId":"1346","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM27.jpeg"},{"imageId":"1347","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM28.jpeg"}]},{"id":"3","name":"OPPO A12 (Black, 32 GB) 3 GB RAM","brand":"OPPO","warranty":"Brand Warranty of 1 Year Available for Mobile Including Battery and 6 Months for Accessories","specs1":"[\"3 GB RAM | 32 GB ROM | Expandable Upto 256 GB\",\"15.8 cm (6.22 inch) HD+ Display\",\"13MP + 2MP | 5MP Front Camera\",\"4230 mAh Battery\",\"MediaTek Helio P35 (MT6765V/CB) Processor\"]","specs2":"[\"3 GB RAM | 32 GB Storage\",\"15.8 cm (6.22 inch) HD+ Display\",\"4230 mAh\",\"13MP + 2MP\",\"5MP Front Camera\"]","images":[{"imageId":"1348","imageUrl":"https://api.forpay.in/shop/images/Phone/OPPOA12Black32GB3GBRAM30.jpeg"},{"imageId":"1349","imageUrl":"https://api.forpay.in/shop/images/Phone/OPPOA12Black32GB3GBRAM31.jpeg"},{"imageId":"1350","imageUrl":"https://api.forpay.in/shop/images/Phone/OPPOA12Black32GB3GBRAM32.jpeg"},{"imageId":"1351","imageUrl":"https://api.forpay.in/shop/images/Phone/OPPOA12Black32GB3GBRAM33.jpeg"},{"imageId":"1352","imageUrl":"https://api.forpay.in/shop/images/Phone/OPPOA12Black32GB3GBRAM34.jpeg"},{"imageId":"1353","imageUrl":"https://api.forpay.in/shop/images/Phone/OPPOA12Black32GB3GBRAM35.jpeg"}]},{"id":"4","name":"Redmi Note 8 Pro (Halo White, 64 GB) 6 GB RAM","brand":"Redmi","warranty":"1 Year Warranty on Phone, 6 Months Warranty on Accessories","specs1":"[\"6 GB RAM | 64 GB ROM | Expandable Upto 512 GB\",\"16.59 cm (6.53 inch) Full HD+ Display\",\"64MP + 8MP + 2MP + 2MP | 20MP Front Camera\",\"4500 mAh Li-polymer Battery\",\"MediaTek Helio G90T Processor\"]","specs2":"[\"6 GB RAM | 64 GB Storage\",\"16.59 cm (6.53 inch) Full HD+ Display\",\"4500 mAh\",\"64MP + 8MP + 2MP + 2MP\",\"20MP Front Camera\"]","images":[{"imageId":"1354","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM40.jpeg"},{"imageId":"1355","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM41.jpeg"},{"imageId":"1356","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM42.jpeg"},{"imageId":"1357","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM43.jpeg"},{"imageId":"1358","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM44.jpeg"},{"imageId":"1359","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM45.jpeg"},{"imageId":"1360","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM46.jpeg"},{"imageId":"1361","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM47.jpeg"},{"imageId":"1362","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM48.jpeg"},{"imageId":"1363","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM49.jpeg"},{"imageId":"1364","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM410.jpeg"},{"imageId":"1365","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM411.jpeg"},{"imageId":"1366","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM412.jpeg"},{"imageId":"1367","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM413.jpeg"},{"imageId":"1368","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM414.jpeg"}]},{"id":"5","name":"Samsung Galaxy M01 (Blue, 32 GB) 3 GB RAM","brand":"Samsung","warranty":"1 Year Manufacturer Warranty for Phone and 6 Months Warranty for in the Box Accessories","specs1":"[\"3 GB RAM | 32 GB ROM | Expandable Upto 512 GB\",\"14.48 cm (5.7 inch) HD+ Display\",\"13MP + 2MP | 5MP Front Camera\",\"4000 mAh Lithium-ion Battery\",\"Qualcomm Snapdragon (SDM439) Octa Core Processor\"]","specs2":"[\"3 GB RAM | 32 GB Storage\",\"14.48 cm (5.7 inch) HD+ Display\",\"4000 mAh\",\"13MP + 2MP\",\"5MP Front Camera\"]","images":[{"imageId":"1369","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Blue32GB3GBRAM50.jpeg"},{"imageId":"1370","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Blue32GB3GBRAM51.jpeg"},{"imageId":"1371","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Blue32GB3GBRAM52.jpeg"},{"imageId":"1372","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Blue32GB3GBRAM53.jpeg"},{"imageId":"1373","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Blue32GB3GBRAM54.jpeg"},{"imageId":"1374","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Blue32GB3GBRAM55.jpeg"}]},{"id":"6","name":"Lenovo K10 Plus (Black, 64 GB) 4 GB RAM","brand":"Lenovo","warranty":"Brand Warranty of 1 Year Available for Mobile and 6 Months for Accessories","specs1":"[\"4 GB RAM | 64 GB ROM | Expandable Upto 2 TB\",\"15.8 cm (6.22 inch) HD+ Display\",\"13MP + 5MP + 8MP | 16MP Front Camera\",\"4050 mAh Battery\",\"Qualcomm SDM632 Processor\"]","specs2":"[\"4 GB RAM | 64 GB Storage\",\"15.8 cm (6.22 inch) HD+ Display\",\"4050 mAh\",\"13MP + 5MP + 8MP\",\"16MP Front Camera\"]","images":[{"imageId":"1375","imageUrl":"https://api.forpay.in/shop/images/Phone/LenovoK10PlusBlack64GB4GBRAM60.jpeg"},{"imageId":"1376","imageUrl":"https://api.forpay.in/shop/images/Phone/LenovoK10PlusBlack64GB4GBRAM61.jpeg"},{"imageId":"1377","imageUrl":"https://api.forpay.in/shop/images/Phone/LenovoK10PlusBlack64GB4GBRAM62.jpeg"},{"imageId":"1378","imageUrl":"https://api.forpay.in/shop/images/Phone/LenovoK10PlusBlack64GB4GBRAM63.jpeg"},{"imageId":"1379","imageUrl":"https://api.forpay.in/shop/images/Phone/LenovoK10PlusBlack64GB4GBRAM64.jpeg"}]},{"id":"7","name":"Vivo Y50 (Iris Blue, 128 GB) 8 GB RAM","brand":"Vivo","warranty":"1 Year Manufacturer Warranty for Phone and 6 Months Warranty for in the Box Accessories","specs1":"[\"8 GB RAM | 128 GB ROM | Expandable Upto 256 GB\",\"16.59 cm (6.53 inch) Display\",\"13MP + 8MP + 2MP + 2MP | 16MP Front Camera\",\"5000 mAh Lithium-ion Battery\",\"Qualcomm Snapdragon 665 Processor\"]","specs2":"[\"8 GB RAM | 128 GB Storage\",\"16.59 cm (6.53 inch) Display\",\"5000 mAh\",\"13MP + 8MP + 2MP + 2MP\",\"16MP Front Camera\"]","images":[{"imageId":"1380","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoY50IrisBlue128GB8GBRAM70.jpeg"},{"imageId":"1381","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoY50IrisBlue128GB8GBRAM71.jpeg"},{"imageId":"1382","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoY50IrisBlue128GB8GBRAM72.jpeg"},{"imageId":"1383","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoY50IrisBlue128GB8GBRAM73.jpeg"},{"imageId":"1384","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoY50IrisBlue128GB8GBRAM74.jpeg"},{"imageId":"1385","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoY50IrisBlue128GB8GBRAM75.jpeg"},{"imageId":"1386","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoY50IrisBlue128GB8GBRAM76.jpeg"}]},{"id":"8","name":"Realme 5 Pro (Crystal Green, 64 GB) 6 GB RAM","brand":"Realme","warranty":"Brand Warranty of 1 Year Available for Mobile and 6 Months for Accessories","specs1":"[\"6 GB RAM | 64 GB ROM | Expandable Upto 256 GB\",\"16.0 cm (6.3 inch) Full HD+ Display\",\"48MP + 8MP + 2MP + 2MP Quad Camera | 16MP Front Camera\",\"4035 mAh Battery\",\"Qualcomm Snapdragon SDM712 Octa Core 2.3 GHz Processor\",\"VOOC Flash Charge 3.0\",\"Triple Card Slot\"]","specs2":"[\"6 GB RAM | 64 GB Storage\",\"16.0 cm (6.3 inch) Full HD+ Display\",\"4035 mAh\",\"48MP + 8MP + 2MP + 2MP Quad Camera\",\"16MP Front Camera\"]","images":[{"imageId":"1387","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM80.jpeg"},{"imageId":"1388","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM81.jpeg"},{"imageId":"1389","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM82.jpeg"},{"imageId":"1390","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM83.jpeg"},{"imageId":"1391","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM84.jpeg"},{"imageId":"1392","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM85.jpeg"},{"imageId":"1393","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM86.jpeg"},{"imageId":"1394","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM87.jpeg"},{"imageId":"1395","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM88.jpeg"}]},{"id":"9","name":"Samsung Galaxy M01 (Black, 32 GB) 3 GB RAM","brand":"Samsung","warranty":"1 Year Manufacturer Warranty for Phone and 6 Months Warranty for in the Box Accessories","specs1":"[\"3 GB RAM | 32 GB ROM | Expandable Upto 512 GB\",\"14.48 cm (5.7 inch) HD+ Display\",\"13MP + 2MP | 5MP Front Camera\",\"4000 mAh Lithium-ion Battery\",\"Qualcomm Snapdragon (SDM439) Octa Core Processor\"]","specs2":"[\"3 GB RAM | 32 GB Storage\",\"14.48 cm (5.7 inch) HD+ Display\",\"4000 mAh\",\"13MP + 2MP\",\"5MP Front Camera\"]","images":[{"imageId":"1396","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Black32GB3GBRAM90.jpeg"},{"imageId":"1397","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Black32GB3GBRAM91.jpeg"},{"imageId":"1398","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Black32GB3GBRAM92.jpeg"},{"imageId":"1399","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Black32GB3GBRAM93.jpeg"},{"imageId":"1400","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Black32GB3GBRAM94.jpeg"},{"imageId":"1401","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Black32GB3GBRAM95.jpeg"}]},{"id":"10","name":"Vivo Z1x (Phantom Purple, 128 GB) 8 GB RAM","brand":"Vivo","warranty":"Brand Warranty of 1 Year Available for Mobile and 6 Months for In-box Accessories","specs1":"[\"8 GB RAM | 128 GB ROM\",\"16.21 cm (6.38 inch) Full HD+ Display\",\"48MP + 8MP + 2MP | 32MP Front Camera\",\"4500 mAh Li-ion Battery\",\"Qualcomm Snapdragon 712 AIE Processor\",\"22.5 W Vivo Flash Charge\"]","specs2":"[\"8 GB RAM | 128 GB Storage\",\"16.21 cm (6.38 inch) Full HD+ Display\",\"4500 mAh\",\"48MP + 8MP + 2MP\",\"32MP Front Camera\"]","images":[{"imageId":"1402","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM100.jpeg"},{"imageId":"1403","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM101.jpeg"},{"imageId":"1404","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM102.jpeg"},{"imageId":"1405","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM103.jpeg"},{"imageId":"1406","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM104.jpeg"},{"imageId":"1407","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM105.jpeg"},{"imageId":"1408","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM106.jpeg"},{"imageId":"1409","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM107.jpeg"}]}]}
     * shopId :
     * resCode : 200
     * resText :
     */

    private DataBean data;
    private String shopId;
    private String resCode;
    private String resText;

    protected ProductSuggestionModel(Parcel in) {
        shopId = in.readString();
        resCode = in.readString();
        resText = in.readString();
    }

    public static final Creator<ProductSuggestionModel> CREATOR = new Creator<ProductSuggestionModel>() {
        @Override
        public ProductSuggestionModel createFromParcel(Parcel in) {
            return new ProductSuggestionModel(in);
        }

        @Override
        public ProductSuggestionModel[] newArray(int size) {
            return new ProductSuggestionModel[size];
        }
    };

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shopId);
        parcel.writeString(resCode);
        parcel.writeString(resText);
    }

    public static class DataBean implements Parcelable{
        /**
         * shopName : Test ABC
         * imageId : 1409
         * imgUrl : https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM107.jpeg
         * contactNumber : 8999898998
         * deliveryRange : 10
         * latitude : 21.974209
         * longitude : 70.793411
         * productSuggestion : [{"id":"1","name":"Redmi Note 8 Pro (Halo White, 128 GB) 6 GB RAM","brand":"Redmi","warranty":"1 Year Warranty on Phone, 6 Months Warranty on Accessories","specs1":"[\"6 GB RAM | 128 GB ROM | Expandable Upto 512 GB\",\"16.59 cm (6.53 inch) Full HD+ Display\",\"64MP + 8MP + 2MP + 2MP | 20MP Front Camera\",\"4500 mAh Li-polymer Battery\",\"MediaTek Helio G90T Processor\"]","specs2":"[\"6 GB RAM | 128 GB Storage\",\"16.59 cm (6.53 inch) Full HD+ Display\",\"4500 mAh\",\"64MP + 8MP + 2MP + 2MP\",\"20MP Front Camera\"]","images":[{"imageId":"1324","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM10.jpeg"},{"imageId":"1325","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM11.jpeg"},{"imageId":"1326","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM12.jpeg"},{"imageId":"1327","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM13.jpeg"},{"imageId":"1328","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM14.jpeg"},{"imageId":"1329","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM15.jpeg"},{"imageId":"1330","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM16.jpeg"},{"imageId":"1331","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM17.jpeg"},{"imageId":"1332","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM18.jpeg"},{"imageId":"1333","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM19.jpeg"},{"imageId":"1334","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM110.jpeg"},{"imageId":"1335","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM111.jpeg"},{"imageId":"1336","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM112.jpeg"},{"imageId":"1337","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM113.jpeg"},{"imageId":"1338","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM114.jpeg"}]},{"id":"2","name":"Realme Narzo 10A (So Blue, 32 GB) 3 GB RAM","brand":"Realme","warranty":"Brand Warranty of 1 Year Available for Mobile and 6 Months for Accessories","specs1":"[\"3 GB RAM | 32 GB ROM | Expandable Upto 256 GB\",\"16.56 cm (6.52 inch) HD+ Display\",\"12MP + 2MP + 2MP | 5MP Front Camera\",\"5000 mAh Lithium-ion Battery\",\"MediaTek Helio G70 (12 nm) Processor\"]","specs2":"[\"3 GB RAM | 32 GB Storage\",\"16.56 cm (6.52 inch) HD+ Display\",\"5000 mAh\",\"12MP + 2MP + 2MP\",\"5MP Front Camera\"]","images":[{"imageId":"1339","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM20.jpeg"},{"imageId":"1340","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM21.jpeg"},{"imageId":"1341","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM22.jpeg"},{"imageId":"1342","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM23.jpeg"},{"imageId":"1343","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM24.jpeg"},{"imageId":"1344","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM25.jpeg"},{"imageId":"1345","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM26.jpeg"},{"imageId":"1346","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM27.jpeg"},{"imageId":"1347","imageUrl":"https://api.forpay.in/shop/images/Phone/RealmeNarzo10ASoBlue32GB3GBRAM28.jpeg"}]},{"id":"3","name":"OPPO A12 (Black, 32 GB) 3 GB RAM","brand":"OPPO","warranty":"Brand Warranty of 1 Year Available for Mobile Including Battery and 6 Months for Accessories","specs1":"[\"3 GB RAM | 32 GB ROM | Expandable Upto 256 GB\",\"15.8 cm (6.22 inch) HD+ Display\",\"13MP + 2MP | 5MP Front Camera\",\"4230 mAh Battery\",\"MediaTek Helio P35 (MT6765V/CB) Processor\"]","specs2":"[\"3 GB RAM | 32 GB Storage\",\"15.8 cm (6.22 inch) HD+ Display\",\"4230 mAh\",\"13MP + 2MP\",\"5MP Front Camera\"]","images":[{"imageId":"1348","imageUrl":"https://api.forpay.in/shop/images/Phone/OPPOA12Black32GB3GBRAM30.jpeg"},{"imageId":"1349","imageUrl":"https://api.forpay.in/shop/images/Phone/OPPOA12Black32GB3GBRAM31.jpeg"},{"imageId":"1350","imageUrl":"https://api.forpay.in/shop/images/Phone/OPPOA12Black32GB3GBRAM32.jpeg"},{"imageId":"1351","imageUrl":"https://api.forpay.in/shop/images/Phone/OPPOA12Black32GB3GBRAM33.jpeg"},{"imageId":"1352","imageUrl":"https://api.forpay.in/shop/images/Phone/OPPOA12Black32GB3GBRAM34.jpeg"},{"imageId":"1353","imageUrl":"https://api.forpay.in/shop/images/Phone/OPPOA12Black32GB3GBRAM35.jpeg"}]},{"id":"4","name":"Redmi Note 8 Pro (Halo White, 64 GB) 6 GB RAM","brand":"Redmi","warranty":"1 Year Warranty on Phone, 6 Months Warranty on Accessories","specs1":"[\"6 GB RAM | 64 GB ROM | Expandable Upto 512 GB\",\"16.59 cm (6.53 inch) Full HD+ Display\",\"64MP + 8MP + 2MP + 2MP | 20MP Front Camera\",\"4500 mAh Li-polymer Battery\",\"MediaTek Helio G90T Processor\"]","specs2":"[\"6 GB RAM | 64 GB Storage\",\"16.59 cm (6.53 inch) Full HD+ Display\",\"4500 mAh\",\"64MP + 8MP + 2MP + 2MP\",\"20MP Front Camera\"]","images":[{"imageId":"1354","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM40.jpeg"},{"imageId":"1355","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM41.jpeg"},{"imageId":"1356","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM42.jpeg"},{"imageId":"1357","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM43.jpeg"},{"imageId":"1358","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM44.jpeg"},{"imageId":"1359","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM45.jpeg"},{"imageId":"1360","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM46.jpeg"},{"imageId":"1361","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM47.jpeg"},{"imageId":"1362","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM48.jpeg"},{"imageId":"1363","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM49.jpeg"},{"imageId":"1364","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM410.jpeg"},{"imageId":"1365","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM411.jpeg"},{"imageId":"1366","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM412.jpeg"},{"imageId":"1367","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM413.jpeg"},{"imageId":"1368","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite64GB6GBRAM414.jpeg"}]},{"id":"5","name":"Samsung Galaxy M01 (Blue, 32 GB) 3 GB RAM","brand":"Samsung","warranty":"1 Year Manufacturer Warranty for Phone and 6 Months Warranty for in the Box Accessories","specs1":"[\"3 GB RAM | 32 GB ROM | Expandable Upto 512 GB\",\"14.48 cm (5.7 inch) HD+ Display\",\"13MP + 2MP | 5MP Front Camera\",\"4000 mAh Lithium-ion Battery\",\"Qualcomm Snapdragon (SDM439) Octa Core Processor\"]","specs2":"[\"3 GB RAM | 32 GB Storage\",\"14.48 cm (5.7 inch) HD+ Display\",\"4000 mAh\",\"13MP + 2MP\",\"5MP Front Camera\"]","images":[{"imageId":"1369","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Blue32GB3GBRAM50.jpeg"},{"imageId":"1370","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Blue32GB3GBRAM51.jpeg"},{"imageId":"1371","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Blue32GB3GBRAM52.jpeg"},{"imageId":"1372","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Blue32GB3GBRAM53.jpeg"},{"imageId":"1373","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Blue32GB3GBRAM54.jpeg"},{"imageId":"1374","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Blue32GB3GBRAM55.jpeg"}]},{"id":"6","name":"Lenovo K10 Plus (Black, 64 GB) 4 GB RAM","brand":"Lenovo","warranty":"Brand Warranty of 1 Year Available for Mobile and 6 Months for Accessories","specs1":"[\"4 GB RAM | 64 GB ROM | Expandable Upto 2 TB\",\"15.8 cm (6.22 inch) HD+ Display\",\"13MP + 5MP + 8MP | 16MP Front Camera\",\"4050 mAh Battery\",\"Qualcomm SDM632 Processor\"]","specs2":"[\"4 GB RAM | 64 GB Storage\",\"15.8 cm (6.22 inch) HD+ Display\",\"4050 mAh\",\"13MP + 5MP + 8MP\",\"16MP Front Camera\"]","images":[{"imageId":"1375","imageUrl":"https://api.forpay.in/shop/images/Phone/LenovoK10PlusBlack64GB4GBRAM60.jpeg"},{"imageId":"1376","imageUrl":"https://api.forpay.in/shop/images/Phone/LenovoK10PlusBlack64GB4GBRAM61.jpeg"},{"imageId":"1377","imageUrl":"https://api.forpay.in/shop/images/Phone/LenovoK10PlusBlack64GB4GBRAM62.jpeg"},{"imageId":"1378","imageUrl":"https://api.forpay.in/shop/images/Phone/LenovoK10PlusBlack64GB4GBRAM63.jpeg"},{"imageId":"1379","imageUrl":"https://api.forpay.in/shop/images/Phone/LenovoK10PlusBlack64GB4GBRAM64.jpeg"}]},{"id":"7","name":"Vivo Y50 (Iris Blue, 128 GB) 8 GB RAM","brand":"Vivo","warranty":"1 Year Manufacturer Warranty for Phone and 6 Months Warranty for in the Box Accessories","specs1":"[\"8 GB RAM | 128 GB ROM | Expandable Upto 256 GB\",\"16.59 cm (6.53 inch) Display\",\"13MP + 8MP + 2MP + 2MP | 16MP Front Camera\",\"5000 mAh Lithium-ion Battery\",\"Qualcomm Snapdragon 665 Processor\"]","specs2":"[\"8 GB RAM | 128 GB Storage\",\"16.59 cm (6.53 inch) Display\",\"5000 mAh\",\"13MP + 8MP + 2MP + 2MP\",\"16MP Front Camera\"]","images":[{"imageId":"1380","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoY50IrisBlue128GB8GBRAM70.jpeg"},{"imageId":"1381","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoY50IrisBlue128GB8GBRAM71.jpeg"},{"imageId":"1382","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoY50IrisBlue128GB8GBRAM72.jpeg"},{"imageId":"1383","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoY50IrisBlue128GB8GBRAM73.jpeg"},{"imageId":"1384","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoY50IrisBlue128GB8GBRAM74.jpeg"},{"imageId":"1385","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoY50IrisBlue128GB8GBRAM75.jpeg"},{"imageId":"1386","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoY50IrisBlue128GB8GBRAM76.jpeg"}]},{"id":"8","name":"Realme 5 Pro (Crystal Green, 64 GB) 6 GB RAM","brand":"Realme","warranty":"Brand Warranty of 1 Year Available for Mobile and 6 Months for Accessories","specs1":"[\"6 GB RAM | 64 GB ROM | Expandable Upto 256 GB\",\"16.0 cm (6.3 inch) Full HD+ Display\",\"48MP + 8MP + 2MP + 2MP Quad Camera | 16MP Front Camera\",\"4035 mAh Battery\",\"Qualcomm Snapdragon SDM712 Octa Core 2.3 GHz Processor\",\"VOOC Flash Charge 3.0\",\"Triple Card Slot\"]","specs2":"[\"6 GB RAM | 64 GB Storage\",\"16.0 cm (6.3 inch) Full HD+ Display\",\"4035 mAh\",\"48MP + 8MP + 2MP + 2MP Quad Camera\",\"16MP Front Camera\"]","images":[{"imageId":"1387","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM80.jpeg"},{"imageId":"1388","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM81.jpeg"},{"imageId":"1389","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM82.jpeg"},{"imageId":"1390","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM83.jpeg"},{"imageId":"1391","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM84.jpeg"},{"imageId":"1392","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM85.jpeg"},{"imageId":"1393","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM86.jpeg"},{"imageId":"1394","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM87.jpeg"},{"imageId":"1395","imageUrl":"https://api.forpay.in/shop/images/Phone/Realme5ProCrystalGreen64GB6GBRAM88.jpeg"}]},{"id":"9","name":"Samsung Galaxy M01 (Black, 32 GB) 3 GB RAM","brand":"Samsung","warranty":"1 Year Manufacturer Warranty for Phone and 6 Months Warranty for in the Box Accessories","specs1":"[\"3 GB RAM | 32 GB ROM | Expandable Upto 512 GB\",\"14.48 cm (5.7 inch) HD+ Display\",\"13MP + 2MP | 5MP Front Camera\",\"4000 mAh Lithium-ion Battery\",\"Qualcomm Snapdragon (SDM439) Octa Core Processor\"]","specs2":"[\"3 GB RAM | 32 GB Storage\",\"14.48 cm (5.7 inch) HD+ Display\",\"4000 mAh\",\"13MP + 2MP\",\"5MP Front Camera\"]","images":[{"imageId":"1396","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Black32GB3GBRAM90.jpeg"},{"imageId":"1397","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Black32GB3GBRAM91.jpeg"},{"imageId":"1398","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Black32GB3GBRAM92.jpeg"},{"imageId":"1399","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Black32GB3GBRAM93.jpeg"},{"imageId":"1400","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Black32GB3GBRAM94.jpeg"},{"imageId":"1401","imageUrl":"https://api.forpay.in/shop/images/Phone/SamsungGalaxyM01Black32GB3GBRAM95.jpeg"}]},{"id":"10","name":"Vivo Z1x (Phantom Purple, 128 GB) 8 GB RAM","brand":"Vivo","warranty":"Brand Warranty of 1 Year Available for Mobile and 6 Months for In-box Accessories","specs1":"[\"8 GB RAM | 128 GB ROM\",\"16.21 cm (6.38 inch) Full HD+ Display\",\"48MP + 8MP + 2MP | 32MP Front Camera\",\"4500 mAh Li-ion Battery\",\"Qualcomm Snapdragon 712 AIE Processor\",\"22.5 W Vivo Flash Charge\"]","specs2":"[\"8 GB RAM | 128 GB Storage\",\"16.21 cm (6.38 inch) Full HD+ Display\",\"4500 mAh\",\"48MP + 8MP + 2MP\",\"32MP Front Camera\"]","images":[{"imageId":"1402","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM100.jpeg"},{"imageId":"1403","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM101.jpeg"},{"imageId":"1404","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM102.jpeg"},{"imageId":"1405","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM103.jpeg"},{"imageId":"1406","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM104.jpeg"},{"imageId":"1407","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM105.jpeg"},{"imageId":"1408","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM106.jpeg"},{"imageId":"1409","imageUrl":"https://api.forpay.in/shop/images/Phone/VivoZ1xPhantomPurple128GB8GBRAM107.jpeg"}]}]
         */

        private String shopName;
        private String imageId;
        private String imgUrl;
        private String contactNumber;
        private String deliveryRange;
        private String latitude;
        private String longitude;
        private List<ProductSuggestionBean> productSuggestion;

        protected DataBean(Parcel in) {
            shopName = in.readString();
            imageId = in.readString();
            imgUrl = in.readString();
            contactNumber = in.readString();
            deliveryRange = in.readString();
            latitude = in.readString();
            longitude = in.readString();
            productSuggestion = in.createTypedArrayList(ProductSuggestionBean.CREATOR);
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getDeliveryRange() {
            return deliveryRange;
        }

        public void setDeliveryRange(String deliveryRange) {
            this.deliveryRange = deliveryRange;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public List<ProductSuggestionBean> getProductSuggestion() {
            return productSuggestion;
        }

        public void setProductSuggestion(List<ProductSuggestionBean> productSuggestion) {
            this.productSuggestion = productSuggestion;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(shopName);
            parcel.writeString(imageId);
            parcel.writeString(imgUrl);
            parcel.writeString(contactNumber);
            parcel.writeString(deliveryRange);
            parcel.writeString(latitude);
            parcel.writeString(longitude);
            parcel.writeTypedList(productSuggestion);
        }

        public static class ProductSuggestionBean implements Parcelable {
            /**
             * id : 1
             * name : Redmi Note 8 Pro (Halo White, 128 GB) 6 GB RAM
             * brand : Redmi
             * warranty : 1 Year Warranty on Phone, 6 Months Warranty on Accessories
             * specs1 : ["6 GB RAM | 128 GB ROM | Expandable Upto 512 GB","16.59 cm (6.53 inch) Full HD+ Display","64MP + 8MP + 2MP + 2MP | 20MP Front Camera","4500 mAh Li-polymer Battery","MediaTek Helio G90T Processor"]
             * specs2 : ["6 GB RAM | 128 GB Storage","16.59 cm (6.53 inch) Full HD+ Display","4500 mAh","64MP + 8MP + 2MP + 2MP","20MP Front Camera"]
             * images : [{"imageId":"1324","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM10.jpeg"},{"imageId":"1325","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM11.jpeg"},{"imageId":"1326","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM12.jpeg"},{"imageId":"1327","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM13.jpeg"},{"imageId":"1328","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM14.jpeg"},{"imageId":"1329","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM15.jpeg"},{"imageId":"1330","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM16.jpeg"},{"imageId":"1331","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM17.jpeg"},{"imageId":"1332","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM18.jpeg"},{"imageId":"1333","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM19.jpeg"},{"imageId":"1334","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM110.jpeg"},{"imageId":"1335","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM111.jpeg"},{"imageId":"1336","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM112.jpeg"},{"imageId":"1337","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM113.jpeg"},{"imageId":"1338","imageUrl":"https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM114.jpeg"}]
             */

            private String id;
            private String name;
            private String brand;
            private String warranty;
            private String specs1;
            private String specs2;
            private String imageUrl;
            private String shopId;
            private int position;
            private List<ImagesBean> images;

            protected ProductSuggestionBean(Parcel in) {
                id = in.readString();
                name = in.readString();
                brand = in.readString();
                warranty = in.readString();
                specs1 = in.readString();
                specs2 = in.readString();
                imageUrl = in.readString();
                shopId = in.readString();
                position = in.readInt();
                images = in.createTypedArrayList(ImagesBean.CREATOR);
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(id);
                dest.writeString(name);
                dest.writeString(brand);
                dest.writeString(warranty);
                dest.writeString(specs1);
                dest.writeString(specs2);
                dest.writeString(imageUrl);
                dest.writeString(shopId);
                dest.writeInt(position);
                dest.writeTypedList(images);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<ProductSuggestionBean> CREATOR = new Creator<ProductSuggestionBean>() {
                @Override
                public ProductSuggestionBean createFromParcel(Parcel in) {
                    return new ProductSuggestionBean(in);
                }

                @Override
                public ProductSuggestionBean[] newArray(int size) {
                    return new ProductSuggestionBean[size];
                }
            };

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getBrand() {
                return brand;
            }

            public void setBrand(String brand) {
                this.brand = brand;
            }

            public String getWarranty() {
                return warranty;
            }

            public void setWarranty(String warranty) {
                this.warranty = warranty;
            }

            public String getSpecs1() {
                return specs1;
            }

            public void setSpecs1(String specs1) {
                this.specs1 = specs1;
            }

            public String getSpecs2() {
                return specs2;
            }

            public void setSpecs2(String specs2) {
                this.specs2 = specs2;
            }

            public List<ImagesBean> getImages() {
                return images;
            }

            public void setImages(List<ImagesBean> images) {
                this.images = images;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getShopId() {
                return shopId;
            }

            public void setShopId(String shopId) {
                this.shopId = shopId;
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public static class ImagesBean implements Parcelable{
                /**
                 * imageId : 1324
                 * imageUrl : https://api.forpay.in/shop/images/Phone/RedmiNote8ProHaloWhite128GB6GBRAM10.jpeg
                 * specs": "[\"6 GB RAM | 128 GB ROM | Expandable Upto 512 GB\",\"16.59 cm (6.53 inch) Full HD+ Display\",\"64MP + 8MP + 2MP + 2MP | 20MP Front Camera\",\"4500 mAh Li-polymer Battery\",\"MediaTek Helio G90T Processor\"]"
                 */

                private String imageId;
                private String imageUrl;
                private String specs;

                protected ImagesBean(Parcel in) {
                    imageId = in.readString();
                    imageUrl = in.readString();
                    specs = in.readString();
                }

                public static final Creator<ImagesBean> CREATOR = new Creator<ImagesBean>() {
                    @Override
                    public ImagesBean createFromParcel(Parcel in) {
                        return new ImagesBean(in);
                    }

                    @Override
                    public ImagesBean[] newArray(int size) {
                        return new ImagesBean[size];
                    }
                };

                public String getImageId() {
                    return imageId;
                }

                public void setImageId(String imageId) {
                    this.imageId = imageId;
                }

                public String getImageUrl() {
                    return imageUrl;
                }

                public void setImageUrl(String imageUrl) {
                    this.imageUrl = imageUrl;
                }

                public String getSpecs() {
                    return specs;
                }

                public void setSpecs(String specs) {
                    this.specs = specs;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel parcel, int i) {
                    parcel.writeString(imageId);
                    parcel.writeString(imageUrl);
                    parcel.writeString(specs);
                }
            }
        }
    }
}


