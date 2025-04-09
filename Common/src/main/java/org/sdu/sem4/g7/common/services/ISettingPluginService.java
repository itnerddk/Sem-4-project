package org.sdu.sem4.g7.common.services;

import java.util.List;

import org.sdu.sem4.g7.common.data.SettingGroup;

public interface ISettingPluginService {
    void addSettings(List<SettingGroup> setting);
}
