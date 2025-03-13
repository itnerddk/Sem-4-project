import org.sdu.sem4.g7.common.services.IPostEntityProcessingService;

module Collision {
    requires Common;
    provides IPostEntityProcessingService with org.sdu.sem4.g7.collision.CollisionDetector;
}