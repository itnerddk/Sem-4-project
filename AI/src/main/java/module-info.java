import org.sdu.sem4.g7.ai.LogicService;
import org.sdu.sem4.g7.common.aware.IGameDataAware;
import org.sdu.sem4.g7.common.aware.IMapAware;
import org.sdu.sem4.g7.common.aware.IWorldAware;
import org.sdu.sem4.g7.common.services.ILogicService;

module AI {
    requires Common;
    requires javafx.graphics;
    provides ILogicService with LogicService;
    provides IMapAware with LogicService;
    provides IGameDataAware with LogicService;
    provides IWorldAware with LogicService;
}
