import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public final class SDESCracker {
    private SDESCracker() {}

    // 针对单对 (P,C) 的暴力破解
    public static List<int[]> bruteForceKeysForPair(byte p, byte c) {
        final List<int[]> matches = Collections.synchronizedList(new ArrayList<>());
        IntStream.range(0, 1024).parallel().forEach(k -> {
            int[] key10 = SDESUtil.intTo10BitKey(k);
            byte enc = SDESUtil.encryptByte(p, key10);
            if (enc == c) {
                matches.add(key10);
            }
        });
        return matches;
    }

    // 多对 (P,C) 的暴力破解
    public static List<int[]> bruteForceKeysForPairs(byte[] plains, byte[] ciphers) {
        if (plains.length != ciphers.length) throw new IllegalArgumentException("明密文对数量不一致");
        final List<int[]> matches = Collections.synchronizedList(new ArrayList<>());
        IntStream.range(0, 1024).parallel().forEach(k -> {
            int[] key10 = SDESUtil.intTo10BitKey(k);
            boolean ok = true;
            for (int i = 0; i < plains.length; i++) {
                if (SDESUtil.encryptByte(plains[i], key10) != ciphers[i]) {
                    ok = false;
                    break;
                }
            }
            if (ok) matches.add(key10);
        });
        return matches;
    }
}


