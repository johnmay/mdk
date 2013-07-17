package uk.ac.ebi.mdk.domain.annotation.rex;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jan
 * Date: 15/07/13
 * Time: 09:25
 * To change this template use File | Settings | File Templates.
 */
public class RExAnnotation
{
    private List<RExExtract> extracts;
    private List<RExCompound> compounds;

    public RExAnnotation(List<RExExtract> extracts, List<RExCompound> compounds)
    {
        this.extracts = extracts;
        this.compounds = compounds;
    }

    public List<RExExtract> getExtracts()
    {
        return extracts;
    }

    public List<RExCompound> getCompounds()
    {
        return compounds;
    }
}
