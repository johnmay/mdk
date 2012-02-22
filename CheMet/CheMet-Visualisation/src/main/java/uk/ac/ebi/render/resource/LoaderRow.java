package uk.ac.ebi.render.resource;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import net.sf.furbelow.SpinningDial;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.chemet.render.ViewUtilities;
import uk.ac.ebi.io.service.exception.MissingLocationException;
import uk.ac.ebi.io.service.loader.ResourceLoader;
import uk.ac.ebi.io.service.loader.structure.ChEBIStructureLoader;
import uk.ac.ebi.io.service.loader.structure.HMDBStructureLoader;
import uk.ac.ebi.io.service.loader.structure.KEGGCompoundStructureLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * ${Name}.java - 21.02.2012 <br/> Description...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class LoaderRow extends JComponent {

    private JButton delete;
    private JButton revert;
    private JButton configure;
    private JLabel name;
    private JButton update;

    public LoaderRow(final ResourceLoader resourceLoader) {

        final Window window = SwingUtilities.getWindowAncestor(this);

        delete = ButtonFactory.newCleanButton(ViewUtilities.getIcon("images/cutout/trash_12x12.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                resourceLoader.clean();
                delete.setEnabled(resourceLoader.canBackup() || resourceLoader.canRevert());
                revert.setEnabled(resourceLoader.canRevert());
            }
        });
        delete.setToolTipText("Delete the current index and it's backup");
        revert = ButtonFactory.newCleanButton(ViewUtilities.getIcon("images/cutout/revert_16x16.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                resourceLoader.revert();
                delete.setEnabled(resourceLoader.canBackup() || resourceLoader.canRevert());
                revert.setEnabled(resourceLoader.canRevert());
            }
        });
        revert.setToolTipText("Revert to the previous version of the index");
        configure = ButtonFactory.newCleanButton(ViewUtilities.getIcon("images/cutout/cog_16x16.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ResourceLoaderConfig dialog = new ResourceLoaderConfig(window, resourceLoader);
                dialog.setAnchor(configure);
                dialog.setVisible(true);
                dialog.configure();
                update.setEnabled(resourceLoader.isAvailable());
                delete.setEnabled(resourceLoader.canBackup() || resourceLoader.canRevert());
                revert.setEnabled(resourceLoader.canRevert());
            }
        });
        configure.setToolTipText("Configure loader");

        name = LabelFactory.newLabel(resourceLoader.getIndex().getName());

        update = ButtonFactory.newCleanButton(ViewUtilities.getIcon("images/cutout/update_16x16.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                name.setIcon(new SpinningDial(16, 16));
                update.setEnabled(false);
                delete.setEnabled(false);
                configure.setEnabled(false);
                revert.setEnabled(false);

                SwingWorker worker = new SwingWorker() {

                    @Override
                    protected Object doInBackground() throws Exception {
                        try {
                            resourceLoader.load();
                        } catch (MissingLocationException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        return null;
                    };

                    @Override
                    protected void done() {
                        delete.setEnabled(resourceLoader.canBackup() || resourceLoader.canRevert());
                        revert.setEnabled(resourceLoader.canRevert());
                        configure.setEnabled(true);
                        update.setEnabled(resourceLoader.isAvailable());
                        name.setIcon(null);
                    }
                };

                worker.execute();
            }
        });
        update.setToolTipText("Update the index");


        update.setEnabled(resourceLoader.isAvailable());
        delete.setEnabled(resourceLoader.canBackup() || resourceLoader.canRevert());
        revert.setEnabled(resourceLoader.canRevert());

        setLayout(new FormLayout("4dlu, min, 4dlu, min, 4dlu, min, 4dlu, p, 4dlu, min, 4dlu", "p")

        );
        CellConstraints cc = new CellConstraints();

        add(delete, cc.xy(2, 1)

        );

        add(revert, cc.xy(4, 1)

        );

        add(configure, cc.xy(6, 1)

        );

        add(update, cc.xy(8, 1)

        );

        add(name, cc.xy(10, 1)

        );
    }

    public static void main(String[] args) throws IOException {

        JFrame frame = new JFrame("Resource Loading");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Box box = Box.createVerticalBox();
        frame.setContentPane(box);
        box.add(Box.createRigidArea(new Dimension(5, 5)));
        box.add(Box.createGlue());
        box.add(new LoaderRow(new ChEBIStructureLoader()));
        box.add(Box.createGlue());
        box.add(new LoaderRow(new KEGGCompoundStructureLoader()));
        box.add(Box.createGlue());
        box.add(new LoaderRow(new HMDBStructureLoader()));
        box.add(Box.createGlue());
        box.add(Box.createRigidArea(new Dimension(5, 5)));
        frame.setVisible(true);

    }

}
