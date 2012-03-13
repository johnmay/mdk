package uk.ac.ebi.interfaces.entities;

import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.Genome;
import uk.ac.ebi.interfaces.identifiers.Identifier;

import java.io.File;
import java.util.Collection;

/**
 * IReconstruction - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface IReconstruction extends AnnotatedEntity {

    public Genome getGenome();

    public void setGenome(Genome genome);

    public Collection<GeneProduct> getProducts();

    public Collection<MetabolicReaction> getReactome();

    public Collection<Metabolite> getMetabolome();

    public Identifier getTaxonomy();

    public File getContainer();

    public void setContainer(File f);

    public void setTaxonomy(Identifier identifier);

    public void addMetabolite(Metabolite metabolite);

    public void addProduct(GeneProduct product);

    public void addReaction(MetabolicReaction reaction);


}
