package com.example.deerdiary;

public interface Person {
    void setFirstName(String firstName);
    void setLastName(String lastName);
    void setEmail(String email);
    void setImageURL(String imageURL);
    String getFirstName();
    String getLastName();
    String getEmail();
    String getImageURL();
}
