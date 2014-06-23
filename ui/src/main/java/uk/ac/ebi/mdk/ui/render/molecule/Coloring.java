/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
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

package uk.ac.ebi.mdk.ui.render.molecule;

import uk.ac.ebi.caf.utility.preference.type.StringPreference;
import uk.ac.ebi.mdk.domain.DomainPreferences;

import java.awt.Color;

/**
 * Structure depiction color scheeme.
 */
public enum Coloring {
    /** Elements and lines are drawn black. */
    BLACK(new Color(0x444444), Color.WHITE),
    /** Elements and lines are drawn white. */
    WHITE(Color.WHITE, new Color(0x444444)),

    /**
     * Corey, Pauling, and Koltun colouring of different element types. Lines
     * are drawn black.
     */
    CPK(new Color(0x444444), Color.WHITE);

    private final Color fg, bg;

    private Coloring(Color fg, Color bg) {
        this.fg = fg;
        this.bg = bg;
    }

    /**
     * Foreground color.
     *
     * @return color
     */
    public Color fgColor() {
        return fg;
    }

    /**
     * Background color.
     *
     * @return color
     */
    public Color bgColor() {
        return bg;
    }

    /**
     * Load the preferred color from preferences.
     * @return coloring
     */
    public static Coloring loadPreference() {
        DomainPreferences preferences = DomainPreferences.getInstance();
        StringPreference  pref = preferences.getPreference("STRUCT_VIEW_COL");
        Coloring coloring = Coloring.valueOf(pref.get());
        if (coloring == null)
            return BLACK;
        return coloring;
    }

    /**
     * Store this color as the preferred coloring.
     */
    public void storePreference() {
        DomainPreferences preferences = DomainPreferences.getInstance();
        StringPreference  pref = preferences.getPreference("STRUCT_VIEW_COL");
        pref.put(name());
    }
}