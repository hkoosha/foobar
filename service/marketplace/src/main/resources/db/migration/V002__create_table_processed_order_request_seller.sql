
CREATE TABLE marketplace__processed_order_request_seller
(

    order_request_id                       CHAR(36)        NOT NULL    PRIMARY KEY
  , version                                LONG            NOT NULL
  , created                                DATETIME(6)     NOT NULL
  , updated                                DATETIME(6)     NOT NULL

)
