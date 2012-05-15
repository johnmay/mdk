package uk.ac.ebi.mdk.ui.component.compare;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.list.MutableJList;
import uk.ac.ebi.mdk.ui.render.list.DefaultRenderer;

import javax.swing.*;

/**
 * Provides editing of entity matcher preference
 *
 * @author John May
 */
public class MatcherStackList extends MutableJList<MatcherDescription> {

    private static final Logger LOGGER = Logger.getLogger(MatcherStackList.class);

    public MatcherStackList() {
        super(MatcherDescription.class);
        setCellRenderer(new DefaultRenderer<MatcherDescription>() {
            @Override
            public JLabel getRendererComponent(JList list, MatcherDescription matcher, int index, boolean isSelected, boolean cellHasFocus) {

                JLabel label = super.getRendererComponent(list, matcher, index, isSelected, cellHasFocus);

                label.setText(matcher.getName());
                label.setToolTipText(matcher.getDescription());

                return label;

            }
        });
    }

}
