package io.koosha.foobar.entity;

import io.koosha.foobar.ProtoSerde;


public final class StateProtoSerde {

    private StateProtoSerde() {

        throw new RuntimeException("instantiation not supported");
    }


    public static final class Deser extends ProtoSerde.Deser<StateProto.State> {

    }

    public static final class Ser extends ProtoSerde.Ser<StateProto.State> {

    }

}
