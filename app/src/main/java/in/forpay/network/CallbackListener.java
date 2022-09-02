package in.forpay.network;

public interface CallbackListener {
    public void onResult(Exception e, String result, ResponseManager responseManager);
}
