DROP SCHEMA PUBLIC CASCADE;
CREATE TABLE reaction (id IDENTITY,
                       accession VARCHAR(10),
                       CONSTRAINT accession_index UNIQUE(accession));
CREATE TABLE ec (id IDENTITY,
                 reaction_id INT,
                 ec VARCHAR(12),
                 CONSTRAINT reaction_enzyme_fk FOREIGN KEY (reaction_id)
                                               REFERENCES  reaction(id));
CREATE TABLE compound (id IDENTITY,
                       accession VARCHAR(10),
                       CONSTRAINT compound_accession_index UNIQUE(accession));
CREATE TABLE participant (reaction_id INT,
                          compound_id INT,
                          coefficient DOUBLE,
                          side CHAR,
                          CONSTRAINT side_enum CHECK ( side IN ( 'r', 'p') ),
                          CONSTRAINT compound_participant_fk FOREIGN KEY (compound_id) REFERENCES compound(id),
                          CONSTRAINT reaction_participant_fk FOREIGN KEY (reaction_id) REFERENCES reaction(id));
