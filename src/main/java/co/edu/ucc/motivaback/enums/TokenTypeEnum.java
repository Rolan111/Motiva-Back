package co.edu.ucc.motivaback.enums;

public enum TokenTypeEnum {
    BEARER("BEARER", "Bearer");

    private final String key;
    private String value;

    TokenTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
