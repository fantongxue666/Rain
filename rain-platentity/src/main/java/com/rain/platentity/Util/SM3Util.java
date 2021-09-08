package com.rain.platentity.Util;

import org.bouncycastle.crypto.digests.SM3Digest;

public class SM3Util  {

    public static byte[] hash(byte[] srcData) {
        SM3Digest digest = new SM3Digest();
        digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }
}
