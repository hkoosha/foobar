
CREATE TABLE marketplace__order_request
(

    order_request_id    CHAR(36)        NOT NULL    PRIMARY KEY
  , version             LONG            NOT NULL
  , created             DATETIME(6)     NOT NULL
  , updated             DATETIME(6)     NOT NULL
  , line_item_id_pool   LONG            NOT NULL
  , customer_id         CHAR(36)        NOT NULL
  , seller_id           CHAR(36)
  , state               VARCHAR(127)    NOT NULL
  , sub_total           LONG

)
