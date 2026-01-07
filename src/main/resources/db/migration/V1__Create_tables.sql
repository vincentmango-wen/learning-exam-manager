-- study_subjectsテーブル
CREATE TABLE study_subjects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_name (name)
);

-- study_itemsテーブル
CREATE TABLE study_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject_id BIGINT NOT NULL,
    title VARCHAR(500) NOT NULL,
    status ENUM('NOT_STARTED', 'IN_PROGRESS', 'DONE') NOT NULL DEFAULT 'NOT_STARTED',
    study_time INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_subject_id (subject_id),
    INDEX idx_status (status),
    FOREIGN KEY (subject_id) REFERENCES study_subjects(id) ON DELETE CASCADE
);

-- examsテーブル
CREATE TABLE exams (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject_id BIGINT NOT NULL,
    exam_name VARCHAR(200) NOT NULL,
    max_score INT NOT NULL DEFAULT 100,
    passing_score INT NOT NULL DEFAULT 60,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_subject_id (subject_id),
    FOREIGN KEY (subject_id) REFERENCES study_subjects(id) ON DELETE CASCADE
);

-- exam_resultsテーブル
CREATE TABLE exam_results (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_id BIGINT NOT NULL,
    score INT NOT NULL,
    passed BOOLEAN NOT NULL,
    taken_at DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_exam_id (exam_id),
    INDEX idx_taken_at (taken_at),
    INDEX idx_passed (passed),
    FOREIGN KEY (exam_id) REFERENCES exams(id) ON DELETE CASCADE
);