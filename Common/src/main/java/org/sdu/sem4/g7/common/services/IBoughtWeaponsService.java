package org.sdu.sem4.g7.common.services;

import java.util.List;

public interface IBoughtWeaponsService {
    boolean isWeaponBought(String weaponId);
    List<String> getBoughtWeaponIds();
}

