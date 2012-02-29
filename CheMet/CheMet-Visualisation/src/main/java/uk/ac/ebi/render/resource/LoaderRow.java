package uk.ac.ebi.render.resource;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.sun.awt.AWTUtilities;
import net.sf.furbelow.SpinningDial;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.chemet.render.ViewUtilities;
import uk.ac.ebi.chemet.service.loader.data.ChEBIDataLoader;
import uk.ac.ebi.chemet.service.loader.multiple.HMDBMetabocardsLoader;
import uk.ac.ebi.chemet.service.loader.multiple.KEGGCompoundLoader;
import uk.ac.ebi.chemet.service.loader.name.ChEBINameLoader;
import uk.ac.ebi.chemet.service.loader.single.TaxonomyLoader;
import uk.ac.ebi.chemet.service.loader.structure.ChEBIStructureLoader;
import uk.ac.ebi.chemet.service.loader.structure.HMDBStructureLoader;
import uk.ac.ebi.chemet.service.loader.structure.KEGGCompoundStructureLoader;
import uk.ac.ebi.render.resource.location.FileLocationEditor;
import uk.ac.ebi.service.ResourceLoader;
import uk.ac.ebi.service.SingleIndexResourceLoader;
import uk.ac.ebi.service.exception.MissingLocationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

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
    private JButton cancel;
    private ResourceLoader loader;
    private SwingWorker worker;

    private static final Logger LOGGER = Logger.getLogger(LoaderRow.class);

    public LoaderRow(final ResourceLoader loader, final Window window) {

        this.loader = loader;

        System.out.println("Creating loader for UI " + loader.getName());
        setOpaque(false);

        delete = ButtonFactory.newCleanButton(ViewUtilities.getIcon("images/cutout/trash_12x12.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loader.clean();
                delete.setEnabled(loader.canBackup() || loader.canRevert());
                revert.setEnabled(loader.canRevert());
            }
        });
        System.out.println("Delete... done");
        delete.setToolTipText("Delete the current index and it's backup");
        revert = ButtonFactory.newCleanButton(ViewUtilities.getIcon("images/cutout/revert_16x16.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loader.revert();
                delete.setEnabled(loader.canBackup() || loader.canRevert());
                revert.setEnabled(loader.canRevert());
            }
        });
        System.out.println("Revert... done");
        revert.setToolTipText("Revert to the previous version of the index");
        configure = ButtonFactory.newCleanButton(ViewUtilities.getIcon("images/cutout/cog_16x16.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ResourceLoaderConfig dialog = new ResourceLoaderConfig(window, loader);
                dialog.setAnchor(configure);
                dialog.setVisible(true);
                dialog.configure();
                update.setEnabled(loader.canUpdate());
                delete.setEnabled(loader.canBackup() || loader.canRevert());
                revert.setEnabled(loader.canRevert());
            }
        });
        configure.setToolTipText("Configure loader");
        System.out.println("Configure... done");
        name = LabelFactory.newLabel(loader.getName());

        update = ButtonFactory.newCleanButton(ViewUtilities.getIcon("images/cutout/update_16x16.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                System.out.println("Invoked update()");

                name.setIcon(new SpinningDial(16, 16));
                update.setEnabled(false);
                delete.setEnabled(false);
                configure.setEnabled(false);
                revert.setEnabled(false);

                // ensure the loader isn't cancelled and update
                loader.backup();
                loader.uncancel();
                cancel.setEnabled(true);
                cancel.setVisible(true);

                worker = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        try {
                            System.out.println("update()");
                            loader.update();
                            System.out.println("done");
                            cancel.setEnabled(false);
                            cancel.setVisible(false);
                        } catch (MissingLocationException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        updateButtons();
                        return null;
                    }

                };

                worker.execute();
            }
        }

        );

        cancel = ButtonFactory.newCleanButton(ViewUtilities.getIcon("images/cutout/cancel_16x16.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loader.cancel();

                // wait for worker to finish then revert
                try {
                    worker.get();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ExecutionException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                loader.revert();
                updateButtons();
                cancel.setEnabled(false);
                cancel.setVisible(false);
            }
        }

        );
        System.out.println("Update... done");
        update.setToolTipText("Update the index");

        System.out.println("Setting state");
        update.setEnabled(loader.canUpdate());
        delete.setEnabled(loader.canBackup() || loader.canRevert());
        revert.setEnabled(loader.canRevert());
        cancel.setEnabled(false);
        cancel.setVisible(false);

        setLayout(new FormLayout("4dlu, min, 1dlu, min, 1dlu, min, 1dlu, min, 4dlu, p:grow, 4dlu, right:min, 1dlu", "p")

        );

        System.out.println("Laying out loader components");
        CellConstraints cc = new CellConstraints();

        add(delete, cc.xy(2, 1));
        add(revert, cc.xy(4, 1));
        add(configure, cc.xy(6, 1));
        add(update, cc.xy(8, 1));
        add(name, cc.xy(10, 1));
        add(cancel, cc.xy(12, 1));
    }

    public void updateButtons() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                delete.setEnabled(loader.canBackup() || loader.canRevert());
                revert.setEnabled(loader.canRevert());
                configure.setEnabled(true);
                update.setEnabled(loader.canUpdate());
                name.setIcon(null);
            }
        });
    }


    public static void main(String[] args) throws IOException {

        JFrame frame = new JFrame("Resource Loading");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Box box = Box.createVerticalBox();
        frame.setContentPane(box);
        box.add(Box.createRigidArea(new Dimension(5, 5)));
        box.add(Box.createGlue());
        box.add(new LoaderRow(new ChEBIStructureLoader(), frame));
        box.add(Box.createGlue());
        box.add(new LoaderRow(new KEGGCompoundStructureLoader(), frame));
        box.add(Box.createGlue());
        box.add(new LoaderRow(new HMDBStructureLoader(), frame));
        box.add(Box.createGlue());
        box.add(new LoaderRow(new HMDBMetabocardsLoader(), frame));
        box.add(Box.createGlue());
        box.add(new LoaderRow(new KEGGCompoundLoader(), frame));
        box.add(Box.createGlue());
        box.add(new LoaderRow(new ChEBINameLoader(), frame));
        box.add(Box.createGlue());
        box.add(new LoaderRow(new ChEBIDataLoader(), frame));
        box.add(Box.createGlue());
        box.add(new LoaderRow(new TaxonomyLoader(), frame));
        box.add(Box.createGlue());
        box.add(Box.createRigidArea(new Dimension(5, 5)));

        frame.setVisible(true);
        frame.pack();

    }

}
