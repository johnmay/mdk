/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.metabolomes.http.uniprot;

/**
 * UniProtSearchTerm.java – MetabolicDevelopmentKit – Jun 22, 2011
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public enum UniProtSearchTerm {

    EC("ec"),
    PROTEIN_NAME("name"),
    GENE("gene"),
    PROTEIN_FAMILY("family"),
    DOMAIN("domain"),
    // --
    ORGANISM("organism"),
    TAXONOMY("taxonomy"),
    VIRUS_HOST("host"),
    ORGANELLE("organelle"),
    // --
    //todo: add support for other annotations
    AND("AND"),
    NOT("NOT"),
    ;

    private String token;

    private UniProtSearchTerm(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
