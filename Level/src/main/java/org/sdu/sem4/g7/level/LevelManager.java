package org.sdu.sem4.g7.level;

import org.sdu.sem4.g7.common.services.ILevelService;
import org.sdu.sem4.g7.common.services.ServiceLocator;

public class LevelManager implements ILevelService {
    // Key used to save level
    private static final String persistenceLevelKey = "Level";
    // Key used to save XP
    private static final String persistenceXPKey = "xp";
    
    // Default level and XP if no persistence service is available
    private int level = 1;
    private int currXP = 0;

    @Override
    public int getLevel() {
        ServiceLocator.getPersistenceService().ifPresent(
            persistenceService -> {
                // load level from persistence
                if (persistenceService.intExists(persistenceLevelKey)) {
                    int loadedLevel = persistenceService.getInt(persistenceLevelKey);
                    this.level = loadedLevel;
                } else {
                    this.level = 1;
                }
            }
        );
        return level;
    }
    
    @Override
    public void levelUp() {
        level++;
        // Save level to persistence
        ServiceLocator.getPersistenceService().ifPresent(
            persistenceService -> {
                persistenceService.setInt("Level", level);
            }
        );
    }
    
    @Override
    public void setLevel(int level) {
        if (level > 0) this.level = level;
        
    }

    @Override
    public int getCurrXP() {
        ServiceLocator.getPersistenceService().ifPresent(
            persistenceService -> {
                // load XP from persistence
                int loadedXP = persistenceService.getInt(persistenceXPKey);
                this.currXP = loadedXP;
            }
        );
        return currXP;
    }
    @Override
    public void addXP(int xp) {
        currXP += xp;
        while(currXP >= getNextLevelXP()) {
            currXP -= getNextLevelXP();
            levelUp();
        }
        // Save XP to persistence
        ServiceLocator.getPersistenceService().ifPresent(
            persistenceService -> {
                persistenceService.setInt("xp", currXP);
            }
        );

    }

    public int getNextLevelXP() {
        return 100 + (level - 1) * 50; 
    }
    
    @Override
    public void setXP(int xp) {
        currXP = xp;
        
    }
}
