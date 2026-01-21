ALTER TABLE cart_items DROP FOREIGN KEY fk_cart;

-- Parent table change karein
ALTER TABLE carts MODIFY id BINARY(16) NOT NULL;

-- Child table ka reference column bhi change karein
ALTER TABLE cart_items MODIFY cart_id BINARY(16) NOT NULL;