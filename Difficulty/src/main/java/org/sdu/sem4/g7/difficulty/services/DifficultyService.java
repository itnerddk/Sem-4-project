package org.sdu.sem4.g7.difficulty.services;

import org.sdu.sem4.g7.common.enums.DifficultyEnum;
import org.sdu.sem4.g7.common.services.IDifficultyService;
import org.sdu.sem4.g7.common.services.ServiceLocator;

public class DifficultyService implements IDifficultyService {

    /**
     * Key used to save difficulty
     */
    private static final String persistenceKey = "difficulty";

    /**
     * Local difficulty level, if the persistence service is not present
     */
    private DifficultyEnum localDifficulty;


    @Override
    public DifficultyEnum getDifficulty() {
        ServiceLocator.getPersistenceService().ifPresent(
            persistenceService -> {
                // load difficulty level from persistence
                DifficultyEnum difficulty = DifficultyEnum.valueOf(persistenceService.getString(persistenceKey));
                localDifficulty = difficulty;
            }
        );
        return localDifficulty;
    }

    @Override
    public void setDifficulty(DifficultyEnum difficulty) {
        ServiceLocator.getPersistenceService().ifPresent(
            persistenceService -> {
                persistenceService.setString(persistenceKey, difficulty.name());
            }
        );
        localDifficulty = difficulty;
    }

    @Override
    public int getMultiplier() {
        switch (getDifficulty()) {
            case EASY:
                return 10;

            case NORMAL:
                return 20;
            
            case HARD:
                return 30;
            
            default:
                return 20;
        }
    }
    

}
