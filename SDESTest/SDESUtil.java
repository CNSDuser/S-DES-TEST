import java.nio.charset.StandardCharsets;

public final class SDESUtil {
    private SDESUtil() {}

    // 将一个字节转换为8位比特数组（高位在前）
    public static int[] byteToBits(byte b) {
        int[] bits = new int[8];
        for (int i = 7; i >= 0; i--) {
            bits[7 - i] = (b >> i) & 1;
        }
        return bits;
    }

    // 将8位比特数组转换为字节（只取前8位）
    public static byte bitsToByte(int[] bits) {
        int value = 0;
        for (int i = 0; i < 8 && i < bits.length; i++) {
            value = (value << 1) | (bits[i] & 1);
        }
        return (byte) (value & 0xFF);
    }

    // 单字节加/解密
    public static byte encryptByte(byte p, int[] key10) {
        int[] pBits = byteToBits(p);
        int[] cBits = SDES.encrypt(pBits, key10);
        return bitsToByte(cBits);
    }

    public static byte decryptByte(byte c, int[] key10) {
        int[] cBits = byteToBits(c);
        int[] pBits = SDES.decrypt(cBits, key10);
        return bitsToByte(pBits);
    }

    // 批量：字节数组
    public static byte[] encryptBytes(byte[] plain, int[] key10) {
        byte[] out = new byte[plain.length];
        for (int i = 0; i < plain.length; i++) {
            out[i] = encryptByte(plain[i], key10);
        }
        return out;
    }

    public static byte[] decryptBytes(byte[] cipher, int[] key10) {
        byte[] out = new byte[cipher.length];
        for (int i = 0; i < cipher.length; i++) {
            out[i] = decryptByte(cipher[i], key10);
        }
        return out;
    }

    // 字符串（ASCII）
    public static byte[] stringToAscii(String s) {
        return s.getBytes(StandardCharsets.US_ASCII);
    }

    public static String asciiToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.US_ASCII);
    }

    // HEX 工具（大写、每字节两位）
    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    public static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            sb.append(HEX[(b >>> 4) & 0x0F]).append(HEX[b & 0x0F]);
        }
        return sb.toString();
    }

    public static byte[] fromHex(String hex) {
        String s = hex.replaceAll("\\s+", "").toUpperCase();
        if (s.length() % 2 != 0) throw new IllegalArgumentException("HEX长度必须为偶数");
        byte[] out = new byte[s.length() / 2];
        for (int i = 0; i < out.length; i++) {
            int hi = Character.digit(s.charAt(2 * i), 16);
            int lo = Character.digit(s.charAt(2 * i + 1), 16);
            if (hi < 0 || lo < 0) throw new IllegalArgumentException("非法HEX字符");
            out[i] = (byte) ((hi << 4) | lo);
        }
        return out;
    }

    // 将十进制0..1023转 10 位二进制数组
    public static int[] intTo10BitKey(int k) {
        int[] bits = new int[10];
        for (int i = 9; i >= 0; i--) {
            bits[9 - i] = (k >> i) & 1;
        }
        return bits;
    }

    public static String bits10ToBinaryString(int[] bits10) {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) sb.append(bits10[i] & 1);
        return sb.toString();
    }
}
