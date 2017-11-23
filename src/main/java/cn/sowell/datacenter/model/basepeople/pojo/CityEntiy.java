package cn.sowell.datacenter.model.basepeople.pojo;

public class CityEntiy {
    /**
     * 省份 城市
     */

    String c;
    String n;


    public CityEntiy(String c, String n) {
        this.c = c;
        this.n = n;
    }

    public CityEntiy() {
    }



    public String getc() {
        return c;
    }

    public void setc(String c) {
        this.c = c;
    }

    public String getn() {
        return n;
    }

    public void setn(String n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return "CityEntiy{" +
                "c='" + c + '\'' +
                ", n='" + n + '\'' +
                '}';
    }
}
