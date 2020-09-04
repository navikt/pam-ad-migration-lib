package no.nav.pam.ad.migration.mapper;

/**
 * Parent class of exceptions thrown during DTO/entity mapping.
 */
public class MappingException extends Exception {

    public MappingException(String message) {
        super(message);
    }

    public MappingException(Runtime cause) {
        super(cause);
    }

    /**
     * {@link RuntimeException} variant of {@code MappingException}, primarily to avoid too much hassle inside streams
     * while keeping a checked exception to enforce handling.
     */
    public static class Runtime extends RuntimeException {

        public Runtime(String message) {
            super(message);
        }

    }

}
