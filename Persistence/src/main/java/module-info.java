import org.sdu.sem4.g7.common.services.IPersistenceService;

module Persistence {
    requires transitive Common;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    provides IPersistenceService with org.sdu.sem4.g7.persistence.services.PersistenceService;
}
