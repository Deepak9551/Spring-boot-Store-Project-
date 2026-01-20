ALTER TABLE mosh2.cart_items DROP FOREIGN KEY fk_cart;

-- Parent table change karein
ALTER TABLE mosh2.carts MODIFY id BINARY(16) NOT NULL;

-- Child table ka reference column bhi change karein
ALTER TABLE mosh2.cart_items MODIFY cart_id BINARY(16) NOT NULL;