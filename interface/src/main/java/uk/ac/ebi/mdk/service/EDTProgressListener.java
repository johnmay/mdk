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

package uk.ac.ebi.mdk.service;

import javax.swing.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Intercept progress message and send on the Event Dispatch Thread (EDT).
 * @author John May
 */
public class EDTProgressListener implements ProgressListener {

    private final ProgressListener listener;

    private EDTProgressListener(ProgressListener listener) {
        checkNotNull(listener);
        this.listener = listener;
    }

    @Override public void progressed(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                listener.progressed(message);
            }
        });
    }

    public static ProgressListener safeDispatch(ProgressListener listener){
        return new EDTProgressListener(listener);
    }
}
