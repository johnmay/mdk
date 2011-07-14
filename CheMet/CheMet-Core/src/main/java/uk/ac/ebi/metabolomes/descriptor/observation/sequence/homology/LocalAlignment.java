/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package uk.ac.ebi.metabolomes.descriptor.observation.sequence.homology;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.text.Highlighter.Highlight;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;
import uk.ac.ebi.metabolomes.identifier.GenericIdentifier;
import uk.ac.ebi.metabolomes.identifier.IdentifierFactory;
import uk.ac.ebi.metabolomes.identifier.UniProtIdentifier;
import uk.ac.ebi.metabolomes.descriptor.observation.AbstractObservation;

/**
 * LocalAlignment.java
 * Observation of local alignment with this protein
 *
 * @author johnmay
 * @date Apr 6, 2011
 */
public abstract class LocalAlignment extends AbstractObservation {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( LocalAlignment.class );
    private static final long serialVersionUID = -6587018434706846674L;
    // Hit_id and Hit_def
    private String hitId;
    private List<AbstractIdentifier> hitIdentifiers;
    private String hitDefinition;
    private String alignment;
    private String sequence;
    // e value and bit score
    private Double expectedValue;
    private Double bitScore;
    private Integer[] queryRange;
    private Integer[] hitRange;
    private Integer hitLength;
    private Integer positive;
    private Integer identity;
    private Integer alignLength;

    public Integer getHitLength() {
        return hitLength;
    }

    public Integer getAlignmentLength() {
        return alignLength;
    }

    public void setAlignmentLength( Integer gaps ) {
        this.alignLength = gaps;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity( Integer identity ) {
        this.identity = identity;
    }

    public Integer getPositive() {
        return positive;
    }

    public void setPositive( Integer positive ) {
        this.positive = positive;
    }

    public Integer[] getHitRange() {
        return hitRange;
    }

    public void setHitRange( Integer[] hitRange ) {
        this.hitRange = hitRange;
    }

    public Integer[] getQueryRange() {
        return queryRange;
    }

    public void setQueryRange( Integer[] queryRange ) {
        this.queryRange = queryRange;
    }

    public void setHitLength( Integer len ) {
        this.hitLength = len;
    }



    @Override
    public String getObservationName() {
        return hitId;
    }

    public LocalAlignment() {
    }

    public LocalAlignment( AbstractIdentifier hitIdentifier ,
                           String hitDef ,
                           String alignment ,
                           String sequence ,
                           Double expectedValue ,
                           Double bitScore ) {
        this.hitIdentifiers.add( hitIdentifier );
        this.hitDefinition = hitDef;
        this.alignment = alignment;
        this.sequence = sequence;
        this.expectedValue = expectedValue;
        this.bitScore = bitScore;
    }

    public String getIdString() {
        return hitId;
    }

    public void setHitIdString( String hitId ) {
        this.hitId = hitId;
    }

    public List<AbstractIdentifier> getIdentifiers() {
        if ( hitIdentifiers == null ) {
            hitIdentifiers = IdentifierFactory.getIdentifiers( hitId );
        }
        return Collections.unmodifiableList( hitIdentifiers );
    }

    /**
     * Returns the first UniProt identifier in the identifier list or
     * null if none is found
     * @return UniProtIdentifier
     */
    public UniProtIdentifier getUniProtIdentifier() {
        if ( hitIdentifiers == null ) {
            getIdentifiers();
        }
        for ( AbstractIdentifier abstractIdentifier : hitIdentifiers ) {
            if ( abstractIdentifier instanceof UniProtIdentifier ) {
                return ( UniProtIdentifier ) abstractIdentifier;
            }
        }
        return null;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment( String alignment ) {
        this.alignment = alignment;
    }

    public Double getBitScore() {
        return bitScore;
    }

    public void setBitScore( double bitScore ) {
        this.bitScore = bitScore;
    }

    public String getDefinition() {
        return hitDefinition;
    }

    public void setDefinition( String def ) {
        this.hitDefinition = def;
    }

    public Double getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue( double expectedValue ) {
        this.expectedValue = expectedValue;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence( String sequence ) {
        this.sequence = sequence;
    }

    /**
     *
     * @param width
     * @param height
     * @return
     * @deprecated See new CheMet-visualisation class AlignmentRenderer
     */
    @Override
    @Deprecated
    public BufferedImage getObservationImage( int width , int height ) {

        BufferedImage img = new BufferedImage( width , height , BufferedImage.TYPE_4BYTE_ABGR );

        float halfheight = height / 2;
        float hitBarHeight = height / 2;
        float hitBarCorner = 0;

        Graphics2D g2 = ( Graphics2D ) img.getGraphics();
        if ( isHighlighted() ) {
            g2.setColor( getBackground() );
            g2.fillRect( 0 , 0 , width , height );
        }
        g2.setColor( Color.GRAY );
        g2.drawLine( 0 , ( int ) halfheight , width , ( int ) halfheight );
        int bitscore = ( int ) getBitScore().doubleValue();
        g2.setColor( BlastScoreColor.getColorForScore( bitscore ).getColor() );

        // normalise length by the total length of the sequence
        Integer[] homologyRange = getQueryRange();

        float seqLength = getProduct().getSequence().length();
        int homologyStart = ( int ) ( width * ( ( float ) homologyRange[0] / ( float ) seqLength ) );
        int homologyEnd = ( int ) ( width * ( ( float ) homologyRange[1] / ( float ) seqLength ) );
        float hitBarX = homologyStart;
        float hitBarY = ( ( float ) height - hitBarHeight ) / 2;
        float hitBarWidth = homologyEnd - homologyStart;

        RoundRectangle2D.Float hitBar = new RoundRectangle2D.Float( hitBarX ,
                                                                    hitBarY ,
                                                                    hitBarWidth ,
                                                                    hitBarHeight ,
                                                                    hitBarCorner ,
                                                                    hitBarCorner );
        g2.fill( hitBar );
        g2.dispose();

        return img;
    }

    @Override
    public String getObservationDescription() {
        String hitDef = hitDefinition;
        return "<html>" + ( ( hitDef.length() > MAX_TOOLTIP_DEFINITION_LENGTH ) ? hitDef.substring( 0 , MAX_TOOLTIP_DEFINITION_LENGTH ) : hitDef ) + "<br>"
               + "E:  " + getExpectedValue() + "<br>"
               + "S': " + getBitScore() + "<br>"
               + "Query Range: " + Arrays.asList( getQueryRange() ) + "<br>"
               + "Hit Range: " + Arrays.asList( getHitRange() )
               + "</html>";
    }
    // to stop ultra long names in blast hits
    public static final int MAX_TOOLTIP_DEFINITION_LENGTH = 75;
}
