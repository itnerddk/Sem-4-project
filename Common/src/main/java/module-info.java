module Common {
    requires javafx.base;
    requires transitive javafx.graphics;
    requires com.fasterxml.jackson.annotation;
    exports org.sdu.sem4.g7.common.services;
    exports org.sdu.sem4.g7.common.data;
    exports org.sdu.sem4.g7.common.enums;

    uses org.sdu.sem4.g7.common.services.ICurrencyService;
    uses org.sdu.sem4.g7.common.services.ILevelService;

}