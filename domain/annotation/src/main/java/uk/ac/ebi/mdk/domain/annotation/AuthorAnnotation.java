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

package uk.ac.ebi.mdk.domain.annotation;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.MetaInfo;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.domain.annotation.primitive.StringAnnotation;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.domain.DefaultLoader;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/**
 *          AuthorAnnotation – 2011.09.14 <br>
 *          Class metaInfo
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Context
@Brief("Comment")
@Description("Short comment that has been added by an author")
public class AuthorAnnotation
        extends AbstractAnnotation
        implements StringAnnotation {

    private static final Logger LOGGER = Logger.getLogger(AuthorAnnotation.class);

    private String author;

    private String annotation;

    private static MetaInfo metaInfo = DefaultLoader.getInstance().getMetaInfo(
            AuthorAnnotation.class);


    public AuthorAnnotation() {
    }


    /**
     *
     * Constructor using the system property 'user.name' as the author
     *
     * @param annotation
     *
     */
    public AuthorAnnotation(String annotation) {
        this(System.getProperty("user.name"), annotation);
    }


    /**
     *
     * Constructor providing both author and the described annotation
     *
     * @param author
     * @param annotation
     *
     */
    public AuthorAnnotation(String author, String annotation) {
        this.author = author;
        this.annotation = annotation;
    }


    /**
     *
     * Accessor to the annotation described by the author
     *
     * @return
     *
     */
    public String getAnnotation() {
        return annotation;
    }


    /**
     *
     * Accessor to the author of the annotation
     *
     * @return
     */
    public String getAuthor() {
        return author;
    }


    public void setAuthor(String author) {
        this.author = author;
    }





    /**
     *
     * Returns the author annotation in the form: <pre> @author ...annotation..</pre>
     *
     */
    @Override
    public String toString() {
        return annotation;
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        author = in.readUTF();
        annotation = in.readUTF();
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeUTF(author);
        out.writeUTF(annotation);
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return author == null ? "Author Comment " : "@" + author;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return metaInfo.description;
    }




    /**
     * @inheritDoc
     */
    @Override
    public AuthorAnnotation newInstance() {
        return new AuthorAnnotation();
    }


    public String getValue() {
        return annotation;
    }


    public void setValue(String value) {
        this.annotation = value;
    }


    public AuthorAnnotation getInstance(String value) {
        return new AuthorAnnotation(value);
    }
}
