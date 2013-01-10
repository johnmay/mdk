/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.service.loader;

import org.junit.Test;
import org.mockito.Mockito;
import uk.ac.ebi.mdk.service.ProgressListener;

import java.io.IOException;

/**
 * @author John May
 */
public class AbstractResourceLoaderTest {

    AbstractResourceLoader loader = new AbstractResourceLoader() {
        @Override public boolean canBackup() {
            return false;
        }

        @Override public boolean canRevert() {
            return false;
        }

        @Override public void update() throws IOException {

        }

        @Override public void backup() {

        }

        @Override public void revert() {

        }

        @Override public void clean() {

        }

        @Override public String getName() {
            return null;
        }
    };

    @Test(expected = IllegalArgumentException.class)
    public void testAddProgressListener() throws Exception {
        loader.addProgressListener(null);
    }

    @Test
    public void testFireProgressUpdate_String() throws Exception {
        ProgressListener listener = Mockito.mock(ProgressListener.class);
        loader.addProgressListener(listener);
        loader.fireProgressUpdate("mocked message");
        Mockito.verify(listener, Mockito.times(1)).progressed("mocked message");
    }

    @Test
    public void testFireProgressUpdate_double() throws Exception {
        ProgressListener listener = Mockito.mock(ProgressListener.class);
        loader.addProgressListener(listener);
        loader.fireProgressUpdate(0.5d);
        Mockito.verify(listener, Mockito.times(1)).progressed(Mockito.anyString());
    }
}
