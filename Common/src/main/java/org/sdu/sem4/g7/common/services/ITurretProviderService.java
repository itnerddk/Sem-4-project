package org.sdu.sem4.g7.common.services;

import java.util.List;
import java.util.ServiceLoader.Provider;

import org.sdu.sem4.g7.common.data.Entity;

public interface ITurretProviderService {
    public List<Provider<? extends Entity>> getTurrets();
}
