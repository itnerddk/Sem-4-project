import org.sdu.sem4.g7.common.services.IPostEntityProcessingService;

module Collision {
    requires transitive Common;
    requires transitive javafx.graphics;
    provides IPostEntityProcessingService with org.sdu.sem4.g7.collision.CollisionDetector;
}