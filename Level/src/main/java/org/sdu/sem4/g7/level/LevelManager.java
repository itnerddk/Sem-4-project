package org.sdu.sem4.g7.level;

import org.sdu.sem4.g7.common.services.ILevelService;

public class LevelManager implements ILevelService {
    private int level = 1;

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void levelUp() {
        level++;
    }

    @Override
    public void setLevel(int level) {
        if (level > 0) this.level = level;
    }
}
