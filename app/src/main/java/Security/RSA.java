package Security;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA {
    public static KeyPair generatedKeyPair;
    public static void main(String[] args) throws Exception {
        Main();
    }

	/* / *//* / */

    public static void Main() {
        try {
            String path = "C:/Users/LENOVO/workspace/RSA";

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

            keyGen.initialize(4096);
            generatedKeyPair = keyGen.genKeyPair();

            //System.out.println("Generated Key Pair");
            //dumpKeyPair(generatedKeyPair);
            //SaveKeyPair(path, generatedKeyPair);

            //KeyPair loadedKeyPair = LoadKeyPair(path, "RSA");
            //System.out.println("Loaded Key Pair");
            //dumpKeyPair(loadedKeyPair);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private static void dumpKeyPair(KeyPair keyPair) {
        PublicKey pub = keyPair.getPublic();
        System.out.println("Public Key: " + getHexString(pub.getEncoded()));
        System.out.println("Public Key: " +"30820222300d06092a864886f70d01010105000382020f003082020a0282020100d7d774dd8a5326b7e17d74796ae19b1ba0d7f905cc9074b4001bed78512bcdf9330b813bf70c5bcf0c389ddb557f6152e144ae0ee6b3b1a5c0a10fd93130ca5470473396e2ffb809e1743654017ed3646ce342f8bda2cb788c6f64b03ba75f9d6bf28bf039bb4c0ccb0c0921ae7f4ba4c6eb6d211fdbdc52677a9a6ef150ac7d15a44eafb510ad16229da438966fb1e9bc6425e67518f99ffadece69935f571f6d375e16fa930335c9656386525042e8c35b2c4286410267911960542ddef1c620d30a631c2d41f2be09bd6df0eaad3333987e8c94c42691d97695701ae2ac0e1e48ea925c86cbaf48ccdc8353a51ca6e95236a4de948028ffde2ffa44f5888a411fb02dfe6f77ea40057714b02a7f7a22173d6f09336bb39a2067ae67cdf26d5ab652850d897b31013a0dfae1a6b24c3713670b6ec45ecae586996f65e00d50a258d8d061e9b69cb61571c97ec5ceeb2c343f43e9c17d9ce1a6107ab241631f1ca556ae821f7a6c468f13bb6da0fc27a15189b8b58d43c5b9a5d3e06d2689d399ba3567a55f2de1440e035746c87bdcf0635588f9d000aeb3bd0122e233010b454a7e5131e2061f82ca1a5c81b54faf269afa2ef4aeb6ae5b2a79cbb1ac9e770ecd138b66178d0267232a379bcd191358ff4a3374a819a93966188708d3991605c5d5f89146086fb048600116268fab91867434ed0c599d643b7ce313a1a4450203010001");
        PrivateKey priv = keyPair.getPrivate();
        System.out.println("Private Key: " + getHexString(priv.getEncoded()));
    }

    public static String getPublicKey(KeyPair keyPair){
        PublicKey pub = keyPair.getPublic();
        return getHexString(pub.getEncoded());
    }


    public static String getPrivateKey(KeyPair keyPair){
        PrivateKey pub = keyPair.getPrivate();
        return getHexString(pub.getEncoded());
    }

    private static String getHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static void SaveKeyPair(String path, KeyPair keyPair) throws IOException {
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // Store Public Key.
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        FileOutputStream fos = new FileOutputStream(path + "/public.key");
        fos.write(x509EncodedKeySpec.getEncoded());
        fos.close();

        // Store Private Key.
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        fos = new FileOutputStream(path + "/private.key");
        fos.write(pkcs8EncodedKeySpec.getEncoded());
        fos.close();
    }

    public static KeyPair LoadKeyPair(String path, String algorithm)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Read Public Key.
        File filePublicKey = new File(path + "/public.key");
        FileInputStream fis = new FileInputStream(path + "/public.key");
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();

        // Read Private Key.
        File filePrivateKey = new File(path + "/private.key");
        fis = new FileInputStream(path + "/private.key");
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();

        // Generate KeyPair.
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        return new KeyPair(publicKey, privateKey);
    }

}
