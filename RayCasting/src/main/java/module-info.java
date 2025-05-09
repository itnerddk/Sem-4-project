import org.sdu.sem4.g7.common.aware.IMapAware;
import org.sdu.sem4.g7.common.aware.IWorldAware;
import org.sdu.sem4.g7.common.services.IRayCastingService;
import org.sdu.sem4.g7.rayCasting.RayCasting;

module RayCasting {
    requires Common;

    provides IRayCastingService with RayCasting;
    provides IMapAware with RayCasting;
    provides IWorldAware with RayCasting;
}