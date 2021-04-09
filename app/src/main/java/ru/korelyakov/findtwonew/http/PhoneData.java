package ru.korelyakov.findtwonew.http;

import java.io.Serializable;

import ru.korelyakov.findtwonew.ApplicationClass;
import ru.korelyakov.findtwonew.Game;

public class PhoneData implements Serializable {

    private String android_id;
    private String phone_model;
    private String phone_brand;
    private String locale;
    private boolean charging;
    private double battery_level;
    private boolean vpn;
    private String media_source;
    public String appsflyer_id;
    private String campaign_id;
    private String af_ad_id;
    private String advertising_id;

    public String getAndroid_id() {
        return android_id;
    }

    public void setAndroid_id(String android_id) {
        this.android_id = android_id;
    }

    public String getPhone_model() {
        return phone_model;
    }

    public void setPhone_model(String phone_model) {
        this.phone_model = phone_model;
    }

    public String getPhone_brand() {
        return phone_brand;
    }

    public void setPhone_brand(String phone_brand) {
        this.phone_brand = phone_brand;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public boolean isCharging() {
        return charging;
    }

    public void setCharging(boolean charging) {
        this.charging = charging;
    }

    public double getBattery_level() {
        return battery_level;
    }

    public void setBattery_level(double battery_level) {
        this.battery_level = battery_level;
    }

    public boolean isVpn() {
        return vpn;
    }

    public void setVpn(boolean vpn) {
        this.vpn = vpn;
    }

    public String getMedia_source() {
        return media_source;
    }

    public void setMedia_source(String media_source) {
        this.media_source = media_source;
    }

    public String getAppsflyer_id() {
        return appsflyer_id;
    }

    public void setAppsflyer_id(String appsflyer_id) {
        this.appsflyer_id = appsflyer_id;
    }

    public String getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(String campaign_id) {
        this.campaign_id = campaign_id;
    }

    public String getAf_ad_id() {
        return af_ad_id;
    }

    public void setAf_ad_id(String af_ad_id) {
        this.af_ad_id = af_ad_id;
    }

    public String getAdvertising_id() {
        return advertising_id;
    }

    public void setAdvertising_id(String advertising_id) {
        this.advertising_id = advertising_id;
    }

    public PhoneData() {
    }

    public PhoneData(String android_id,
                     String phone_model,
                     String phone_brand,
                     String locale,
                     boolean charging,
                     double battery_level,
                     boolean vpn,
                     String media_source,
                     String appsflyer_id,
                     String campaign_id,
                     String af_ad_id,
                     String advertising_id) {
        this.android_id = android_id;
        this.phone_model = phone_model;
        this.phone_brand = phone_brand;
        this.locale = locale;
        this.charging = charging;
        this.battery_level = battery_level;
        this.vpn = vpn;
        this.media_source = media_source;
        this.appsflyer_id = appsflyer_id;
        this.campaign_id = campaign_id;
        this.af_ad_id = af_ad_id;
        this.advertising_id = advertising_id;
    }
}
