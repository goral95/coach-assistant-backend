create table players(
    id int primary key auto_increment,
    name varchar(255)  not null,
    surname varchar(255)  not null,
    birth_date datetime  not null,
    footed varchar(255)  null,
    position varchar(255)  null,
    city varchar(255)  null,
    create_date datetime null,
    modify_date datetime null,
    deleted boolean default false
);

INSERT INTO players (name, surname, birth_date, footed, position, city)
VALUES 
    ('John', 'Doe', '1990-05-15', 'Right', 'Forward', 'New York'),
    ('Alice', 'Smith', '1988-08-21', 'Left', 'Midfielder', 'Los Angeles'),
    ('Bob', 'Johnson', '1995-03-10', 'Right', 'Defender', 'Chicago');