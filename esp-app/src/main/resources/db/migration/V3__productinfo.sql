create table esp.product_item_info
(
    id          bigserial not null
        constraint product_item_info_pkey
            primary key,
    external_id text,
    description text,

    ADD constraint product_info_product_item_fk foreign key (id)
        references esp.product_item (id) match simple
        on update no action
        on delete no action;

UPDATE esp.product_item_info
SET external_id = product_item.external_id FROM esp.product_item
SET description = product_item.description FROM esp.product_item

ALTER TABLE esp.product_item
DROP COLUMN external_id,
DROP COLUMN description,

);
