package cn.sowell.datacenter.model.basepeople.dto;

public class FieldDataDto {

    /**
     *                 json.put("id", "2");
     json.put("title", "身份证号");
     json.put("title_en", "idcode");
     json.put("type", "1");
     */
    private  String  id;
    private  String title;
    private  String title_en;
    private  String type;

    public FieldDataDto() {
    }

    public FieldDataDto(String id, String title, String title_en, String type) {
        this.id = id;
        this.title = title;
        this.title_en = title_en;
        this.type = type;
    }

    @Override
    public String toString() {
        return "FieldDataDto{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", title_en='" + title_en + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
