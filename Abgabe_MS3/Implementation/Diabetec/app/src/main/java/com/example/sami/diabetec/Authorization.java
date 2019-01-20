package com.example.sami.diabetec;

public class Authorization {

    private String id_token;
    private String access_token;
    private int expires_in;
    private String token_type;
    private String refresh_token;

    public String getId_token() {
        return id_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }







    public Authorization(String id_token, String access_token, int expires_in, String token_type, String refresh_token) {
        this.id_token = id_token;
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
    }
}
