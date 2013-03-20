package uk.ac.ebi.mdk.ui.component.service;

import com.jgoodies.animation.AbstractAnimation;
import com.jgoodies.animation.Animation;
import com.jgoodies.animation.AnimationEvent;
import com.jgoodies.animation.AnimationListener;
import com.jgoodies.animation.Animator;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import net.sf.furbelow.SpinningDial;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.CalloutDialog;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.caf.utility.ResourceUtility;
import uk.ac.ebi.mdk.service.ProgressListener;
import uk.ac.ebi.mdk.service.ResourceLoader;
import uk.ac.ebi.mdk.service.exception.MissingLocationException;
import uk.ac.ebi.mdk.service.location.LocationDescription;
import uk.ac.ebi.mdk.service.location.LocationFactory;
import uk.ac.ebi.mdk.service.location.ResourceDirectoryLocation;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;
import uk.ac.ebi.mdk.service.location.ResourceLocation;
import uk.ac.ebi.mdk.ui.component.service.location.DirectoryLocationEditor;
import uk.ac.ebi.mdk.ui.component.service.location.FileLocationEditor;
import uk.ac.ebi.mdk.ui.component.service.location.LocationEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static uk.ac.ebi.mdk.service.EDTProgressListener.safeDispatch;

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
    private JLabel info;
    private SwingWorker worker;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm");

    private LoaderConfiguration configuration;

    private static final Logger LOGGER = Logger.getLogger(LoaderRow.class);

    public LoaderRow(final ResourceLoader loader, final Window window, final LocationFactory factory) {

        this.loader = loader;

        setOpaque(false);

        configuration = new LoaderConfiguration(loader, factory);

        configuration.setVisible(false);

        delete = ButtonFactory.newCleanButton(ResourceUtility
                                                      .getIcon("/uk/ac/ebi/chemet/render/images/cutout/trash_12x12.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loader.clean();
                delete.setEnabled(loader.canBackup() || loader.canRevert());
                revert.setEnabled(loader.canRevert());
                updateButtons();
            }
        });
        delete.setToolTipText("Delete the current index and it's backup");
        revert = ButtonFactory.newCleanButton(ResourceUtility
                                                      .getIcon("/uk/ac/ebi/chemet/render/images/cutout/revert_16x16.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loader.revert();
                delete.setEnabled(loader.canBackup() || loader.canRevert());
                revert.setEnabled(loader.canRevert());
                updateButtons();
            }
        });
        revert.setToolTipText("Revert to the previous version of the index");

        final Animation expand = new Expand(configuration, 500);
        final Animation collapse = new Collapse(configuration, 500);

        configure = ButtonFactory.newCleanButton(ResourceUtility
                                                         .getIcon("/uk/ac/ebi/chemet/render/images/cutout/cog_16x16.png"), new AbstractAction() {
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

        // name will indicate the progress
        loader.addProgressListener(safeDispatch(new ProgressListener() {
            @Override public void progressed(String message) {
                // already on EDT
                name.setText(message);
            }
        }));


        update = ButtonFactory.newCleanButton(ResourceUtility
                                                      .getIcon("/uk/ac/ebi/chemet/render/images/cutout/update_16x16.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                SpinningDial dial = new SpinningDial(16, 16);
                name.setIcon(dial);
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
                            name.setText(loader.getName());
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

        cancel = ButtonFactory.newCleanButton(ResourceUtility
                                                      .getIcon("/uk/ac/ebi/chemet/render/images/cutout/cancel_16x16.png"), new AbstractAction() {
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

        info = loader.loaded() ? LabelFactory.newLabel("updated " + dateFormat
                .format(loader.updated())) : LabelFactory.newLabel("");
        info.setForeground(info.getForeground().brighter());
        Font orgFont = info.getFont();
        info.setFont(new Font(orgFont.getName(), orgFont
                .getStyle(), (int) (orgFont.getSize() * 0.9)));

        CellConstraints cc = new CellConstraints();

        Box controls = Box.createHorizontalBox();
        controls.add(Box.createHorizontalStrut(10));
        controls.add(delete);
        controls.add(Box.createHorizontalStrut(10));
        controls.add(revert);
        controls.add(Box.createHorizontalStrut(10));
        controls.add(configure);
        controls.add(Box.createHorizontalStrut(10));
        controls.add(update);
        controls.add(Box.createHorizontalStrut(10));
        controls.add(cancel);

        setLayout(new FormLayout("p, p:grow, right:p, 4dlu, min",
                                 "5px, p, 5px, p"));


        add(controls, cc.xy(1, 2));
        add(configuration, cc.xyw(1, 4, 3, cc.LEFT, cc.CENTER));
        add(name, cc.xy(2, 2));
        add(info, cc.xy(3, 2));
        add(cancel, cc.xy(5, 2));
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
                name.setText(loader.getName());
                if (loader.loaded()) {
                    info.setText("updated " + dateFormat
                            .format(loader.updated()));
                } else {
                    info.setText("");
                }
            }
        });
    }


    class LoaderConfiguration extends JPanel {

        private Map<String, LocationEditor> editors = new HashMap<String, LocationEditor>();
        private ResourceLoader loader;
        private LocationFactory factory;

        public LoaderConfiguration(ResourceLoader loader, LocationFactory factory) {

            setBorder(BorderFactory
                              .createMatteBorder(0, 1, 1, 1, Color.LIGHT_GRAY));

            this.loader = loader;
            this.factory = factory;

            FormLayout layout = new FormLayout("right:min, p");
            CellConstraints cc = new CellConstraints();
            setLayout(layout);
            editors = new HashMap<String, LocationEditor>();


            for (Map.Entry<String, LocationDescription> e : loader
                    .getRequiredResources().entrySet()) {


                layout.appendRow(new RowSpec(Sizes.PREFERRED));
                add(LabelFactory.newFormLabel(e.getValue().getName(), e
                        .getValue().getDescription()), cc.xy(1, layout
                        .getRowCount()));

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
                    ResourceLocation location = e.getValue()
                                                 .getResourceLocation();
                    // replace with individual UI components for selecting
                    if (location != null) {
                        loader.addLocation(key, location);
                    } else {
                        loader.removeLocation(key);
                    }
                } catch (IOException ex) {
                    LOGGER.error(ex.getMessage());
                }


            }


        }

    }


    class Expand extends ResizePanel implements AnimationListener {

        private JPanel panel;

        protected Expand(JPanel panel, long duration) {
            super(panel, new Dimension(panel.getPreferredSize().width, 0), panel
                    .getPreferredSize(), duration);
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
            final CalloutDialog dialog = new CalloutDialog(SwingUtilities
                                                             .getWindowAncestor(panel),
                                                     Dialog.ModalityType.MODELESS);

            dialog.setAnchor(configure);
            dialog.getMainPanel().add(LabelFactory
                                              .newLabel("Once configured, click the cog to register your changes"));
            dialog.setAlwaysOnTop(true);
            dialog.pack();
            dialog.place();
            dialog.setVisible(true);

            Timer t = new Timer(2500, new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                }
            });
            t.start();

        }

    }

    class Collapse extends ResizePanel implements AnimationListener {
        private JPanel panel;

        protected Collapse(JPanel panel, long duration) {
            super(panel, panel
                    .getPreferredSize(), new Dimension(panel.getPreferredSize().width, 0), duration);
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
            double proportion = (Math
                    .cos(position * Math.PI + Math.PI) + 1) / 2;

            int width = startSize.width + (int) ((xdiff) * proportion);
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
