package kr.co.aura.mtelo.healthcare.addinfo.exercise;

import java.io.Serializable;

/**
 * Created by harksoo on 2016-03-23.
 */
public class ExerciseMain implements Serializable {
    String userId = "";
    String bodyType = "";
    String calorie = "";
    String calorieAverage = "";
    String calorieMax = "";
    String distance = "";
    String exerciseDate = "";
    String exerciseId = "";
    String exerciseIdNext = "";
    String exerciseIdPrev = "";
    String exerciseImg = "";
    String exerciseName = "";
    String rangkingClass = "";
    String rangkingGrade = "";
    String step = "";

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getCalorieAverage() {
        return calorieAverage;
    }

    public void setCalorieAverage(String calorieAverage) {
        this.calorieAverage = calorieAverage;
    }

    public String getCalorieMax() {
        return calorieMax;
    }

    public void setCalorieMax(String calorieMax) {
        this.calorieMax = calorieMax;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getExerciseDate() {
        return exerciseDate;
    }

    public void setExerciseDate(String exerciseDate) {
        this.exerciseDate = exerciseDate;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseIdNext() {
        return exerciseIdNext;
    }

    public void setExerciseIdNext(String exerciseIdNext) {
        this.exerciseIdNext = exerciseIdNext;
    }

    public String getExerciseIdPrev() {
        return exerciseIdPrev;
    }

    public void setExerciseIdPrev(String exerciseIdPrev) {
        this.exerciseIdPrev = exerciseIdPrev;
    }

    public String getExerciseImg() {
        return exerciseImg;
    }

    public void setExerciseImg(String exerciseImg) {
        this.exerciseImg = exerciseImg;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getRangkingClass() {
        return rangkingClass;
    }

    public void setRangkingClass(String rangkingClass) {
        this.rangkingClass = rangkingClass;
    }

    public String getRangkingGrade() {
        return rangkingGrade;
    }

    public void setRangkingGrade(String rangkingGrade) {
        this.rangkingGrade = rangkingGrade;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "ExerciseMain{" +
                "userId='" + userId + '\'' +
                ", bodyType='" + bodyType + '\'' +
                ", calorie='" + calorie + '\'' +
                ", calorieAverage='" + calorieAverage + '\'' +
                ", calorieMax='" + calorieMax + '\'' +
                ", distance='" + distance + '\'' +
                ", exerciseDate='" + exerciseDate + '\'' +
                ", exerciseId='" + exerciseId + '\'' +
                ", exerciseIdNext='" + exerciseIdNext + '\'' +
                ", exerciseIdPrev='" + exerciseIdPrev + '\'' +
                ", exerciseImg='" + exerciseImg + '\'' +
                ", exerciseName='" + exerciseName + '\'' +
                ", rangkingClass='" + rangkingClass + '\'' +
                ", rangkingGrade='" + rangkingGrade + '\'' +
                ", step='" + step + '\'' +
                '}';
    }
}
