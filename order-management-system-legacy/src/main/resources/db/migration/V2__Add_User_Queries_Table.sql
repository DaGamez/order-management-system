-- Create user queries table
CREATE TABLE IF NOT EXISTS user_queries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    query_type VARCHAR(50) NOT NULL,
    query_details VARCHAR(255),
    user_id BIGINT NOT NULL,
    timestamp DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create index for faster querying by timestamp
CREATE INDEX IF NOT EXISTS idx_user_queries_timestamp ON user_queries(timestamp);

-- Create index for faster querying by user_id
CREATE INDEX IF NOT EXISTS idx_user_queries_user_id ON user_queries(user_id);
