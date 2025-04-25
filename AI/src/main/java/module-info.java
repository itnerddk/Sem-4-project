module AI {
    requires Common;
    requires javafx.graphics;
    provides org.sdu.sem4.g7.common.services.ILogicService with org.sdu.sem4.g7.ai.LogicService;
}
