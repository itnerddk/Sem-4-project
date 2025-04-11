package org.sdu.sem4.g7.common.services;

import org.sdu.sem4.g7.common.data.Entity;

public interface IInventoryService {
    void add(Entity entity);
    void remove(Entity entity);
}
