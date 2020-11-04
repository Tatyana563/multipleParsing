create schema esp;

create table esp.market
(
    id   bigserial primary key not null,
    name text                  not null unique,
    url  text                  not null unique
);

create table esp.city
(
    id   bigserial primary key not null,
    name text                  not null unique
);

create table esp.market_city
(
    id        bigserial primary key not null,
    url       text                  not null,
    fk_market bigint                not null,
    fk_city   bigint                not null,
    constraint market_city__market_fkey foreign key (fk_market)
        references esp.market (id) match simple
        on update no action
        on delete no action,
    constraint market_city__city_fkey foreign key (fk_city)
        references esp.city (id) match simple
        on update no action
        on delete no action
);

create table esp.menu_item
(
    id             bigserial primary key not null,
    name           text                  not null,
    url            text                  not null,
    fk_market      bigint                not null,
    fk_parent_item bigint,
    constraint menu_item__market_fkey foreign key (fk_market)
        references esp.market (id) match simple
        on update no action
        on delete no action,
    constraint menu_item__parent_item_fkey foreign key (fk_parent_item)
        references esp.menu_item (id) match simple
        on update no action
        on delete no action
);

create table esp.product_item
(
    id           bigserial primary key not null,
    name         text                  not null,
    url          text                  not null,
    code         text,
    external_id  text                  not null,
    description  text,
    image_url    text,
    fk_menu_item bigint                not null,
    constraint product_item__menu_item_fkey foreign key (fk_menu_item)
        references esp.menu_item (id) match simple
        on update no action
        on delete no action
);

create table esp.product_price
(
    id              bigserial primary key not null,
    price           double precision      not null,
    fk_market_city  bigint                not null,
    fk_product_item bigint                not null,
    constraint product_price__market_city_fkey foreign key (fk_market_city)
        references esp.market_city (id) match simple
        on update no action
        on delete no action,
    constraint product_price__product_item_fkey foreign key (fk_product_item)
        references esp.product_item (id) match simple
        on update no action
        on delete no action
);