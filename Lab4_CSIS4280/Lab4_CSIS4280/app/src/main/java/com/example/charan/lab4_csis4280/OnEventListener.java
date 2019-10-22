package com.example.charan.lab4_csis4280;

public interface OnEventListener<T> {

    public void onSuccess(T result);
    public void onSuccessCountry(T result);
    public void onSuccessCountryLanguage(T result);
    public void onFailure(Exception e);
}
