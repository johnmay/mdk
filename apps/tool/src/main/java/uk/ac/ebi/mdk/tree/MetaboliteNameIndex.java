package uk.ac.ebi.mdk.tree;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import uk.ac.ebi.mdk.domain.annotation.Synonym;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import java.util.Collection;

/** @author John May */
public class MetaboliteNameIndex {

    private final Reconstruction reconstruction;
    private final boolean        synonyms;
    private final Multimap<String, Metabolite> nameMap = HashMultimap.create();

    MetaboliteNameIndex(Reconstruction recon, boolean synonyms) {
        this.reconstruction = recon;
        this.synonyms = false;
        for (Metabolite m : recon.metabolome()) {
            nameMap.put(key(m.getName()), m);
            if (synonyms) {
                for (Synonym synonym : m.getAnnotations(Synonym.class)) {
                    nameMap.put(key(synonym.getValue()), m);
                }
            }
        }
    }

    public Collection<Metabolite> ofName(String name) {
        return nameMap.get(key(name));
    }

    static strictfp String key(String str) {
        return str.toLowerCase().replaceAll("[- ,(){}\\[\\]]", "");
    }

}
