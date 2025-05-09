module Shop {
    requires Common;

    provides org.sdu.sem4.g7.common.services.IBoughtWeaponsService
            with org.sdu.sem4.g7.shop.BoughtWeaponsServiceImpl;

}