module Level {
    requires Common;

    provides org.sdu.sem4.g7.common.services.ILevelService
            with org.sdu.sem4.g7.level.LevelManager;

    exports org.sdu.sem4.g7.level;
}
