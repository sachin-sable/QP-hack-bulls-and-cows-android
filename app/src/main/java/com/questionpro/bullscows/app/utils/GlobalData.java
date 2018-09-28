package com.questionpro.bullscows.app.utils;

public class GlobalData {
    private static GlobalData globalData = null;

    public String getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    private String currentWord = "";
    private GlobalData(){

    }

    public static GlobalData getInstance(){
        if(globalData == null){
            globalData = new GlobalData();
        }
        return globalData;
    }
}
