package co.edu.ucc.motivaback.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
public class KeyPairComponent {
    private KeyPair keysPair;

    public KeyPairComponent(@Value("${keyPairs.public.location}") String publicKeyPath, @Value("${keyPairs.private.location}") String privateKeyPath) {
        try {
            this.keysPair = new KeyPair(loadPublic(publicKeyPath), loadPrivate(privateKeyPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PrivateKey loadPrivate(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
        var spec = new PKCS8EncodedKeySpec(keyBytes);
        var kf = KeyFactory.getInstance("RSA");

        return kf.generatePrivate(spec);
    }

    public static PublicKey loadPublic(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
        var spec = new X509EncodedKeySpec(keyBytes);
        var kf = KeyFactory.getInstance("RSA");

        return kf.generatePublic(spec);
    }

    public KeyPair getKeysPair() {
        return keysPair;
    }
}
