import org.sdu.sem4.g7.audio.*;
import org.sdu.sem4.g7.common.services.IAudioProcessingService;
import org.sdu.sem4.g7.common.services.IGamePluginService;
import org.sdu.sem4.g7.common.services.ISettingPluginService;

module Audio {
    requires javafx.media;
    requires Common;
    provides IAudioProcessingService with AudioProcessing;
    provides IGamePluginService with AudioProcessing;
    provides ISettingPluginService with AudioSettings;
}