
insert into local_user(email_verified, email, password, first_name, last_name, username)
values (false,'zouliga@yallo.com','$2a$10$PNo8Xwo44ivU7lXhVbw3d.2jVusPPajiqHs1tTM20g4IL/xeWhrr2','latif','zou','latif'),
       (false,'hallo@yallo.com','$2a$10$PNo8Xwo44ivU7lXhVbw3d.2jVusPPajiqHs1tTM20g4IL/xeWhrr2','hallo','zou','hallo');


        INSERT INTO product (name, short_description, long_description, price)
        VALUES ('Product #1', 'Product one short description.', 'This is a very long description of product #1.', 5.50) ,
         ('Product #2', 'Product two short description.', 'This is a very long description of product #2.', 10.56) ,
         ('Product #3', 'Product three short description.', 'This is a very long description of product #3.', 2.74),
         ('Product #4', 'Product four short description.', 'This is a very long description of product #4.', 15.69),
         ('Product #5', 'Product five short description.', 'This is a very long description of product #5.', 42.59);

        -- INSERT statements for inventory
        INSERT INTO inventory (product_id, quantity)
        VALUES (1, 5),
        (2, 8),
        (3, 12),
        (4, 73),
        (5, 2);

        INSERT INTO address (address_line1, city, country, user_id)
        VALUES ('123 Tester Hill', 'Testerton', 'England', 1),
        ('312 Spring Boot', 'Hibernate', 'England', 2);

        -- SELECT statements to retrieve order IDs

        INSERT INTO web_order (address_id, user_id)
        VALUES (1, 1),
         (2, 2),
         (1, 1),
         (1, 1),
         (2, 2);

        -- INSERT statements for web_order_quantities

        INSERT INTO web_order_quantities (order_id, product_id, quantity) VALUES
                                                                              (1, 1, 5),
         (1, 2, 5),
         (2, 3, 5),
         (2, 2, 5),
         (2, 5, 5),
         (3, 3, 5),
         (4, 4, 5),
         (4, 2, 5),
         (5, 2, 5),
         (5, 1, 5);