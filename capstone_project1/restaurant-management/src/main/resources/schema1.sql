-- Create the user table for the User entity
CREATE TABLE app_user (
                          id SERIAL PRIMARY KEY,
                          email VARCHAR(255) UNIQUE NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          role VARCHAR(20) NOT NULL
);

-- Create the restaurant table for the Restaurant entity
CREATE TABLE restaurant (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            location VARCHAR(255) NOT NULL,
                            owner_id INTEGER NOT NULL,
                            CONSTRAINT fk_owner
                                FOREIGN KEY(owner_id)
                                    REFERENCES app_user(id)
                                    ON DELETE CASCADE  -- If a user is deleted, all their restaurants will also be deleted
);

-- Create the menu_item table for the MenuItem entity
CREATE TABLE menu_item (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           description TEXT,
                           price DECIMAL(10,2) NOT NULL,
                           image_url VARCHAR(255),
                           restaurant_id INTEGER NOT NULL,
                           CONSTRAINT fk_restaurant
                               FOREIGN KEY(restaurant_id)
                                   REFERENCES restaurant(id)
                                   ON DELETE CASCADE  -- If a restaurant is deleted, all its menu items will also be deleted
);

-- Create the orders table for the Order entity
CREATE TABLE orders (
                        id SERIAL PRIMARY KEY,
                        user_id INTEGER NOT NULL,
                        restaurant_id INTEGER NOT NULL,
                        total_price DECIMAL(10,2) NOT NULL,
                        status VARCHAR(20) NOT NULL,
                        created_at TIMESTAMP DEFAULT NOW(),
                        CONSTRAINT fk_order_user
                            FOREIGN KEY(user_id)
                                REFERENCES app_user(id)
                                ON DELETE CASCADE,  -- If a user is deleted, all their orders will also be deleted
                        CONSTRAINT fk_order_restaurant
                            FOREIGN KEY(restaurant_id)
                                REFERENCES restaurant(id)
                                ON DELETE CASCADE  -- If a restaurant is deleted, all its orders will also be deleted
);

-- Create the order_detail table for the OrderDetail entity
CREATE TABLE order_detail (
                              id SERIAL PRIMARY KEY,
                              order_id INTEGER NOT NULL,
                              menu_item_id INTEGER NOT NULL,
                              quantity INTEGER NOT NULL,
                              CONSTRAINT fk_order
                                  FOREIGN KEY(order_id)
                                      REFERENCES orders(id)
                                      ON DELETE CASCADE,  -- If an order is deleted, its order details will also be deleted
                              CONSTRAINT fk_menu_item
                                  FOREIGN KEY(menu_item_id)
                                      REFERENCES menu_item(id)
                                      ON DELETE CASCADE
);

-- Create the cart table for the Cart entity
CREATE TABLE cart (
                      id SERIAL PRIMARY KEY,
                      user_id INTEGER NOT NULL,
                      menu_item_id INTEGER NOT NULL,
                      quantity INTEGER NOT NULL,
                      CONSTRAINT fk_cart_user
                          FOREIGN KEY(user_id)
                              REFERENCES app_user(id)
                              ON DELETE CASCADE,  -- If a user is deleted, their cart items will also be deleted
                      CONSTRAINT fk_cart_menu_item
                          FOREIGN KEY(menu_item_id)
                              REFERENCES menu_item(id)
                              ON DELETE CASCADE
);
