package no.nav.pam.ad.migration.entity;

import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.internal.util.compare.ComparableComparator;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.LiteralType;
import org.hibernate.type.VersionType;
import org.hibernate.type.descriptor.java.LocalDateTimeJavaDescriptor;
import org.hibernate.type.descriptor.sql.TimestampTypeDescriptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Locale;

/**
 * Custom Hibernate {@link VersionType} based on {@link org.hibernate.type.LocalDateTimeType}
 *
 * <p>Should be used for <code>LocalDateTime</code> timestamp fields used with {@link javax.persistence.Version @Version} JPA annotation.
 *
 * <p>If the local JVM timestamp resolution is higher than what database can store, problems with optimistic locking
 * on timestamp fields will occur. (Java &gt;= 9 can produce timestamps with higher than microsecond precision on some platforms.)
 * This Hibernate type should remedy that by always truncating local timestamp precision to microseconds, which
 * is what the typical database can store exactly.
 */
public class MicrosecondLocalDateTimeVersionType extends AbstractSingleColumnStandardBasicType<LocalDateTime>
        implements VersionType<LocalDateTime>, LiteralType<LocalDateTime> {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss.S", Locale.ENGLISH );;

    public MicrosecondLocalDateTimeVersionType() {
        super(TimestampTypeDescriptor.INSTANCE, LocalDateTimeJavaDescriptor.INSTANCE);
    }

    public String getName() {
        return LocalDateTime.class.getSimpleName();
    }

    protected boolean registerUnderJavaType() {
        return true;
    }

    public String objectToSQLString(LocalDateTime value, Dialect dialect) throws Exception {
        return "{ts '" + FORMATTER.format(value) + "'}";
    }

    public LocalDateTime seed(SharedSessionContractImplementor session) {
        return LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }

    public LocalDateTime next(LocalDateTime current, SharedSessionContractImplementor session) {
        return LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }

    public Comparator<LocalDateTime> getComparator() {
        return ComparableComparator.INSTANCE;
    }
}
