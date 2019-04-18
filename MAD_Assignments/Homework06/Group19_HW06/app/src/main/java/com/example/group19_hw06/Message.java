package com.example.group19_hw06;

import java.util.Date;

public class Message {
    String msgText, imgUrl, firstName, lastName;
    Date dt;

    @Override
    public String toString() {
        return "Message{" +
                "msgText='" + msgText + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dt=" + dt +
                '}';
    }
}
