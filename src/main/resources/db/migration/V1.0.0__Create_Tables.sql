CREATE TABLE Coin
(
    id      UUID        NOT NULL PRIMARY KEY,
    name    VARCHAR(50) NOT NULL,
    code    VARCHAR(10) NOT NULL,
    enabled BOOL        NOT NULL
);