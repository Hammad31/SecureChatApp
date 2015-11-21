package Security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;

public class Final_RSA {
    public KeyPairGenerator kpg;
    public KeyPair kp;
    public PublicKey publicKey;
    public PrivateKey privateKey;
    public byte[] publicKeyBytes;
    public byte[] privateKeyBytes;

    public Final_RSA()
            throws Exception {

        kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(4096);
        kp = kpg.genKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();

        publicKeyBytes = publicKey.getEncoded();
        privateKeyBytes = privateKey.getEncoded();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        X509EncodedKeySpec pub = new X509EncodedKeySpec(publicKeyBytes);
        publicKey = keyFactory.generatePublic(pub);

        PKCS8EncodedKeySpec pri = new PKCS8EncodedKeySpec(privateKeyBytes);
        privateKey = keyFactory.generatePrivate(pri);

        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.ENCRYPT_MODE, kp.getPublic());

        String myMessage = new String("Secret Message");
        SealedObject myEncyptedMessage = new SealedObject(myMessage, c);


    }

    private static void dumpKeyPair(KeyPair keyPair) {
        PublicKey pub = keyPair.getPublic();
        System.out.println("Public Key: " + getHexString(pub.getEncoded()));
        PrivateKey priv = keyPair.getPrivate();
        System.out.println("Private Key: " + getHexString(priv.getEncoded()));
    }

    private static String getHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

}
