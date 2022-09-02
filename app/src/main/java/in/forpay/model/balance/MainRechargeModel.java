package in.forpay.model.balance;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MainRechargeModel implements Parcelable {


    /**
     * service : [{"id":"44","service":"Bsnl Landline","type":"landline","smsCode":"","imgId":"44","imgUrl":"https://forpay.in/images/operator/44.png","params":[{"name":"mobile","placeHolder":"Account Number","label":"","inputType":"text","minLength":"10","maxLength":"10"},{"name":"opvalue1","placeHolder":"Number with STD Code","label":"","inputType":"","minLength":"0","maxLength":"0"}]},{"id":"45","service":"Bsnl Landline Corporate","type":"landline","smsCode":"","imgId":"45","imgUrl":"https://forpay.in/images/operator/45.png","params":[{"name":"mobile","placeHolder":"Number with STD Code","label":"Account Number","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"31","service":"Bsnl Postpaid","type":"postpaid","smsCode":"BSP","imgId":"31","imgUrl":"https://forpay.in/images/operator/31.png","params":[{"name":"mobile","placeHolder":"Mobile Number","label":"Enter 10 Digit Mobile Number","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"5","service":"Bsnl Special","type":"mobile","smsCode":"BSS","imgId":"5","imgUrl":"https://forpay.in/images/operator/5.png","params":[{"name":"mobile","placeHolder":"Mobile Number","label":"Enter 10 Digit Mobile Number","inputType":"number","minLength":"10","maxLength":"10"}]},{"id":"4","service":"Bsnl Topup","type":"mobile","smsCode":"BS","imgId":"4","imgUrl":"https://forpay.in/images/operator/4.png","params":[{"name":"mobile","placeHolder":"Mobile Number","label":"Enter 10 Digit Mobile Number","inputType":"number","minLength":"10","maxLength":"10"}]},{"id":"100","service":"Cafe Coffee Day eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"100","imgUrl":"https://forpay.in/images/operator/100.png","params":[]},{"id":"17","service":"Central UP Gas Limited","type":"gas","smsCode":"","imgId":"17","imgUrl":"https://forpay.in/images/operator/17.png","params":[{"name":"mobile","placeHolder":"Customer Code/CRN Number","label":"Please enter your Customer Code/CRN Number.","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"57","service":"Cesc - West Bengal","type":"electricity","smsCode":"","imgId":"57","imgUrl":"https://forpay.in/images/operator/57.png","params":[{"name":"mobile","placeHolder":"Customer Id","label":"Please enter your 11 digit Customer Id","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"168","service":"Chamundeshwari Electricity Supply Corporation Ltd. (Cesc, Mysore) - KA","type":"electricity","smsCode":"","imgId":"168","imgUrl":"https://forpay.in/images/operator/168.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"18","service":"Charotar Gas Sahakari Mandali","type":"gas","smsCode":"","imgId":"18","imgUrl":"https://forpay.in/images/operator/18.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"101","service":"Cleartrip eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"101","imgUrl":"https://forpay.in/images/operator/101.png","params":[]},{"id":"231","service":"Comway Broadband","type":"broadband","smsCode":"","imgId":"231","imgUrl":"https://forpay.in/images/operator/231.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"232","service":"Connect Broadband","type":"broadband","smsCode":"","imgId":"232","imgUrl":"https://forpay.in/images/operator/232.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"102","service":"Croma eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"102","imgUrl":"https://forpay.in/images/operator/102.png","params":[]},{"id":"212","service":"Crossword eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"212","imgUrl":"https://forpay.in/images/operator/212.png","params":[]},{"id":"179","service":"CSPDCL - Chhattisgarh","type":"electricity","smsCode":"","imgId":"179","imgUrl":"https://forpay.in/images/operator/179.png","params":[{"name":"mobile","placeHolder":"BP Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"213","service":"Cult.Fit eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"213","imgUrl":"https://forpay.in/images/operator/213.png","params":[]},{"id":"233","service":"D VoiS Communications","type":"broadband","smsCode":"","imgId":"233","imgUrl":"https://forpay.in/images/operator/233.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"59","service":"Dakshin Haryana Bijli Vitran Nigam (Dhbvn) - Haryana","type":"electricity","smsCode":"","imgId":"59","imgUrl":"https://forpay.in/images/operator/59.png","params":[{"name":"mobile","placeHolder":"Account Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"144","service":"Daman and Diu Electricity","type":"electricity","smsCode":"","imgId":"144","imgUrl":"https://forpay.in/images/operator/144.png","params":[{"name":"mobile","placeHolder":"Account Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"177","service":"Delhi Jal Board","type":"water","smsCode":"","imgId":"177","imgUrl":"https://forpay.in/images/operator/177.png","params":[{"name":"mobile","placeHolder":"K Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"33","service":"Delhi Metro","type":"metro","smsCode":"DLM","imgId":"33","imgUrl":"https://forpay.in/images/operator/33.png","params":[{"name":"mobile","placeHolder":"Metro Card Number","label":"Please enter your 8-12 digit Metro Card Number","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"234","service":"DEN Broadband","type":"broadband","smsCode":"","imgId":"234","imgUrl":"https://forpay.in/images/operator/234.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"197","service":"Department of Power - NAGALAND","type":"electricity","smsCode":"","imgId":"197","imgUrl":"https://forpay.in/images/operator/197.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"25","service":"Dish Dth","type":"dth","smsCode":"DD","imgId":"25","imgUrl":"https://forpay.in/images/operator/25.png","params":[{"name":"mobile","placeHolder":"Customer Id","label":"Customer ID starts with 0 and is 11 digits long. To locate it, press the MENU button on remote","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"60","service":"DNH Power Distribution Company Limited - Dadra and Nagar Haveli","type":"electricity","smsCode":"","imgId":"60","imgUrl":"https://forpay.in/images/operator/60.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"214","service":"Dominos eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"214","imgUrl":"https://forpay.in/images/operator/214.png","params":[]},{"id":"140","service":"Eastern Power Distribution Company of Andhra Pradesh Ltd. (APEPDCL) - ","type":"electricity","smsCode":"","imgId":"140","imgUrl":"https://forpay.in/images/operator/140.png","params":[{"name":"mobile","placeHolder":"Service Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"198","service":"Electricity Department - PUDUCHERRY","type":"electricity","smsCode":"","imgId":"198","imgUrl":"https://forpay.in/images/operator/198.png","params":[{"name":"mobile","placeHolder":"Service Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"104","service":"Fastrack eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"104","imgUrl":"https://forpay.in/images/operator/104.png","params":[]},{"id":"105","service":"Flipkart eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"105","imgUrl":"https://forpay.in/images/operator/105.png","params":[]},{"id":"235","service":"Fusionnet Web Services","type":"broadband","smsCode":"","imgId":"235","imgUrl":"https://forpay.in/images/operator/235.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"215","service":"Gaana Subscription Voucher (1 Month)","type":"digitalvoucher","smsCode":"","imgId":"215","imgUrl":"https://forpay.in/images/operator/215.png","params":[]},{"id":"106","service":"Gant eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"106","imgUrl":"https://forpay.in/images/operator/106.png","params":[]},{"id":"146","service":"Gescom - Karnataka","type":"electricity","smsCode":"","imgId":"146","imgUrl":"https://forpay.in/images/operator/146.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"19","service":"Green Gas","type":"gas","smsCode":"","imgId":"19","imgUrl":"https://forpay.in/images/operator/19.png","params":[{"name":"mobile","placeHolder":"CRN Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"48","service":"Gujarat Gas","type":"gas","smsCode":"","imgId":"48","imgUrl":"https://forpay.in/images/operator/48.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"Consumer Number","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"20","service":"Haryana City Gas","type":"gas","smsCode":"","imgId":"20","imgUrl":"https://forpay.in/images/operator/20.png","params":[{"name":"mobile","placeHolder":"11 Digit CRN Number","label":"Please enter your 11 digit CRN Number","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"236","service":"Hathway Broadband","type":"broadband","smsCode":"","imgId":"236","imgUrl":"https://forpay.in/images/operator/236.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"227","service":"HDFC Life Insurance","type":"insurance","smsCode":"","imgId":"227","imgUrl":"https://forpay.in/images/operator/227.png","params":[{"name":"mobile","placeHolder":"Policy Number","label":"","inputType":"text","minLength":"10","maxLength":"10"},{"name":"opvalue1","placeHolder":"Date Of Birth","label":"","inputType":"","minLength":"0","maxLength":"0"}]},{"id":"107","service":"Helios eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"107","imgUrl":"https://forpay.in/images/operator/107.png","params":[]},{"id":"169","service":"HESCOM - KARNATAKA","type":"electricity","smsCode":"","imgId":"169","imgUrl":"https://forpay.in/images/operator/169.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"108","service":"Hidesign eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"108","imgUrl":"https://forpay.in/images/operator/108.png","params":[]},{"id":"170","service":"Himachal Pradesh State Electricity Board","type":"electricity","smsCode":"","imgId":"170","imgUrl":"https://forpay.in/images/operator/170.png","params":[{"name":"mobile","placeHolder":"K Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"109","service":"Himalaya eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"109","imgUrl":"https://forpay.in/images/operator/109.png","params":[]},{"id":"35","service":"Hyderabad Metro","type":"metro","smsCode":"HYM","imgId":"35","imgUrl":"https://forpay.in/images/operator/35.png","params":[{"name":"mobile","placeHolder":"Metro Card Number","label":"Enter Metro Card Number","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"237","service":"I-ON Broadband","type":"broadband","smsCode":"","imgId":"237","imgUrl":"https://forpay.in/images/operator/237.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"77","service":"ICICI Prudential Life Insurance","type":"insurance","smsCode":"","imgId":"77","imgUrl":"https://forpay.in/images/operator/77.png","params":[{"name":"mobile","placeHolder":"Policy Number","label":"","inputType":"text","minLength":"10","maxLength":"10"},{"name":"opvalue1","placeHolder":"Date Of Birth","label":"","inputType":"","minLength":"0","maxLength":"0"}]},{"id":"3","service":"Idea","type":"mobile","smsCode":"ID","imgId":"3","imgUrl":"https://forpay.in/images/operator/3.png","params":[{"name":"mobile","placeHolder":"Mobile Number","label":"Enter 10 Digit Mobile Number","inputType":"number","minLength":"10","maxLength":"10"}]},{"id":"32","service":"Idea Postpaid","type":"postpaid","smsCode":"IDP","imgId":"32","imgUrl":"https://forpay.in/images/operator/32.png","params":[{"name":"mobile","placeHolder":"Mobile Number","label":"Enter 10 Digit Mobile Number","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"147","service":"India Power - West Bengal","type":"electricity","smsCode":"","imgId":"147","imgUrl":"https://forpay.in/images/operator/147.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"21","service":"IndianOil - Adani Gas","type":"gas","smsCode":"","imgId":"21","imgUrl":"https://forpay.in/images/operator/21.png","params":[{"name":"mobile","placeHolder":"Consumer Id","label":"Please enter your 10 digit Customer ID.","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"49","service":"Indraprastha Gas","type":"gas","smsCode":"","imgId":"49","imgUrl":"https://forpay.in/images/operator/49.png","params":[{"name":"mobile","placeHolder":"Business Partner Number","label":"Business Partner Number","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"238","service":"Instanet Broadband","type":"broadband","smsCode":"","imgId":"238","imgUrl":"https://forpay.in/images/operator/238.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"22","service":"IRM Energy","type":"gas","smsCode":"","imgId":"22","imgUrl":"https://forpay.in/images/operator/22.png","params":[{"name":"mobile","placeHolder":"Consumer Id","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"110","service":"IZOD eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"110","imgUrl":"https://forpay.in/images/operator/110.png","params":[]},{"id":"62","service":"Jaipur Vidyut Vitran Nigam - Rajasthan","type":"electricity","smsCode":"","imgId":"62","imgUrl":"https://forpay.in/images/operator/62.png","params":[{"name":"mobile","placeHolder":"K Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"63","service":"Jamshedpur Utilities & Service","type":"electricity","smsCode":"","imgId":"63","imgUrl":"https://forpay.in/images/operator/63.png","params":[{"name":"mobile","placeHolder":"Business Partner Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"167","service":"JBVNL - JHARKHAND","type":"electricity","smsCode":"","imgId":"167","imgUrl":"https://forpay.in/images/operator/167.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"},{"name":"opvalue1","placeHolder":"Subdivision Code","label":"","inputType":"","minLength":"0","maxLength":"0"}]},{"id":"216","service":"JioSaavn eGift Voucher (1 Month)","type":"digitalvoucher","smsCode":"","imgId":"216","imgUrl":"https://forpay.in/images/operator/216.png","params":[]},{"id":"217","service":"Jockey eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"217","imgUrl":"https://forpay.in/images/operator/217.png","params":[]},{"id":"64","service":"Jodhpur Vidyut Vitran Nigam - Rajasthan","type":"electricity","smsCode":"","imgId":"64","imgUrl":"https://forpay.in/images/operator/64.png","params":[{"name":"mobile","placeHolder":"K Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"111","service":"Joyalukkas Diamond eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"111","imgUrl":"https://forpay.in/images/operator/111.png","params":[]},{"id":"218","service":"Kalyan Diamond eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"218","imgUrl":"https://forpay.in/images/operator/218.png","params":[]},{"id":"219","service":"Kalyan Gold Coin eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"219","imgUrl":"https://forpay.in/images/operator/219.png","params":[]},{"id":"220","service":"Kalyan Gold Jewellery eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"220","imgUrl":"https://forpay.in/images/operator/220.png","params":[]},{"id":"188","service":"Kerala State Electricity Board Ltd. (KSEB) - KERALA","type":"electricity","smsCode":"","imgId":"188","imgUrl":"https://forpay.in/images/operator/188.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"199","service":"KESCO - KANPUR","type":"electricity","smsCode":"","imgId":"199","imgUrl":"https://forpay.in/images/operator/199.png","params":[{"name":"mobile","placeHolder":"K Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"148","service":"Kota Electricity Distribution - Rajasthan","type":"electricity","smsCode":"","imgId":"148","imgUrl":"https://forpay.in/images/operator/148.png","params":[{"name":"mobile","placeHolder":"K Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"221","service":"Kurlon eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"221","imgUrl":"https://forpay.in/images/operator/221.png","params":[]},{"id":"112","service":"Lakme Salon eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"112","imgUrl":"https://forpay.in/images/operator/112.png","params":[]},{"id":"113","service":"Levis eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"113","imgUrl":"https://forpay.in/images/operator/113.png","params":[]},{"id":"114","service":"Lifestyle eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"114","imgUrl":"https://forpay.in/images/operator/114.png","params":[]},{"id":"239","service":"M-NET Fiber Fast","type":"broadband","smsCode":"","imgId":"239","imgUrl":"https://forpay.in/images/operator/239.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"65","service":"Madhya Kshetra Vitaran (Rural) - MADHYA PRADESH","type":"electricity","smsCode":"","imgId":"65","imgUrl":"https://forpay.in/images/operator/65.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"195","service":"Madhya Kshetra Vitaran (Urban) - MADHYA PRADESH","type":"electricity","smsCode":"","imgId":"195","imgUrl":"https://forpay.in/images/operator/195.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"50","service":"Mahanagar Gas","type":"gas","smsCode":"","imgId":"50","imgUrl":"https://forpay.in/images/operator/50.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"Please enter your 12 digit Consumer Number","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"42","service":"Maharashtra Natural Gas","type":"gas","smsCode":"","imgId":"42","imgUrl":"https://forpay.in/images/operator/42.png","params":[{"name":"mobile","placeHolder":"BP Number","label":"Please enter your BP Number from 7 to 10 digit.","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"222","service":"Mainland China eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"222","imgUrl":"https://forpay.in/images/operator/222.png","params":[]},{"id":"115","service":"MakeMyTrip eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"115","imgUrl":"https://forpay.in/images/operator/115.png","params":[]},{"id":"116","service":"MakeMyTrip Holiday eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"116","imgUrl":"https://forpay.in/images/operator/116.png","params":[]},{"id":"117","service":"MakeMyTrip Hotel eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"117","imgUrl":"https://forpay.in/images/operator/117.png","params":[]},{"id":"149","service":"Mepdcl - Meghalaya","type":"electricity","smsCode":"","imgId":"149","imgUrl":"https://forpay.in/images/operator/149.png","params":[{"name":"mobile","placeHolder":"Consumer Id","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"200","service":"MESCOM - MANGALORE (RAPDRP)","type":"electricity","smsCode":"","imgId":"200","imgUrl":"https://forpay.in/images/operator/200.png","params":[{"name":"mobile","placeHolder":"Service Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"150","service":"Mgvcl - Gujarat","type":"electricity","smsCode":"","imgId":"150","imgUrl":"https://forpay.in/images/operator/150.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"94","service":"MONEY TRANSFER","type":"moneytransfer","smsCode":"","imgId":"94","imgUrl":"https://forpay.in/images/operator/94.png","params":[]},{"id":"66","service":"MSEDC - Maharastra","type":"electricity","smsCode":"","imgId":"66","imgUrl":"https://forpay.in/images/operator/66.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"},{"name":"opvalue1","placeHolder":"Billing Unit","label":"","inputType":"","minLength":"0","maxLength":"0"}]},{"id":"46","service":"Mtnl Delhi Landline","type":"landline","smsCode":"","imgId":"46","imgUrl":"https://forpay.in/images/operator/46.png","params":[{"name":"mobile","placeHolder":"Number With Std Code","label":"","inputType":"text","minLength":"10","maxLength":"10"},{"name":"opvalue1","placeHolder":"Account Number","label":"","inputType":"","minLength":"0","maxLength":"0"}]},{"id":"36","service":"Mtnl Mumbai Landline","type":"landline","smsCode":"","imgId":"36","imgUrl":"https://forpay.in/images/operator/36.png","params":[{"name":"mobile","placeHolder":"Account Number","label":"Number Without Std Code","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"34","service":"Mumbai Metro","type":"metro","smsCode":"MUM","imgId":"34","imgUrl":"https://forpay.in/images/operator/34.png","params":[{"name":"mobile","placeHolder":"Metro Card Number","label":"Enter Metro Card Number","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"176","service":"Municipal Corporation of Gurugram","type":"water","smsCode":"","imgId":"176","imgUrl":"https://forpay.in/images/operator/176.png","params":[{"name":"mobile","placeHolder":"K Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"151","service":"Muzaffarpur Vidyut Vitran","type":"electricity","smsCode":"","imgId":"151","imgUrl":"https://forpay.in/images/operator/151.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"118","service":"Myntra eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"118","imgUrl":"https://forpay.in/images/operator/118.png","params":[]},{"id":"119","service":"Nautica eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"119","imgUrl":"https://forpay.in/images/operator/119.png","params":[]},{"id":"152","service":"Nbpdcl - Bihar","type":"electricity","smsCode":"","imgId":"152","imgUrl":"https://forpay.in/images/operator/152.png","params":[{"name":"mobile","placeHolder":"CA Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"201","service":"NDMC - DELHI","type":"electricity","smsCode":"","imgId":"201","imgUrl":"https://forpay.in/images/operator/201.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"153","service":"Nesco - Odisha","type":"electricity","smsCode":"","imgId":"153","imgUrl":"https://forpay.in/images/operator/153.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"240","service":"Netplus Broadband","type":"broadband","smsCode":"","imgId":"240","imgUrl":"https://forpay.in/images/operator/240.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"241","service":"Nextra Broadband","type":"broadband","smsCode":"","imgId":"241","imgUrl":"https://forpay.in/images/operator/241.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"120","service":"Nike eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"120","imgUrl":"https://forpay.in/images/operator/120.png","params":[]},{"id":"67","service":"Noida Power - Noida","type":"electricity","smsCode":"","imgId":"67","imgUrl":"https://forpay.in/images/operator/67.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"121","service":"Nykaa eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"121","imgUrl":"https://forpay.in/images/operator/121.png","params":[]},{"id":"122","service":"P N Rao eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"122","imgUrl":"https://forpay.in/images/operator/122.png","params":[]},{"id":"123","service":"Pantaloons eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"123","imgUrl":"https://forpay.in/images/operator/123.png","params":[]},{"id":"69","service":"Paschim Kshetra Vitaran - MADHYA PRADESH","type":"electricity","smsCode":"","imgId":"69","imgUrl":"https://forpay.in/images/operator/69.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"124","service":"Pavers England eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"124","imgUrl":"https://forpay.in/images/operator/124.png","params":[]},{"id":"125","service":"Peter England eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"125","imgUrl":"https://forpay.in/images/operator/125.png","params":[]},{"id":"154","service":"Pgvcl - Gujarat","type":"electricity","smsCode":"","imgId":"154","imgUrl":"https://forpay.in/images/operator/154.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"126","service":"Planet Fashion eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"126","imgUrl":"https://forpay.in/images/operator/126.png","params":[]},{"id":"194","service":"Poorv Kshetra Vitaran (Rural) - MADHYA PRADESH","type":"electricity","smsCode":"","imgId":"194","imgUrl":"https://forpay.in/images/operator/194.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"193","service":"Poorv Kshetra Vitaran (Urban) - MADHYA PRADESH","type":"electricity","smsCode":"","imgId":"193","imgUrl":"https://forpay.in/images/operator/193.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"127","service":"Prestige Smart Kitchen eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"127","imgUrl":"https://forpay.in/images/operator/127.png","params":[]},{"id":"171","service":"PSPCL - PUNJAB","type":"electricity","smsCode":"","imgId":"171","imgUrl":"https://forpay.in/images/operator/171.png","params":[{"name":"mobile","placeHolder":"Account Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"129","service":"PVR Cinemas eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"129","imgUrl":"https://forpay.in/images/operator/129.png","params":[]},{"id":"70","service":"Reliance Energy - Mumbai","type":"electricity","smsCode":"","imgId":"70","imgUrl":"https://forpay.in/images/operator/70.png","params":[{"name":"mobile","placeHolder":"Account Number","label":"","inputType":"text","minLength":"10","maxLength":"10"},{"name":"opvalue1","placeHolder":"Cycle Number","label":"","inputType":"","minLength":"0","maxLength":"0"}]},{"id":"93","service":"Reliance Jio","type":"mobile","smsCode":"RJ","imgId":"93","imgUrl":"https://forpay.in/images/operator/93.png","params":[{"name":"mobile","placeHolder":"Mobile Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"41","service":"Sabarmati Gas","type":"gas","smsCode":"","imgId":"41","imgUrl":"https://forpay.in/images/operator/41.png","params":[{"name":"mobile","placeHolder":"Consumer Id","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"155","service":"Sbpdcl - Bihar","type":"electricity","smsCode":"","imgId":"155","imgUrl":"https://forpay.in/images/operator/155.png","params":[{"name":"mobile","placeHolder":"CA Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"131","service":"Shoppers Stop eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"131","imgUrl":"https://forpay.in/images/operator/131.png","params":[]},{"id":"203","service":"Sikkim Power (RURAL)","type":"electricity","smsCode":"","imgId":"203","imgUrl":"https://forpay.in/images/operator/203.png","params":[{"name":"mobile","placeHolder":"Service Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"204","service":"Sikkim Power (URBAN)","type":"electricity","smsCode":"","imgId":"204","imgUrl":"https://forpay.in/images/operator/204.png","params":[{"name":"mobile","placeHolder":"Service Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"40","service":"Siti Energy","type":"gas","smsCode":"","imgId":"40","imgUrl":"https://forpay.in/images/operator/40.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"157","service":"Southco - Odisha","type":"electricity","smsCode":"","imgId":"157","imgUrl":"https://forpay.in/images/operator/157.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"72","service":"Southern Power - Telangana","type":"electricity","smsCode":"","imgId":"72","imgUrl":"https://forpay.in/images/operator/72.png","params":[{"name":"mobile","placeHolder":"Service Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"141","service":"Southern Power Distribution Company of A.P Ltd (APSPDCL) - Andhra Prad","type":"electricity","smsCode":"","imgId":"141","imgUrl":"https://forpay.in/images/operator/141.png","params":[{"name":"mobile","placeHolder":"Service Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"242","service":"Spectranet Broadband","type":"broadband","smsCode":"","imgId":"242","imgUrl":"https://forpay.in/images/operator/242.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"225","service":"Starbucks eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"225","imgUrl":"https://forpay.in/images/operator/225.png","params":[]},{"id":"26","service":"Sun Dth","type":"dth","smsCode":"SD","imgId":"26","imgUrl":"https://forpay.in/images/operator/26.png","params":[{"name":"mobile","placeHolder":"Customer Id","label":"Enter 11 digit customer id","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"223","service":"Swiggy eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"223","imgUrl":"https://forpay.in/images/operator/223.png","params":[]},{"id":"224","service":"Taj Hotels eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"224","imgUrl":"https://forpay.in/images/operator/224.png","params":[]},{"id":"79","service":"Tata AIA Life Insurance","type":"insurance","smsCode":"","imgId":"79","imgUrl":"https://forpay.in/images/operator/79.png","params":[{"name":"mobile","placeHolder":"Policy Number","label":"","inputType":"text","minLength":"10","maxLength":"10"},{"name":"opvalue1","placeHolder":"Date Of Birth","label":"","inputType":"","minLength":"0","maxLength":"0"}]},{"id":"160","service":"Tata Power - Ajmer","type":"electricity","smsCode":"","imgId":"160","imgUrl":"https://forpay.in/images/operator/160.png","params":[{"name":"mobile","placeHolder":"K Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"73","service":"Tata Power - Delhi","type":"electricity","smsCode":"","imgId":"73","imgUrl":"https://forpay.in/images/operator/73.png","params":[{"name":"mobile","placeHolder":"Customer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"158","service":"Tata Power - Mumbai","type":"electricity","smsCode":"","imgId":"158","imgUrl":"https://forpay.in/images/operator/158.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"27","service":"Tata Sky Dth","type":"dth","smsCode":"TSD","imgId":"27","imgUrl":"https://forpay.in/images/operator/27.png","params":[{"name":"mobile","placeHolder":"Customer Id","label":"Subscriber ID starts with 1 and is 10 digits long. To locate it, press the home button on remote.","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"189","service":"Telangana Co-Operative Electric Supply Society Ltd","type":"electricity","smsCode":"","imgId":"189","imgUrl":"https://forpay.in/images/operator/189.png","params":[{"name":"mobile","placeHolder":"Unique Service Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"190","service":"Telangana State Southern Power Distribution Compan","type":"electricity","smsCode":"","imgId":"190","imgUrl":"https://forpay.in/images/operator/190.png","params":[{"name":"mobile","placeHolder":"Unique Service Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"132","service":"Thomas Cook eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"132","imgUrl":"https://forpay.in/images/operator/132.png","params":[]},{"id":"243","service":"Tikona Broadband","type":"broadband","smsCode":"","imgId":"243","imgUrl":"https://forpay.in/images/operator/243.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"244","service":"Timbl Broadband","type":"broadband","smsCode":"","imgId":"244","imgUrl":"https://forpay.in/images/operator/244.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"133","service":"Titan eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"133","imgUrl":"https://forpay.in/images/operator/133.png","params":[]},{"id":"159","service":"Tneb- Tamil Nadu","type":"electricity","smsCode":"","imgId":"159","imgUrl":"https://forpay.in/images/operator/159.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"74","service":"Torrent Power","type":"electricity","smsCode":"","imgId":"74","imgUrl":"https://forpay.in/images/operator/74.png","params":[{"name":"mobile","placeHolder":"Service Number","label":"","inputType":"text","minLength":"10","maxLength":"10"},{"name":"opvalue1","placeHolder":"City Name","label":"","inputType":"","minLength":"0","maxLength":"0"}]},{"id":"187","service":"Torrent Power - Agra","type":"electricity","smsCode":"","imgId":"187","imgUrl":"https://forpay.in/images/operator/187.png","params":[{"name":"mobile","placeHolder":"Service Number","label":"","inputType":"text","minLength":"10","maxLength":"10"},{"name":"opvalue1","placeHolder":"City","label":"","inputType":"","minLength":"0","maxLength":"0"}]},{"id":"186","service":"Torrent Power - Ahemdabad","type":"electricity","smsCode":"","imgId":"186","imgUrl":"https://forpay.in/images/operator/186.png","params":[{"name":"mobile","placeHolder":"Service Number","label":"","inputType":"text","minLength":"10","maxLength":"10"},{"name":"opvalue1","placeHolder":"City","label":"","inputType":"","minLength":"0","maxLength":"0"}]},{"id":"185","service":"Torrent Power - Bhiwandi","type":"electricity","smsCode":"","imgId":"185","imgUrl":"https://forpay.in/images/operator/185.png","params":[{"name":"mobile","placeHolder":"Service Number","label":"","inputType":"text","minLength":"10","maxLength":"10"},{"name":"opvalue1","placeHolder":"City","label":"","inputType":"","minLength":"0","maxLength":"0"}]},{"id":"205","service":"Torrent Power - SMK","type":"electricity","smsCode":"","imgId":"205","imgUrl":"https://forpay.in/images/operator/205.png","params":[{"name":"mobile","placeHolder":"Service Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"184","service":"Torrent Power - Surat","type":"electricity","smsCode":"","imgId":"184","imgUrl":"https://forpay.in/images/operator/184.png","params":[{"name":"mobile","placeHolder":"Service Number","label":"","inputType":"text","minLength":"10","maxLength":"10"},{"name":"opvalue1","placeHolder":"City","label":"","inputType":"","minLength":"0","maxLength":"0"}]},{"id":"39","service":"Tripura Natural Gas","type":"gas","smsCode":"","imgId":"39","imgUrl":"https://forpay.in/images/operator/39.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"75","service":"TSECL - Tripura","type":"electricity","smsCode":"","imgId":"75","imgUrl":"https://forpay.in/images/operator/75.png","params":[{"name":"mobile","placeHolder":"Consumer Id","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"245","service":"TTN BroadBand","type":"broadband","smsCode":"","imgId":"245","imgUrl":"https://forpay.in/images/operator/245.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"161","service":"Ugvcl - Gujarat","type":"electricity","smsCode":"","imgId":"161","imgUrl":"https://forpay.in/images/operator/161.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"172","service":"UHBVN - HARYANA","type":"electricity","smsCode":"","imgId":"172","imgUrl":"https://forpay.in/images/operator/172.png","params":[{"name":"mobile","placeHolder":"Account Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"38","service":"Unique Central Piped Gases Pvt Ltd (UCPGPL)","type":"gas","smsCode":"","imgId":"38","imgUrl":"https://forpay.in/images/operator/38.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"Please enter your Consumer Number without special characters and spaces","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"162","service":"Upcl - Uttarakhand","type":"electricity","smsCode":"","imgId":"162","imgUrl":"https://forpay.in/images/operator/162.png","params":[{"name":"mobile","placeHolder":"Service Connection Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"163","service":"Uppcl (Rural) - Uttar Pradesh","type":"electricity","smsCode":"","imgId":"163","imgUrl":"https://forpay.in/images/operator/163.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"164","service":"Uppcl (Urban) - Uttar Pradesh","type":"electricity","smsCode":"","imgId":"164","imgUrl":"https://forpay.in/images/operator/164.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"175","service":"Urban Improvement Trust (UIT) - BHIWADI","type":"water","smsCode":"","imgId":"175","imgUrl":"https://forpay.in/images/operator/175.png","params":[{"name":"mobile","placeHolder":"Customer Id","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"134","service":"US Polo Assn eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"134","imgUrl":"https://forpay.in/images/operator/134.png","params":[]},{"id":"178","service":"Uttarakhand Jal Sansthan","type":"water","smsCode":"","imgId":"178","imgUrl":"https://forpay.in/images/operator/178.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"246","service":"V-FiberNet Broadband","type":"broadband","smsCode":"","imgId":"246","imgUrl":"https://forpay.in/images/operator/246.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"37","service":"Vadodara Gas","type":"gas","smsCode":"","imgId":"37","imgUrl":"https://forpay.in/images/operator/37.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"Consumer Number","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"135","service":"Van Heusen eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"135","imgUrl":"https://forpay.in/images/operator/135.png","params":[]},{"id":"28","service":"Videocon Dth","type":"dth","smsCode":"VD","imgId":"28","imgUrl":"https://forpay.in/images/operator/28.png","params":[{"name":"mobile","placeHolder":"Customer Id","label":"To know your Customer ID SMS 'ID' to 566777 from your registered mobile number.","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"136","service":"VLCC eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"136","imgUrl":"https://forpay.in/images/operator/136.png","params":[]},{"id":"10","service":"Vodafone","type":"mobile","smsCode":"VO","imgId":"10","imgUrl":"https://forpay.in/images/operator/10.png","params":[{"name":"mobile","placeHolder":"Mobile Number","label":"Enter 10 Digit Mobile Number","inputType":"number","minLength":"10","maxLength":"10"}]},{"id":"29","service":"Vodafone Postpaid","type":"postpaid","smsCode":"VOP","imgId":"29","imgUrl":"https://forpay.in/images/operator/29.png","params":[{"name":"mobile","placeHolder":"Mobile Number","label":"Enter 10 Digit Mobile Number","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"174","service":"WBSEDCL - WEST BENGAL","type":"electricity","smsCode":"","imgId":"174","imgUrl":"https://forpay.in/images/operator/174.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"165","service":"Wesco - Odisha","type":"electricity","smsCode":"","imgId":"165","imgUrl":"https://forpay.in/images/operator/165.png","params":[{"name":"mobile","placeHolder":"Consumer Number","label":"","inputType":"text","minLength":"10","maxLength":"10"}]},{"id":"137","service":"Westside eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"137","imgUrl":"https://forpay.in/images/operator/137.png","params":[]},{"id":"138","service":"Yatra eGift Voucher","type":"digitalvoucher","smsCode":"","imgId":"138","imgUrl":"https://forpay.in/images/operator/138.png","params":[]},{"id":"226","service":"Zee5 Subsription Voucher","type":"digitalvoucher","smsCode":"","imgId":"226","imgUrl":"https://forpay.in/images/operator/226.png","params":[]}]
     * quickMenu : [{"imgId":"1","imgurl":"https://forpay.in/images/icon/mobile.png","type":"mobile","name":"Prepaid Recharge"},{"imgId":"2","imgurl":"https://forpay.in/images/icon/dth.png","type":"dth","name":"Dth Recharge"},{"imgId":"3","imgurl":"https://forpay.in/images/icon/mobile.png","type":"postpaid","name":"Postpaid Recharge"},{"imgId":"4","imgurl":"https://forpay.in/images/icon/metro.png","type":"metro","name":"Metro Card Recharge"},{"imgId":"5","imgUrl":"https://forpay.in/images/icon/shopping.png","type":"shop","name":"Shopping"},{"imgId":"6","imgUrl":"https://forpay.in/images/icon/bus.png","type":"bus","name":"Bus"},{"imgId":"7","imgurl":"https://forpay.in/images/icon/landline.png","type":"landline","name":"Landline Bill Payment"},{"imgId":"8","imgurl":"https://forpay.in/images/icon/electricity.png","type":"electricity","name":"Electricity Bill Payment"},{"imgId":"9","imgurl":"https://forpay.in/images/icon/gas.png","type":"gas","name":"Gas Bill Payment"},{"imgId":"10","imgurl":"https://forpay.in/images/icon/insurance.png","type":"insurance","name":"Insurance Payment"},{"imgId":"11","imgurl":"https://forpay.in/images/icon/water.png","type":"water","name":"Water Bill Payment"},{"imgId":"12","imgurl":"https://forpay.in/images/icon/landline.png","type":"broadband","name":"Broadband Bill Payment"},{"imgId":"13","imgurl":"https://forpay.in/images/icon/moneyTransfer.png","type":"moneytransfer","name":"Money Transfer"},{"imgId":"14","imgurl":"https://forpay.in/images/icon/gift.png","type":"digitalvoucher","name":"Gift Voucher"},{"imgId":"15","imgUrl":"https://forpay.in/images/icon/pan.png","type":"pan","0":"Pan Agent"}]
     * icon : null
     * fastModeEnabled : yes
     * resCode : 200
     * bal : 124.05
     * incentiveBal : 0.00
     * homeBannerList : [{"imgId":"1","url":"http://oxyme.in/apiConnect/image/slideBanner/banner1.png","activity":"bus","inputData":""},{"imgId":"2","url":"http://oxyme.in/apiConnect/image/slideBanner/banner2.png","activity":"rechargeHistory","inputData":""},{"imgId":"3","url":"http://oxyme.in/apiConnect/image/slideBanner/banner3.png","activity":"giftVoucher","inputData":"flipkart"}]
     * couponOffer : [{"text":"10 Rs Coupon Available","coupon":"FREE10"},{"text":"20 Rs Coupon Available","coupon":"FREE20"}]
     * resText : SUCCESS
     */

    private Object icon;
    private String fastModeEnabled;
    private String resCode;
    private String bal;
    private String incentiveBal;
    private String gatewayLimit;
    private String offerUrl="";
    private String showGoogleAd="";
    private String resText;
    private List<ServiceBean> service;
    private List<QuickMenuBean> quickMenu;
    private List<MainMenuBean> mainMenu;
    private List<HomeBannerListBean> homeBannerList;
    private List<CouponOfferBean> couponOffer;
    private List<NavigationMenu> navigationMenu;
    private List<HistoryMenu> historyMenu;
    private ArrayList<PaymentGateway> paymentGateway;

    public String getShowGoogleAd() {
        return showGoogleAd;
    }

    public void setShowGoogleAd(String showGoogleAd) {
        this.showGoogleAd = showGoogleAd;
    }

    public String getOfferUrl() {
        return offerUrl;
    }

    public void setOfferUrl(String offerUrl) {
        this.offerUrl = offerUrl;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    protected MainRechargeModel(Parcel in) {
        fastModeEnabled = in.readString();
        resCode = in.readString();
        bal = in.readString();
        incentiveBal = in.readString();
        gatewayLimit = in.readString();
        resText = in.readString();
    }

    public static final Creator<MainRechargeModel> CREATOR = new Creator<MainRechargeModel>() {
        @Override
        public MainRechargeModel createFromParcel(Parcel in) {
            return new MainRechargeModel(in);
        }

        @Override
        public MainRechargeModel[] newArray(int size) {
            return new MainRechargeModel[size];
        }
    };

    public Object getIcon() {
        return icon;
    }

    public void setIcon(Object icon) {
        this.icon = icon;
    }

    public ArrayList<PaymentGateway> getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(ArrayList<PaymentGateway> paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String getFastModeEnabled() {
        return fastModeEnabled;
    }

    public void setFastModeEnabled(String fastModeEnabled) {
        this.fastModeEnabled = fastModeEnabled;
    }


    public String getBal() {
        return bal;
    }

    public void setBal(String bal) {
        this.bal = bal;
    }

    public String getIncentiveBal() {
        return incentiveBal;
    }

    public void setIncentiveBal(String incentiveBal) {
        this.incentiveBal = incentiveBal;
    }

    public String getGatewayLimit() {
        return gatewayLimit;
    }

    public void setGatewayLimit(String gatewayLimit) {
        this.gatewayLimit = gatewayLimit;
    }

    public String getResText() {
        return resText;
    }

    public void setResText(String resText) {
        this.resText = resText;
    }

    public List<ServiceBean> getService() {
        return service;
    }

    public void setService(List<ServiceBean> service) {
        this.service = service;
    }

    public List<QuickMenuBean> getQuickMenu() {
        return quickMenu;
    }

    public void setQuickMenu(List<QuickMenuBean> quickMenu) {
        this.quickMenu = quickMenu;
    }

    public List<MainMenuBean> getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(List<MainMenuBean> mainkMenu) {
        this.mainMenu = mainMenu;
    }

    public List<NavigationMenu> getNavigationMenu() {
        return navigationMenu;
    }

    public void setNavigationMenu(List<NavigationMenu> navigationMenu) {
        this.navigationMenu = navigationMenu;
    }

    public List<HistoryMenu> getHistoryMenu() {return historyMenu;}

    public void setHistoryMenu(List<HistoryMenu> historyMenu) {
        this.historyMenu = historyMenu;
    }

    public static Creator<MainRechargeModel> getCREATOR() {
        return CREATOR;
    }

    public List<HomeBannerListBean> getHomeBannerList() {
        return homeBannerList;
    }

    public void setHomeBannerList(List<HomeBannerListBean> homeBannerList) {
        this.homeBannerList = homeBannerList;
    }

    public List<CouponOfferBean> getCouponOffer() {
        return couponOffer;
    }

    public void setCouponOffer(List<CouponOfferBean> couponOffer) {
        this.couponOffer = couponOffer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fastModeEnabled);
        parcel.writeString(resCode);
        parcel.writeString(bal);
        parcel.writeString(incentiveBal);
        parcel.writeString(resText);
    }

    public static class ServiceBean implements Parcelable{
        /**
         * id : 44
         * service : Bsnl Landline
         * type : landline
         * smsCode :
         * imgId : 44
         * imgUrl : https://forpay.in/images/operator/44.png
         * params : [{"name":"mobile","placeHolder":"Account Number","label":"","inputType":"text","minLength":"10","maxLength":"10"},{"name":"opvalue1","placeHolder":"Number with STD Code","label":"","inputType":"","minLength":"0","maxLength":"0"}]
         */

        private String id;
        private String service;
        private String type;
        private String smsCode;
        private String imgId;
        private String imgUrl;
        private String topIconUrl;
        private String isBillFetch;
        private String margin;
        private String instruction;
        private List<ParamsBean> params;

        protected ServiceBean(Parcel in) {
            id = in.readString();
            service = in.readString();
            type = in.readString();
            smsCode = in.readString();
            imgId = in.readString();
            imgUrl = in.readString();
            topIconUrl = in.readString();
            isBillFetch=in.readString();
            margin=in.readString();
            instruction=in.readString();
        }

        public static final Creator<ServiceBean> CREATOR = new Creator<ServiceBean>() {
            @Override
            public ServiceBean createFromParcel(Parcel in) {
                return new ServiceBean(in);
            }

            @Override
            public ServiceBean[] newArray(int size) {
                return new ServiceBean[size];
            }
        };

        public String getMargin() {
            return margin;
        }

        public void setMargin(String margin) {
            this.margin = margin;
        }

        public String getInstruction() {
            return instruction;
        }

        public void setInstruction(String instruction) {
            this.instruction = instruction;
        }

        public String getIsBillFetch() {
            return isBillFetch;
        }

        public void setIsBillFetch(String isBillFetch) {
            this.isBillFetch = isBillFetch;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSmsCode() {
            return smsCode;
        }

        public void setSmsCode(String smsCode) {
            this.smsCode = smsCode;
        }

        public String getImgId() {
            return imgId;
        }

        public void setImgId(String imgId) {
            this.imgId = imgId;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getTopIconUrl() {
            return topIconUrl;
        }

        public void setTopIconUrl(String topIconUrl) {
            this.topIconUrl = topIconUrl;
        }

        public List<ParamsBean> getParams() {
            return params;
        }

        public void setParams(List<ParamsBean> params) {
            this.params = params;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(id);
            parcel.writeString(service);
            parcel.writeString(type);
            parcel.writeString(smsCode);
            parcel.writeString(imgId);
            parcel.writeString(imgUrl);
            parcel.writeString(topIconUrl);
            parcel.writeString(isBillFetch);
            parcel.writeString(margin);
            parcel.writeString(instruction);
        }

        public static class ParamsBean implements Parcelable{
            /**
             * name : mobile
             * placeHolder : Account Number
             * label :
             * inputType : text
             * minLength : 10
             * maxLength : 10
             */

            private String name;
            private String placeHolder;
            private String label;
            private String inputType;
            private String minLength;
            private String maxLength;

            protected ParamsBean(Parcel in) {
                name = in.readString();
                placeHolder = in.readString();
                label = in.readString();
                inputType = in.readString();
                minLength = in.readString();
                maxLength = in.readString();
            }

            public static final Creator<ParamsBean> CREATOR = new Creator<ParamsBean>() {
                @Override
                public ParamsBean createFromParcel(Parcel in) {
                    return new ParamsBean(in);
                }

                @Override
                public ParamsBean[] newArray(int size) {
                    return new ParamsBean[size];
                }
            };

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPlaceHolder() {
                return placeHolder;
            }

            public void setPlaceHolder(String placeHolder) {
                this.placeHolder = placeHolder;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getInputType() {
                return inputType;
            }

            public void setInputType(String inputType) {
                this.inputType = inputType;
            }

            public String getMinLength() {
                return minLength;
            }

            public void setMinLength(String minLength) {
                this.minLength = minLength;
            }

            public String getMaxLength() {
                return maxLength;
            }

            public void setMaxLength(String maxLength) {
                this.maxLength = maxLength;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString(name);
                parcel.writeString(placeHolder);
                parcel.writeString(label);
                parcel.writeString(inputType);
                parcel.writeString(minLength);
                parcel.writeString(maxLength);
            }
        }
    }


    public static class MainMenuBean implements Parcelable{
        /**
         * imgId : 1
         * imgurl : https://forpay.in/images/icon/mobile.png
         * type : mobile
         * name : Prepaid Recharge
         * imgUrl : https://forpay.in/images/icon/shopping.png
         * 0 : Pan Agent
         */

        private String imgId;
        private String imgurl;
        private String type;
        private String name;
        private String imgUrl;
        @SerializedName("0")
        private String _$0;
        private String isNew;
        private String isBbps;
        private String url;

        protected MainMenuBean(Parcel in) {
            imgId = in.readString();
            imgurl = in.readString();
            type = in.readString();
            name = in.readString();
            imgUrl = in.readString();
            _$0 = in.readString();
            isNew=in.readString();
            isBbps=in.readString();
            url=in.readString();
        }


        public static final Creator<MainMenuBean> CREATOR = new Creator<MainMenuBean>() {
            @Override
            public MainMenuBean createFromParcel(Parcel in) {
                return new MainMenuBean(in);
            }

            @Override
            public MainMenuBean[] newArray(int size) {
                return new MainMenuBean[size];
            }
        };

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getImgId() {
            return imgId;
        }

        public void setImgId(String imgId) {
            this.imgId = imgId;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String get_$0() {
            return _$0;
        }

        public void set_$0(String _$0) {
            this._$0 = _$0;
        }

        public String getIsBbps() {
            return isBbps;
        }

        public void setIsBbps(String isBbps) {
            this.isBbps = isBbps;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIsNew() {
            return isNew;
        }

        public void setIsNew(String isNew) {
            this.isNew = isNew;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(imgId);
            parcel.writeString(imgurl);
            parcel.writeString(type);
            parcel.writeString(name);
            parcel.writeString(imgUrl);
            parcel.writeString(_$0);
            parcel.writeString(isNew);
            parcel.writeString(isBbps);
            parcel.writeString(url);
        }
    }

    public static class HistoryMenu implements Parcelable{
        String activity="";
        String title="";
        protected HistoryMenu(Parcel in){


            activity=in.readString();
            title=in.readString();
        }

        public static final Creator<HistoryMenu> CREATOR = new Creator<HistoryMenu>() {
            @Override
            public HistoryMenu createFromParcel(Parcel in) {
                return new HistoryMenu(in);
            }

            @Override
            public HistoryMenu[] newArray(int size) {
                return new HistoryMenu[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }
        public String getActivity() {
            return activity;
        }
        public String getTitle(){return title;}

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(activity);
            parcel.writeString(title);

        }

    }

    public static class NavigationMenu implements Parcelable{

        String openIn, name, url, iconUrl, isNew;


        protected NavigationMenu(Parcel in) {
            openIn=in.readString();
            name=in.readString();
            url=in.readString();
            iconUrl=in.readString();
            isNew=in.readString();
        }

        public static final Creator<NavigationMenu> CREATOR = new Creator<NavigationMenu>() {
            @Override
            public NavigationMenu createFromParcel(Parcel in) {
                return new NavigationMenu(in);
            }

            @Override
            public NavigationMenu[] newArray(int size) {
                return new NavigationMenu[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        public String getOpenIn() {
            return openIn;
        }

        public void setOpenIn(String openIn) {
            this.openIn = openIn;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getIsNew() {
            return isNew;
        }

        public void setIsNew(String isNew) {
            this.isNew = isNew;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(openIn);
            parcel.writeString(name);
            parcel.writeString(url);
            parcel.writeString(iconUrl);
            parcel.writeString(isNew);
        }
    }


    public static class QuickMenuBean implements Parcelable{
        /**
         * imgId : 1
         * imgurl : https://forpay.in/images/icon/mobile.png
         * type : mobile
         * name : Prepaid Recharge
         * imgUrl : https://forpay.in/images/icon/shopping.png
         * 0 : Pan Agent
         */

        private String imgId;
        private String imgurl;
        private String type;
        private String name;
        private String imgUrl;
        @SerializedName("0")
        private String _$0;

        private String isBbps;
        private String isNew;
        private String url;

        protected QuickMenuBean(Parcel in) {
            imgId = in.readString();
            imgurl = in.readString();
            type = in.readString();
            name = in.readString();
            imgUrl = in.readString();
            _$0 = in.readString();
            isNew= in.readString();
            isBbps = in.readString();
            url=in.readString();
        }

        public static final Creator<QuickMenuBean> CREATOR = new Creator<QuickMenuBean>() {
            @Override
            public QuickMenuBean createFromParcel(Parcel in) {
                return new QuickMenuBean(in);
            }

            @Override
            public QuickMenuBean[] newArray(int size) {
                return new QuickMenuBean[size];
            }
        };

        public String getImgId() {
            return imgId;
        }

        public void setImgId(String imgId) {
            this.imgId = imgId;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }


        public String get_$0() {
            return _$0;
        }

        public void set_$0(String _$0) {
            this._$0 = _$0;
        }

        public String getIsNew() {
            return isNew;
        }

        public void setIsNew(String isNew) {
            this.isNew = isNew;
        }

        public String getIsBbps() {
            return isBbps;
        }

        public void setIsBbps(String isBbps) {
            this.isBbps = isBbps;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(imgId);
            parcel.writeString(imgurl);
            parcel.writeString(type);
            parcel.writeString(name);
            parcel.writeString(imgUrl);
            parcel.writeString(_$0);
            parcel.writeString(isNew);
            parcel.writeString(isBbps);
            parcel.writeString(url);
        }
    }

    public static class HomeBannerListBean implements Parcelable{
        /**
         * imgId : 1
         * url : http://oxyme.in/apiConnect/image/slideBanner/banner1.png
         * activity : bus
         * inputData :
         */

        private String imgId;
        private String url;
        private String activity;
        private String inputData;

        protected HomeBannerListBean(Parcel in) {
            imgId = in.readString();
            url = in.readString();
            activity = in.readString();
            inputData = in.readString();
        }

        public static final Creator<HomeBannerListBean> CREATOR = new Creator<HomeBannerListBean>() {
            @Override
            public HomeBannerListBean createFromParcel(Parcel in) {
                return new HomeBannerListBean(in);
            }

            @Override
            public HomeBannerListBean[] newArray(int size) {
                return new HomeBannerListBean[size];
            }
        };

        public String getImgId() {
            return imgId;
        }

        public void setImgId(String imgId) {
            this.imgId = imgId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getActivity() {
            return activity;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }

        public String getInputData() {
            return inputData;
        }

        public void setInputData(String inputData) {
            this.inputData = inputData;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(imgId);
            parcel.writeString(url);
            parcel.writeString(activity);
            parcel.writeString(inputData);
        }
    }

    public static class CouponOfferBean implements Parcelable{
        /**
         * text : 10 Rs Coupon Available
         * coupon : FREE10
         */

        private String id;
        private String text;
        private String coupon;
        private String expireDate;

        protected CouponOfferBean(Parcel in) {
            text = in.readString();
            coupon = in.readString();
            expireDate = in.readString();
            id = in.readString();
        }

        public static final Creator<CouponOfferBean> CREATOR = new Creator<CouponOfferBean>() {
            @Override
            public CouponOfferBean createFromParcel(Parcel in) {
                return new CouponOfferBean(in);
            }

            @Override
            public CouponOfferBean[] newArray(int size) {
                return new CouponOfferBean[size];
            }
        };

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getCoupon() {
            return coupon;
        }

        public void setCoupon(String coupon) {
            this.coupon = coupon;
        }

        public String getExpireDate() {
            return expireDate;
        }

        public void setExpireDate(String expireDate) {
            this.expireDate = expireDate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(text);
            parcel.writeString(coupon);
            parcel.writeString(expireDate);
            parcel.writeString(id);
        }
    }

    public static class PaymentGateway implements Parcelable{

        private String name;
        private String iconUrl;
        private String text;
        private String type;

        protected PaymentGateway(Parcel in) {
            name = in.readString();
            iconUrl = in.readString();
            text = in.readString();
            type = in.readString();
        }

        public static final Creator<HomeBannerListBean> CREATOR = new Creator<HomeBannerListBean>() {
            @Override
            public HomeBannerListBean createFromParcel(Parcel in) {
                return new HomeBannerListBean(in);
            }

            @Override
            public HomeBannerListBean[] newArray(int size) {
                return new HomeBannerListBean[size];
            }
        };

        public String getName() {
            return name;
        }

        public void setName(String imgId) {
            this.name = imgId;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(name);
            parcel.writeString(iconUrl);
            parcel.writeString(text);
            parcel.writeString(type);
        }
    }
}
