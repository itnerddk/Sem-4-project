module Upgrades {
    exports org.sdu.sem4.g7.upgrades;
    requires Common;

    provides org.sdu.sem4.g7.common.services.IUpgradeService
            with org.sdu.sem4.g7.upgrades.UpgradeProvider;
    provides org.sdu.sem4.g7.common.services.IUpgradeStatsService
            with org.sdu.sem4.g7.upgrades.UpgradeProvider;
}
