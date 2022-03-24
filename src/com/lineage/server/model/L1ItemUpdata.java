package com.lineage.server.model;

import com.lineage.config.ConfigRate;

public class L1ItemUpdata {
    public static double enchant_wepon_up9x(double enchant_level_tmp) {
        if (enchant_level_tmp <= 0.0d) {
            enchant_level_tmp = 1.0d;
        }
        return (100.0d + (((double) ConfigRate.ENCHANT_CHANCE_WEAPON) * enchant_level_tmp)) / ((enchant_level_tmp - 7.0d) * enchant_level_tmp);
    }

    public static double enchant_wepon_up9(double enchant_level_tmp) {
        if (enchant_level_tmp <= 0.0d) {
            enchant_level_tmp = 1.0d;
        }
        return (100.0d + (((double) ConfigRate.ENCHANT_CHANCE_WEAPON) * enchant_level_tmp)) / (2.0d * enchant_level_tmp);
    }

    public static double enchant_wepon_dn9(double enchant_level_tmp) {
        if (enchant_level_tmp <= 0.0d) {
            enchant_level_tmp = 1.0d;
        }
        return (100.0d + (((double) ConfigRate.ENCHANT_CHANCE_WEAPON) * enchant_level_tmp)) / enchant_level_tmp;
    }

    public static double enchant_armor_up9x(double enchant_level_tmp) {
        if (enchant_level_tmp <= 0.0d) {
            enchant_level_tmp = 1.0d;
        }
        return (100.0d + (((double) ConfigRate.ENCHANT_CHANCE_ARMOR) * enchant_level_tmp)) / ((enchant_level_tmp - 7.0d) * enchant_level_tmp);
    }

    public static double enchant_armor_up9(double enchant_level_tmp) {
        if (enchant_level_tmp <= 0.0d) {
            enchant_level_tmp = 1.0d;
        }
        return (100.0d + (((double) ConfigRate.ENCHANT_CHANCE_ARMOR) * enchant_level_tmp)) / (2.0d * enchant_level_tmp);
    }

    public static double enchant_armor_dn9(double enchant_level_tmp) {
        if (enchant_level_tmp <= 0.0d) {
            enchant_level_tmp = 1.0d;
        }
        return (100.0d + (((double) ConfigRate.ENCHANT_CHANCE_ARMOR) * enchant_level_tmp)) / enchant_level_tmp;
    }

    public static int enchant_Dmg(int enchant_level_tmp) {
        if (enchant_level_tmp <= 0) {
            enchant_level_tmp = 1;
        }
        return 35 / enchant_level_tmp;
    }

    public static int enchant_Hit(int enchant_level_tmp) {
        if (enchant_level_tmp <= 0) {
            enchant_level_tmp = 1;
        }
        return 35 / enchant_level_tmp;
    }

    public static int enchant_Sp(int enchant_level_tmp) {
        if (enchant_level_tmp <= 0) {
            enchant_level_tmp = 1;
        }
        return 35 / enchant_level_tmp;
    }

    /* renamed from: x1 */
    public static int m7x1() {
        return 1;
    }

    /* renamed from: x2 */
    public static int m8x2() {
        return 2;
    }

    /* renamed from: x3 */
    public static int m9x3() {
        return 4;
    }

    /* renamed from: x4 */
    public static int m10x4() {
        return 8;
    }

    /* renamed from: x5 */
    public static int m11x5() {
        return 16;
    }

    /* renamed from: w1 */
    public static double m4w1(double r) {
        return 0.1d * r;
    }

    /* renamed from: w2 */
    public static double m5w2(double r) {
        return 0.2d * r;
    }

    /* renamed from: w3 */
    public static double m6w3(double r) {
        return 0.3d * r;
    }

    public static int poewr(int itemid) {
        switch (itemid) {
            case 46510:
            case 46513:
            case 46516:
            case 46528:
            case 46531:
            case 46534:
                return 40;
            case 46511:
            case 46514:
            case 46517:
            case 46529:
            case 46532:
            case 46535:
                return 20;
            case 46512:
            case 46515:
            case 46518:
            case 46530:
            case 46533:
            case 46536:
                return 5;
            case 46519:
            case 46522:
            case 46525:
            case 46537:
            case 46540:
            case 46543:
                return 50;
            case 46520:
            case 46523:
            case 46526:
            case 46538:
            case 46541:
            case 46544:
                return 25;
            case 46521:
            case 46524:
            case 46527:
            case 46539:
            case 46542:
            case 46545:
                return 7;
            case 46546:
            case 46549:
            case 46552:
            case 46555:
                return 150;
            case 46547:
            case 46550:
            case 46553:
            case 46556:
                return 75;
            case 46548:
            case 46551:
            case 46554:
            case 46557:
                return 35;
            case 46558:
            case 46561:
                return 15;
            case 46559:
            case 46562:
                return 15;
            case 46560:
            case 46563:
                return 15;
            case 46564:
            case 46567:
                return 15;
            case 46565:
            case 46568:
                return 15;
            case 46566:
            case 46569:
                return 7;
            case 46570:
            case 46573:
            case 46576:
            case 46579:
            case 46582:
            case 46585:
            case 46588:
            case 46591:
            case 46594:
            case 46597:
            case 46600:
            case 46603:
                return 60;
            case 46571:
            case 46574:
            case 46577:
            case 46580:
            case 46583:
            case 46586:
            case 46589:
            case 46592:
            case 46595:
            case 46598:
            case 46601:
            case 46604:
                return 30;
            case 46572:
            case 46575:
            case 46578:
            case 46581:
            case 46584:
            case 46587:
            case 46590:
            case 46593:
            case 46596:
            case 46599:
            case 46602:
            case 46605:
                return 15;
            case 46606:
            case 46607:
            case 46608:
            case 46609:
            case 46610:
            case 46611:
            case 46612:
            case 46613:
            case 46614:
            case 46615:
            case 46616:
            case 46617:
            case 46618:
            case 46619:
            default:
                return 0;
            case 46620:
            case 46623:
            case 46626:
            case 46629:
            case 46632:
            case 46635:
                return 60;
            case 46621:
            case 46624:
            case 46627:
            case 46630:
            case 46633:
            case 46636:
                return 30;
            case 46622:
            case 46625:
            case 46628:
            case 46631:
            case 46634:
            case 46637:
                return 15;
        }
    }

    public static double upRX(int itemid) {
        switch (itemid) {
            case 46610:
            case 46615:
                return 0.1d;
            case 46611:
            case 46616:
                return 0.2d;
            case 46612:
            case 46617:
                return 0.3d;
            case 46613:
            case 46618:
                return 0.4d;
            case 46614:
            case 46619:
                return 0.5d;
            default:
                return 0.0d;
        }
    }

    public static int upR(int enchant_level_tmp) {
        switch (enchant_level_tmp) {
            case 1:
                return 0 + 70;
            case 2:
                return 0 + 65;
            case 3:
                return 0 + 60;
            case 4:
                return 0 + 55;
            case 5:
                return 0 + 50;
            case 6:
                return 0 + 45;
            case 7:
                return 0 + 40;
            case 8:
                return 0 + 35;
            case 9:
                return 0 + 30;
            case 10:
                return 0 + 25;
            case 11:
                return 0 + 20;
            case 12:
                return 0 + 15;
            case 13:
                return 0 + 10;
            case 14:
                return 0 + 5;
            case 15:
                return 0 + 0;
            default:
                return 0;
        }
    }
}
