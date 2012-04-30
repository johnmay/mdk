/**
 * ResourcePanel.java
 *
 * 2011.10.27
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
package uk.ac.ebi.render.resource;

import javax.swing.*;


/**
 *          ResourcePanel - 2011.10.27 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ResourcePanel extends JPanel {
//
//    private static final Logger LOGGER = Logger.getLogger(ResourcePanel.class);
//
//    private RemoteResourceManager manager = RemoteResourceManager.getInstance();
//
//    private CellConstraints cc = new CellConstraints();
//
//    private FormLayout layout = new FormLayout("min, 4dlu, min, 4dlu, p, 4dlu, p, 4dlu, min");
//
//    private Map<RemoteResource, JLabel> labelMap = new HashMap();
//
//    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm");
//
//
//    public ResourcePanel() {
//
//        setBackground(Color.WHITE);
//        setBorder(Borders.createEmptyBorder("6dlu, 6dlu, 6dlu, 6dlu"));
//        setLayout(layout);
//        for (RemoteResource resource : manager.getResources()) {
//            layout.appendRow(new RowSpec(Sizes.PREFERRED));
//
//            JLabel desc = new JLabel();
//            JLabel date = new JLabel();
//            JTextField local = new JTextField(20);
//            JTextField remote = new JTextField(20);
//            JButton update = new JButton(new UpdateResource(resource, date));
//
//            local.setEditable(false);
//            remote.setEditable(false);
//
//            date.setFont(new Font("VERDANA", Font.BOLD, 11));
//            date.setFont(new Font("VERDANA", Font.PLAIN, 11));
//            local.setFont(new Font("VERDANA", Font.PLAIN, 8));
//            remote.setFont(new Font("VERDANA", Font.PLAIN, 8));
//            update.setFont(new Font("VERDANA", Font.PLAIN, 11));
//
//            desc.setText(resource.getDescription() + ":");
//            desc.setHorizontalAlignment(SwingConstants.RIGHT);
//            date.setText(resource.getLastUpdated().getTime() == 0l ? "Not loaded" : dateFormat.format(resource.getLastUpdated()));
//            local.setText(resource.getLocal().getPath());
//            remote.setText(resource.getRemote().toString());
//
//            labelMap.put(resource, desc);
//
//            add(desc, cc.xy(1, layout.getRowCount()));
//            add(date, cc.xy(3, layout.getRowCount()));
//            add(local, cc.xy(5, layout.getRowCount()));
//            add(remote, cc.xy(7, layout.getRowCount()));
//            add(update, cc.xy(9, layout.getRowCount()));
//        }
//    }
//
//
//    private class UpdateResource extends AbstractAction {
//
//        private RemoteResource resource;
//
//        private AbstractAction button;
//
//        private JLabel date;
//
//
//        public UpdateResource(RemoteResource resource, JLabel date) {
//            super("Update");
//            this.resource = resource;
//            this.date = date;
//        }
//
//
//        public void actionPerformed(ActionEvent e) {
//            this.button = this;
//
//            try {
//                this.setEnabled(false);
//                labelMap.get(resource).setIcon(new SpinningDial(16, 16));
//                SwingWorker worker = new SwingWorker() {
//
//                    @Override
//                    protected Object doInBackground() throws Exception {
//                        deleteDir(resource.getLocal());
//                        resource.update();
//                        SwingUtilities.invokeLater(new Runnable() {
//
//                            public void run() {
//                                button.setEnabled(true);
//                                date.setText(resource.getLastUpdated().getTime() == 0l ? "Not loaded" : dateFormat.format(resource.getLastUpdated()));
//                                labelMap.get(resource).setIcon(null);
//                            }
//                        });
//                        return null;
//                    }
//                };
//                worker.execute();
//            } catch (Exception ex) {
//                java.util.logging.Logger.getLogger(ResourcePanel.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//
//
//    public static boolean deleteDir(File dir) {
//        if (dir.isDirectory()) {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++) {
//                boolean success = deleteDir(new File(dir, children[i]));
//                if (!success) {
//                    return false;
//                }
//            }
//        }
//
//        // The directory is now empty so delete it
//        return dir.delete();
//    }
//
//
//    public static void main(String[] args) {
//
//        SwingUtilities.invokeLater(new Runnable() {
//
//            public void run() {
//                JFrame frame = new JFrame();
//                frame.setTitle("Resource Manager");
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.add(new ResourcePanel());
//                frame.pack();
//                frame.setVisible(true);
//            }
//        });
//
//    }
}
