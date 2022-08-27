ALTER TABLE responsible_officer
    ADD division VARCHAR(100) NULL;

ALTER TABLE responsible_officer
    ADD region VARCHAR(100) NULL;

ALTER TABLE responsible_officer
    ADD territory VARCHAR(100) NULL;

ALTER TABLE responsible_officer
    MODIFY name VARCHAR(100);