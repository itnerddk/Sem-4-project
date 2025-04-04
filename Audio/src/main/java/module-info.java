import org.sdu.sem4.g7.audio.AudioProcessing;
import org.sdu.sem4.g7.common.services.IAudioProcessingService;
import org.sdu.sem4.g7.common.services.IGamePluginService;

module Audio {
    requires javafx.media;
    requires Common;
    provides IAudioProcessingService with AudioProcessing;
    provides IGamePluginService with AudioProcessing;
}    