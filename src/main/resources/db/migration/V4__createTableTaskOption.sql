CREATE TABLE TaskOption(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    option_name varchar(80) NOT NULL,
    isCorrect BOOLEAN,
    task_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_task FOREIGN KEY (task_id) REFERENCES task(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;