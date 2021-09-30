CREATE TABLE Coins
(
    id      UUID        NOT NULL PRIMARY KEY,
    name    VARCHAR(50) NOT NULL,
    code    VARCHAR(10) NOT NULL,
    enabled BOOL        NOT NULL
);

CREATE TABLE Orders
(
    id          UUID        NOT NULL PRIMARY KEY,
    coinId      UUID        NOT NULL,
    userId      UUID        NOT NULL,
    quantity    INTEGER     NOT NULL,
    buyAtPrice  DECIMAL     NOT NULL,
    orderStatus VARCHAR(50) NOT NULL
);

CREATE TABLE Fees
(
    id         UUID    NOT NULL PRIMARY KEY,
    coinId     UUID    NOT NULL,
    percentage DECIMAL NOT NULL
);
