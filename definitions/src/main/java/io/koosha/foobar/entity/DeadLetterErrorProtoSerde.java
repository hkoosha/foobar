package io.koosha.foobar.entity;

import io.koosha.foobar.ProtoSerde;


public final class DeadLetterErrorProtoSerde {

    private DeadLetterErrorProtoSerde() {

        throw new UnsupportedOperationException("instantiation not supported");
    }


    public static final class Deser extends ProtoSerde.Deser<DeadLetterErrorProto.DeadLetterError> {

    }

    public static final class Ser extends ProtoSerde.Ser<DeadLetterErrorProto.DeadLetterError> {

    }

}
