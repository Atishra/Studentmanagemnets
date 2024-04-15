package net.javaguides.sms.Model;

public class CommonResponse {

    private String msg;
    private String code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CommonResponse()
    {
        msg="Successfully process.";
        code="0000";
    }
}
