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
    private DifficultyEnum localDifficulty = DifficultyEnum.NORMAL;


    @Override
    public DifficultyEnum getDifficulty() {
        ServiceLocator.getPersistenceService().ifPresent(
            persistenceService -> {
                // load difficulty level from persistence
                String difficultyString = persistenceService.getString(persistenceKey);

                // if difficulty is null, then set the difficulty to normal
                if (difficultyString == null) {
                    setDifficulty(DifficultyEnum.NORMAL);
                    difficultyString = "normal";
                }

                // set local difficulty
                localDifficulty = DifficultyEnum.valueOf(difficultyString);
            }
        );
        return localDifficulty;
    }

    @Override
    public void setDifficulty(DifficultyEnum difficulty) {
        ServiceLocator.getPersistenceService().ifPresent(
            persistenceService -> {
                persistenceService.setString(persistenceKey, difficulty.toString());
            }
        );
        localDifficulty = difficulty;
    }

    @Override
    public float getMultiplier() {
        switch (getDifficulty()) {
            case EASY:
                return 0.8f;

            case NORMAL:
                return 1f;
            
            case HARD:
                return 1.5f;
        }
        return 1f;
    }
    

}
