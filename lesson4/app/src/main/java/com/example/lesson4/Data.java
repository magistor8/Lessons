package com.example.lesson4;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {

    private String subtotalResult;
    private String totalResult;

    private String tempString;
    //Последняя операция: 1 - какое-то число, 2 - какое-то действие
    private int lastOperation;
    //Кол-во аргументов
    private int argCount;

    protected Data(Parcel in) {
        tempString = in.readString();
        lastOperation = in.readInt();
        argCount = in.readInt();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public Data() {

    }

    public String getTempString() {
        return tempString;
    }

    public int getLastOperation() {
        return lastOperation;
    }

    public int getArgCount() {
        return argCount;
    }

    public String getSubtotalResult() {
        return subtotalResult;
    }

    public String getTotalResult() {
        return totalResult;
    }

    public void setTempString(String tempString) {
        this.tempString = tempString;
    }

    public void setLastOperation(int lastOperation) {
        this.lastOperation = lastOperation;
    }

    public void setArgCount(int argCount) {
        this.argCount = argCount;
    }

    public void setSubtotalResult(String subtotalResult) {
        this.subtotalResult = subtotalResult;
    }

    public void setTotalResult(String totalResult) {
        this.totalResult = totalResult;
    }

    protected void argsDec() {
        this.argCount--;
    }
    protected void argsInc() {
        this.argCount++;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tempString);
        dest.writeInt(lastOperation);
        dest.writeInt(argCount);
    }


}
