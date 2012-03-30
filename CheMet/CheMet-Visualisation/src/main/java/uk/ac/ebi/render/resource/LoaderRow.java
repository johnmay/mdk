package uk.ac.ebi.render.resource;

import com.jgoodies.animation.*;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import net.sf.furbelow.SpinningDial;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.chemet.render.ViewUtilities;
import uk.ac.ebi.render.resource.location.DirectoryLocationEditor;
import uk.ac.ebi.render.resource.location.FileLocationEditor;
import uk.ac.ebi.render.resource.location.LocationEditor;
import uk.ac.ebi.service.ResourceLoader;
import uk.ac.ebi.service.exception.MissingLocationException;
import uk.ac.ebi.service.location.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * ${Name}.java - 21.02.2012 <br/> MetaInfo...
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

    private LoaderConfiguration configuration;

    private static final Logger LOGGER = Logger.getLogger(LoaderRow.class);

    public LoaderRow(final ResourceLoader loader, final Window window, final LocationFactory factory) {

        this.loader = loader;

        setOpaque(false);

        configuration = new LoaderConfiguration(loader, factory);

        configuration.setVisible(false);

        delete = ButtonFactory.newCleanButton(ViewUtilities.getIcon("images/cutout/trash_12x12.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loader.clean();
                delete.setEnabled(loader.canBackup() || loader.canRevert());
                revert.setEnabled(loader.canRevert());
            }
        });
        delete.setToolTipText("Delete the current index and it's backup");
        revert = ButtonFactory.newCleanButton(ViewUtilities.getIcon("images/cutout/revert_16x16.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loader.revert();
                delete.setEnabled(loader.canBackup() || loader.canRevert());
                revert.setEnabled(loader.canRevert());
            }
        });
        revert.setToolTipText("Revert to the previous version of the index");

        final Animation expand = new Expand(configuration, 500);
        final Animation collapse = new Collapse(configuration, 500);

        configure = ButtonFactory.newCleanButton(ViewUtilities.getIcon("images/cutout/cog_16x16.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                update.setEnabled(loader.canUpdate());
                delete.setEnabled(loader.canBackup() || loader.canRevert());
                revert.setEnabled(loader.canRevert());

                if (configuration.isVisible()) {
                    configuration.configure();
                    new Animator(collapse, 32).start();
                    updateButtons();
                } else {
                    new Animator(expand, 32).start();
                }

            }
        });
        configure.setToolTipText("Configure loader");
        name = LabelFactory.newLabel(loader.getName());

        update = ButtonFactory.newCleanButton(ViewUtilities.getIcon("images/cutout/update_16x16.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

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
                            loader.update();
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
        update.setToolTipText("Update the index");

        update.setEnabled(false);
        new MyUpdateChecker().execute();
        delete.setEnabled(loader.canBackup() || loader.canRevert());
        revert.setEnabled(loader.canRevert());
        cancel.setEnabled(false);
        cancel.setVisible(false);


        CellConstraints cc = new CellConstraints();

        Box controls = Box.createHorizontalBox();
        controls.add(delete);
        controls.add(revert);
        controls.add(configure);
        controls.add(update);
        controls.add(cancel);

        setLayout(new FormLayout("p, p:grow, min",
                                 "p, p"));


        add(controls, cc.xy(1, 1));
        add(name, cc.xy(2, 1));
        add(cancel, cc.xy(3, 1));
        add(configuration, cc.xyw(1, 2, 3, cc.LEFT, cc.CENTER));
        setBackground(Color.WHITE);


    }

    class MyUpdateChecker extends SwingWorker<Boolean, Object> {
        @Override
        protected Boolean doInBackground() {
            return loader.canUpdate();
        }

        @Override
        protected void done() {
            try {
                update.setEnabled(get());
            } catch (Exception e) {
                // ignore
            }
        }
    }


    public void updateButtons() {

        new MyUpdateChecker().execute();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                delete.setEnabled(loader.canBackup() || loader.canRevert());
                revert.setEnabled(loader.canRevert());
                configure.setEnabled(true);
                name.setIcon(null);
            }
        });
    }


    class LoaderConfiguration extends JPanel {

        private Map<String, LocationEditor> editors = new HashMap<String, LocationEditor>();
        private ResourceLoader loader;
        private LocationFactory factory;

        public LoaderConfiguration(ResourceLoader loader, LocationFactory factory) {

            setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.LIGHT_GRAY));

            this.loader = loader;
            this.factory = factory;

            FormLayout layout = new FormLayout("right:min, p");
            CellConstraints cc = new CellConstraints();
            setLayout(layout);
            editors = new HashMap<String, LocationEditor>();


            for (Map.Entry<String, LocationDescription> e : loader.getRequiredResources().entrySet()) {


                layout.appendRow(new RowSpec(Sizes.PREFERRED));
                add(LabelFactory.newFormLabel(e.getValue().getName(), e.getValue().getDescription()), cc.xy(1, layout.getRowCount()));

                LocationEditor editor = null;

                Class c = e.getValue().getType();
                if (c.equals(ResourceFileLocation.class)) {
                    editor = new FileLocationEditor(factory);
                    editors.put(e.getKey(), editor);
                    editor.setup(e.getValue());
                } else if (c.equals(ResourceDirectoryLocation.class)) {
                    editor = new DirectoryLocationEditor(factory);
                    editors.put(e.getKey(), editor);
                    editor.setup(e.getValue());
                }

                add((JComponent) editor, cc.xy(2, layout.getRowCount()));

            }


        }

        public void configure() {

            java.util.List<ResourceLocation> locationList = new ArrayList<ResourceLocation>();

            for (Map.Entry<String, LocationEditor> e : editors.entrySet()) {

                String key = e.getKey();

                try {
                    // replace with individual UI components for selecting
                    loader.addLocation(key, e.getValue().getResourceLocation());
                } catch (IOException ex) {

                }


            }


        }

    }


    class Expand extends ResizePanel implements AnimationListener {

        private JPanel panel;

        protected Expand(JPanel panel, long duration) {
            super(panel, new Dimension(panel.getPreferredSize().width, 0), panel.getPreferredSize(), duration);
            this.panel = panel;
            addAnimationListener(this);
        }

        @Override
        public void animationStarted(AnimationEvent animationEvent) {
            panel.setPreferredSize(new Dimension(panel.getPreferredSize().width, 0));
            panel.setVisible(true);
        }

        @Override
        public void animationStopped(AnimationEvent animationEvent) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

    }

    class Collapse extends ResizePanel implements AnimationListener {
        private JPanel panel;

        protected Collapse(JPanel panel, long duration) {
            super(panel, panel.getPreferredSize(), new Dimension(panel.getPreferredSize().width, 0), duration);
            this.panel = panel;
            addAnimationListener(this);
        }

        @Override
        public void animationStarted(AnimationEvent animationEvent) {
        }

        @Override
        public void animationStopped(AnimationEvent animationEvent) {
            panel.setVisible(false);
        }
    }

    class ResizePanel extends AbstractAnimation {

        private final JPanel panel;
        private final Dimension endSize;
        private final Dimension startSize;
        private int xdiff;
        private int ydiff;

        protected ResizePanel(JPanel panel,
                              Dimension startSize,
                              Dimension endSize,
                              long duration) {
            super(duration);
            this.panel = panel;
            this.startSize = startSize;
            this.endSize = endSize;

            xdiff = endSize.width - startSize.width;
            ydiff = endSize.height - startSize.height;

        }

        @Override
        protected void applyEffect(long l) {

            if (l == 0) return; // ignore resting state

            double position = (double) l / duration();
            double proportion = (Math.cos(position * Math.PI + Math.PI) + 1) / 2;

            int width  = startSize.width + (int) ((xdiff) * proportion);
            int height = startSize.height + (int) ((ydiff) * proportion);
            Dimension newSize = new Dimension(width,
                                              height);

            panel.setPreferredSize(newSize);
            revalidate();

        }
    }


    //    public static void main(String[] args) throws IOException {
    //
    //        JFrame frame = new JFrame("Resource Loading");
    //        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //        Box box = Box.createVerticalBox();
    //        frame.setContentPane(box);
    //        box.add(Box.createRigidArea(new Dimension(5, 5)));
    //        box.add(Box.createGlue());
    //        box.add(new LoaderRow(new ChEBIStructureLoader(), frame, ));
    //        box.add(Box.createGlue());
    //        box.add(new LoaderRow(new KEGGCompoundStructureLoader(), frame));
    //        box.add(Box.createGlue());
    //        box.add(new LoaderRow(new HMDBStructureLoader(), frame));
    //        box.add(Box.createGlue());
    //        box.add(new LoaderRow(new HMDBMetabocardsLoader(), frame));
    //        box.add(Box.createGlue());
    //        box.add(new LoaderRow(new KEGGCompoundLoader(), frame));
    //        box.add(Box.createGlue());
    //        box.add(new LoaderRow(new ChEBINameLoader(), frame));
    //        box.add(Box.createGlue());
    //        box.add(new LoaderRow(new ChEBIDataLoader(), frame));
    //        box.add(Box.createGlue());
    //        box.add(new LoaderRow(new TaxonomyLoader(), frame));
    //        box.add(Box.createGlue());
    //        box.add(Box.createRigidArea(new Dimension(5, 5)));
    //
    //        frame.setVisible(true);
    //        frame.pack();
    //
    //    }

}
