module Inventory {
    requires TankParts;
    requires Player;
    requires transitive Common;
    
    exports org.sdu.sem4.g7.inventory;
    provides org.sdu.sem4.g7.common.services.IGamePluginService with org.sdu.sem4.g7.inventory.Inventory;
    provides org.sdu.sem4.g7.common.services.IEntityProcessingService with org.sdu.sem4.g7.inventory.Inventory;
    provides org.sdu.sem4.g7.common.services.IInventoryService with org.sdu.sem4.g7.inventory.Inventory;
}
