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

import java.io.FileFilter;

/**
 *
 * @author johnmay
 * @date   Apr 18, 2011
 */
public class FileFilterManager {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( FileFilterManager.class );

    private ProjectFilter mnb;
    private FastaFileFilter fasta;

    private FileFilterManager() {
        mnb = new ProjectFilter();
        fasta = new FastaFileFilter();
    }

    public static FileFilterManager getInstance() {
        return FileFilterManagerHolder.INSTANCE;
    }

    private static class FileFilterManagerHolder {
        private static final FileFilterManager INSTANCE = new FileFilterManager();
    }

    public ProjectFilter getProjectFilter() {
        return mnb;
    }

    public FastaFileFilter getFastaFilter() {
        return fasta;
    }

 }
