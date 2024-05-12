CREATE TABLE IF NOT EXISTS logs
(
    timestamp DATETIME,
    thread String,
    level String,
    logger String,
    message String,
    environment String,
    service String
)
ENGINE = MergeTree()
PRIMARY KEY (timestamp);