package com.jdh.community_spring.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

public class SimplePasswordEncoder {
  private final static int SALT_LENGTH = 16;
  public static String encode(String rawPassword) {
    byte[] salt = createSalt(SALT_LENGTH);

    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(salt);
      byte[] hashedPassword = md.digest(rawPassword.getBytes());

      return HexFormat.of().formatHex(salt) + HexFormat.of().formatHex(hashedPassword);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("암호화 알고리즘이 존재하지 않음", e);
    }
  }

  private static byte[] createSalt(int length) {
    byte[] salt = new byte[length];
    SecureRandom random = new SecureRandom();
    random.nextBytes(salt);

    return salt;
  }
}
