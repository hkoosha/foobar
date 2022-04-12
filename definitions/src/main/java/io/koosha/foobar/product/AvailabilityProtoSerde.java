package io.koosha.foobar.product;

import io.koosha.foobar.ProtoSerde;

public final class AvailabilityProtoSerde {

    private AvailabilityProtoSerde() {

        throw new UnsupportedOperationException("instantiation not supported");
    }


    public static final class Deser extends ProtoSerde.Deser<AvailabilityProto.Availability> {

    }


    public static final class Ser extends ProtoSerde.Ser<AvailabilityProto.Availability> {

    }

}
