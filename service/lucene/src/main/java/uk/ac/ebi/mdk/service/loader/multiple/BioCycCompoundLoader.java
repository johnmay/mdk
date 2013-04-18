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

package uk.ac.ebi.mdk.service.loader.multiple;

import org.apache.log4j.Logger;
import org.openscience.cdk.Isotope;
import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import uk.ac.ebi.mdk.io.text.attribute.AttributedEntry;
import uk.ac.ebi.mdk.io.text.biocyc.BioCycDatReader;
import uk.ac.ebi.mdk.io.text.attribute.Attribute;
import uk.ac.ebi.mdk.io.text.biocyc.CompoundAttribute;
import uk.ac.ebi.mdk.service.MultiIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.AbstractMultiIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.location.SystemLocation;
import uk.ac.ebi.mdk.service.loader.writer.DefaultDataIndexWriter;
import uk.ac.ebi.mdk.service.loader.writer.DefaultNameIndexWriter;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static uk.ac.ebi.mdk.io.text.biocyc.CompoundAttribute.*;

/**
 * Loads BioCyc Compound data (Name and Charge)
 *
 * @author John May
 */
public class BioCycCompoundLoader extends AbstractMultiIndexResourceLoader {

    private static final Logger  LOGGER      = Logger.getLogger(BioCycCompoundLoader.class);
    private static final Pattern REMOVE_TAGS = Pattern.compile("</?(?:i|sub|sup|em|small)/?>", Pattern.CASE_INSENSITIVE);
    private static final Pattern ATOM_CHARGE = Pattern.compile("\\(.+?\\s(.+?)\\)");

    private final String org;

    public BioCycCompoundLoader(String org) {

        this.org = org;
        addRequiredResource("BioCyc Compounds",
                            "compounds.dat file from a BioCyc database",
                            ResourceFileLocation.class);

    }

    @Override
    public void update() throws IOException {

        ResourceFileLocation location = getLocation("biocyc.compounds");

        BioCycDatReader<CompoundAttribute> reader = new BioCycDatReader<CompoundAttribute>(location.open(), values());
        DefaultNameIndexWriter nameWriter = new DefaultNameIndexWriter(getIndex("biocyc.names"));
        DefaultDataIndexWriter dataWriter = new DefaultDataIndexWriter(getIndex("biocyc.data"));

        int count = 0;
        while (reader.hasNext()) {

            AttributedEntry<CompoundAttribute, String> entry = reader.next();

            String identifier = entry.has(UNIQUE_ID) ? entry.getFirst(UNIQUE_ID) : "";
            String commonName = entry.has(COMMON_NAME) ? entry.getFirst(COMMON_NAME) : "";

            Collection<String> synonyms = entry.get(SYNONYMS);
            String systematicName = entry.has(SYSTEMATIC_NAME) ? entry.getFirst(SYSTEMATIC_NAME) : "";

            nameWriter.write(org + ":" + identifier, clean(commonName), clean(systematicName), clean(synonyms));

            dataWriter.write(org + ":" + identifier,
                             entry.has(ATOM_CHARGES) ? getCharge(entry.get(ATOM_CHARGES)) : "",
                             entry.has(CHEMICAL_FORMULA) ? getFormula(entry.get(CHEMICAL_FORMULA)) : "");

            if(++count % 200 == 0)
                fireProgressUpdate(location.progress());


        }

        nameWriter.close();
        dataWriter.close();
        reader.close();
        location.close();

    }

    public String getFormula(Collection<String> formulaParts) {

        MolecularFormula formula = new MolecularFormula();

        for (String formulaPart : formulaParts) {
            String part = formulaPart.substring(1, formulaPart.length() - 1);
            String[] subpart = part.split(" ");
            formula.addIsotope(new Isotope(subpart[0]), Integer.parseInt(subpart[1]));
        }

        return MolecularFormulaManipulator.getString(formula);

    }

    public String getCharge(Collection<String> atomCharges) {

        Integer charge = 0;

        for (String atomCharge : atomCharges) {
            Matcher matcher = ATOM_CHARGE.matcher(atomCharge);
            if (matcher.find()) {
                charge += Integer.parseInt(matcher.group(1));
            }
        }

        return charge.toString();

    }

    public Collection<String> clean(Collection<String> names) {
        Collection<String> cleaned = new ArrayList<String>();
        for (String name : names) {
            cleaned.add(clean(name));
        }
        return cleaned;
    }

    public String clean(String name) {

        name = REMOVE_TAGS.matcher(name).replaceAll("");

        // could use a map here but this isn't too slow
        name = name.replaceAll("&alpha;", "α");
        name = name.replaceAll("&beta;", "β");
        name = name.replaceAll("&gamma;", "γ");
        name = name.replaceAll("&kappa;", "κ");
        name = name.replaceAll("&iota;", "ι");
        name = name.replaceAll("&nu;", "ν");
        name = name.replaceAll("&mu;", "μ");
        name = name.replaceAll("&Delta;", "Δ");
        name = name.replaceAll("&delta;", "Δ");
        name = name.replaceAll("&epsilon;", "ε");
        name = name.replaceAll("&omega;", "ω");
        name = name.replaceAll("&psi;", "Ψ");
        name = name.replaceAll("&Psi;", "Ψ");
        name = name.replaceAll("&chi;", "χ");
        name = name.replaceAll("&plusmn;", "±");
        name = name.replaceAll("&pi;", "π");
        name = name.replaceAll("&tau;", "τ");
        name = name.replaceAll("&zeta;", "ζ");

        // arrows
        name = name.replaceAll("&rarr;", "→");
        name = name.replaceAll("&larr;", "←");

        return name;

    }

    @Override
    public String getName() {
        return "BioCyc Loader";
    }

    public static void main(String[] args) throws IOException {
        MultiIndexResourceLoader loader = new BioCycCompoundLoader("META");
        loader.addLocation("biocyc.compounds", new SystemLocation("/databases/biocyc/MetaCyc/data/compounds.dat"));
        if (loader.canBackup()) loader.backup();
        if (loader.canUpdate()) loader.update();
    }

}
