package com.mycompany.knowledge.miami.publish.engine.gongan;

public enum Role {

    BiluEntityXianyiren("嫌疑人"),
    BiluEntityZhengren("证人"),
    BiluEntityBaoanren("报案人"),
    BiluEntityDangshiren("当事人"),
    BiluEntityShouhairen("受害人");

    private final String type;
    Role(String type) {
        if(type != null)
            this.type = type.toLowerCase();
        else this.type = "valid";
    }

    public String toString() {
        return this.type;
    }

    public static Role fromString(String type) {
        try {
            return valueOf(type);
        } catch (Exception var6) {
            return null;
        }
    }
}
