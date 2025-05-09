module CommonTest {
    requires Common;
    requires org.junit.jupiter.api; // Ignore this is "missing", it's not being very smart. If removed it throws errors as it can't find it.
    exports org.sdu.sem4.g7.common.tests to org.junit.platform.commons;

    opens org.sdu.sem4.g7.common.tests to org.junit.platform.commons;
}
