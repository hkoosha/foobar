package io.koosha.foobar.order_request;

import io.koosha.foobar.ProtoSerde;


public final class OrderRequestSellerProtoSerde {

    private OrderRequestSellerProtoSerde() {

        throw new UnsupportedOperationException("instantiation not supported");
    }


    public static final class Deser extends ProtoSerde.Deser<OrderRequestSellerProto.OrderRequestSeller> {

    }


    public static final class Ser extends ProtoSerde.Ser<OrderRequestSellerProto.OrderRequestSeller> {

    }

}
