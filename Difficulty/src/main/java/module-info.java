module Difficulty {
    requires transitive Common;

    uses org.sdu.sem4.g7.common.services.IPersistenceService;

    provides org.sdu.sem4.g7.common.services.IDifficultyService
        with org.sdu.sem4.g7.difficulty.services.DifficultyService;

    exports org.sdu.sem4.g7.difficulty.services;
}
