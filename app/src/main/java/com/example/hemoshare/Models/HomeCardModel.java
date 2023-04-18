package com.example.hemoshare.Models;

public class HomeCardModel {
    String cardImgUri;
    String message;

    public HomeCardModel() {

    }

    public HomeCardModel(String cardImgUri, String message) {
        this.cardImgUri = cardImgUri;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCardImgUri() {
        return cardImgUri;
    }

    public void setCardImgUri(String cardImgUri) {
        this.cardImgUri = cardImgUri;
    }
}
