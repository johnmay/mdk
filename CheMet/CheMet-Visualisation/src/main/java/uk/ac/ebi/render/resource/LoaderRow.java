package uk.ac.ebi.render.resource;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import net.sf.furbelow.SpinningDial;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.chemet.render.ViewUtilities;
import uk.ac.ebi.service.SingleIndexResourceLoader;
import uk.ac.ebi.service.exception.MissingLocationException;

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
    private JButton cancel;

    private static final Logger LOGGER = Logger.getLogger(LoaderRow.class);

    public LoaderRow(final SingleIndexResourceLoader loader, final Window window) {

        System.out.println("Creating loader for UI " + loader.getName());

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
        name = LabelFactory.newLabel(loader.getIndex().getName());

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

                new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        try {
                            System.out.println("update()");
                            loader.update();
                            System.out.println("done");
                        } catch (MissingLocationException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                delete.setEnabled(loader.canBackup() || loader.canRevert());
                                revert.setEnabled(loader.canRevert());
                                configure.setEnabled(true);
                                update.setEnabled(loader.canUpdate());
                                name.setIcon(null);
                            }
                        });
                        return null;
                    }

                }.execute();

            }
        }

        );

        cancel = ButtonFactory.newCleanButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("cancel()");
                loader.cancel();
                System.out.println("done()");
                loader.revert();
            }
        }

        );
        System.out.println("Update... done");
        update.setToolTipText("Update the index");

        System.out.println("Setting state");
        update.setEnabled(loader.canUpdate());
        delete.setEnabled(loader.canBackup() || loader.canRevert());
        revert.setEnabled(loader.canRevert());

        setLayout(new FormLayout("4dlu, min, 4dlu, min, 4dlu, min, 4dlu, min, 4dlu, min, 4dlu, p:grow, 4dlu", "p")

        );

        System.out.println("Laying out loader components");
        CellConstraints cc = new CellConstraints();

        add(delete, cc.xy(2, 1)

        );

        add(revert, cc.xy(4, 1)

        );

        add(configure, cc.xy(6, 1)

        );

        add(update, cc.xy(8, 1)

        );

        add(cancel, cc.xy(10, 1)

        );

        add(name, cc.xy(12, 1)

        );
    }

    public static void main(String[] args) throws IOException {

        JFrame frame = new JFrame("Resource Loading");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Box box = Box.createVerticalBox();
        frame.setContentPane(box);
        box.add(Box.createRigidArea(new Dimension(5, 5)));
        box.add(Box.createGlue());
        //        box.add(new LoaderRow(new ChEBIStructureLoader()));
        //        box.add(Box.createGlue());
        //        box.add(new LoaderRow(new KEGGCompoundStructureLoader()));
        //        box.add(Box.createGlue());
        //        box.add(new LoaderRow(new HMDBStructureLoader()));
        box.add(Box.createGlue());
        box.add(Box.createRigidArea(new Dimension(5, 5)));
        frame.setVisible(true);

    }

}
