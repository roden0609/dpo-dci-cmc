package hk.gov.cmc.utils.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import de.idyl.crypto.zip.AesZipFileDecrypter;
import de.idyl.crypto.zip.AesZipFileEncrypter;
import de.idyl.crypto.zip.impl.ExtZipEntry;

public class AesZipFileUtil {

    public static File zipAndEncrypt(String fileContentString, String unencryptedFilePath, String password)
            throws IOException {

        String zipFileName = unencryptedFilePath.substring(0, unencryptedFilePath.lastIndexOf(".")) + ".zip";
        File zipOutputFile = new File(zipFileName);

        ByteArrayInputStream bais = null;
        AesZipFileEncrypter enc = null;
        try {
            bais = new ByteArrayInputStream(fileContentString.getBytes("UTF-8"));
            enc = new AesZipFileEncrypter(zipOutputFile);
            enc.add(new File(unencryptedFilePath).getName(), bais, password);
        } finally {
            if (enc != null) {
                enc.close();
            }
            if (bais != null) {
                bais.close();
            }
        }

        return zipOutputFile;
    }

    public static File zipAndEncryptFile(String unencryptedFilePath, String password) throws IOException {

        String zipFileName = unencryptedFilePath.substring(0, unencryptedFilePath.lastIndexOf(".")) + ".zip";
        File zipOutputFile = new File(zipFileName);

        BufferedInputStream bis = null;
        FileInputStream fis = null;
        AesZipFileEncrypter enc = null;
        try {
            fis = new FileInputStream(unencryptedFilePath);
            bis = new BufferedInputStream(fis);
            enc = new AesZipFileEncrypter(zipOutputFile);
            enc.add(new File(unencryptedFilePath).getName(), bis, password);
        } finally {
            if (enc != null) {
                enc.close();
            }
            if (bis != null) {
                bis.close();
            }
            if (fis != null) {
                fis.close();
            }
        }

        return zipOutputFile;
    }

    public static File unzipAndDecryptFile(String encryptedFilePath, String password) throws Exception {

        AesZipFileDecrypter dec = null;
        File outputFile = null;
        try {
            dec = new AesZipFileDecrypter(new File(encryptedFilePath));
            List<ExtZipEntry> entries = dec.getEntryList();
            for (ExtZipEntry zipEntry : entries) {
                String fileDir = new File(encryptedFilePath).getParentFile().getPath();
                outputFile = new File(fileDir + "/" + zipEntry.getName());
                dec.extractEntry(zipEntry, outputFile, password);
            }
        } finally {
            if (dec != null) {
                dec.close();
            }
        }

        return outputFile;
    }
}
