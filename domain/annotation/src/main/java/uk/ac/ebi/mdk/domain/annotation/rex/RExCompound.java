package uk.ac.ebi.mdk.domain.annotation.rex;

import uk.ac.ebi.mdk.domain.annotation.AbstractAnnotation;
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.lang.annotation.Description;

import java.util.*;

@Context(Reaction.class)
@Brief("REx Compound")
@Description("Text-mining information for a compound in the reaction")
public class RExCompound extends AbstractAnnotation
{
    private final String id;
    private final Type type;
    private final boolean isInBRENDA, isInSeed;
    private final Map<String, String> alternativePathways, otherPathways;
    private Map<String, Integer> branchLengths;
    private Map<String, Double> branchScores;
    private double extraction, relevance;

    public static enum Type {
        SUBSTRATE,
        PRODUCT;

        @Override public String toString() {
            return name().toLowerCase(Locale.ENGLISH);
        }

    }

    public RExCompound()
    {
        this(null, null, false, false, Collections.<String, String>emptyMap(), Collections.<String, String>emptyMap(), Collections.<String, Integer>emptyMap(), Collections.<String, Double>emptyMap(), 0, 0);
    }

    public RExCompound(String id,
                       Type type,
                       boolean isInBRENDA,
                       boolean isInSeed,
                       Map<String, String> alternativePathways,
                       Map<String, String> otherPathways,
                       Map<String, Integer> branchLengths,
                       Map<String, Double> branchScores,
                       double extraction,
                       double relevance)
    {
        this.id = id;
        this.type = type;
        this.isInBRENDA = isInBRENDA;
        this.isInSeed = isInSeed;
        this.alternativePathways = alternativePathways;
        this.otherPathways = otherPathways;
        this.branchLengths = branchLengths;
        this.branchScores = branchScores;
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

    public boolean isInBRENDA()
    {
        return isInBRENDA;
    }

    public boolean isInSeed()
    {
        return isInSeed;
    }

    public Map<String, String> getAlternativePathways()
    {
        return alternativePathways;
    }

    public Map<String, String> getOtherPathways()
    {
        return otherPathways;
    }

    public Map<String, Integer> getBranchLengths()
    {
        return branchLengths;
    }

    public Map<String, Double> getBranchScores()
    {
        return branchScores;
    }

    public void addBranch(String id, int length, double score)
    {
        branchLengths.put(id, length);
        branchScores.put(id, score);
    }

    public void setExtraction(double score)
    {
        extraction = score;
    }

    public double getExtraction()
    {
        return extraction;
    }

    public void setRelevance(double score)
    {
        relevance = score;
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
