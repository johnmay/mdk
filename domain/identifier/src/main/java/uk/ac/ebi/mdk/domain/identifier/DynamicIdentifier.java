/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.domain.identifier;

import org.apache.log4j.Logger;

/**
 * Provides a dynamic identifier where the brief/description
 * can be specified.
 * TODO This possibly needs improving possibly via
 * TODO registering to a factory which keeps track of the unique brief/descriptions
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DynamicIdentifier extends AbstractIdentifier {

    private static final Logger LOGGER = Logger.getLogger(DynamicIdentifier.class);
    
    private String brief;
    private String description;
    private String accession;

    public DynamicIdentifier(String brief, String description, String accession) {
        super(accession);
        this.brief = brief;
        this.description = description;
    }

    public DynamicIdentifier(String brief, String accession) {
        this(brief, "Unavailable", accession);
    }

    public DynamicIdentifier(String accession) {
        this("Unknown", accession);
    }

    public DynamicIdentifier(){
        this("");
    }

    @Override
    public String getShortDescription() {
        return brief;
    }

    @Override
    public String getLongDescription() {
        return description;
    }

    @Override
    public Identifier newInstance() {
        return new DynamicIdentifier();
    }
}
