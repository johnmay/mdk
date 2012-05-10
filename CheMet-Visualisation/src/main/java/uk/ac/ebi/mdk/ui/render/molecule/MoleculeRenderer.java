/**
 * MoleculeRenderer.java
 *
 * 2011.09.08
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.mdk.ui.render.molecule;

import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.templates.MoleculeFactory;
import uk.ac.ebi.caf.component.theme.ThemeManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 *          MoleculeRenderer – 2011.09.08 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MoleculeRenderer {

    private static final Logger LOGGER = Logger.getLogger(MoleculeRenderer.class);

    private AtomContainerRenderer renderer;

    private RendererModel model;

    private final StructureDiagramGenerator structureGenerator = new StructureDiagramGenerator();


    protected MoleculeRenderer() {
        List<IGenerator<IAtomContainer>> generators = new ArrayList<IGenerator<IAtomContainer>>();
        generators.add(new BasicSceneGenerator());
        generators.add(new BasicBondGenerator());
        generators.add(new BasicAtomGenerator());
        renderer = new AtomContainerRenderer(generators, new AWTFontManager());
        model = renderer.getRenderer2DModel();
    }


    public static MoleculeRenderer getInstance() {
        return MoleculeRendererHolder.INSTANCE;
    }


    private static class MoleculeRendererHolder {

        private static final MoleculeRenderer INSTANCE = new MoleculeRenderer();
    }


    public BufferedImage getImage(IAtomContainer molecule, Rectangle bounds) throws CDKException {

        return getImage(molecule, bounds, Color.WHITE);

    }


    public BufferedImage getImage(IAtomContainer molecule,
                                  Rectangle bounds,
                                  Color background) throws CDKException {


        BufferedImage img = new BufferedImage(bounds.width, bounds.height,
                                              BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = (Graphics2D) img.getGraphics();
//        structureGenerator.setMolecule(new Molecule(molecule));
//        structureGenerator.generateCoordinates();
//        IMolecule moleculeWithXYZ = structureGenerator.getMolecule();
        g2.setColor(background);
        g2.fill(bounds);
        try {
            renderer.paint(molecule, new AWTDrawVisitor(g2), bounds, true);
        } catch (IllegalArgumentException ex) {
            LOGGER.debug("Molecule did not have coordinates!");
            String unrendered = "no-coordinates";
            g2.setFont(ThemeManager.getInstance().getTheme().getBodyFont().deriveFont(9.0f));
            int width = g2.getFontMetrics().stringWidth(unrendered);
            g2.setColor(Color.RED);
            g2.drawString(unrendered, bounds.width / 2 - width / 2, bounds.height / 2);
        }

        g2.dispose();

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
