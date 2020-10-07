create schema esp;

create table esp.e_shop
(
    id   bigserial primary key not null,
    name text,
    url  text
);

create table esp.catalog_section
(
    id        bigserial primary key not null,
    name      text,
    url       text,
    fk_e_shop bigint                not null,
    constraint catalog_section__eshop_fkey foreign key (fk_e_shop)
        references esp.e_shop (id) match simple
        on update no action
        on delete no action
);

create table esp.section_group
(
    id                 bigserial primary key not null,
    name               text,
    url                text,
    fk_catalog_section bigint                not null,
    constraint section_group__catalog_section_fkey foreign key (fk_catalog_section)
        references esp.catalog_section (id) match simple
        on update no action
        on delete no action
);

create table esp.item_category
(
    id               bigserial primary key not null,
    name             text,
    url              text,
    fk_section_group bigint                not null,
    constraint item_category__section_group_fkey foreign key (fk_section_group)
        references esp.section_group (id) match simple
        on update no action
        on delete no action
);

create table esp.item
(
    id               bigserial primary key not null,
    name             text,
    url              text,
    available        boolean               not null,
    code             text,
    description      text,
    image_url        text,
    price            double precision,
    fk_item_category bigint                not null,
    constraint item__item_category_fkey foreign key (fk_item_category)
        references esp.item_category (id) match simple
        on update no action
        on delete no action
);