BEGIN;
ALTER TABLE esp.product_price ADD COLUMN fk_city bigint;

UPDATE esp.product_price
SET fk_city = market_city.fk_city
FROM esp.market_city WHERE fk_market_city = market_city.id;

ALTER TABLE esp.product_price
ADD constraint product_price__city_fkey foreign key (fk_city)
    references esp.city (id) match simple
    on update no action
    on delete no action;
alter table esp.product_price alter column fk_city set not null;
ALTER TABLE esp.product_price DROP COLUMN fk_market_city;
COMMIT;