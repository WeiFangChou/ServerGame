package com.lineage.server.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DigitalUtil {
    private static final Log _log = LogFactory.getLog(DigitalUtil.class);

    public static long returnMin(long[] longs) {
        long i = -1;
        try {
            for (long count : longs) {
                if (i == -1) {
                    i = count;
                }
                i = Math.min(i, count);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return i;
    }

    public static long returnMin(int[] ints) {
        long i = -1;
        try {
            for (int i2 : ints) {
                long count = (long) i2;
                if (i == -1) {
                    i = count;
                }
                i = Math.min(i, count);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return i;
    }

    public static long returnMin(short[] shorts) {
        long i = -1;
        try {
            for (short s : shorts) {
                long count = (long) s;
                if (i == -1) {
                    i = count;
                }
                i = Math.min(i, count);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return i;
    }

    public static long returnMin(byte[] bytes) {
        long i = -1;
        try {
            for (byte b : bytes) {
                long count = (long) b;
                if (i == -1) {
                    i = count;
                }
                i = Math.min(i, count);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return i;
    }

    public static long returnMax(long[] longs) {
        long i = -1;
        try {
            for (long count : longs) {
                i = Math.max(i, count);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return i;
    }

    public static long returnMax(int[] ints) {
        long i = -1;
        try {
            for (int i2 : ints) {
                i = Math.max(i, (long) i2);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return i;
    }

    public static long returnMax(short[] shorts) {
        long i = -1;
        try {
            for (short s : shorts) {
                i = Math.max(i, (long) s);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return i;
    }

    public static long returnMax(byte[] bytes) {
        long i = -1;
        try {
            for (byte b : bytes) {
                i = Math.max(i, (long) b);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return i;
    }
}
