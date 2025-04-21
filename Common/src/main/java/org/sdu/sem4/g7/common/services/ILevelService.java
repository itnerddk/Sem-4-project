package org.sdu.sem4.g7.common.services;

public interface ILevelService {
    int getLevel();
    void levelUp();
    void setLevel(int level);

    int getNextLevelXP();
    int getCurrXP();
    void addXP(int xp);
    
    // Method for persistent storage
    void setXP(int xp);
}