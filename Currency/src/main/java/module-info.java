module Currency {
    requires transitive Common;

    uses org.sdu.sem4.g7.common.services.IPersistenceService;

    provides org.sdu.sem4.g7.common.services.ICurrencyService
            with org.sdu.sem4.g7.currency.CurrencyManager;

    exports org.sdu.sem4.g7.currency;
}
