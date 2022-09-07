
CREATE TABLE marketplace__order_request
(

    order_request_id    CHAR(36)        NOT NULL    PRIMARY KEY
  , line_item_id_pool   BIGINT          NOT NULL
  , customer_id         CHAR(36)        NOT NULL
  , seller_id           CHAR(36)
  , state               VARCHAR(127)    NOT NULL
  , sub_total           BIGINT

  , version             BIGINT          NOT NULL
  , created             TIMESTAMP(6)    NOT NULL
  , updated             TIMESTAMP(6)    NOT NULL

)
