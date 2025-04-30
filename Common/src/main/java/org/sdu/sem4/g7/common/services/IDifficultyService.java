package org.sdu.sem4.g7.common.services;

import org.sdu.sem4.g7.common.enums.DifficultyEnum;

public interface IDifficultyService {
    
    public DifficultyEnum getDifficulty();

    public void setDifficulty(DifficultyEnum difficulty);

    public float getMultiplier();

}
