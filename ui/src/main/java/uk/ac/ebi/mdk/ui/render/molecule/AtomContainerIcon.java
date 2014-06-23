/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
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

package uk.ac.ebi.mdk.ui.render.molecule;

import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.elements.ElementGroup;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.List;

import static org.openscience.cdk.renderer.generators.BasicSceneGenerator.Scale;

/**
 * Displays a structure (CDK AtomContainer) in a resizable icon. The icon
 * is drawn transparent and the background should be set in Swing separately.
 *
 * @author John May
 */
public class AtomContainerIcon extends ImageIcon {

    private final RendererModel                    rendererModel;
    private final List<IGenerator<IAtomContainer>> generators;
    private final AtomContainerRenderer            renderer;
    private final Rectangle2D                      modelBounds;
    private final ElementGroup                     diagram;
    private final double                           scale;
    
    /**
     * Create an icon for the given container. 
     * 
     * @param container structure
     * @param coloring type of colouring
     */
    public AtomContainerIcon(final IAtomContainer container, final Coloring coloring) {

        if (container == null)
            throw new IllegalArgumentException("no container provided");
        for (IAtom atm : container.atoms())
            if (atm.getPoint2d() == null)
                throw new IllegalArgumentException("no 2D depiction");

        this.generators = Arrays.asList(new BasicSceneGenerator(),
                                        new SmoothGenerator(coloring == Coloring.CPK,
                                                            coloring == Coloring.WHITE ? Color.WHITE
                                                                                       : new Color(0x444444)
                                        )
                                       );
        this.renderer = new AtomContainerRenderer(generators,
                                                  new AWTFontManager());
        this.rendererModel = renderer.getRenderer2DModel();

        if (container.getBondCount() > 0 || container.getAtomCount() == 1) {
            scale = renderer.calculateScaleForBondLength(GeometryTools.getBondLengthAverage(container));
        }
        else if (container.getAtomCount() > 1) {
            scale = renderer.calculateScaleForBondLength(estimatedBondLength(container));
        }
        else {
            throw new IllegalStateException();
        }

        rendererModel.set(Scale.class, scale);

        diagram = new ElementGroup();
        for (IGenerator<IAtomContainer> generator : generators) {
            diagram.add(generator.generate(container, rendererModel));
        }
        modelBounds = renderer.getBounds(diagram);
    }
    
    public Rectangle bounds() {
        return new Rectangle((int) modelBounds.getWidth(), (int) modelBounds.getHeight());
    }

    /**
     * Draw the structure (provided in the constructor) fitted to the given
     * bounds.
     *
     * @param g2     java 2D graphics
     * @param bounds bounds
     */
    public void render(Graphics2D g2, Rectangle bounds) {

        double scale = rendererModel.getParameter(Scale.class).getValue();

        double drawWidth = bounds.getWidth();
        double drawHeight = bounds.getHeight();

        double diagramWidth = modelBounds.getWidth() * scale;
        double diagramHeight = modelBounds.getHeight() * scale;

        double margin = 10;
        double margin2 = margin * 2;

        double zoom = Math.min(drawWidth / (diagramWidth + margin2), drawHeight / (diagramHeight + margin2));

        AffineTransform transform = new AffineTransform();
        transform.translate(bounds.getCenterX(), bounds.getCenterY());
        transform.scale(1, -1); // Converts between CDK Y-up & Java2D Y-down coordinate-systems
        transform.scale(scale, scale);
        transform.scale(zoom, zoom);
        transform.translate(-this.modelBounds.getCenterX(), -this.modelBounds.getCenterY());

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        AWTDrawVisitor awtDV = new AWTDrawVisitor(g2);
        awtDV.setTransform(transform);
        awtDV.visit(diagram);
    }

    /** @inheritDoc */
    @Override public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        Insets insets = ((Container) c).getInsets();
        x = insets.left;
        y = insets.top;

        int w = c.getWidth() - x - insets.right;
        int h = c.getHeight() - y - insets.bottom;

        render((Graphics2D) g, new Rectangle(w, h));
    }

    /** @inheritDoc */
    @Override public int getIconWidth() {
        return 0;
    }

    /** @inheritDoc */
    @Override public int getIconHeight() {
        return 0;
    }

    // required to scale disconnected structures, copied from AtomContainerRenderer
    private static double estimatedBondLength(IAtomContainer container) {

        if (container.getBondCount() > 0)
            throw new IllegalArgumentException("structure has at least one bond - disconnected scaling not need");
        if (container.getAtomCount() < 2)
            throw new IllegalArgumentException("structure must have at least two atoms");

        int nAtoms = container.getAtomCount();
        double minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < nAtoms; i++)
            for (int j = i + 1; j < nAtoms; j++)
                minDistance = Math.min(container.getAtom(i).getPoint2d().distance(container.getAtom(j).getPoint2d()), minDistance);

        return minDistance / 1.5; // non-bonded, if they were they would be closer
    }
}
