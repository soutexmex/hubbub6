ALTER TABLE Profiles DROP userid;
ALTER TABLE Users DROP profileid;

DROP TABLE Posts;
DROP TABLE Profiles;
DROP TABLE Users;

CREATE TABLE Users (
    username VARCHAR(12) NOT NULL UNIQUE,
    password VARCHAR(15) NOT NULL,
    profileid INT,
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY
);

CREATE TABLE Profiles (    
    joindate DATE DEFAULT CURRENT_DATE,
    firstname VARCHAR(20) NOT NULL,
    lastname VARCHAR(30) NOT NULL,
    email VARCHAR(100) NOT NULL,
    zip VARCHAR(10) NOT NULL,
    userid INT NOT NULL,
    biography LONG VARCHAR,
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY
);

ALTER TABLE Profiles ADD FOREIGN KEY (userid) REFERENCES Users (id);
ALTER TABLE Users ADD FOREIGN KEY (profileid) REFERENCES Profiles (id);

CREATE TABLE Posts (
    content VARCHAR(140) NOT NULL,
    authorid INT NOT NULL,
    postdate DATE DEFAULT CURRENT_DATE,
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY
);

ALTER TABLE Posts ADD FOREIGN KEY (authorid) REFERENCES Users (id);

INSERT INTO Users (username, password) VALUES
    ('johndoe','password'),
    ('jilljack','password');

INSERT INTO Profiles( joindate, firstname, lastname, email, zip, userid) VALUES
    ('2013-05-09', 'John', 'Doe', 'jd@example.com', '98008', 1),
    ('2013-10-31', 'Jill', 'Jack', 'jj@nowhere.com', '24201', 2);

UPDATE Users SET profileid=1 WHERE id=1;
UPDATE Users SET profileid=2 WHERE id=2;

INSERT INTO Posts (content, authorid, postdate) VALUES
    ('I''m a white-hat hacking my wonky Twonky server.', 1, '2013-05-09'),
    ('My wonky Twonky server conked out.', 1, '2014-06-23'),
    ('I see good reason not to configure Twonky.', 2, '2013-11-01');
