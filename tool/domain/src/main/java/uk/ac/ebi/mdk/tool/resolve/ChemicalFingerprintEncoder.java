
/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
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

/**
 * ChemicalFingerprintEncoder.java
 *
 * 2011.09.22
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.mdk.tool.resolve;

import org.apache.log4j.Logger;

import java.util.regex.Pattern;


/**
 *          ChemicalFingerprintEncoder – 2011.09.22 <br>
 *          Extension of fingerprint encoder for chemical names. The name is first filtered for
 *          mentions of alpha/beta which are standardised.
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ChemicalFingerprintEncoder
  extends FingerprintEncoder {

    private static final Logger LOGGER = Logger.getLogger(ChemicalFingerprintEncoder.class);

    /*
     * Alpha/Beta patterns
     */
    private static Pattern ALPHA_UNIFIER = Pattern.compile(
      "&[Aa]lpha;|α|alpha|&#945;|&#x3B1;|&#913;|&#x391;");
    private static Pattern BETA_UNIFIER = Pattern.compile(
      "&[Bb]eta;|β|beta|&#946;|&#x3B2;|&#914;|&#x392;");


    /**
     *
     * Normalises the greek characters alpha and beta written in many forms. Including HTML Entity,
     * Decimal and Hex encodings
     *
     * @param string
     * @return
     * 
     */
    public static String normaliseGreekCharacters(String string) {
        String clean = ALPHA_UNIFIER.matcher(string).replaceAll("a");
        clean = BETA_UNIFIER.matcher(clean).replaceAll("B");
        return clean;
    }


    @Override
    public String encode(String string) {

        String clean = normaliseGreekCharacters(string);

        return super.encode(clean);
    }

}

