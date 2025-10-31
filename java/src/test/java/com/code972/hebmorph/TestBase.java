/***************************************************************************
 *   Copyright (C) 2010-2015 by                                            *
 *      Itamar Syn-Hershko <itamar at code972 dot com>                     *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Affero General Public License           *
 *   version 3, as published by the Free Software Foundation.              *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU Affero General Public License for more details.                   *
 *                                                                         *
 *   You should have received a copy of the GNU Affero General Public      *
 *   License along with this program; if not, see                          *
 *   <http://www.gnu.org/licenses/>.                                       *
 **************************************************************************/
package com.code972.hebmorph;

import com.code972.hebmorph.datastructures.DictHebMorph;
import com.code972.hebmorph.hspell.HSpellDictionaryLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public abstract class TestBase {
    private static DictHebMorph dict;

    public static String DICT_PATH = null;

    static {
        try {
            DICT_PATH = resolveDictPath();
        } catch (Exception ignored) {
            // keep default relative path to avoid NPE; loader will error nicely if missing
            DICT_PATH = "../hspell-data-files/";
        }
    }

    private static String resolveDictPath() throws IOException {
        if (DICT_PATH != null) return DICT_PATH;

        // 1) System property override
        String sysProp = System.getProperty("hebmorph.hspell.path");
        if (sysProp != null && sysProp.trim().length() > 0) {
            DICT_PATH = ensureTrailingSlash(new File(sysProp).getCanonicalPath());
            return DICT_PATH;
        }

        // 2) Environment variable override
        String env = System.getenv("HEBMORPH_HSPELL_PATH");
        if (env != null && env.trim().length() > 0) {
            DICT_PATH = ensureTrailingSlash(new File(env).getCanonicalPath());
            return DICT_PATH;
        }

        // 3) Try common relative locations from current working dir
        String[] bases = new String[] {".", "..", "../..", "../../.."};
        for (String base : bases) {
            File f = new File(base + "/hspell-data-files");
            if (f.exists() && f.isDirectory()) {
                DICT_PATH = ensureTrailingSlash(f.getCanonicalPath());
                return DICT_PATH;
            }
        }

        // 4) Fallback to default expected sibling path for repo layout
        File sibling = new File("../hspell-data-files");
        if (sibling.exists() && sibling.isDirectory()) {
            DICT_PATH = ensureTrailingSlash(sibling.getCanonicalPath());
            return DICT_PATH;
        }

        throw new IOException("Cannot locate hspell-data-files. Set -Dhebmorph.hspell.path or HEBMORPH_HSPELL_PATH env var.");
    }

    private static String ensureTrailingSlash(String p) {
        return p.endsWith("/") ? p : (p + "/");
    }

    protected synchronized DictHebMorph getDictionary() throws IOException {
        if (dict == null) {
            dict = new HSpellDictionaryLoader().loadDictionaryFromPath(resolveDictPath());
        }
        return dict;
    }

    protected static String readFileToString(String path) throws IOException {
        try (FileInputStream stream = new FileInputStream(new File(path))) {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            /* Instead of using default, pass in a decoder. */
            return Charset.forName("UTF-8").decode(bb).toString();
        }
    }
}
