package in.forpay.model.request;

public class LanguageList {


        public String code = "";
        public String language = "";
        public String languageLocal="";

    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }



    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

        public LanguageList() {
        }

    public String getLanguageLocal() {
        return languageLocal;
    }

    public void setLanguageLocal(String languageLocal) {
        this.languageLocal = languageLocal;
    }

    public LanguageList(String code, String language) {

            this.code = code;
            this.language = language;

        }




}

