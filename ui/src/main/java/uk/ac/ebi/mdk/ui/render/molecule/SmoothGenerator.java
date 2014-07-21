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

package uk.ac.ebi.mdk.ui.render.molecule;

import com.google.common.primitives.Doubles;
import org.openscience.cdk.config.Isotopes;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.graph.Cycles;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IRingSet;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.elements.Bounds;
import org.openscience.cdk.renderer.elements.ElementGroup;
import org.openscience.cdk.renderer.elements.GeneralPath;
import org.openscience.cdk.renderer.elements.IRenderingElement;
import org.openscience.cdk.renderer.elements.LineElement;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.generators.IGeneratorParameter;
import org.openscience.cdk.ringsearch.RingSearch;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;
import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** @author John May */
public class SmoothGenerator implements IGenerator<IAtomContainer> {

    private final static boolean DISPLAY_TERMINAL_CARBONS = true;

    private final static Font font = new Font("Verdana", Font.PLAIN, 18);

    private static final FontRenderContext FONT_RENDER_CONTEXT = new FontRenderContext(new AffineTransform(), true, false);

    private static final Color DEFAULT_FG = new Color(0x444444);
    private final Color fg;
    private static final Color debug = new Color(0xFF6666);

    private boolean coloured;

    public SmoothGenerator(boolean coloured, Color fg) {
        this.coloured = coloured;
        String[] symbols = "Se, F, C, As, N, O, H, I, K, –, Cl, P, Co, 2+, S, R, Hg, •, Br, +, 3–, ., 2, Mg, 4–".split(", ");
        for (String text : symbols)
            obtainGlyph(text);
        this.fg = fg;
    }

    public SmoothGenerator(boolean coloured) {
        this(coloured, DEFAULT_FG);
    }

    public SmoothGenerator(Color fg) {
        this(false, fg);
    }

    public SmoothGenerator() {
        this(false);
    }

    Color getColor(IAtom atom) {
        if (!coloured)
            return fg;
        switch (atom.getAtomicNumber()) {
            case 7:
                return new Color(0x4F4BFF);
            case 8:
                return new Color(0xFF464F);
            case 9:  // F
            case 17: // Cl
                return new Color(0x76FF5A);
            case 35: // Br
                return new Color(0x972D1D);
            case 53: // I
                return new Color(0x68307F);
            case 15: // P
                return new Color(0xFF8443);
            case 16: // S
                return new Color(0xEFD657);
        }
        return fg;
    }

    Set<String> cachedGlyphs() {
        return glyphs.keySet();
    }

    @Override public List<IGeneratorParameter<?>> getParameters() {
        return Collections.emptyList();
    }

    @Override public IRenderingElement generate(IAtomContainer container, RendererModel model) {
        ElementGroup group = new ElementGroup();

        Double scale = model.get(BasicSceneGenerator.Scale.class);

        Area[] filled = new Area[container.getAtomCount()];
        Path2D[] symbols = new Path2D[container.getAtomCount()];

        for (IAtom atom : container.atoms()) {

            int atNum = container.getAtomNumber(atom);

            if (!displaySymbol(container, atom))
                continue;

            Shape shape = obtainGlyph(atom.getSymbol());

            double x = atom.getPoint2d().x;
            double y = atom.getPoint2d().y;
            Rectangle2D bounds = shape.getBounds2D();
            AffineTransform at = AffineTransform.getTranslateInstance(x - (bounds.getX() / scale)
                                                                              - ((bounds.getWidth() / scale) / 2),
                                                                      y + ((bounds.getY() + bounds.getHeight()) / scale)
                                                                              - ((bounds.getHeight() / scale) / 2)
                                                                     );
            at.scale(1 / scale,
                     1 / -scale);

            Shape symbol = at.createTransformedShape(shape);

            filled[atNum] = new Area(getConvexHullPath2D(symbol));

            //group.add(GeneralPath.filledPathOf(cvxhul, debug)); // optional
            symbols[atNum] = new Path2D.Double();
            symbols[atNum].append(symbol.getPathIterator(new AffineTransform()), false);

            // place hydrogens
            int hPlacement = hPlacement(container, x, y, atom, group);

            Shape hydrogenLabel = new Area(toShape("H", x, y, scale));

            double wS = symbol.getBounds2D().getWidth();
            double hS = symbol.getBounds2D().getHeight();
            double wH = hydrogenLabel.getBounds2D().getWidth();
            double wH2 = hydrogenLabel.getBounds2D().getWidth();
            double hH = hydrogenLabel.getBounds2D().getHeight();

            hydrogenLabel = AffineTransform.getTranslateInstance(-((symbol.getBounds2D().getWidth() / 2) - (wH / 2)),
                                                                 0)
                                           .createTransformedShape(hydrogenLabel);

            if (atom.getImplicitHydrogenCount() != null && atom.getImplicitHydrogenCount() > 1) {
                Shape hCount = toShape(Integer.toString(atom.getImplicitHydrogenCount()), x, y, scale);
                hCount = AffineTransform.getScaleInstance(0.6, 0.6)
                                        .createTransformedShape(hCount);
                Rectangle2D hCountBounds = hCount.getBounds2D();
                hCount = AffineTransform.getTranslateInstance(x - hCountBounds.getX()
                                                                      + ((wH / 2) + (wH * 0.2)),
                                                              y - hCountBounds.getY()
                                                                      - ((hCountBounds.getHeight()) / 2)
                                                                      - (hH / 2))
                                        .createTransformedShape(hCount);
                Area hArea = new Area(hydrogenLabel);
                hArea.add(new Area(hCount));
                hydrogenLabel = hArea;
                wH2 = hydrogenLabel.getBounds2D().getWidth(); // adjust width to do
            }

            double spacing = hS * 0.2;

            switch (hPlacement) {
                case NORTH:
                    hydrogenLabel = AffineTransform.getTranslateInstance(0, hS + (spacing))
                                                   .createTransformedShape(hydrogenLabel);
                    break;
                case NORTH_EAST:
                case EAST:
                case SOUTH_EAST:
                    hydrogenLabel = AffineTransform.getTranslateInstance(wS + (spacing), 0)
                                                   .createTransformedShape(hydrogenLabel);
                    break;
                case SOUTH:
                    hydrogenLabel = AffineTransform.getTranslateInstance(0, -(hH + (spacing)))
                                                   .createTransformedShape(hydrogenLabel);
                    break;
                case NORTH_WEST:
                case WEST:
                case SOUTH_WEST:
                    hydrogenLabel = AffineTransform.getTranslateInstance(-(wH2 + (spacing)), 0)
                                                   .createTransformedShape(hydrogenLabel);
                    break;
                default:
                    hydrogenLabel = null;
                    break;
            }

            int q = atom.getFormalCharge();
            int nRad = 0;
            Shape radicalLabel = null;

            // render radical (only single supported for now)
            if ((nRad = container.getConnectedSingleElectronsCount(atom)) > 0) {
                String text = nRad > 1 ? q != 0 ? "(" + nRad + "\u2022" + ")" : nRad + "\u2022"
                                       : "\u2022";
                radicalLabel = toShape(text, x, y, scale);
                radicalLabel = AffineTransform.getScaleInstance(0.6, 0.6)
                                              .createTransformedShape(radicalLabel);
                Rectangle2D radicalBounds = radicalLabel.getBounds2D();
                radicalLabel = AffineTransform.getTranslateInstance(x - radicalBounds.getX()
                                                                            + ((wS / 2) + (spacing)),
                                                                    y - radicalBounds.getY()
                                                                            - ((radicalBounds.getHeight()) / 2)
                                                                            + (hS / 2))
                                              .createTransformedShape(radicalLabel);
                if (hPlacement == EAST) {
                    radicalLabel = AffineTransform.getTranslateInstance(wH + (spacing),
                                                                        0)
                                                  .createTransformedShape(radicalLabel);
                }
                else if (hPlacement == NORTH && atom.getImplicitHydrogenCount() > 1) {
                    System.err.println("H subscript collides with radical!");
                }

                filled[container.getAtomNumber(atom)].add(new Area(getConvexHullPath2D(radicalLabel)));
                symbols[atNum].append(radicalLabel.getPathIterator(new AffineTransform()), false);
            }

            // render charge

            if (q != 0) {
                String text = q > 0 ? "+" : "\u2013";
                if (Math.abs(q) > 1)
                    text = Math.abs(q) + text;

                Shape chargeLabel = toShape(text, x, y, scale);
                chargeLabel = AffineTransform.getScaleInstance(0.6, 0.6)
                                             .createTransformedShape(chargeLabel);
                Rectangle2D chareBounds = chargeLabel.getBounds2D();
                chargeLabel = AffineTransform.getTranslateInstance(x - chareBounds.getX()
                                                                           + ((wS / 2) + (spacing)),
                                                                   y - chareBounds.getY()
                                                                           - ((chareBounds.getHeight()) / 2)
                                                                           + (hS / 2))
                                             .createTransformedShape(chargeLabel);
                // move charge over to the side of the H / radical
                if (hPlacement == EAST) {
                    chargeLabel = AffineTransform.getTranslateInstance(wH + (spacing),
                                                                       0)
                                                 .createTransformedShape(chargeLabel);
                }
                else if (hPlacement == NORTH && atom.getImplicitHydrogenCount() > 1) {
                    System.err.println("H subscript collides with charge!");
                }

                if (radicalLabel != null) {
                    chargeLabel = AffineTransform.getTranslateInstance(radicalLabel.getBounds2D().getWidth() + (spacing),
                                                                       0)
                                                 .createTransformedShape(chargeLabel);
                }

                filled[atNum].add(new Area(getConvexHullPath2D(chargeLabel)));
                symbols[atNum].append(chargeLabel.getPathIterator(new AffineTransform()), false);
            }

            Integer mass = atom.getMassNumber();
            if (renderMass(mass, atom.getAtomicNumber())) {
                String text = mass.toString();
                Shape massLabel = toShape(text, x, y, scale);
                massLabel = AffineTransform.getScaleInstance(0.6, 0.6)
                                           .createTransformedShape(massLabel);
                Rectangle2D massBounds = massLabel.getBounds2D();
                massLabel = AffineTransform.getTranslateInstance(x - massBounds.getX()
                                                                         - ((wS / 2) + (spacing))
                                                                         - massBounds.getWidth(),
                                                                 y - massBounds.getY()
                                                                         - ((massBounds.getHeight()) / 2)
                                                                         + (hS / 2))
                                           .createTransformedShape(massLabel);
                // move H label placement
                if (hPlacement == WEST) {
                    hydrogenLabel = AffineTransform.getTranslateInstance(-(massBounds.getWidth()
                            - (wH2 - wH) // adjust for subscript
                            + spacing
                    ),
                                                                         0)
                                                   .createTransformedShape(hydrogenLabel);
                }

                filled[container.getAtomNumber(atom)].add(new Area(getConvexHullPath2D(massLabel)));
                symbols[atNum].append(massLabel.getPathIterator(new AffineTransform()), false);

            }

            if (hydrogenLabel != null) {
                filled[container.getAtomNumber(atom)].add(new Area(getConvexHullPath2D(hydrogenLabel)));
                symbols[atNum].append(hydrogenLabel.getPathIterator(new AffineTransform()), false);
            }

            // group.add(GeneralPath.filledPathOf(filled[container.getAtomNumber(atom)], debug));

        }

        double bondWidthRatio = 1;
        double bondWidth = bondWidthRatio * toShape("i", 5, 5, scale).getBounds2D().getWidth();
        double bondSeparation = 4;

        RingSearch ringSearch = new RingSearch(container);
        IRingSet ringSet = Cycles.mcb(container).toRingSet();

        ringSet.sortAtomContainers(new Comparator<IAtomContainer>() {
            @Override public int compare(IAtomContainer a, IAtomContainer b) {

                int aSize = a.getAtomCount();
                int bSize = b.getAtomCount();

                // IUPAC say 6 member rings take precedence
                if (aSize == 6)
                    aSize = 2;
                if (bSize == 6)
                    bSize = 2;

                if (aSize > bSize)
                    return +1;
                if (aSize < bSize)
                    return -1;

                Point2d p1 = GeometryTools.get2DCenter(a);
                Point2d p2 = GeometryTools.get2DCenter(b);

                if (p1.x > p2.x)
                    return +1;
                if (p1.x < p2.x)
                    return -1;
                if (p1.y > p2.y)
                    return +1;
                if (p1.y < p2.y)
                    return -1;

                return 0;
            }
        });

        for (IBond bond : container.bonds()) {

            IAtom a1 = bond.getAtom(0);
            IAtom a2 = bond.getAtom(1);

            final int u = container.getAtomNumber(a1);
            final int v = container.getAtomNumber(a2);

            Point2d p1 = a1.getPoint2d();
            Point2d p2 = a2.getPoint2d();

            double x1 = a1.getPoint2d().x;
            double x2 = a2.getPoint2d().x;
            double y1 = a1.getPoint2d().y;
            double y2 = a2.getPoint2d().y;

            double dx = x2 - x1;
            double dy = y2 - y1;

            double mag = Math.sqrt((dx * dx) + (dy * dy));

            dx /= mag;
            dy /= mag;

            double pad = 1.5 * bondWidth;

            if (filled[u] != null) {
                Point2D p = getMinIntersection(filled[u], new Line2D.Double(x1, y1, x2, y2));
                if (p != null) {
                    x1 = p.getX();
                    y1 = p.getY();
                    x1 += dx * pad;
                    y1 += dy * pad;
                }
            }

            if (filled[v] != null) {
                Point2D p = getMinIntersection(filled[v], new Line2D.Double(x1, y1, x2, y2));
                if (p != null) {
                    x2 = p.getX();
                    y2 = p.getY();
                    x2 -= dx * pad;
                    y2 -= dy * pad;
                }
            }

            double bondSep = (bondSeparation / scale);

            int a1count = container.getConnectedAtomsCount(a1);
            int a2count = container.getConnectedAtomsCount(a2);


            switch (bond.getOrder()) {
                case SINGLE:

                    // orthogonal unit vector
                    double dox = (x1 + dy) - x1;
                    double doy = (y1 - dx) - y1;
                    double omag = Math.sqrt((dox * dox) + (doy * doy));

                    dox /= omag;
                    doy /= omag;


                    double thinEnd = 0.25 * bondSep;
                    double fatEnd = 1.5 * bondSep;
                    double step = mag / 12;

                    double end = magnitude(p1.x, p1.y, x2, y2);
                    double start = magnitude(p1.x, p1.y, x1, y1);

                    double x3 = p2.x - dox * fatEnd;
                    double y3 = p2.y - doy * fatEnd;
                    double x4 = p2.x + dox * fatEnd;
                    double y4 = p2.y + doy * fatEnd;

                    double dx3 = x3 - p1.x;
                    double dy3 = y3 - p1.y;
                    double dx4 = x4 - p1.x;
                    double dy4 = y4 - p1.y;

                    double mag3 = Math.sqrt((dx3 * dx3) + (dy3 * dy3));
                    double mag4 = Math.sqrt((dx4 * dx4) + (dy4 * dy4));


                    // wedge
                    if (bond.getStereo() == IBond.Stereo.UP) {
                        Path2D path = new Path2D.Double();

                        double theta = Math.acos(mag / mag3);
                        double fact = Math.tan(theta);

                        Point2D p3 = new Point2D.Double(p1.x + dx * end - dox * (end * fact),
                                                        p1.y + dy * end - doy * (end * fact));
                        Point2D p4 = new Point2D.Double(p1.x + dx * end + dox * (end * fact),
                                                        p1.y + dy * end + doy * (end * fact));
                        Point2D p5 = p3;
                        Point2D p6 = p4;

                        double magPlus = 2 * mag;

                        Line2D.Double l1 = new Line2D.Double(p1.x, p1.y,
                                                             p1.x + dx * magPlus - dox * magPlus * fact,
                                                             p1.y + dy * magPlus - doy * magPlus * fact);
                        Line2D.Double l2 = new Line2D.Double(p1.x, p1.y,
                                                             p1.x + dx * magPlus + dox * magPlus * fact,
                                                             p1.y + dy * magPlus + doy * magPlus * fact);

                        if (filled[v] == null && a2count > 1 && !ringSearch.cyclic(u, v)) {


                            // debug
                            //group.add(new LineElement(l1.x1, l1.y1, l1.x2, l1.y2, bondWidth, debug));
                            //group.add(new LineElement(l2.x1, l2.y1, l2.x2, l2.y2, bondWidth, debug));

                            for (IBond b2 : container.getConnectedBondsList(a2)) {
                                if (b2 == bond || b2.getOrder() != IBond.Order.SINGLE)
                                    continue;
                                Line2D.Double line = toLine(b2);
                                if (l1.intersectsLine(line)) {
                                    Point2D intersect = getIntersection(l1, line);
                                    if (p5 == p3 || magnitude(p1.x, p1.y, intersect.getX(), intersect.getY()) < magnitude(p1.x, p1.y, p5.getX(), p5.getY()))
                                        p5 = intersect;
                                }
                                else if (l2.intersectsLine(line)) {
                                    Point2D intersect = getIntersection(l2, line);
                                    if (p6 == p4 || magnitude(p1.x, p1.y, intersect.getX(), intersect.getY()) < magnitude(p1.x, p1.y, p6.getX(), p6.getY()))
                                        p6 = intersect;
                                }
                            }
                        }

                        boolean bifurcated = false;
                        if (p5 != p3 && p6 != p4) {
                            bifurcated = true;
                        }
                        else if (p5 != p3) {

                            double slantDx = p5.getX() - x2;
                            double slantDy = p5.getY() - y2;
                            double slantMag = Math.sqrt((slantDx * slantDx) + (slantDy * slantDy));
                            slantDx /= slantMag;
                            slantDy /= slantMag;

                            Point2D intersect = getIntersection(l2,
                                                                new Line2D.Double(p5.getX(), p5.getY(),
                                                                                  p5.getX() + slantDx * 2 * slantMag,
                                                                                  p5.getY() + slantDy * 2 * slantMag));
                            p5 = adjustP2(new Point2D.Double(p1.x, p1.y), p5, 0.6 * bondWidth);
                            p6 = adjustP2(new Point2D.Double(p1.x, p1.y), intersect, 0.6 * bondWidth);
                        }
                        else if (p6 != p4) {
                            double slantDx = p6.getX() - x2;
                            double slantDy = p6.getY() - y2;
                            double slantMag = Math.sqrt((slantDx * slantDx) + (slantDy * slantDy));
                            slantDx /= slantMag;
                            slantDy /= slantMag;

                            Point2D intersect = getIntersection(l1,
                                                                new Line2D.Double(p6.getX(), p6.getY(),
                                                                                  p6.getX() + slantDx * 2 * slantMag,
                                                                                  p6.getY() + slantDy * 2 * slantMag));
                            p5 = adjustP2(new Point2D.Double(p1.x, p1.y), intersect, 0.6 * bondWidth);
                            p6 = adjustP2(new Point2D.Double(p1.x, p1.y), p6, 0.6 * bondWidth);
                        }

                        path.moveTo(x1 + dox * (start * fact),
                                    y1 + doy * (start * fact));
                        path.lineTo(x1 - dox * (start * fact),
                                    y1 - doy * (start * fact));
                        path.lineTo(p5.getX(), p5.getY());
                        if (bifurcated)
                            path.lineTo(x2, y2);
                        path.lineTo(p6.getX(), p6.getY());

                        path.closePath();
                        group.add(GeneralPath.shapeOf(path, fg));
                    }
                    else if (bond.getStereo() == IBond.Stereo.DOWN) {

                        double theta = Math.acos(mag / mag3);
                        double fact = Math.tan(theta);

                        Point2D p3 = new Point2D.Double(p1.x + dx * end - dox * (end * fact),
                                                        p1.y + dy * end - doy * (end * fact));
                        Point2D p4 = new Point2D.Double(p1.x + dx * end + dox * (end * fact),
                                                        p1.y + dy * end + doy * (end * fact));
                        Point2D p5 = p3;
                        Point2D p6 = p4;

                        double magPlus = 2 * mag;

                        Line2D.Double l1 = new Line2D.Double(p1.x, p1.y,
                                                             p1.x + dx * magPlus - dox * magPlus * fact,
                                                             p1.y + dy * magPlus - doy * magPlus * fact);
                        Line2D.Double l2 = new Line2D.Double(p1.x, p1.y,
                                                             p1.x + dx * magPlus + dox * magPlus * fact,
                                                             p1.y + dy * magPlus + doy * magPlus * fact);

                        if (filled[v] == null && a2count == 2) {


                            // debug
                            //group.add(new LineElement(l1.x1, l1.y1, l1.x2, l1.y2, bondWidth, debug));
                            //group.add(new LineElement(l2.x1, l2.y1, l2.x2, l2.y2, bondWidth, debug));

                            for (IBond b2 : container.getConnectedBondsList(a2)) {
                                if (b2 == bond || b2.getOrder() != IBond.Order.SINGLE)
                                    continue;
                                Line2D.Double line = toLine(b2);
                                if (l1.intersectsLine(line)) {
                                    Point2D intersect = getIntersection(l1, line);
                                    if (p5 == p3 || magnitude(p1.x, p1.y, intersect.getX(), intersect.getY()) < magnitude(p1.x, p1.y, p5.getX(), p5.getY()))
                                        p5 = intersect;
                                }
                                else if (l2.intersectsLine(line)) {
                                    Point2D intersect = getIntersection(l2, line);
                                    if (p6 == p4 || magnitude(p1.x, p1.y, intersect.getX(), intersect.getY()) < magnitude(p1.x, p1.y, p6.getX(), p6.getY()))
                                        p6 = intersect;
                                }
                            }
                        }

                        if (p5 != p3 && p6 != p4) {
                            p5 = p3; // bifurcated -> reset
                            p6 = p4;
                        }
                        else if (p5 != p3) {

                            double slantDx = p5.getX() - x2;
                            double slantDy = p5.getY() - y2;
                            double slantMag = Math.sqrt((slantDx * slantDx) + (slantDy * slantDy));
                            slantDx /= slantMag;
                            slantDy /= slantMag;

                            Point2D intersect = getIntersection(l2,
                                                                new Line2D.Double(p5.getX(), p5.getY(),
                                                                                  p5.getX() + slantDx * 2 * slantMag,
                                                                                  p5.getY() + slantDy * 2 * slantMag));
                            p6 = intersect;
                        }
                        else if (p6 != p4) {
                            double slantDx = p6.getX() - x2;
                            double slantDy = p6.getY() - y2;
                            double slantMag = Math.sqrt((slantDx * slantDx) + (slantDy * slantDy));
                            slantDx /= slantMag;
                            slantDy /= slantMag;

                            Point2D intersect = getIntersection(l1,
                                                                new Line2D.Double(p6.getX(), p6.getY(),
                                                                                  p6.getX() + slantDx * 2 * slantMag,
                                                                                  p6.getY() + slantDy * 2 * slantMag));
                            p5 = intersect;
                        }

                        int nHatchSection = (int) (mag / bondWidth);

                        if (nHatchSection % 3 != 0)
                            nHatchSection -= nHatchSection % 3;

                        Path2D path = new Path2D.Double();

                        mag3 = magnitude(p1.x, p1.y, p5.getX(), p5.getY());
                        mag4 = magnitude(p1.x, p1.y, p6.getX(), p6.getY());

                        dx3 = (p5.getX() - p1.x) / mag3;
                        dy3 = (p5.getY() - p1.y) / mag3;
                        dx4 = (p6.getX() - p1.x) / mag4;
                        dy4 = (p6.getY() - p1.y) / mag4;

//                        group.add(new OvalElement(p5.getX(), p5.getY(), 0.03, Color.blue));
//                        group.add(new OvalElement(p6.getX(), p6.getY(), 0.03, Color.blue));
//                        group.add(new LineElement(p1.x, p1.y,
//                                                  p1.x + dx3 * mag3,
//                                                  p1.y + dy3 * mag3, bondWidth, debug));
//                        group.add(new LineElement(p1.x, p1.y,
//                                                  p1.x + dx4 * mag4,
//                                                  p1.y + dy4 * mag4, bondWidth, debug));
//                        
                        mag /= nHatchSection;
                        mag3 /= nHatchSection;
                        mag4 /= nHatchSection;

//                        group.add(new LineElement(p1.x, p1.y, p1.x + dx * end, p1.y + dy * end, bondWidth, debug));

                        for (int i = 2; i <= nHatchSection; i += 3) {
                            if ((i - 1) * mag > start) {
                                path.moveTo(p1.x + dx3 * (i + 1) * mag3,
                                            p1.y + dy3 * (i + 1) * mag3);
                                path.lineTo(p1.x + dx4 * (i + 1) * mag4,
                                            p1.y + dy4 * (i + 1) * mag4);
                            }
//                            else if ((i + 1) * mag >= start && (i + 1) * mag <= end) {
//                                path.moveTo(p1.x + dox * start * fact + dx * start,
//                                            p1.y + doy * start * fact + dy * start);
//                                path.lineTo(p1.x - dox * start * fact + dx * start,
//                                            p1.y - doy * start * fact + dy * start);
//                                path.lineTo(p1.x - dox * (i + 1) * mag * fact + dx * ((i + 1) * mag),
//                                            p1.y - doy * (i + 1) * mag * fact + dy * ((i + 1) * mag));
//                                path.lineTo(p1.x + dox * (i + 1) * mag * fact + dx * ((i + 1) * mag),
//                                            p1.y + doy * (i + 1) * mag * fact + dy * ((i + 1) * mag));
//                                path.closePath();
//                            }
//                            else if (i * mag >= start && i * mag <= end) {
//                                path.moveTo(p1.x + dox * (i * mag) * fact + dx * (i * mag),
//                                            p1.y + doy * (i * mag) * fact + dy * (i * mag));
//                                path.lineTo(p1.x - dox * (i * mag) * fact + dx * (i * mag),
//                                            p1.y - doy * (i * mag) * fact + dy * (i * mag));
//                                path.lineTo(p1.x - dox * end * fact + dx * end,
//                                            p1.y - doy * end * fact + dy * end);
//                                path.lineTo(p1.x + dox * end * fact + dx * end,
//                                            p1.y + doy * end * fact + dy * end);
//                                path.closePath();
//                            }
                        }
                        group.add(GeneralPath.outlineOf(path, bondWidth, fg));
                    }
                    else if (bond.getStereo() == IBond.Stereo.UP_OR_DOWN) {

                        ElementGroup subgroup = new ElementGroup();

                        Path2D path = new Path2D.Double();
                        path.moveTo(p1.x, p1.y);
                        double xPeak = dox * bondSep;
                        double yPeak = doy * bondSep;
                        boolean started = false;
                        for (int i = 0; i < 24; i += 2) {
                            double stepMag = ((2 + i) / 2) * step;
                            if (i % 4 == 0) {
                                xPeak *= -1;
                                yPeak *= -1;
                            }
                            if (stepMag < start || stepMag > end)
                                continue;
                            if (i % 4 == 0) {
                                if (!started) {
                                    path.moveTo(p1.x + dx * (i / 2) * step,
                                                p1.y + dy * (i / 2) * step);
                                    started = true;
                                }
                                path.curveTo(p1.x + dx * ((i / 2)) * step + xPeak / 2,
                                             p1.y + dy * ((i / 2)) * step + yPeak / 2,
                                             p1.x + dx * ((1 + i) / 2d) * step + xPeak,
                                             p1.y + dy * ((1 + i) / 2d) * step + yPeak,
                                             p1.x + dx * stepMag + xPeak,
                                             p1.y + dy * stepMag + yPeak);
                            }
                            else {
                                if (!started) {
                                    path.moveTo(p1.x + dx * (i / 2) * step + xPeak,
                                                p1.y + dy * (i / 2) * step + yPeak);
                                    started = true;
                                }
                                path.curveTo(p1.x + dx * ((1 + i) / 2d) * step + xPeak,
                                             p1.y + dy * ((1 + i) / 2d) * step + yPeak,
                                             p1.x + dx * stepMag + (xPeak / 2),
                                             p1.y + dy * stepMag + (yPeak / 2),
                                             p1.x + dx * stepMag,
                                             p1.y + dy * stepMag);
                            }
                        }
                        group.add(GeneralPath.outlineOf(path, bondWidth, fg));
                        group.add(subgroup);
                    }
                    else {
                        group.add(new LineElement(x1, y1, x2, y2, bondWidth, fg));
                    }
                    break;
                case DOUBLE:
                    
                    if (ringSearch.cyclic(u, v)) {
                        group.add(new LineElement(x1, y1, x2, y2, bondWidth, fg));
                        IAtomContainer ring = ringSet.getRings(bond).getAtomContainer(0);

                        Point2d centre = GeometryTools.get2DCenter(ring);

                        double dcx, dcy;

                        // set the side to draw the bond on (the pointing into the ring)
                        if (dot(x1, y1, x1 + dy, y1 - dx, centre.x, centre.y) > 0) {
                            dcx = (x1 + dy) - x1;
                            dcy = (y1 - dx) - y1;
                        }
                        else {
                            dcx = (x1 - dy) - x1;
                            dcy = (y1 + dx) - y1;
                        }

                        double cmag = Math.sqrt((dcx * dcx) + (dcy * dcy));

                        dcx /= cmag;
                        dcy /= cmag;

                        //group.add(new LineElement(x1, y1, y1, -x1, bondWidth, debug));
                        //group.add(new LineElement(x1, y1, -y1, x1, bondWidth, debug));

                        double cx = (p1.x + p2.x) / 2;
                        double cy = (p1.y + p2.y) / 2;

                        // debug
                        //group.add(new OvalElement(centre.x, centre.y, 0.2,  debug));
                        //group.add(new LineElement(cx, cy, cx + dcx, cy + dcy, bondWidth, debug));

                        dcx /= cmag;
                        dcy /= cmag;

                        Point2d p3 = new Point2d();
                        Point2d p4 = new Point2d();

                        if (x1 != p1.x || y1 != p1.y) {
                            p3.x = x1 + dcx * (2 * (bondSeparation / scale));
                            p3.y = y1 + dcy * (2 * (bondSeparation / scale));
                        }
                        else {
                            IAtom other = getOtherConnectedBond(ring, a1, bond).getConnectedAtom(a1);
                            double theta = theta(p1.x, p1.y, p2.x, p2.y, other.getPoint2d().x, other.getPoint2d().y);
                            double opp = (2 * (bondSeparation / scale)) / Math.tan(theta / 2);
                            opp += (bondSeparation / scale) / 2; // improved aesthetics due to rounded caps
                            p3.x = p1.x + dx * opp;
                            p3.y = p1.y + dy * opp;
                            p3.x += dcx * (2 * (bondSeparation / scale));
                            p3.y += dcy * (2 * (bondSeparation / scale));
//                                group.add(new OvalElement(p1.x + dx * opp,
//                                                          p1.y + dy * opp,
//                                                          0.01, debug));
//                                double deg   = Math.toDegrees(theta);
//                                group.add(new TextElement(p1.x, p1.y, String.format("%.2f", deg), debug));
                        }

                        if (x2 != p2.x || y2 != p2.y) {
                            p4.x = x2 + dcx * (2 * (bondSeparation / scale));
                            p4.y = y2 + dcy * (2 * (bondSeparation / scale));
                        }
                        else {
                            IAtom other = getOtherConnectedBond(ring, a2, bond).getConnectedAtom(a2);
                            double theta = theta(p2.x, p2.y, p1.x, p1.y, other.getPoint2d().x, other.getPoint2d().y);
                            double opp = (2 * (bondSeparation / scale)) / Math.tan(theta / 2);
                            opp += (bondSeparation / scale) / 2;
                            p4.x = p2.x - dx * opp;
                            p4.y = p2.y - dy * opp;
                            p4.x += dcx * (2 * (bondSeparation / scale));
                            p4.y += dcy * (2 * (bondSeparation / scale));
//                                group.add(new OvalElement(p2.x - dx * opp,
//                                                          p2.y - dy * opp,
//                                                          0.01, debug));
                        }

                        group.add(new LineElement(p3.x, p3.y, p4.x, p4.y, bondWidth, fg));

                    }
                    else {
                        double dcx, dcy;

                        // set the side to draw the bond on
                        if (a1count == 2) {
                            IAtom other = getOtherConnectedBond(container, a1, bond).getConnectedAtom(a1);
                            if (dot(x1, y1, x1 + dy, y1 - dx, other.getPoint2d().x, other.getPoint2d().y) > 0) {
                                dcx = (x1 + dy) - x1;
                                dcy = (y1 - dx) - y1;
                            }
                            else {
                                dcx = (x1 - dy) - x1;
                                dcy = (y1 + dx) - y1;
                            }
                        }
                        else if (a2count == 2) {
                            IAtom other = getOtherConnectedBond(container, a2, bond).getConnectedAtom(a2);
                            if (dot(x1, y1, x1 + dy, y1 - dx, other.getPoint2d().x, other.getPoint2d().y) > 0) {
                                dcx = (x1 + dy) - x1;
                                dcy = (y1 - dx) - y1;
                            }
                            else {
                                dcx = (x1 - dy) - x1;
                                dcy = (y1 + dx) - y1;
                            }
                        }
                        else {
                            dcx = (x1 - dy) - x1;
                            dcy = (y1 + dx) - y1;
                        }

                        double cmag = Math.sqrt((dcx * dcx) + (dcy * dcy));

                        dcx /= cmag;
                        dcy /= cmag;

                        if (a1count == 2 || a2count == 2) {

                            // cis/trans

                            Point2d p3 = new Point2d();
                            Point2d p4 = new Point2d();

                            IAtom other = a1count > 1 ? getOtherConnectedBond(container, a1, bond).getConnectedAtom(a1) : null;
                            IAtom other2 = a2count > 1 ? getOtherConnectedBond(container, a2, bond).getConnectedAtom(a2) : null;

                            boolean straighten = false;

                            if (x1 != p1.x || y1 != p1.y) {
                                if (other == null && filled[v] == null) { // terminal?
                                    p3.x = x1 + dcx * (2 * (bondSeparation / scale));
                                    p3.y = y1 + dcy * (2 * (bondSeparation / scale));
                                    // shift symbol to be centred on double bond gap
                                    if (symbols[u] != null) {
                                        symbols[u] = new Path2D.Double(AffineTransform.getTranslateInstance(dcx * (bondSeparation / scale),
                                                                                                            dcy * (bondSeparation / scale))
                                                                                      .createTransformedShape(symbols[u]));
                                    }
                                }
                                else {
                                    straighten = true;
                                }
                            }
                            else if (other != null) {
                                double theta = theta(p1.x, p1.y, p2.x, p2.y, other.getPoint2d().x, other.getPoint2d().y);
                                double opp = (2 * (bondSeparation / scale)) / Math.tan(theta / 2);
                                opp += (bondSeparation / scale) / 2; // improved aesthetics due to rounded caps
                                // shallow angle -> draw as symetric
                                straighten = straighten || Math.abs(Math.toDegrees(theta) - 180) < 5;
                                p3.x = p1.x + dx * opp;
                                p3.y = p1.y + dy * opp;
                                p3.x += dcx * (2 * (bondSeparation / scale));
                                p3.y += dcy * (2 * (bondSeparation / scale));
                            }
                            else {
                                p3.x = p1.x;
                                p3.y = p1.y;
                                p3.x += dcx * (2 * (bondSeparation / scale));
                                p3.y += dcy * (2 * (bondSeparation / scale));
                            }

                            if (x2 != p2.x || y2 != p2.y) {
                                if (other2 == null) { // terminal?
                                    p4.x = x2 + dcx * (2 * (bondSeparation / scale));
                                    p4.y = y2 + dcy * (2 * (bondSeparation / scale));
                                    // shift symbol to be centred on double bond gap
                                    if (symbols[v] != null && filled[u] == null) {
                                        symbols[v] = new Path2D.Double(AffineTransform.getTranslateInstance(dcx * (bondSeparation / scale),
                                                                                                            dcy * (bondSeparation / scale))
                                                                                      .createTransformedShape(symbols[v]));
                                    }
                                }
                                else {
                                    straighten = true;
                                }
                            }
                            else if (other2 != null) {
                                double theta = theta(p2.x, p2.y, p1.x, p1.y, other2.getPoint2d().x, other2.getPoint2d().y);
                                double opp = (2 * (bondSeparation / scale)) / Math.tan(theta / 2);
                                opp += (bondSeparation / scale) / 2;
                                // shallow angle -> draw as symetric
                                straighten = straighten || Math.abs(Math.toDegrees(theta) - 180) < 5;
                                p4.x = p2.x - dx * opp;
                                p4.y = p2.y - dy * opp;
                                p4.x += dcx * (2 * (bondSeparation / scale));
                                p4.y += dcy * (2 * (bondSeparation / scale));
                            }
                            else {
                                p4.x = p2.x;
                                p4.y = p2.y;
                                p4.x += dcx * (2 * (bondSeparation / scale));
                                p4.y += dcy * (2 * (bondSeparation / scale));
                            }

                            if (straighten) {
                                createLines(new Point2d(x1, y1), new Point2d(x2, y2), bondWidth, bondSeparation / scale, fg, group);
                            }
                            else {
                                group.add(new LineElement(x1, y1, x2, y2, bondWidth, fg));
                                group.add(new LineElement(p3.x, p3.y, p4.x, p4.y, bondWidth, fg));
                            }
                        }
                        else {

                            double[] out = generateDistanceData(new Point2d(x1, y1),
                                                                new Point2d(x2, y2),
                                                                bondSep);

                            if (a1count == 3 && (x1 == p1.x && y1 == p1.y)) {
                                for (IBond b : container.getConnectedBondsList(a1)) {
                                    Line2D.Double bLine = toLine(b);
                                    if (bLine.intersectsLine(out[0] - dx * mag, out[1] - dy * mag, out[4], out[5])) {
                                        Point2D p = getIntersection(bLine, new Line2D.Double(out[0] - dx * mag, out[1] - dy * mag, out[4], out[5]));
                                        out[0] = p.getX() + dx * 0.5 * bondWidth; // we back off a bit so the stroke doesn't protrude
                                        out[1] = p.getY() + dy * 0.5 * bondWidth;
                                    }
                                    else if (bLine.intersectsLine(out[2] - dx * mag, out[3] - dy * mag, out[6], out[7])) {
                                        Point2D p = getIntersection(bLine, new Line2D.Double(out[2] - dx * mag, out[3] - dy * mag, out[6], out[7]));
                                        out[2] = p.getX() + dx * 0.5 * bondWidth;
                                        out[3] = p.getY() + dy * 0.5 * bondWidth;
                                    }
                                }
                            } else if (x1 != p1.x || y1 != p1.y) {
                                Point2D intersect1 = getMinIntersection(filled[u], new Line2D.Double(out[0] - dx * pad, out[1] - dy * pad, out[4], out[5]));    
                                Point2D intersect2 = getMinIntersection(filled[u], new Line2D.Double(out[2] - dx * pad, out[3] - dy * pad, out[6], out[7]));
                                // if (intersect2 != null) group.add(new OvalElement(intersect2.getX(), intersect2.getY(), 0.2, debug));
                                if (intersect1 != null && intersect2 != null) {
                                    System.err.println("handle");
                                } else if (intersect1 != null) {
                                    double magAdjust = pad + magnitude(out[0], out[1], intersect1.getX(), intersect1.getY());
                                    out[0] += dx * magAdjust;
                                    out[1] += dy * magAdjust;
                                    out[2] += dx * magAdjust;
                                    out[3] += dy * magAdjust;
                                } else if (intersect2 != null) {
                                    double magAdjust = pad + magnitude(out[2], out[3], intersect2.getX(), intersect2.getY());
                                    out[0] += dx * magAdjust;
                                    out[1] += dy * magAdjust;
                                    out[2] += dx * magAdjust;
                                    out[3] += dy * magAdjust;
                                }
                            }
                            
                            if (a2count == 3 && (x2 == p2.x || y2 == p2.y)) {
                                for (IBond b : container.getConnectedBondsList(a2)) {
                                    Line2D.Double bLine = toLine(b);
                                    if (bLine.intersectsLine(out[0], out[1], out[4] + dx * mag, out[5] + dy * mag)) {
                                        Point2D p = getIntersection(bLine, new Line2D.Double(out[0], out[1], out[4] + dx * mag, out[5] + dy * mag));
                                        out[4] = p.getX() - dx * 0.5 * bondWidth;
                                        out[5] = p.getY() - dy * 0.5 * bondWidth;
                                    }
                                    else if (bLine.intersectsLine(out[2], out[3], out[6] + dx * mag, out[7] + dy * mag)) {
                                        Point2D p = getIntersection(bLine, new Line2D.Double(out[2], out[3], out[6] + dx * mag, out[7] + dy * mag));
                                        out[6] = p.getX() - dx * 0.5 * bondWidth;
                                        out[7] = p.getY() - dy * 0.5 * bondWidth;
                                    }
                                }
                            } else if (x2 != p2.x || y2 != p2.y) {
                                Point2D intersect1 = getMinIntersection(filled[v], new Line2D.Double(out[0], out[1], out[4] + dx * pad, out[5] + dy * pad ));
                                Point2D intersect2 = getMinIntersection(filled[v], new Line2D.Double(out[2], out[3], out[6] + dx * pad, out[7] + dy * pad));
                                // if (intersect2 != null) group.add(new OvalElement(intersect2.getX(), intersect2.getY(), 0.2, debug));
                                if (intersect1 != null && intersect2 != null) {
                                    System.err.println("handle");
                                } else if (intersect1 != null) {
                                    double magAdjust = magnitude(out[0], out[1], intersect1.getX(), intersect1.getY()) - pad;
                                    out[4] = out[0] + dx * magAdjust;
                                    out[5] = out[1] + dy * magAdjust;
                                    out[6] = out[2] + dx * magAdjust;
                                    out[7] = out[3] + dy * magAdjust;
                                } else if (intersect2 != null) {
                                    double magAdjust = magnitude(out[2], out[3], intersect2.getX(), intersect2.getY()) - pad;
                                    out[4] = out[0] + dx * magAdjust;
                                    out[5] = out[1] + dy * magAdjust;
                                    out[6] = out[2] + dx * magAdjust;
                                    out[7] = out[3] + dy * magAdjust;
                                }
                            } 

                            group.add(new LineElement(out[0], out[1], out[4], out[5], bondWidth, fg));
                            group.add(new LineElement(out[2], out[3], out[6], out[7], bondWidth, fg));
                        }
                    }
                    break;
                case TRIPLE:
                    group.add(new LineElement(x1, y1, x2, y2, bondWidth, fg));
                    createLines(new Point2d(x1, y1), new Point2d(x2, y2), bondWidth, 1.5 * bondSeparation / scale, fg, group);
                    break;
                default:
                    System.err.println("unsupported bond type: " + bond.getOrder());
            }
        }

        // compute depiction bounds
        double minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        double maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        double halfBondWidth = bondWidth / 2;

        for (int v = 0; v < container.getAtomCount(); v++) {
            if (symbols[v] != null) {
                group.add(GeneralPath.shapeOf(symbols[v], getColor(container.getAtom(v))));
                Rectangle2D rect = filled[v].getBounds2D();
                if (rect.getMaxX() > maxX)
                    maxX = rect.getMaxX();
                if (rect.getMinX() < minX)
                    minX = rect.getX();
                if (rect.getMaxY() > maxY)
                    maxY = rect.getMaxY();
                if (rect.getMinY() < minY)
                    minY = rect.getY();
            }
            else {
                Point2d p = container.getAtom(v).getPoint2d();
                if (p.y - halfBondWidth < minY)
                    minY = p.y - halfBondWidth;
                if (p.x - halfBondWidth < minX)
                    minX = p.x - halfBondWidth;
                if (p.x + halfBondWidth > maxX)
                    maxX = p.x + halfBondWidth;
                if (p.y + halfBondWidth > maxY)
                    maxY = p.y + halfBondWidth;
            }
        }

        // debug: show bounding box
        // group.add(new RectangleElement(minX, minY, maxX - minX, maxY - minY, false, debug));
        group.add(new Bounds(minX, minY, maxX, maxY));
        
//        String id = container.getProperty("ChEBI ID");
//        
//        if (id == null)
//            id = "n/a";
//        
//        Shape idLabel  = toShape(id, minX, minY, scale);
//        Rectangle2D idBounds = idLabel.getBounds2D();
//        idLabel = AffineTransform.getTranslateInstance(idBounds.getWidth() / 2,
//                                                       idBounds.getHeight() / 2)
//                                 .createTransformedShape(idLabel);
//        
//        //group.add(new OvalElement(minX, minY, 0.05, debug));
//        //group.add(new OvalElement(minX, maxY, 0.05, Color.BLUE));
//        
//        group.add(GeneralPath.shapeOf(idLabel.getBounds2D(), new Color(0x44FFFFFF, true)));
//        group.add(GeneralPath.shapeOf(idLabel, fg));
        
        return group;
    }

    static Line2D.Double toLine(IBond bond) {
        return new Line2D.Double(bond.getAtom(0).getPoint2d().x,
                                 bond.getAtom(0).getPoint2d().y,
                                 bond.getAtom(1).getPoint2d().x,
                                 bond.getAtom(1).getPoint2d().y);
    }

    private IBond getOtherConnectedBond(IAtomContainer container, IAtom atom, IBond bond) {
        List<IBond> bonds = container.getConnectedBondsList(atom);
        for (IBond other : bonds) {
            if (other != bond)
                return other;
        }
        return null;
    }

    private double theta(double x1, double y1, double x2, double y2, double x3, double y3) {
        return Math.acos(dot(x1, y1, x2, y2, x3, y3) / (magnitude(x1, y1, x2, y2) * magnitude(x1, y1, x3, y3)));
    }

    private double dot(double x1, double y1, double x2, double y2, double x3, double y3) {
        return ((x2 - x1) * (x3 - x1)) + ((y2 - y1) * (y3 - y1));
    }

    private double magnitude(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    private void createLines(Point2d point1, Point2d point2, double width, double dist,
                             Color color, ElementGroup group) {
        double[] out = generateDistanceData(point1, point2, dist);
        LineElement l1 =
                new LineElement(out[0], out[1], out[4], out[5], width, color);
        LineElement l2 =
                new LineElement(out[2], out[3], out[6], out[7], width, color);
        group.add(l1);
        group.add(l2);
    }

    private double[] generateDistanceData(Point2d point1, Point2d point2, double dist) {
        Vector2d normal = new Vector2d();
        normal.sub(point2, point1);
        normal = new Vector2d(-normal.y, normal.x);
        normal.normalize();
        normal.scale(dist);

        Point2d line1p1 = new Point2d();
        Point2d line1p2 = new Point2d();
        line1p1.add(point1, normal);
        line1p2.add(point2, normal);

        normal.negate();
        Point2d line2p1 = new Point2d();
        Point2d line2p2 = new Point2d();
        line2p1.add(point1, normal);
        line2p2.add(point2, normal);

        return new double[]{
                line1p1.x, line1p1.y, line2p1.x, line2p1.y,
                line1p2.x, line1p2.y, line2p2.x, line2p2.y};
    }

    boolean renderMass(Integer massNumber, int elem) {
        try {
            return massNumber != null
                    && massNumber != Isotopes.getInstance().getMajorIsotope(elem).getMassNumber();
        } catch (IOException e) {
            return false;
        }
    }

    public Point2D getMinIntersection(final Shape poly, final Line2D.Double line) {

        double midX = line.x2 - line.x1;
        double midY = line.y2 - line.y1;

        final PathIterator pathIterator = poly.getPathIterator(null); //Getting an iterator along the polygon path
        final double[] curr = new double[6]; //Double array with length 6 needed by iterator
        final double[] first = new double[2]; //First point (needed for closing polygon path)
        final double[] prev = new double[2]; //Previously visited point
        Point2D min = null;
        double minMag = Integer.MAX_VALUE;
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(curr)) {
                case PathIterator.SEG_LINETO: {
                    final Line2D.Double currentLine = new Line2D.Double(prev[0], prev[1], curr[0], curr[1]);
                    if (currentLine.intersectsLine(line)) {
                        Point2D intersect = getIntersection(currentLine, line);
                        if (magnitude(midX, midY, intersect.getX(), intersect.getY()) < minMag)
                            min = intersect;
                    }
                    prev[0] = curr[0];
                    prev[1] = curr[1];
                    break;
                }
                case PathIterator.SEG_CLOSE: {
                    final Line2D.Double currentLine = new Line2D.Double(curr[0], curr[1], first[0], first[1]);
                    if (currentLine.intersectsLine(line)) {
                        Point2D intersect = getIntersection(currentLine, line);
                        if (magnitude(midX, midY, intersect.getX(), intersect.getY()) < minMag)
                            min = intersect;
                    }
                    break;
                }
                case PathIterator.SEG_MOVETO:
                    first[0] = prev[0] = curr[0];
                    first[1] = prev[1] = curr[1];
                    break;
                default: {
                    throw new IllegalArgumentException("Unsupported PathIterator segment type.");
                }
            }
            pathIterator.next();
        }
        return min;

    }

    public static Point2D getIntersection(final Line2D.Double line1, final Line2D.Double line2) {

        final double x1, y1, x2, y2, x3, y3, x4, y4;
        x1 = line1.x1;
        y1 = line1.y1;
        x2 = line1.x2;
        y2 = line1.y2;
        x3 = line2.x1;
        y3 = line2.y1;
        x4 = line2.x2;
        y4 = line2.y2;
        final double x = (
                (x2 - x1) * (x3 * y4 - x4 * y3) - (x4 - x3) * (x1 * y2 - x2 * y1)
        ) /
                (
                        (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)
                );
        final double y = (
                (y3 - y4) * (x1 * y2 - x2 * y1) - (y1 - y2) * (x3 * y4 - x4 * y3)
        ) /
                (
                        (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)
                );

        return new Point2D.Double(x, y);

    }

    public static Point2D getIntersection(double x1, double y1, double x2, double y2,
                                          double x3, double y3, double x4, double y4) {

        Line2D.Double line1 = new Line2D.Double(x1, y1, x2, y2);
        Line2D.Double line2 = new Line2D.Double(x3, y3, x4, y4);
        if (line1.intersects(x3, y3, x4, y4))
            return getIntersection(line1, line2);
        return null;
    }

    static final int NONE       = 0;
    static final int NORTH      = 3;
    static final int NORTH_EAST = 2;
    static final int EAST       = 1;
    static final int SOUTH_EAST = 8;
    static final int SOUTH      = 7;
    static final int SOUTH_WEST = 6;
    static final int WEST       = 5;
    static final int NORTH_WEST = 4;

    static int hPlacement(IAtomContainer container, double x, double y, IAtom atom, ElementGroup group) {
        if (atom.getImplicitHydrogenCount() == null || atom.getImplicitHydrogenCount() == 0)
            return NONE;
        List<IAtom> neighbors = container.getConnectedAtomsList(atom);
        double ux = 0;
        double uy = 0;
        for (int i = 0; i < neighbors.size(); i++) {
            Point2d p = neighbors.get(i).getPoint2d();
            double dx = p.x - x;
            double dy = p.y - y;
            double mag = Math.sqrt((dx * dx) + (dy * dy));
            dx /= mag;
            dy /= mag;
            ux += dx;
            uy += dy;
        }
        ux /= neighbors.size();
        uy /= neighbors.size();
        if (neighbors.size() > 1) {
//            if (ux < 0.1 && uy < 0.1) {
//                System.out.println(ux + ", " + uy);
//                return 1;
//            }
//            else {
            // debug
            // group.add(new LineElement(x, y, x - ux, y - uy, 0.001, debug));
            // group.add(new TextElement(x, y, "    " + Integer.toString(((((int) Math.round(Math.atan2(-uy, -ux) / (2 * Math.PI / 8))) + 8) % 8)), debug));
            return 1 + (((int) Math.round(Math.atan2(-uy, -ux) / (2 * Math.PI / 8))) + 8) % 8;
//            }
        }
        else if (neighbors.size() == 1) {
            if (Math.abs(ux) < 0.1 || ux < 0)
                return EAST;
            return WEST;
        }
        else if (neighbors.size() == 0) {
            // special case, it's H20 not OH2
            switch (atom.getAtomicNumber()) {
                case 8:  // O
                case 16: // S
                case 34: // Se
                case 52: // Te
                case 9:  // F
                case 17: // Cl
                case 35: // Br
                case 53: // I
                    return WEST;
            }
            return EAST;
        }

        throw new InternalError("unplaced hydrogen label");
    }


    static boolean displaySymbol(IAtomContainer container, IAtom atom) {

        // non-carbon?
        if (atom.getAtomicNumber() != 6)
            return true;

        // charged
        if (atom.getFormalCharge() != 0)
            return true;

        // radical
        if (container.getConnectedSingleElectronsCount(atom) > 0)
            return true;

        int db = 0, deg = 0;
        for (IBond bond : container.getConnectedBondsList(atom)) {
            if (bond.getOrder() == IBond.Order.DOUBLE)
                db++;
            deg++;
        }
        
        if (deg == 0)
            return true;

        if (DISPLAY_TERMINAL_CARBONS && deg == 1)
            return true;

        if (deg == 2 && db == 2) // allene/cumulene
            return true;

        return false;
    }

    Shape toShape(String text, double x, double y, double scale) {
        Shape shape = obtainGlyph(text);
        Rectangle2D bounds = shape.getBounds2D();
        AffineTransform at = AffineTransform.getTranslateInstance(x - (bounds.getX() / scale)
                                                                          - ((bounds.getWidth() / scale) / 2),
                                                                  y + ((bounds.getY() + bounds.getHeight()) / scale)
                                                                          - ((bounds.getHeight() / scale) / 2));
        at.scale(1 / scale,
                 1 / -scale);

        return at.createTransformedShape(shape);
    }

    private final Map<String, Shape> glyphs = new HashMap<String, Shape>();

    private Shape obtainGlyph(String text) {
        Shape shape = glyphs.get(text);
        if (shape == null)
            glyphs.put(text, shape = createGlyph(text));
        return shape;
    }

    private Shape createGlyph(String text) {
        return font.createGlyphVector(FONT_RENDER_CONTEXT, text)
                   .getOutline();
    }

    public static Shape getConvexHullPath2D(Shape shape) {
        List<Point2D.Double> points = new LinkedList<Point2D.Double>();
        double[] coords = new double[6];
        for (PathIterator i = shape.getPathIterator(null); !i.isDone(); i.next()) {
            switch (i.currentSegment(coords)) {
                case PathIterator.SEG_CLOSE:
                    break;
                case PathIterator.SEG_MOVETO:
                case PathIterator.SEG_LINETO:
                    points.add(new Point2D.Double(coords[0], coords[1]));
                    break;
                case PathIterator.SEG_QUADTO:
                    points.add(new Point2D.Double(coords[0], coords[1]));
                    points.add(new Point2D.Double(coords[2], coords[3]));
                    break;
                case PathIterator.SEG_CUBICTO:
                    points.add(new Point2D.Double(coords[0], coords[1]));
                    points.add(new Point2D.Double(coords[2], coords[3]));
                    points.add(new Point2D.Double(coords[4], coords[5]));
                    break;
            }

        }
        Path2D.Double path2d = new Path2D.Double();
        Point2D.Double[] chPoints = getConvexHull(points.toArray(new Point2D.Double[0]));
        if (!points.isEmpty()) {
            path2d.moveTo(chPoints[0].x, chPoints[0].y);
            for (int i = 1; i < chPoints.length; i++) {
                path2d.lineTo(chPoints[i].x, chPoints[i].y);
            }
        }
        return path2d;
    }

    static Point2D unit(Point2D p1, Point2D p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        double mag = Math.sqrt((dx * dx) + (dy * dy));
        dx /= mag;
        dy /= mag;
        return new Point2D.Double(dx, dy);
    }

    static Point2D adjustP2(Point2D p1, Point2D p2, double fact) {
        Point2D unit = unit(p1, p2);
        return new Point2D.Double(p2.getX() + unit.getX() * fact,
                                  p2.getY() + unit.getY() * fact);
    }

    // from JHotDraw LGPL
    public static Point2D.Double[] getConvexHull(Point2D.Double[] points) {
        // Quickly return if no work is needed
        if (points.length < 3) {
            return points.clone();
        }

        // Sort points from left to right O(n log n)
        Point2D.Double[] sorted = points.clone();
        Arrays.sort(sorted, new Comparator<Point2D.Double>() {

            @Override
            public int compare(Point2D.Double o1, Point2D.Double o2) {
                int c1 = Doubles.compare(o1.x, o2.x);
                if (c1 != 0)
                    return c1;
                return Doubles.compare(o1.y, o2.y);
            }
        });

        Point2D.Double[] hull = new Point2D.Double[sorted.length + 2];

        // Process upper part of convex hull O(n)
        int upper = 0; // Number of points in upper part of convex hull
        hull[upper++] = sorted[0];
        hull[upper++] = sorted[1];
        for (int i = 2; i < sorted.length; i++) {
            hull[upper++] = sorted[i];
            while (upper > 2 && !isRightTurn(hull[upper - 3], hull[upper - 2], hull[upper - 1])) {
                hull[upper - 2] = hull[upper - 1];
                upper--;
            }
        }

        // Process lower part of convex hull O(n)
        int lower = upper; // (lower - number + 1) = number of points in the lower part of the convex hull
        hull[lower++] = sorted[sorted.length - 2];
        for (int i = sorted.length - 3; i >= 0; i--) {
            hull[lower++] = sorted[i];
            while (lower - upper > 1 && !isRightTurn(hull[lower - 3], hull[lower - 2], hull[lower - 1])) {
                hull[lower - 2] = hull[lower - 1];
                lower--;
            }
        }
        lower -= 1;

        // Reduce array
        Point2D.Double[] convexHull = new Point2D.Double[lower];
        System.arraycopy(hull, 0, convexHull, 0, lower);
        return convexHull;
    }

    // from JHotDraw LGPL
    public static boolean isRightTurn(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
        if (p1.equals(p2) || p2.equals(p3)) {
            // no right turn if points are at same location
            return false;
        }
        double val = (p2.x * p3.y + p1.x * p2.y + p3.x * p1.y) - (p2.x * p1.y + p3.x * p2.y + p1.x * p3.y);
        return val > 0;
    }
}
