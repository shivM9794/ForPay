package in.forpay.model.response;

public class GetPanAgentResponse {

    private Data data;
    private String resCode = "";
    private String resText = "";
    private String psaAmount="";
    private String[] psaQuestion;
    private String[] psaAnswer;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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

    public String getPsaAmount(){return psaAmount;}

    public void setPsaAmount(String psaAmount){this.psaAmount=psaAmount;}

    public String[] getPsaQuestion() {
        return psaQuestion;
    }

    public void setPsaQuestion(String[] psaQuestion) {
        this.psaQuestion = psaQuestion;
    }

    public String[] getPsaAnswer() {
        return psaAnswer;
    }

    public void setPsaAnswer(String[] psaAnswer) {
        this.psaAnswer = psaAnswer;
    }

    public class Data {
        String psaId="";

        public String getPsaId() {
            return psaId;
        }

        public void setPsaId(String psaId) {
            this.psaId = psaId;
        }
    }
}
