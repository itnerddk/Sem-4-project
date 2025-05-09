package org.sdu.sem4.g7.shop;

import org.sdu.sem4.g7.common.services.IBoughtWeaponsService;
import org.sdu.sem4.g7.common.services.ServiceLocator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoughtWeaponsServiceImpl implements IBoughtWeaponsService {

    private static final String SAVE_KEY = "ownedWeapons";

    @Override
    public boolean isWeaponBought(String weaponId) {
        return getBoughtWeaponIds().contains(weaponId);
    }

    @Override
    public List<String> getBoughtWeaponIds() {
        return ServiceLocator.getPersistenceService()
                .map(p -> p.getStringList(SAVE_KEY))
                .orElse(Collections.emptyList());
    }


    public void buyWeapon(String weaponId) {
        ServiceLocator.getPersistenceService().ifPresent(p -> {
            List<String> owned = p.getStringList(SAVE_KEY);


            if (owned == null) {
                owned = new ArrayList<>();
            } else {
                owned = new ArrayList<>(owned);
            }

            if (!owned.contains(weaponId)) {
                owned.add(weaponId);
                p.saveStringList(SAVE_KEY, owned);
            }
        });
    }


}
