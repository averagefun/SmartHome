CREATE TABLE IF NOT EXISTS states
(
    hub_id Int64,
    id Int64,
    value Float64,
    date TIMESTAMP
)
ENGINE = MergeTree()
ORDER BY (date, hub_id, id);