package kr.co.aura.mtelo.healthcare.addinfo.exercise;

import java.io.Serializable;

/**
 * Created by harks on 2016-03-27.
 */
public class ExerciseTab implements Serializable {

    String userId = "";

    String valueType = "";
    String userValue = "";

    String averageType = "";
    String averageValue = "";
    String averageCnt = "";
    String bodyType = "";

    String bodyType1 = "";
    String bodyType1Max = "";
    String bodyType2 = "";
    String bodyType2Max = "";
    String bodyType3 = "";
    String bodyType3Max = "";
    String bodyType4 = "";
    String bodyType4Max = "";
    String bodyType5 = "";
    String bodyType5Max = "";
    String bodyType6 = "";
    String bodyType6Max = "";


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getUserValue() {
        return userValue;
    }

    public void setUserValue(String userValue) {
        this.userValue = userValue;
    }

    public String getAverageType() {
        return averageType;
    }

    public void setAverageType(String averageType) {
        this.averageType = averageType;
    }

    public String getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(String averageValue) {
        this.averageValue = averageValue;
    }

    public String getAverageCnt() {
        return averageCnt;
    }

    public void setAverageCnt(String averageCnt) {
        this.averageCnt = averageCnt;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getBodyType1() {
        return bodyType1;
    }

    public void setBodyType1(String bodyType1) {
        this.bodyType1 = bodyType1;
    }

    public String getBodyType1Max() {
        return bodyType1Max;
    }

    public void setBodyType1Max(String bodyType1Max) {
        this.bodyType1Max = bodyType1Max;
    }

    public String getBodyType2() {
        return bodyType2;
    }

    public void setBodyType2(String bodyType2) {
        this.bodyType2 = bodyType2;
    }

    public String getBodyType2Max() {
        return bodyType2Max;
    }

    public void setBodyType2Max(String bodyType2Max) {
        this.bodyType2Max = bodyType2Max;
    }

    public String getBodyType3() {
        return bodyType3;
    }

    public void setBodyType3(String bodyType3) {
        this.bodyType3 = bodyType3;
    }

    public String getBodyType3Max() {
        return bodyType3Max;
    }

    public void setBodyType3Max(String bodyType3Max) {
        this.bodyType3Max = bodyType3Max;
    }

    public String getBodyType4() {
        return bodyType4;
    }

    public void setBodyType4(String bodyType4) {
        this.bodyType4 = bodyType4;
    }

    public String getBodyType4Max() {
        return bodyType4Max;
    }

    public void setBodyType4Max(String bodyType4Max) {
        this.bodyType4Max = bodyType4Max;
    }

    public String getBodyType5() {
        return bodyType5;
    }

    public void setBodyType5(String bodyType5) {
        this.bodyType5 = bodyType5;
    }

    public String getBodyType5Max() {
        return bodyType5Max;
    }

    public void setBodyType5Max(String bodyType5Max) {
        this.bodyType5Max = bodyType5Max;
    }

    public String getBodyType6() {
        return bodyType6;
    }

    public void setBodyType6(String bodyType6) {
        this.bodyType6 = bodyType6;
    }

    public String getBodyType6Max() {
        return bodyType6Max;
    }

    public void setBodyType6Max(String bodyType6Max) {
        this.bodyType6Max = bodyType6Max;
    }


    @Override
    public String toString() {
        return "ExerciseTab{" +
                "userId='" + userId + '\'' +
                ", valueType='" + valueType + '\'' +
                ", userValue='" + userValue + '\'' +
                ", averageType='" + averageType + '\'' +
                ", averageValue='" + averageValue + '\'' +
                ", averageCnt='" + averageCnt + '\'' +
                ", bodyType='" + bodyType + '\'' +
                ", bodyType1='" + bodyType1 + '\'' +
                ", bodyType1Max='" + bodyType1Max + '\'' +
                ", bodyType2='" + bodyType2 + '\'' +
                ", bodyType2Max='" + bodyType2Max + '\'' +
                ", bodyType3='" + bodyType3 + '\'' +
                ", bodyType3Max='" + bodyType3Max + '\'' +
                ", bodyType4='" + bodyType4 + '\'' +
                ", bodyType4Max='" + bodyType4Max + '\'' +
                ", bodyType5='" + bodyType5 + '\'' +
                ", bodyType5Max='" + bodyType5Max + '\'' +
                ", bodyType6='" + bodyType6 + '\'' +
                ", bodyType6Max='" + bodyType6Max + '\'' +
                '}';
    }
}
