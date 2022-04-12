package io.koosha.foobar;


public final class HeaderHelper {

    private HeaderHelper() {

        throw new UnsupportedOperationException("instantiation not supported");
    }


    public static HeaderProto.Header create(final String source,
                                            final long timestamp) {

        return HeaderProto.Header.newBuilder()
                .setSource(source)
                .setTimestamp(timestamp)
                .build();
    }

}
