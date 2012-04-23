package uk.ac.ebi.interfaces.entities;

import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.Genome;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.mdk.domain.entity.collection.Metabolome;
import uk.ac.ebi.mdk.domain.entity.collection.Proteome;
import uk.ac.ebi.mdk.domain.entity.collection.Reactome;
import uk.ac.ebi.mdk.domain.matrix.StoichiometricMatrix;

import java.io.File;
import java.util.Collection;

/**
 * Reconstruction - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface Reconstruction extends AnnotatedEntity {

    public Genome getGenome();

    public void setGenome(Genome genome);

    public Collection<GeneProduct> getProducts();

    public Reactome getReactome();

    public Metabolome getMetabolome();

    public Proteome getProteome();

    public Identifier getTaxonomy();

    public File getContainer();

    public void setContainer(File f);

    public void setTaxonomy(Identifier identifier);

    public void addMetabolite(Metabolite metabolite);

    public void addProduct(GeneProduct product);

    public void addReaction(MetabolicReaction reaction);


    public boolean addSubset(EntityCollection subset);

    public boolean hasMatrix();

    public void setMatrix(StoichiometricMatrix matrix);

    public StoichiometricMatrix getMatrix();

    public Iterable<? extends EntityCollection> getSubsets();
}
