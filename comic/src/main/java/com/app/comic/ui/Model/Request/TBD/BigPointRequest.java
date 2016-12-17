package com.app.comic.ui.Model.Request.TBD;

/**
 * Created by Dell on 11/4/2015.
 */
public class BigPointRequest {


    /*Local Data Send To Server*/
    String CustomerNumber;
    String Hash;

    /*Initiate Class*/
    public BigPointRequest() {
    }

    public BigPointRequest(BigPointRequest data) {
        CustomerNumber = data.getCustomerNumber();
        Hash = data.getHash();
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        CustomerNumber = customerNumber;
    }

    public String getHash() {
        return Hash;
    }

    public void setHash(String hash) {
        Hash = hash;
    }

}
