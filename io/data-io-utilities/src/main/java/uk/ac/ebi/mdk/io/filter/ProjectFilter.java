/*
 * Copyright (C) 2013 John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.io.filter;

import java.io.File;
import javax.swing.filechooser.FileFilter;


/**
 * MNBFileFilter.java
 *
 *
 * @author johnmay
 * @date Apr 15, 2011
 */
public class ProjectFilter extends FileFilter {

    @Override
    public boolean accept(File f) {

        if( ! f.isDirectory() ) {
            return false;
        }

        String path = f.getPath();
        int lastIndex = path.lastIndexOf(".");
        if( lastIndex != -1 ) {
            String extension = path.substring(lastIndex);
            if( extension.equals(".mnb") ) {
                return true;
            }
        }

        return false;

    }


    @Override
    public String getDescription() {
        return "MNB Project";
    }


}

