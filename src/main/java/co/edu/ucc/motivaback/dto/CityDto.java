package co.edu.ucc.motivaback.dto;

/**
 * @author nagredo
 * @project motiva-back
 * @class CityDto
 */

public class CityDto {
    private String documentCityId;
    private String name;
    private String count;

    public String getDocumentCityId() {
        return documentCityId;
    }

    public void setDocumentCityId(String documentCityId) {
        this.documentCityId = documentCityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
