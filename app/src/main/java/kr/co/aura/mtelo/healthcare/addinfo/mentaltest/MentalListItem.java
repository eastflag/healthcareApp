package kr.co.aura.mtelo.healthcare.addinfo.mentaltest;

import java.io.Serializable;

/**
 * Created by harksoo on 2016-03-21.
 */
public class MentalListItem implements Serializable {
    String introType =null;
    String introImg = null;
    String introVideo = null;
    String outroType = null;
    String outroImg = null;
    String outroVideo = null;
    String simliId = null;
    String title = null;
    boolean useYN = false;

    public MentalListItem(){

    }

    public MentalListItem(String introType,
                          String introImg,
                          String introVideo,
                          String outroType,
                          String outroImg,
                          String outroVideo,
                          String simliId,
                          String title,
                          boolean useYN) {
        this.introType = introType;
        this.introImg = introImg;
        this.introVideo = introVideo;
        this.outroType = outroType;
        this.outroImg = outroImg;
        this.outroVideo = outroVideo;
        this.simliId = simliId;
        this.title = title;
        this.useYN = useYN;
    }

    public String getIntroType() {
        return introType;
    }

    public void setIntroType(String introType) {
        this.introType = introType;
    }

    public String getIntroImg() {
        return introImg;
    }

    public void setIntroImg(String introImg) {
        this.introImg = introImg;
    }

    public String getIntroVideo() {
        return introVideo;
    }

    public void setIntroVideo(String introVideo) {
        this.introVideo = introVideo;
    }

    public String getOutroType() {
        return outroType;
    }

    public void setOutroType(String outroType) {
        this.outroType = outroType;
    }

    public String getOutroImg() {
        return outroImg;
    }

    public void setOutroImg(String outroImg) {
        this.outroImg = outroImg;
    }

    public String getOutroVideo() {
        return outroVideo;
    }

    public void setOutroVideo(String outroVideo) {
        this.outroVideo = outroVideo;
    }

    public String getSimliId() {
        return simliId;
    }

    public void setSimliId(String simliId) {
        this.simliId = simliId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isUseYN() {
        return useYN;
    }

    public void setUseYN(boolean useYN) {
        this.useYN = useYN;
    }

    @Override
    public String toString() {
        return "MentalListItem{" +
                "introType='" + introType + '\'' +
                ", introImg='" + introImg + '\'' +
                ", introVideo='" + introVideo + '\'' +
                ", outroType='" + outroType + '\'' +
                ", outroImg='" + outroImg + '\'' +
                ", outroVideo='" + outroVideo + '\'' +
                ", simliId='" + simliId + '\'' +
                ", title='" + title + '\'' +
                ", useYN=" + useYN +
                '}';
    }
}
