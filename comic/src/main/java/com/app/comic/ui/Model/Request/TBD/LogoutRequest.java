package com.app.comic.ui.Model.Request.TBD;

/**
 * Created by Dell on 11/4/2015.
 */
public class LogoutRequest {

    /*Local Data Send To Server*/
    String UserName;
    String TicketId;

    /*Initiate Class*/
    public LogoutRequest() {
    }

    public LogoutRequest(LogoutRequest data) {
        UserName = data.getUsername();
        TicketId = data.getTicketId();
    }

    public String getTicketId() {
        return TicketId;
    }

    public void setTicketId(String ticketId) {
        TicketId = ticketId;
    }

    public String getUsername() {

        return UserName;
    }

    public void setUsername(String username) {

        this.UserName = username;
    }

}
