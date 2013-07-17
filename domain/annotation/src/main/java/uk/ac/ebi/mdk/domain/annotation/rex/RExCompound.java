package uk.ac.ebi.mdk.domain.annotation.rex;

import uk.ac.ebi.mdk.domain.annotation.AbstractAnnotation;
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.lang.annotation.Description;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Context(Reaction.class)
@Brief("REx Compound")
@Description("Text-mining information for a compound in the reaction")
public class RExCompound extends AbstractAnnotation
{
    private final String id;
    private final Type type;
    private final boolean isInSeed, isInBranch;
    private final List<String> alternativePathways, otherPathways;
    private final double extraction, relevance;

    public static enum Type {
        SUBSTRATE,
        PRODUCT;

        @Override public String toString() {
            return name().toLowerCase(Locale.ENGLISH);
        }

    }

    public RExCompound()
    {
        this(null, null, false, false, Collections.<String>emptyList(), Collections.<String>emptyList(), 0, 0);
    }

    public RExCompound(String id,
                       Type type,
                       boolean isInSeed,
                       boolean isInBranch,
                       List<String> alternativePathways,
                       List<String> otherPathways,
                       double extraction,
                       double relevance)
    {
        this.id = id;
        this.type = type;
        this.isInSeed = isInSeed;
        this.isInBranch = isInBranch;
        this.alternativePathways = alternativePathways;
        this.otherPathways = otherPathways;
        this.extraction = extraction;
        this.relevance = relevance;
    }

    public String getID()
    {
        return id;
    }

    public Type getType()
    {
        return type;
    }

    public boolean isInSeed()
    {
        return isInSeed;
    }

    public boolean isInBranch()
    {
        return isInBranch;
    }

    public List<String> getAlternativePathways()
    {
        return alternativePathways;
    }

    public List<String> getOtherPathways()
    {
        return otherPathways;
    }

    public double getExtraction()
    {
        return extraction;
    }

    public double getRelevance()
    {
        return relevance;
    }

    @Override
    public RExCompound newInstance()
    {
        return new RExCompound();
    }
}
