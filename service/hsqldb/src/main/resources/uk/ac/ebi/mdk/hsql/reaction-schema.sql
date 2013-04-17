CREATE TABLE reaction (id IDENTITY,
                       accession VARCHAR(10),
                       ec VARCHAR(20),
                       CONSTRAINT accession_index UNIQUE(accession));
CREATE TABLE compound (id IDENTITY,
                       accession VARCHAR(10),
                       CONSTRAINT compound_accession_index UNIQUE(accession));
CREATE TABLE reactant (reaction_id INT,
                          compound_id INT,
                          coefficient DOUBLE,
                          CONSTRAINT compound_reactant_fk FOREIGN KEY (reaction_id) REFERENCES reaction(id),
                          CONSTRAINT reaction_reactant_fk FOREIGN KEY (compound_id) REFERENCES compound(id));
CREATE TABLE product (reaction_id INT,
                          compound_id INT,
                          coefficient DOUBLE,
                          CONSTRAINT compound_product_fk FOREIGN KEY (reaction_id) REFERENCES reaction(id),
                          CONSTRAINT reaction_product_fk FOREIGN KEY (compound_id) REFERENCES compound(id));
