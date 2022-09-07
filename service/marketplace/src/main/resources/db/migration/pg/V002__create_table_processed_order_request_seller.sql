
CREATE TABLE marketplace__processed_order_request_seller
(

    order_request_id                       CHAR(36)        NOT NULL    PRIMARY KEY

  , version                                BIGINT          NOT NULL
  , created                                TIMESTAMP(6)    NOT NULL
  , updated                                TIMESTAMP(6)    NOT NULL

)
