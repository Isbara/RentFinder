package GradProject.RentFinder.Exception;

public class Response {

    private String code;
    private String msg;

    public Response(String name, String description) {
        code=name;
        msg=description;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}



