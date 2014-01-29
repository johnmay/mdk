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

package uk.ac.ebi.mdk.io;

import uk.ac.ebi.caf.utility.version.Version;

/**
 * @author John May
 */
public class IOConstants {
    @Deprecated
    public static final Version VERSION = new Version(1, 3, 9, 0);

    public static final Version v_1_4_1 = new Version(1, 4, 1, 0);
    public static final Version v_1_4_0 = new Version(1, 4, 0, 0);
    public static final Version v_1_3_9 = new Version(1, 3, 9, 0);

    public static final Version CURRENT = v_1_4_1;
}
