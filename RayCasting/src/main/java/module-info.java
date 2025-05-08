import org.sdu.sem4.g7.common.services.IRayCastingService;
import org.sdu.sem4.g7.rayCasting.RayCasting;

module RayCasting {
    requires Common;

    provides IRayCastingService with RayCasting;
}