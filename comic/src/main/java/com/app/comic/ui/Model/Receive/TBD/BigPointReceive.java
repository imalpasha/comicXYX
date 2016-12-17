package com.app.comic.ui.Model.Receive.TBD;

/*
 * Created by ImalPasha on 11/6/2015.
 */

 /* Response From API */

public class BigPointReceive {


    private String Status;
    private String Message;
    private String PtsBal;

    public BigPointReceive(BigPointReceive returnData) {
        Message = returnData.getMessage();
        Status = returnData.getStatus();
        PtsBal = returnData.getPtsBal();
    }

    public String getPtsBal() {
        return PtsBal;
    }

    public void setPtsBal(String ptsBal) {
        PtsBal = ptsBal;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

}
