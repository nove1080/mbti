CREATE
    USER 'mbti-local'@'localhost' IDENTIFIED BY 'mbti-local';
CREATE
    USER 'mbti-local'@'%' IDENTIFIED BY 'mbti-local';

GRANT ALL PRIVILEGES ON *.* TO
    'mbti-local'@'localhost';
GRANT ALL PRIVILEGES ON *.* TO
    'mbti-local'@'%';

CREATE
    DATABASE mbti DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;