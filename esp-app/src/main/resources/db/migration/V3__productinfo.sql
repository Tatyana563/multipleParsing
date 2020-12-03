create table esp.product_item_info
(
    id              bigserial not null primary key,
    external_id     text,
    description     text,
    fk_product_item bigint    not null,
    constraint product_info__product_item_fk foreign key (fk_product_item)
        references esp.product_item (id) match simple
        on update no action
        on delete no action
);

INSERT INTO  esp.product_item_info (external_id, description, fk_product_item)
    SELECT external_id, description, id FROM esp.product_item;

ALTER TABLE esp.product_item
    DROP COLUMN external_id,
    DROP COLUMN description;
