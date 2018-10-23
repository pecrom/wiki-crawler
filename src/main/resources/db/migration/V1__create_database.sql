CREATE TABLE IF NOT EXISTS article_image
(
  id bigserial NOT NULL,
	article varchar(255) NOT NULL,
	image_path varchar(255) NULL,
	PRIMARY KEY (id)
);
