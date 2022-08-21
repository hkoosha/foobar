package io.koosha.foobar.order_request;

import io.koosha.foobar.ProtoSerde;


public final class OrderRequestStateChangedProtoSerde {

    private OrderRequestStateChangedProtoSerde() {

        throw new UnsupportedOperationException("instantiation not supported");
    }


    public static final class Deser extends ProtoSerde.Deser<OrderRequestStateChangedProto.OrderRequestStateChanged> {

    }


    public static final class Ser extends ProtoSerde.Ser<OrderRequestStateChangedProto.OrderRequestStateChanged> {

    }

}
