CREATE TABLE public.lists (
	id bigserial NOT NULL,
	name varchar(100) NOT NULL,
	description varchar(500) NOT NULL,
	CONSTRAINT lists_PK PRIMARY KEY (id)
);

CREATE TABLE public.items (
	id bigserial NOT NULL,
	list bigint NOT NULL,
	name varchar(100) NOT NULL,
	description varchar(500) NOT NULL,
	CONSTRAINT items_PK PRIMARY KEY (id),
	CONSTRAINT items_lists_FK FOREIGN KEY (list) REFERENCES lists(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);