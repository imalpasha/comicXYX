package com.app.comic.ui.Model.Receive.TBD;

/**
 * Created by Dell on 9/5/2016.
 */
public class LogoutReceive {

    private String Status;
    private String Message;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public LogoutReceive(LogoutReceive data) {
        Status = data.getStatus();
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

}
