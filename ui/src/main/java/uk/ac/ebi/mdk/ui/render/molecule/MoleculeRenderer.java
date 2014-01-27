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

import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.HighlightGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.templates.MoleculeFactory;
import uk.ac.ebi.caf.component.theme.ThemeManager;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.openscience.cdk.geometry.GeometryTools.CoordinateCoverage.FULL;


/**
 * MoleculeRenderer – 2011.09.08 <br> Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class MoleculeRenderer {

    private static final Logger LOGGER = Logger.getLogger(MoleculeRenderer.class);

    private AtomContainerRenderer renderer;

    private RendererModel model;

    private final StructureDiagramGenerator sdg = new StructureDiagramGenerator();

    protected MoleculeRenderer() {
        List<IGenerator<IAtomContainer>> generators = new ArrayList<IGenerator<IAtomContainer>>();

        generators.add(new BasicSceneGenerator());
        generators.add(new HighlightGenerator());
        generators.add(new SmoothGenerator(true));

        renderer = new AtomContainerRenderer(generators, new AWTFontManager());
        model = renderer.getRenderer2DModel();
        model.set(HighlightGenerator.HighlightPalette.class,
                  HighlightGenerator.createPalette(new Color(0xBB67FF62, true),
                                                   new Color(0xBB4C8BFF, true),
                                                   new Color(0xBBFF2A2F, true)));
        model.set(HighlightGenerator.HighlightRadius.class,
                  8d);
        sdg.setUseTemplates(false); // templates currently too slow
    }


    public static MoleculeRenderer getInstance() {
        return MoleculeRendererHolder.INSTANCE;
    }


    private static class MoleculeRendererHolder {

        private static final MoleculeRenderer INSTANCE = new MoleculeRenderer();
    }

    public BufferedImage getImage(IAtomContainer molecule, int size) throws CDKException {

        return getImage(molecule, new Rectangle(0, 0, size, size), Color.WHITE);

    }

    public BufferedImage getImage(IAtomContainer molecule, int size, boolean highlight) throws CDKException {

        return getImage(molecule, new Rectangle(0, 0, size, size), Color.WHITE, highlight);

    }

    public BufferedImage getImage(IAtomContainer molecule, Rectangle bounds) throws CDKException {

        return getImage(molecule, bounds, Color.WHITE);

    }

    public BufferedImage getImage(IAtomContainer container,
                                  Rectangle bounds,
                                  Color background) throws CDKException {
        return getImage(container, bounds, background, false);
    }

    public BufferedImage getImage(IAtomContainer container,
                                  Rectangle bounds,
                                  Color background,
                                  boolean highlighted) throws CDKException {


        Object idmap = null;

        // remove highlight map whilst rendering - it will be put in later
        if (!highlighted) {
            idmap = container.getProperty(HighlightGenerator.ID_MAP);
            container.removeProperty(HighlightGenerator.ID_MAP);
        }

        BufferedImage img = new BufferedImage(bounds.width, bounds.height,
                                              BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) img.getGraphics();
        g2.setColor(background);
        g2.fill(bounds);

        g2.setFont(ThemeManager.getInstance().getTheme().getBodyFont().deriveFont(9.0f));
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC));

        if (GeometryTools.get2DCoordinateCoverage(container) == FULL) {
            renderer.paint(container, new AWTDrawVisitor(g2), bounds, true);
        }
        else {
            try {
                sdg.setMolecule(container, false); // clone or not clone?
                sdg.generateCoordinates();
                renderer.paint(sdg.getMolecule(), new AWTDrawVisitor(g2), bounds, true);
            } catch (Exception e) {
                LOGGER.error("Could not generate coordinates for depiction", e);
                String unrendered = "No 2D Coordinates";
                int width = g2.getFontMetrics().stringWidth(unrendered);
                g2.setColor(Color.RED);
                g2.drawString(unrendered, bounds.width / 2 - width / 2, bounds.height / 2);
            }
        }

        g2.dispose();

        if (idmap != null) {
            container.setProperty(HighlightGenerator.ID_MAP, idmap);
        }

        return img;

    }


    public static void main(String[] args) throws CDKException, IOException {
        MoleculeRenderer MOL_RENDERER = MoleculeRenderer.getInstance();
        BufferedImage buffImg = MOL_RENDERER.getImage(MoleculeFactory.make123Triazole(), new Rectangle(200, 200));
        File tmp = File.createTempFile("test", ".png");
        ImageIO.write(buffImg, "png", new FileOutputStream(tmp));
        System.out.println("writen image to tmp file: " + tmp);
    }
}
