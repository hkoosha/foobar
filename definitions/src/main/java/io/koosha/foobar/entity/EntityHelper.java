package io.koosha.foobar.entity;

import io.koosha.foobar.HeaderHelper;


public final class EntityHelper {

    private EntityHelper() {

        throw new UnsupportedOperationException("instantiation not supported");
    }


    public static DeadLetterErrorProto.DeadLetterError deadLetterErrorOf(String source,
                                                                         long timestamp,
                                                                         String error) {

        return DeadLetterErrorProto.DeadLetterError.newBuilder()
                .setHeader(HeaderHelper.create(source, timestamp))
                .setError(error)
                .build();
    }

}
