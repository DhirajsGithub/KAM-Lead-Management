-- Users table (KAMs)
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    role ENUM('manager', 'admin'),
    last_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT true
   
);

-- Restaurant-User many-to-many relationship
CREATE TABLE restaurant_users (
    restaurant_id INT,
    user_id INT,
    relationship_type ENUM('kam', 'support', 'admin'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (restaurant_id, user_id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(restaurant_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Restaurants table with all relationships
CREATE TABLE restaurants (
    restaurant_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address TEXT,
    city VARCHAR(50),
    state VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    lead_status ENUM('new', 'contacted', 'qualified', 'negotiating', 'converted', 'lost'),
    annual_revenue DECIMAL(15,2),
    timezone VARCHAR(50),
    
    -- One-to-Many relationships (stores latest/primary records)
    primary_contact_id INT,
    latest_interaction_id INT,
    latest_order_id INT,
    current_schedule_id INT,
    latest_metric_id INT,
    
    -- Many-to-Many relationship with users handled through restaurant_users table
    
    FOREIGN KEY (primary_contact_id) REFERENCES contacts(contact_id),
    FOREIGN KEY (latest_interaction_id) REFERENCES interactions(interaction_id),
    FOREIGN KEY (latest_order_id) REFERENCES orders(order_id),
    FOREIGN KEY (current_schedule_id) REFERENCES call_schedules(schedule_id),
    FOREIGN KEY (latest_metric_id) REFERENCES performance_metrics(metric_id)
);

-- Contacts (One-to-Many: one restaurant has many contacts)
CREATE TABLE contacts (
    contact_id INT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INT NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20),
    is_primary BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(restaurant_id)
);

-- Interactions (One-to-Many: one restaurant has many interactions)
CREATE TABLE interactions (
    interaction_id INT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INT NOT NULL,
    contact_id INT,
    user_id INT NOT NULL,
    interaction_type ENUM('call', 'email', 'meeting', 'other'),
    interaction_date TIMESTAMP NOT NULL,
    notes TEXT,
    follow_up_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(restaurant_id),
    FOREIGN KEY (contact_id) REFERENCES contacts(contact_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Orders (One-to-Many: one restaurant has many orders)
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INT NOT NULL,
    order_date TIMESTAMP NOT NULL,
    total_amount DECIMAL(15,2) NOT NULL,
    status ENUM('pending', 'confirmed', 'delivered', 'cancelled'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(restaurant_id)
);

-- Call schedules (One-to-Many: one restaurant has many schedules)
CREATE TABLE call_schedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INT NOT NULL,
    frequency_days INT NOT NULL,
    last_call_date TIMESTAMP,
    next_call_date TIMESTAMP,
    priority_level INT CHECK (priority_level BETWEEN 1 AND 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(restaurant_id)
);

-- Performance metrics (One-to-Many: one restaurant has many metrics)
CREATE TABLE performance_metrics (
    metric_id INT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id INT NOT NULL,
    measurement_date TIMESTAMP NOT NULL,
    order_count INT DEFAULT 0,
    total_revenue DECIMAL(15,2) DEFAULT 0,
    average_order_value DECIMAL(15,2),
    days_since_last_order INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(restaurant_id)
);


-- Essential indexes for performance
CREATE INDEX idx_restaurants_kam ON restaurants(assigned_kam_id);
CREATE INDEX idx_restaurants_status ON restaurants(lead_status);
CREATE INDEX idx_contacts_restaurant ON contacts(restaurant_id);
CREATE INDEX idx_interactions_restaurant ON interactions(restaurant_id);
CREATE INDEX idx_interactions_date ON interactions(interaction_date);
CREATE INDEX idx_orders_restaurant ON orders(restaurant_id);
CREATE INDEX idx_call_schedules_next_call ON call_schedules(next_call_date);
