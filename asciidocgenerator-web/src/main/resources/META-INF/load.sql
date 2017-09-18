insert into GRUPPE (id,bezeichnung) values ('Test', 'Test Dokus');
#
insert into HAUPTNAVIGATION (gruppeId, hauptnavigationname, sortierung) values ('Test', 'Fahrzeuge', 0);
#
#
insert into SEITENNAVIGATION (gruppeId, hauptnavigationname, navigationspfad) values ('Test', 'Fahrzeuge', 'Test/Fahrzeuge/Autos');
insert into SEITENNAVIGATION (gruppeId, hauptnavigationname, navigationspfad) values ('Test', 'Fahrzeuge', 'Test/Fahrzeuge/Autos/Ford');
insert into SEITENNAVIGATION (gruppeId, hauptnavigationname, navigationspfad) values ('Test', 'Fahrzeuge', 'Test/Fahrzeuge/Autos/Ford/Shelby');
insert into SEITENNAVIGATION (gruppeId, hauptnavigationname, navigationspfad) values ('Test', 'Fahrzeuge', 'Test/Fahrzeuge/Autos/Bentley');
#
insert into BEITRAG (navigationspfad, ablagepfad, navigationkey, title, renderzeitpunkt, projektname, repositoryname, vcsurl, vcsversion) values ('Test/Fahrzeuge/Autos', 'C:\tmp\article\Projekt1\Autos.html', 'd56acabd-d405-4025-b391-9a508606bcc0', 'Auto Kollektion', NULL, 'Projekt1', 'Projekt1', 'gitlab.e-bk.m086/Projekt1','1.0.0');
insert into BEITRAG (navigationspfad, ablagepfad, navigationkey, title, renderzeitpunkt, projektname, repositoryname, vcsurl, vcsversion) values ('Test/Fahrzeuge/Autos', 'C:\tmp\article\Projekt1\Maserati.html', 'b49c2cfd-57ac-433d-9ad2-69496fb61c88', 'Autos Kollektion 2', NULL, 'Projekt1', 'Projekt1', 'gitlab.e-bk.m086/Projekt1','1.0.0');
insert into BEITRAG (navigationspfad, ablagepfad, navigationkey, title, renderzeitpunkt, projektname, repositoryname, vcsurl, vcsversion) values ('Test/Fahrzeuge/Autos/Ford', 'C:\tmp\article\Projekt1\Ford\Ford.html', '7b0b63f0-3734-474e-8439-3cb649730884', 'Ford Autos',NULL, 'Projekt1', 'Projekt1', 'gitlab.e-bk.m086/Projekt1','1.0.0');
insert into BEITRAG (navigationspfad, ablagepfad, navigationkey, title, renderzeitpunkt, projektname, repositoryname, vcsurl, vcsversion) values ('Test/Fahrzeuge/Autos/Ford/Shelby', 'C:\tmp\article\Projekt1\Ford\Shelby\Autos.html', 'e694ec23-eda0-4d32-b6cd-1adfc9ca7055', 'Ford Shelby', NULL, 'Projekt1', 'Projekt1', 'gitlab.e-bk.m086/Projekt1','1.0.0');
insert into BEITRAG (navigationspfad, ablagepfad, navigationkey, title, renderzeitpunkt, projektname, repositoryname, vcsurl, vcsversion) values ('Test/Fahrzeuge/Autos/Bentley', 'C:\tmp\article\Projekt1\Bentley\Bentley.html', '0026f98d-fc8f-437b-8b5b-76ca12e3676d', 'Bentley', NULL, 'Projekt1', 'Projekt1', 'gitlab.e-bk.m086/Projekt1','1.0.0');
#
#
insert into KATEGORIE (kategoriename) values ('Autos');
insert into KATEGORIE (kategoriename) values ('Kollektion');
insert into KATEGORIE (kategoriename) values ('Maserati');
insert into KATEGORIE (kategoriename) values ('Ford');
insert into KATEGORIE (kategoriename) values ('Shelby');
insert into KATEGORIE (kategoriename) values ('Bentley');
insert into KATEGORIE (kategoriename) values ('schnell');
#
insert into BEITRAGSKATEGORIE (navigationspfad, ablagepfad, KATEGORIENAME) values ('Test/Fahrzeuge/Autos', 'C:\tmp\article\Projekt1\Autos.html', 'Kollektion');
insert into BEITRAGSKATEGORIE (navigationspfad, ablagepfad, KATEGORIENAME) values ('Test/Fahrzeuge/Autos', 'C:\tmp\article\Projekt1\Autos.html', 'Autos');
insert into BEITRAGSKATEGORIE (navigationspfad, ablagepfad, KATEGORIENAME) values ('Test/Fahrzeuge/Autos', 'C:\tmp\article\Projekt1\Maserati.html', 'Maserati');
insert into BEITRAGSKATEGORIE (navigationspfad, ablagepfad, KATEGORIENAME) values ('Test/Fahrzeuge/Autos', 'C:\tmp\article\Projekt1\Maserati.html', 'Autos');
insert into BEITRAGSKATEGORIE (navigationspfad, ablagepfad, KATEGORIENAME) values ('Test/Fahrzeuge/Autos/Bentley', 'C:\tmp\article\Projekt1\Bentley\Bentley.html', 'Autos');
insert into BEITRAGSKATEGORIE (navigationspfad, ablagepfad, KATEGORIENAME) values ('Test/Fahrzeuge/Autos/Bentley', 'C:\tmp\article\Projekt1\Bentley\Bentley.html', 'Bentley');
insert into BEITRAGSKATEGORIE (navigationspfad, ablagepfad, KATEGORIENAME) values ('Test/Fahrzeuge/Autos/Ford', 'C:\tmp\article\Projekt1\Ford\Ford.html', 'Ford');
insert into BEITRAGSKATEGORIE (navigationspfad, ablagepfad, KATEGORIENAME) values ('Test/Fahrzeuge/Autos/Ford/Shelby', 'C:\tmp\article\Projekt1\Ford\Shelby\Autos.html', 'Ford');
insert into BEITRAGSKATEGORIE (navigationspfad, ablagepfad, KATEGORIENAME) values ('Test/Fahrzeuge/Autos/Ford/Shelby', 'C:\tmp\article\Projekt1\Ford\Shelby\Autos.html', 'Shelby');