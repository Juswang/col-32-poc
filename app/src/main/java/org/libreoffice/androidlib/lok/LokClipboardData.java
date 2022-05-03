package org.libreoffice.androidlib.lok;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes3.dex */
public class LokClipboardData implements Serializable {
    public ArrayList<LokClipboardEntry> clipboardEntries = new ArrayList<>();

    public String getText() {
        Iterator<LokClipboardEntry> it = this.clipboardEntries.iterator();
        while (it.hasNext()) {
            LokClipboardEntry aEntry = it.next();
            if (aEntry.mime.startsWith("text/plain")) {
                return new String(aEntry.data, StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    public String getHtml() {
        Iterator<LokClipboardEntry> it = this.clipboardEntries.iterator();
        while (it.hasNext()) {
            LokClipboardEntry aEntry = it.next();
            if (aEntry.mime.startsWith("text/html")) {
                return new String(aEntry.data, StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    public boolean writeToFile(File file) {
        try {
            FileOutputStream fileStream = new FileOutputStream(file.getAbsoluteFile());
            ObjectOutputStream oos = new ObjectOutputStream(fileStream);
            oos.writeObject(this);
            oos.close();
            fileStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static LokClipboardData createFromFile(File file) {
        try {
            FileInputStream fileStream = new FileInputStream(file.getAbsoluteFile());
            ObjectInputStream ois = new ObjectInputStream(fileStream);
            LokClipboardData data = (LokClipboardData) ois.readObject();
            ois.close();
            fileStream.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public LokClipboardEntry getBest() {
        if (!this.clipboardEntries.isEmpty()) {
            return this.clipboardEntries.get(0);
        }
        return null;
    }
}
