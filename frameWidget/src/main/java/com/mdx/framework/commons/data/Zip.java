//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.commons.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zip {
    private static final int BUFFER = 2048;

    public Zip() {
    }

    public void zip(InputStream in, OutputStream out, String name) throws IOException {
        BufferedInputStream origin = null;
        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(out));
        byte[] data = new byte[2048];
        origin = new BufferedInputStream(in, 2048);
        ZipEntry entry = new ZipEntry(name);
        zipout.putNextEntry(entry);

        int count;
        while((count = origin.read(data, 0, 2048)) != -1) {
            zipout.write(data, 0, count);
        }

        origin.close();
        zipout.close();
        in.close();
    }

    public void zip(InputStream in, OutputStream out) throws IOException {
        this.zip(in, out, "retn");
    }

    public void unzip(String path, OutputStream out) throws IOException {
        this.unzip(new File(path), out);
    }

    public void unzip(File file, OutputStream out) throws IOException {
        this.unzip((InputStream)(new FileInputStream(file)), out);
    }

    public void unzip(InputStream in, OutputStream out) throws IOException {
        ZipInputStream zin = new ZipInputStream(in);
        if(zin.getNextEntry() != null) {
            BufferedInputStream bis = new BufferedInputStream(zin);
            BufferedOutputStream bos = new BufferedOutputStream(out, 2048);
            byte[] buf = new byte[2048];

            int count;
            while((count = bis.read(buf, 0, 2048)) != -1) {
                bos.write(buf, 0, count);
            }

            bos.close();
            bis.close();
            out.close();
        }

        zin.close();
        in.close();
    }

    public static boolean decompress(String file, String tofile) throws Exception {
        ZipFile zipfile = new ZipFile(file);
        File f = new File(tofile);
        return decompress(zipfile, f);
    }

    public static boolean decompress(File file, File toFile) throws Exception {
        return decompress(new ZipFile(file), toFile);
    }

    public static boolean decompress(ZipFile file, File toFile) {
        if(!toFile.exists() || !toFile.isDirectory()) {
            toFile.mkdirs();
        }

        Enumeration enm = file.entries();
        ZipEntry zipe = null;

        try {
            while((zipe = (ZipEntry)enm.nextElement()) != null) {
                if(zipe.isDirectory()) {
                    (new File(toFile.getPath() + "/" + zipe)).mkdirs();
                } else {
                    File tf = new File(toFile.getPath() + "/" + zipe);
                    write(file.getInputStream(zipe), tf);
                }
            }
        } catch (Exception var13) {
            ;
        } finally {
            try {
                file.close();
            } catch (IOException var12) {
                ;
            }

        }

        return false;
    }

    private static boolean write(InputStream in, File tfile) throws Exception {
        FileOutputStream fout = new FileOutputStream(tfile);
        byte[] buf = new byte[2048];

        int count;
        while((count = in.read(buf, 0, 2048)) != -1) {
            fout.write(buf, 0, count);
        }

        in.close();
        fout.close();
        return false;
    }
}
