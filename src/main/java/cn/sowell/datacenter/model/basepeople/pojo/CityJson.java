package cn.sowell.datacenter.model.basepeople.pojo;

import java.util.List;

public class CityJson {
    int provinceNum;
    int cityNum;
    int areaNum;
    int streetNum;
    List<CityEntiy> list;

    public CityJson(int provinceNum, int cityNum, int areaNum,int streetNum, List<CityEntiy> list) {
        this.provinceNum = provinceNum;
        this.cityNum = cityNum;
        this.areaNum = areaNum;
        this.streetNum = streetNum;
        this.list = list;
    }

    public CityJson() {

    }


    public int getProvinceNum() {
        return provinceNum;
    }

    public void setProvinceNum(int provinceNum) {
        this.provinceNum = provinceNum;
    }

    public int getCityNum() {
        return cityNum;
    }

    public void setCityNum(int cityNum) {
        this.cityNum = cityNum;
    }

    public int getAreaNum() {
        return areaNum;
    }

    public void setAreaNum(int areaNum) {
        this.areaNum = areaNum;
    }

    public List<CityEntiy> getList() {
        return list;
    }

    public void setList(List<CityEntiy> list) {
        this.list = list;
    }

    public int getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(int streetNum) {
        this.streetNum = streetNum;
    }
}
