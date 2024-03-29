
CREATE TABLE marketplace__order_request_line_item
(

    internal_order_request_line_item_id    CHAR(36)        NOT NULL    PRIMARY KEY
  , order_request_line_item_id             CHAR(36)        NOT NULL
  , order_request_id                       CHAR(36)        NOT NULL
  , product_id                             CHAR(36)        NOT NULL
  , units                                  BIGINT

  , version                                BIGINT          NOT NULL
  , created                                TIMESTAMP(6)    NOT NULL
  , updated                                TIMESTAMP(6)    NOT NULL

)
