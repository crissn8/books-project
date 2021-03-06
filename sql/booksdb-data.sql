source booksdb.schema.sql;

insert into users values('cris', MD5('cris'), 'cris');
insert into user_roles values ('cris', 'admin');

insert into users values('adri', MD5('adri'), 'adri');
insert into user_roles values ('adri', 'registered');

insert into autor (name) values ("autor1");
insert into autor (name) values ("autor2");
insert into autor (name) values ("autor3");


insert into libros (titulo,lengua, edicion, fecha_ed, fecha_imp, editorial, autor)values("libro1","catalan", "edicion1", "fecha1", "fecha1","editorial1","autor1");
insert into libros (titulo,lengua, edicion, fecha_ed, fecha_imp, editorial, autor)values("libro2","spanish", "edicion2", "fecha2", "fecha2","editorial2","autor2");
insert into libros (titulo,lengua, edicion, fecha_ed, fecha_imp, editorial, autor)values("libro3","english", "edicion3", "fecha3", "fecha3","editorial3","autor3");
insert into libros (titulo,lengua, edicion, fecha_ed, fecha_imp, editorial, autor)values("libro4","catalan", "edicion4", "fecha4", "fecha4","editorial4","autor1");
insert into libros (titulo,lengua, edicion, fecha_ed, fecha_imp, editorial, autor)values("libro5","spanish", "edicion5", "fecha5", "fecha5","editorial5","autor2");


insert into resenya (username,name,texto, libroid)values("cris","cris","eel mejor libro",1);
insert into resenya (username,name,texto, libroid)values("adri","adri","un libro horrible",2);
insert into resenya (username,name,texto, libroid)values("adri","adri","el mejor libro",1);
insert into resenya (username,name,texto, libroid)values("cris","cris","Muy bueno!",3);
insert into resenya (username,name,texto, libroid)values("adri","adri","de lo mejor",4);
