CREATE TABLE Task (
    id BIGINT NOT NULL AUTO_INCREMENT,
    statement VARCHAR(255) NOT NULL,
    task_order INT NOT NULL,
    course_id BIGINT NOT NULL,
    task_type VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES Course(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;