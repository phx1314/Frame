//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.commons.data;

import com.mdx.framework.commons.data.Zip;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Serialize {
    public Serialize() {
    }

    public void serialize(Object obj, OutputStream out) throws IOException {
        ObjectOutputStream oots = new ObjectOutputStream(out);
        oots.writeObject(obj);
        oots.close();
        out.close();
    }

    public Object unSerialize(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream instream = new ObjectInputStream(in);
        Object retn = instream.readObject();
        in.close();
        return retn;
    }

    public static void serializeZip(Object obj, OutputStream out) throws IOException {
        Serialize slz = new Serialize();
        Zip zip = new Zip();
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        slz.serialize(obj, outstream);
        ByteArrayInputStream in = new ByteArrayInputStream(outstream.toByteArray());
        zip.zip(in, out);
    }

    public static Object unserializeZip(String path) throws IOException, ClassNotFoundException {
        return unserializeZip(new File(path));
    }

    public static Object unserializeZip(File file) throws IOException, ClassNotFoundException {
        Serialize slz = new Serialize();
        Zip zip = new Zip();
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        zip.unzip(file, outstream);
        return slz.unSerialize(new ByteArrayInputStream(outstream.toByteArray()));
    }
}
