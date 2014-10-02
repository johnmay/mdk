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
package uk.ac.ebi.mdk.ui.edit.annotation;

import javax.swing.*;

import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.ui.edit.crossreference.IdentifierEditor;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;


/**
 *
 *          CrossReferenceEditor 2012.02.15
 * @version $Rev: 1472 $ : Last Changed $Date: 2012-02-15 12:21:00 +0000 (Wed, 15 Feb 2012) $
 * @author  johnmay
 * @author  $Author: johnmay $ (this version)
 *
 *          Class description
 *
 */
public class CrossReferenceEditor<X extends CrossReference>
        extends AbstractAnnotationEditor<X> {

    private IdentifierEditor identifierEditor;
    private Collection<Class<? extends Identifier>> accept = new HashSet<Class<? extends Identifier>>();

    public CrossReferenceEditor() {
        identifierEditor = new IdentifierEditor();
    }

    public CrossReferenceEditor(Class<? extends Identifier> ... identifiers) {
        accept.addAll(Arrays.asList(identifiers));
        identifierEditor = new IdentifierEditor(accept);
    }


    @Override
    @SuppressWarnings("unchecked")
    public X newAnnotation() {
        X xref = super.newAnnotation();
        if (identifierEditor.isFilled()) {
            xref.setIdentifier(identifierEditor.getIdentifier());
        }
        return xref;
    }


    @Override
    public void setAnnotation(X annotation) {
        super.setAnnotation(annotation);
        if (annotation.getIdentifier() != null) {
            identifierEditor.setIdentifier(annotation.getIdentifier());
        }
    }

    @Override
    public JComponent getComponent() {
        return identifierEditor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CrossReferenceEditor newInstance() {
        return new CrossReferenceEditor(accept.toArray(new Class[0]));
    }
}
